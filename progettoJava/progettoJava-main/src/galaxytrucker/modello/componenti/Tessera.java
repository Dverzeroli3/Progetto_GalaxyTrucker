package galaxytrucker.modello.componenti;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Rappresenta una tessera componente generica di una nave.
 * <p>
 * Questa è la classe base per tutti i componenti della nave. Gestisce le
 * proprietà comuni come il tipo, i connettori sui quattro lati e la rotazione.
 * Fornisce metodi per manipolare e interrogare queste proprietà, oltre a
 * una factory per caricare le tessere da un file di configurazione.
 */

public class Tessera {
    /** Tipo della tessera */
    private TipoTessera tipoTessera;

    /** Array contenente i valori dei connettori: [nord, est, sud, ovest] */
    private int[] connettori;

    /** Rotazione logica della tessera: 0 = 0°, 1 = 90°, 2 = 180°, 3 = 270° */
    private int rotazione;

    /**
     * Costruttore della classe Tessera.
     *
     * @param tipoTessera Tipo della tessera
     * @param nord connettore lato nord
     * @param est connettore lato est
     * @param sud connettore lato sud
     * @param ovest connettore lato ovest
     * @throws IllegalArgumentException se i valori dei connettori non sono validi
     */
    public Tessera(TipoTessera tipoTessera, int nord, int est, int sud, int ovest) {
        this.tipoTessera = tipoTessera;
        this.rotazione = 0;
        if (!isValido(nord) || !isValido(est) || !isValido(sud) || !isValido(ovest)) {
            throw new IllegalArgumentException("I valori dei connettori devono essere tra 0 e 3.");
        }
        this.connettori = new int[]{nord, est, sud, ovest};    
    }

    /**
     * Verifica che la tessera abbia almeno un connettore su un lato.
     * @throws IllegalArgumentException se tutti i connettori sono pari a 0
     */
    public void controlloConnettori() {
        if (connettori[0] + connettori[1] + connettori[2] + connettori[3] == 0) {
            throw new IllegalArgumentException("Una tessera deve avere almeno un connettore.");
        }
    }

    /**
     * Ruota la tessera in senso orario di 90° alla volta.
     * La rotazione è solo logica, non modifica l'ordine fisico dei connettori nell'array.
     */
    public void ruota() {
        rotazione = (rotazione + 1) % 4;
    }

    // GETTERS

    /**
     * Restituisce il tipo della tessera.
     * @return Tipo della tessera
     */
    public TipoTessera getTipoTessera() {
        return tipoTessera;
    }

    /**
     * Restituisce l'array contenente i valori dei connettori originali: [nord, est, sud, ovest].
     * @return Array dei connettori [nord, est, sud, ovest]
     */
    public int[] getConnettori() {
        return connettori;
    }

    /**
     * Restituisce il connettore corrispondente al lato richiesto, tenendo conto della rotazione logica.
     *
     * @param lato Lato da considerare (0 = nord, 1 = est, 2 = sud, 3 = ovest)
     * @return Valore del connettore sul lato richiesto
     * @throws IllegalArgumentException se lato non è compreso tra 0 e 3
     */
    public int getConnettore(int lato) {
        if (lato < 0 || lato > 3) {
            throw new IllegalArgumentException("Lato deve essere compreso tra 0 e 3");
        }
        // Calcola l'indice effettivo nell'array connettori, considerando la rotazione.
        // Esempio: se rotazione = 1 (90° orario), il "nuovo nord" era l'est originale.
        // Quindi (0 - 1 + 4) % 4 = 3, che punta all'ovest originale.
        // Se lato = 0 (nord), (0 - rotazione + 4) % 4
        // Per rotazione 0: (0 - 0 + 4) % 4 = 0 (nord)
        // Per rotazione 1: (0 - 1 + 4) % 4 = 3 (ovest) -> il nuovo nord è l'ovest originale
        // Per rotazione 2: (0 - 2 + 4) % 4 = 2 (sud) -> il nuovo nord è il sud originale
        // Per rotazione 3: (0 - 3 + 4) % 4 = 1 (est) -> il nuovo nord è l'est originale
        // Questo implica che connettori[0] è sempre il connettore "fisico" nord,
        // ma getConnettore(0) restituisce il connettore che si comporta come "nord"
        // dopo la rotazione.
        return connettori[(lato - rotazione + 4) % 4];
    }

    /**
     * Restituisce la rotazione logica attuale della tessera.
     *
     * @return Rotazione logica della tessera (0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°)
     */
    public int getRotazione() {
        return rotazione;
    }

    /**
     * Metodo pensato per l'override nelle sottoclassi per fornire un valore informativo aggiuntivo specifico.
     *
     * @return -1 se non è previsto un valore informativo aggiuntivo per questa tessera
     */
    public int getInfo() {
        return -1;
    }

    /**
     * Metodo pensato per l'override nelle sottoclassi per fornire un'informazione testuale specifica.
     *
     * @return null se non è previsto un'informazione testuale per questa tessera
     */
    public String getInfoTestuale() {
        return null;
    }

    // SETTERS

    /**
     * Imposta il tipo della tessera.
     * @param tipoTessera nuovo tipo da assegnare
     */
    public void setTipoTessera(TipoTessera tipoTessera) {
        this.tipoTessera = tipoTessera;
    }

    /**
     * Imposta i connettori della tessera.
     *
     * @param nord connettore lato nord
     * @param est connettore lato est
     * @param sud connettore lato sud
     * @param ovest connettore lato ovest
     * @throws IllegalArgumentException se i valori non sono validi (non tra 0 e 3) o se tutti i connettori sono a 0
     */
    public void setConnettori(int nord, int est, int sud, int ovest) {
        if (!isValido(nord) || !isValido(est) || !isValido(sud) || !isValido(ovest)) {
            throw new IllegalArgumentException("I valori dei connettori devono essere tra 0 e 3.");
        }
        if (nord + est + sud + ovest == 0) {
            throw new IllegalArgumentException("Una tessera deve avere almeno un connettore.");
        }
        this.connettori = new int[]{nord, est, sud, ovest};    
    }

    /**
     * Imposta la rotazione logica della tessera.
     * @param rotazione valore compreso tra 0 e 3 (0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°)
     */
    public void setRotazione(int rotazione) {
        this.rotazione = rotazione;
    }

    /**
     * Verifica se un valore di connettore è valido, ovvero compreso tra 0 e 3.
     * @param connettore valore da controllare
     * @return true se il valore è tra 0 e 3, false altrimenti
     */
    private boolean isValido(int connettore) {
        return connettore >= 0 && connettore <= 3;
    }

    /**
     * Restituisce una rappresentazione testuale della tessera con i connettori come appaiono
     * dopo l'applicazione della rotazione logica.
     *
     * @return Stringa che include il tipo di tessera e i valori dei connettori (Nord, Est, Sud, Ovest)
     * secondo la rotazione corrente.
     */
    @Override
    public String toString() {
        return tipoTessera + " (" + getConnettore(0) + " " + getConnettore(1) + " " +
                getConnettore(2) + " " + getConnettore(3) + ")";
    }

    /**
     * Restituisce la rappresentazione originale della tessera, ignorando qualsiasi rotazione logica.
     * I connettori sono presentati nell'ordine iniziale: Nord, Est, Sud, Ovest.
     *
     * @return Stringa che include il tipo di tessera e i valori dei connettori nella loro posizione originale.
     */
    public String toStringOriginale() {
        return tipoTessera + " (" + connettori[0] + " " + connettori[1] + " " + connettori[2] + " " + connettori[3] + ")";
    }

    /**
     * Restituisce una rappresentazione testuale della tessera in formato grafico 3x3.
     * Questo metodo mostra i connettori sui lati e la sigla interna della tessera.
     *
     * @return Stringa formattata che visualizza la tessera.
     */
    public String stampaTessera() {
        return "|- " + getConnettore(0) + " -|\n" +
                getConnettore(3) + " " + stampaSigla() + " " + getConnettore(1) + "\n" +
                "|- " + getConnettore(2) + " -|";
    }

    /**
     * Restituisce una sigla rappresentativa del tipo di tessera.
     * Questo metodo può essere sovrascritto nelle sottoclassi per aggiungere informazioni personalizzate
     * alla sigla (ad esempio, il numero di cannoni per una tessera cannone).
     *
     * @return Sigla composta dall'abbreviazione del tipo di tessera.
     */
    protected String stampaSigla() {
        return getTipoTessera().getAbbreviazione() + " ";
    }

    /**
     * Carica le tessere da un file di testo.
     * Il file deve contenere una tessera per riga, con i dati separati da virgole
     * nel formato: {@code Tipo,info,nord,est,sud,ovest}. Le righe vuote o che iniziano con '#' vengono ignorate.
     * Una volta caricate, le tessere vengono mescolate casualmente.
     *
     * @param nomeFile il nome del file da cui leggere le tessere, che deve trovarsi nella directory delle risorse del progetto.
     * @return Una {@link List} di oggetti {@link Tessera} caricati e mescolati dal file.
     * @throws IllegalArgumentException se il formato di una riga non è valido o il tipo di tessera non è riconosciuto.
     */
    public static List<Tessera> prendiTessereDaFile(String nomeFile) {
        TipoTessera tipoTessera;
        int nord, est, sud, ovest;
        int info;
        List<Tessera> tessere = new ArrayList<>();
        InputStream fileInput = Tessera.class.getResourceAsStream("/" + nomeFile);
        if (fileInput == null) {
            System.out.println("File non trovato: " + nomeFile);
            return tessere;
        }

        try (Scanner scanner = new Scanner(fileInput)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] dati = linea.replace(";", "").split(",");
                if (dati.length != 6) {
                    throw new IllegalArgumentException("Formato riga non valido: " + linea);
                }

                tipoTessera = TipoTessera.valueOf(dati[0]);
                info = Integer.parseInt(dati[1]);
                nord = Integer.parseInt(dati[2]);
                est = Integer.parseInt(dati[3]);
                sud = Integer.parseInt(dati[4]);
                ovest = Integer.parseInt(dati[5]);

                Tessera tessera;
                switch (tipoTessera) {
                    case CABINA_CENTRALE: tessera = new TesseraCabinaCentrale(nord, est, sud, ovest); break;
                    case CABINA: tessera = new TesseraCabina(nord, est, sud, ovest); break;
                    case CANNONE: tessera = new TesseraCannone(info, nord, est, sud, ovest); break;
                    case MOTORE: tessera = new TesseraMotore(info, nord, est, sud, ovest); break;
                    case CONNETTORE: tessera = new TesseraConnettore(nord, est, sud, ovest); break;
                    case STIVA: tessera = new TesseraStiva(info, nord, est, sud, ovest); break;
                    case STIVA_SPECIALE: tessera = new TesseraStivaSpeciale(info, nord, est, sud, ovest); break;
                    case BATTERIA: tessera = new TesseraBatteria(info, nord, est, sud, ovest); break;
                    case SCUDO: tessera = new TesseraScudo(nord, est, sud, ovest); break;
                    case ALIENO: tessera = new TesseraAlieno(info, nord, est, sud, ovest); break;
                    default: throw new IllegalArgumentException("Tipo di tessera non riconosciuto: " + tipoTessera);
                }
                tessere.add(tessera);
            }
        } catch (Exception e) {
            System.out.println("Errore durante la lettura del file:");
            e.printStackTrace();
        }

        Collections.shuffle(tessere);
        return tessere;
    }
}