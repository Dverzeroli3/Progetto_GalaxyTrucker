package galaxytrucker.modello.carteAvventura;

import java.io.File;
import java.io.FileNotFoundException; // ECCEZIONE CONTROLLATA
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Costruisce un <strong>mazzo di carte avventura</strong> leggendo un file di testo.
 * <p>Il file può trovarsi in due modi:</p>
 * <ol>
 *   <li><em>Nel class-path</em> (es. <code>src/main/resources/adventure_cards.txt</code>) –
 *       passa solo il nome/relative path: <code>"adventure_cards.txt"</code>.</li>
 *   <li><em>Nel file-system</em> (path assoluto o relativo) – passa quel path.</li>
 * </ol>
 * <h3>Formato</h3>
 * <pre>
 * # righe vuote o che iniziano con # sono ignorate
 * CartaMeteoriti 5
 * CartaContrabbandieri 3
 * </pre>
 * Dove il primo token è il nome ESATTO della classe carta (senza package) e il
 * secondo è la quantità > 0.
 */
public final class MazzoAvventuraBuilder { 

    // Costruttore privato per classe di utilità
	
    private MazzoAvventuraBuilder() { /* utility: non instanziabile */ }

    /**
     * Crea il mazzo leggendo il file indicato (classpath <em>o</em> filesystem).
     * @param resourceOrPath nome del file risorsa o path sul disco
     * @return lista mescolata di {@link CartaAvventura}
     * @throws FileNotFoundException se il path specificato non esiste e non è presente tra le risorse (ECCEZIONE CONTROLLATA)
     * @throws IllegalArgumentException per righe mal formattate, tipo carta sconosciuto o quantità non valida (ECCEZIONE NON CONTROLLATA - RuntimeException)
     */
    public static List<CartaAvventura> daFile(String resourceOrPath)
            throws FileNotFoundException { // Dichiarazione dell'eccezione controllata
        Scanner sc = null;
        List<CartaAvventura> mazzo = new ArrayList<>();
        int lineNo = 0;

        try {
            sc = scannerFor(resourceOrPath); // Può lanciare FileNotFoundException

            while (sc.hasNextLine()) {
                lineNo++;
                String line = sc.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length != 2) {
                    // IllegalArgumentException è una RuntimeException (ECCEZIONE NON CONTROLLATA)
                    throw new IllegalArgumentException("Formato errato alla riga " + lineNo + ": '" + line + "'. Deve essere 'NomeCarta Quantita'.");
                }

                String tipoCarta = parts[0];
                int qta;
                try {
                    qta = Integer.parseInt(parts[1]);
                    if (qta <= 0) {
                        // IllegalArgumentException (ex NON CONTROLLATA)
                        throw new IllegalArgumentException("La quantità deve essere un numero positivo alla riga " + lineNo + ": " + parts[1]);
                    }
                } catch (NumberFormatException e) { // NumberFormatException è una RuntimeException (NON CONTROLLATA)
                    // Rilanciamo come IllegalArgumentException per coerenza (è sempre un'errore di programmazione)
                    throw new IllegalArgumentException("Quantità non numerica alla riga " + lineNo + ": '" + parts[1] + "'", e);
                }

                for (int i = 0; i < qta; i++) {
                    CartaAvventura nuovaCarta;
                    switch (tipoCarta) {
                        case "CartaContrabbandieri":
                            nuovaCarta = new CartaContrabbandieri();
                            break;
                        case "CartaMeteoriti":
                            nuovaCarta = new CartaMeteoriti();
                            break;
                        case "CartaZonaDiGuerra":
                            nuovaCarta = new CartaZonaDiGuerra();
                            break;
                        case "CartaPolvereStellare":
                            nuovaCarta = new CartaPolvereStellare();
                            break;
                        case "CartaSpazioAperto":
                            nuovaCarta = new CartaSpazioAperto();
                            break;
                        case "CartaPianeta":
                            nuovaCarta = new CartaPianeta();
                            break;
                        case "CartaNaveAbbandonata":
                            nuovaCarta = new CartaNaveAbbandonata();
                            break;
                        case "CartaStazioneAbbandonata":
                            nuovaCarta = new CartaStazioneAbbandonata();
                            break;
                        default:
                            // IllegalArgumentException (NON CONTROLLATA)
                            throw new IllegalArgumentException("Tipo di carta sconosciuto '" + tipoCarta + "' alla riga " + lineNo);
                    }
                    mazzo.add(nuovaCarta);
                }
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

        Collections.shuffle(mazzo);
        return mazzo;
    }

    private static Scanner scannerFor(String resourceOrPath)
            throws FileNotFoundException { // Dichiarazione dell'eccezione controllata
        String pathPerClasspath = resourceOrPath.startsWith("/") ? resourceOrPath.substring(1) : resourceOrPath;
        //tolgo lo /se il path è scritto con quello
        
        InputStream in = MazzoAvventuraBuilder.class.getClassLoader().getResourceAsStream(pathPerClasspath);
        //uso il classloader per accedere al file 
        //(così in teoria se si crea un file jar dovrebbe impacchettare e leggere lostesso)

        if (in != null) {
            return new Scanner(in, "UTF-8");
        }
        
        //se non va provo il metodo classico
        File f = new File(resourceOrPath);
        if (!f.exists()) {
            // FileNotFoundException (CONTROLLATA)
            throw new FileNotFoundException("File o risorsa non trovata: " + resourceOrPath + " (percorso assoluto cercato: " + f.getAbsolutePath() + ")");
        }
        // a quanto pare il costruttore di Scanner(File, Charset) può lanciare FileNotFoundException se il file
        // viene cancellato tra il check f.exists() e la creazione dello Scanner, anche se raro.
        // Inoltre, se f è una directory, lancia FileNotFoundException.
        
        return new Scanner(f, "UTF-8");
    }
    
}