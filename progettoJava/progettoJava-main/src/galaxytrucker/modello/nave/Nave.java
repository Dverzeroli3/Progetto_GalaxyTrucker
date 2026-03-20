package galaxytrucker.modello.nave;

import java.util.ArrayList;


import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import galaxytrucker.cli.UserInputProvider;
import java.util.Comparator;
import galaxytrucker.modello.componenti.*; 
import galaxytrucker.modello.dado.Dado;
import java.util.LinkedList;
import java.util.Queue;



/**
 * Rappresenta l'astronave di un giocatore.
 * <p>
 * Gestisce la griglia dei componenti ({@link Tessera}), le regole di piazzamento,
 * le statistiche aggregate della nave (potenza, equipaggio, ecc.) e la logica
 * per rispondere agli eventi di gioco come combattimenti, meteoriti e controlli
 * di integrità.
 */

public class Nave {
    private Tessera[][] matrice;
    private boolean[][] celleAmmesse;
    private int righe, colonne;
    
    private int caricheBatterie;
    private int potenzaMotoriSingoli;
    private double potenzaCannoniSingoli;
    private int numeroPersonale;
    private int spazioStiva;
    private int spazioStivaSpeciale;
    private int numeroComponentiRimossi;
    private int numeroAlieniViola;			
    private int numeroAlieniMarroni;		
    
    private List<Tessera> tessereNave; // Lista delle tessere della nave
    
  
    /**
     * Costruisce una nuova nave per un dato livello di gioco.
     * Inizializza la griglia della nave, piazza la cabina centrale e resetta
     * tutte le statistiche.
     *
     * @param livello il livello della partita (1, 2 o 3), che determina la
     *                dimensione e la forma della griglia.
     */
    public Nave(int livello) {
    	tessereNave = new ArrayList<>();
    	inizializzaNave(livello);
        
        caricheBatterie = 0;
        potenzaMotoriSingoli = 0;
        potenzaCannoniSingoli = 0;
        numeroPersonale = 0;
        spazioStiva = 0;
        spazioStivaSpeciale = 0;
        numeroAlieniViola = 0;		
        numeroAlieniMarroni = 0;
        

    }
    
    /** Restituisce il numero di righe della griglia della nave. @return le righe. */
    public int getRighe() {
        return righe;
    }
    
    /** Restituisce il numero di colonne della griglia della nave. @return le colonne. */
    public int getColonna() {
        return colonne;
    }
    
    /** Restituisce le cariche residue delle batterie. @return le cariche. */
    public int getCaricheBatterie() {
        return caricheBatterie;
    }
    /** Restituisce la potenza base dei motori singoli. @return la potenza. */
    public int getPotenzaMotori() {
        return potenzaMotoriSingoli;
    }
    
    /** Restituisce la potenza base dei cannoni singoli. @return la potenza. */
    public double getPotenzaCannoniSingoli() {
        return potenzaCannoniSingoli;
    }
    
    /** Restituisce il numero di personale umano. @return il personale. */
    public int getNumeroPersonale() {
        return numeroPersonale;
    }
    
    /** Restituisce lo spazio disponibile nelle stive normali. @return lo spazio stiva. */
    public int getspazioStiva() {
        return spazioStiva;
    }
    
    /** Restituisce lo spazio disponibile nelle stive speciali. @return lo spazio stiva speciale. */
    public int spazioStivaSpeciale() {
        return spazioStivaSpeciale;
    }
    
    /** Restituisce il numero di alieni viola. @return gli alieni viola. */
    public int getNumeroAlieniViola() {
        return numeroAlieniViola;
    }
    
    /** Restituisce il numero di alieni marroni. @return gli alieni marroni. */
    public int getNumeroAlieniMarroni() {
        return numeroAlieniMarroni;
    }

    /** Restituisce il numero di componenti persi durante il volo. @return i componenti rimossi. */
    public int getNumeroComponentiRimossi(){
        return numeroComponentiRimossi;
    }
    
    /**
     * Restituisce una stringa riassuntiva delle statistiche principali della nave.
     * @return una stringa multi-linea con le statistiche.
     */
    public String descrizioneNave() {
        return "Cariche Batterie: " + caricheBatterie + "\n" +
               "Potenza Motori: " + potenzaMotoriSingoli + "\n" +
               "Potenza Fuoco: " + potenzaCannoniSingoli + "\n" +
               "Numero Personale: " + numeroPersonale + "\n" +
               "Spazio Merci: " + spazioStiva + "\n" +
               "Spazio Merci Speciali: " + spazioStivaSpeciale + "\n" +
               "Alieni Viola: " + numeroAlieniViola + "\n" +
               "Alieni Marroni: " + numeroAlieniMarroni;
    }
    
    /*
     * Inizializza due matrici dato in input un livello compreso tra 1 e 3
     * Una matrice conterrà le tessere.
     * La seconda matrice sarà una matrice di booleani dove: 
     * -true indicherà la possibilità di inserire una Tessera in quella posizione
     * -false indicherà che quella cella è bloccata e non può essere utilizzata
     * */
    private void inizializzaNave(int livello) {
        
        int[][] bloccate;

        switch (livello) {
            case 1 -> {
                righe = 5; colonne = 5;
                bloccate = new int[][] {
                    {0, 0}, {0, 1}, {0, 3}, {0, 4}, {1, 0}, {1, 4}, {4, 2}
                };
            }
            case 2 -> {
                righe = 5; colonne = 7;
                bloccate = new int[][] {
                    {0, 0}, {0, 1}, {0, 3}, {0, 5}, {0, 6}, {1, 0}, {1, 6}, {4, 3}
                };
            }
            case 3 -> {
                righe = 6; colonne = 9;
                bloccate = new int[][] {
                    {0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 5}, {0, 6}, {0, 7}, {0, 8},
                    {1, 0}, {1, 1}, {1, 2}, {1, 6}, {1, 7}, {1, 8}, {2, 1}, {2, 7},
                    {5, 2}, {5, 6}
                };
            }
            default -> throw new IllegalArgumentException("Livello non valido: deve essere 1, 2 o 3");
        }
        
        matrice = new Tessera[righe][colonne];
        celleAmmesse = new boolean[righe][colonne];
        
     // Le celle vengono inizialmente impostate tutte a true 
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                celleAmmesse[i][j] = true;
            }
        }
     // Poi quelle presenti in bloccate vengono messe a false
        for (int[] c : bloccate) {
        	celleAmmesse[c[0]][c[1]] = false;
        }
        
        
        //Inserimento della tessera predefinita TesseraCabinaCentrale
        //la tessera andrebbe presa dalla lista di tessere
        Tessera cabinaCentrale = new TesseraCabinaCentrale(3,3,3,3);
        
        switch (livello) {
        case 1 -> {
        		piazzaTessera(cabinaCentrale,2,2,0);
            }
        case 2 -> {
        		piazzaTessera(cabinaCentrale,2,3,0);
            }

        case 3 -> {
        	piazzaTessera(cabinaCentrale,3,4,0);
            }
        default -> throw new IllegalArgumentException("Livello non valido: deve essere 1, 2 o 3");
    }
    }

    
    
    /**
     * Controlla se una cella della griglia è utilizzabile per piazzare una tessera.
     * @param righe la riga da controllare.
     * @param colonne la colonna da controllare.
     * @return {@code true} se la cella è ammessa, {@code false} altrimenti.
     */
    public boolean isCellaAmmessa(int righe, int colonne) {
        return celleAmmesse[righe][colonne];
    }
    
    /**
     * Restituisce la matrice bidimensionale che rappresenta la nave.
     * @return un array 2D di {@link Tessera}.
     */
    public Tessera[][] getMatrice() {
        return matrice;
    }
    
    /**
     * Restituisce una rappresentazione testuale della maschera di celle ammesse.
     * @return una stringa che mostra con 'T' e 'F' le celle utilizzabili.
     */
    public String stampaCelleAmmesseToString() {
    	StringBuilder stringa = new StringBuilder();
    	
    	stringa.append("Matrice celle bloccate (T = true, F = false):\n"
    				 + "  0 1 2 3 4\n");
        for (int i = 0; i < righe; i++) {
        	stringa.append(i + " ");
            for (int j = 0; j < colonne; j++) {
                stringa.append(celleAmmesse[i][j] ? "T " : "F ");
            }
            stringa.append("\n");
        }
        return stringa.toString();
    }
    
    
    
    private boolean inMatrice(int x, int y) {
        return x >= 0 && y >= 0 && x < matrice.length && y < matrice[0].length;
    }
    
    /**
     * Piazza una tessera sulla griglia della nave, applicando tutte le regole di validazione.
     *
     * @param tessera la tessera da piazzare.
     * @param riga la riga di destinazione.
     * @param colonna la colonna di destinazione.
     * @param rotazione il numero di rotazioni (0-3) da applicare alla tessera.
     * @throws IllegalArgumentException se il piazzamento viola una qualsiasi regola
     *         (es. coordinate non valide, cella occupata, connessione illegale).
     */
    public void piazzaTessera(Tessera tessera, int riga, int colonna, int rotazione) {
        
    	tessera.setRotazione(rotazione);
    	
    	if (!inMatrice(riga,colonna)) {
            throw new IllegalArgumentException("Coordinate fuori dalla matrice");
            
        }
        if (!celleAmmesse[riga][colonna]) {
            throw new IllegalArgumentException("Cella non ammessa");
           
        }
        if (matrice[riga][colonna] != null) {
            throw new IllegalArgumentException("Spazio già occupato");
        }
        if (!verificaPiazzamento(tessera, riga, colonna)) {
        	throw new IllegalArgumentException("Tessera non connessa correttamente");
        }
        if (tessera.getTipoTessera().equals(TipoTessera.MOTORE) && rotazione != 0) {
        	throw new IllegalArgumentException("Un motore deve puntare sempre verso il retro della nave,"
        			+ " non puo ruotare");
        }
        if (tessera.getTipoTessera().equals(TipoTessera.MOTORE) || tessera.getTipoTessera().equals(TipoTessera.CANNONE)) {
        	if (!controllaCannoniMotori(tessera, riga, colonna)) {
            	throw new IllegalArgumentException("Non puo esserci una tessera davanti alla bocca del cannone"
            			+ " e non puo esserci una tessera dietro ai razzi del motore");
            }
        }
        
     
        matrice[riga][colonna] = tessera;
        celleAmmesse[riga][colonna] = false;
        bloccaVincoliSpeciali(tessera,riga,colonna);
        aggiungiTessera(tessera);		
    }
    
    /*
     * In caso la tessera sia un Motore o un Cannone controlla che la posizione 
     * dove punta il Cannone/Motore sia libera
     * */
    private Boolean controllaCannoniMotori(Tessera tessera, int riga, int colonna) {
    	
    	int r = riga;
        int c = colonna;
    	
    	int rotazione = tessera.getRotazione(); // 0=N, 1=E, 2=S, 3=O

    	// un cannone può puntare in tutte le direzioni, e la cella davanti un cannone non è utilizzabile
    	if (tessera.getTipoTessera().equals(TipoTessera.CANNONE)) { 
            switch (rotazione) {
                case 0: r = r - 1; break; // nord
                case 1: c = c + 1; break; // est
                case 2: r = r + 1; break; // sud
                case 3: c = c - 1; break; // ovest
            }	
    	}
    	// un motore può puntare solo all'indietro, quindi non può ruotare 
    	// e la cella sotto un motore non è utilizzabile
    	if (tessera.getTipoTessera().equals(TipoTessera.MOTORE)) { 
    		r = r + 1;  // sud
    	}
    	
    	// controlla se la posizione davanti al cannone o dietro al motore si trova fuori dalla matrice
    	// se il cannone/motore punta una posizione fuori dalla matrice è ok
        if (!inMatrice(r, c)) {
            return true;
        }
        // controlla se la posizione davanti al cannone o dietro al motore è libera
    	// se il cannone/motore punta una posizione libera è ok
        if (matrice[r][c] == null) {
            return true;
        }
        // altrimenti la tessera non può essere piazzata
        return false;

    }	
    
    /*
     * In caso la tessera sia un Motore o un Cannone rende inutilizzabili delle celle nalla matrice
     * */
    private void bloccaVincoliSpeciali(Tessera tessera, int riga, int colonna) {
    	
    	int rotazione = tessera.getRotazione(); // 0=N, 1=E, 2=S, 3=O

    	// un cannone può puntare in tutte le direzioni, e la cella davanti un cannone non è utilizzabile
    	if (tessera.getTipoTessera().equals(TipoTessera.CANNONE)) { 
            switch (rotazione) {
                case 0: riga = riga - 1; break; // nord
                case 1: colonna = colonna + 1; break; // est
                case 2: riga = riga + 1; break; // sud
                case 3: colonna = colonna - 1; break; // ovest
            }	
    	}
    	// un motore può puntare solo all'indietro, quindi non può ruotare 
    	// e la cella sotto un motore non è utilizzabile
    	if (tessera.getTipoTessera().equals(TipoTessera.MOTORE)) { 
    		riga = riga + 1;  // sud
    	}
    	
    	// Blocca la cella solo se è dentro la matrice
        if (inMatrice(riga, colonna)) {
            celleAmmesse[riga][colonna] = false;
        }

    }	
    
    
    /**
     * Verifica se una tessera è adiacente ad almeno un'altra tessera e se tutte
     * le connessioni con le tessere adiacenti sono valide.
     *
     * @param tessera la tessera da piazzare.
     * @param riga la riga di destinazione.
     * @param colonna la colonna di destinazione.
     * @return {@code true} se il piazzamento è valido, {@code false} altrimenti.
     */
    public boolean verificaPiazzamento(Tessera tessera, int riga, int colonna) {
        boolean almenoUnLatoConnesso = false;	// usato per verificare che almeno un lato si possa connettere

        int connettoreN = tessera.getConnettore(0);
        int connettoreE = tessera.getConnettore(1);
        int connettoreS = tessera.getConnettore(2);
        int connettoreO = tessera.getConnettore(3);

        // Nord
        if (riga > 0 && matrice[riga - 1][colonna] != null) {
            Tessera nord = matrice[riga - 1][colonna];
            int connettoreNordAdiacente = nord.getConnettore(2);
            if (!connettoriCompatibili(connettoreN, connettoreNordAdiacente)) return false;
            if (connettoreN != 0 || connettoreNordAdiacente != 0) almenoUnLatoConnesso = true;
        }

        // Est
        if (colonna < colonne - 1 && matrice[riga][colonna + 1] != null) {
            Tessera est = matrice[riga][colonna + 1];
            int connettoreEstAdiacente = est.getConnettore(3);
            if (!connettoriCompatibili(connettoreE, connettoreEstAdiacente)) return false;
            if (connettoreE != 0 || connettoreEstAdiacente != 0) almenoUnLatoConnesso = true;
        }

        // Sud
        if (riga < righe - 1 && matrice[riga + 1][colonna] != null) {
            Tessera sud = matrice[riga + 1][colonna];
            if (connettoriCompatibili(connettoreS, sud.getConnettore(0))) {
                if (connettoreS != 0 || sud.getConnettore(0) != 0) {
                    almenoUnLatoConnesso = true;
                }
            }
        }

        // Ovest
        if (colonna > 0 && matrice[riga][colonna - 1] != null) {
            Tessera ovest = matrice[riga][colonna - 1];
            int connettoreOvestAdiacente = ovest.getConnettore(1);
            if (!connettoriCompatibili(connettoreO, connettoreOvestAdiacente)) return false;
            if (connettoreO != 0 || connettoreOvestAdiacente != 0) almenoUnLatoConnesso = true;
        }
        //La prima tessera viene piazzata anche se non si connette a nulla ovviamente
        if (nessunaTesseraPiazzata()) {
            almenoUnLatoConnesso = true;
        }

        return almenoUnLatoConnesso;
    }
    
    /*
     * Metodo che serve per determinare se la griglia è completamente vuota, 
     * cioè se non è ancora stata piazzata nessuna tessera
     * */
    private boolean nessunaTesseraPiazzata() {
        for (int riga = 0; riga < righe; riga++) {
            for (int colonna = 0; colonna < colonne; colonna++) {
                if (matrice[riga][colonna] != null) {
                    return false; // Almeno una tessera è già stata piazzata
                }
            }
        }
        return true; // Tutte le celle sono vuote
    }
    
    /*
     * Metodo che verifica se due lati di due tessere adiacenti si possono collegare
     * */
    private boolean connettoriCompatibili(int c1, int c2) {
        // Se uno dei due è 0, va bene a patto che anche l'altro lo sia
    	if (c1 == 0 || c2 == 0) return c1==0 && c2==0;  //se uno è zero lo deve essere anche l'altro

        // Se uno dei due è 3, si può collegare con qualsiasi altro
        if (c1 == 3 || c2 == 3) return true;

        // Se entrambi sono uguali (1-1, 2-2), va bene
        return c1 == c2;
    }
    
    /**
     * Stampa una rappresentazione ASCII della nave e della griglia vuota.
     * Mostra le tessere piazzate e le caselle vuote dove è possibile costruire.
     * @return una stringa multi-linea che rappresenta la nave.
     */
    public String stampaNaveConGriglia() {
        int righe = matrice.length;
        int colonne = matrice[0].length;
        StringBuilder stringBuilder = new StringBuilder();

        // Intestazione colonne
        stringBuilder.append("    ");  // Spazio per indice riga
        for (int j = 0; j < colonne; j++) {
        	stringBuilder.append("   ").append(j).append("    ");
        }
        stringBuilder.append("\n");

        for (int i = 0; i < righe; i++) {
            // Ogni tessera stampa 3 righe
            StringBuilder riga1 = new StringBuilder();
            StringBuilder riga2 = new StringBuilder();
            StringBuilder riga3 = new StringBuilder();

            for (int j = 0; j < colonne; j++) {
                Tessera t = matrice[i][j];
                if (t != null) {
                    String[] righeTessera = t.stampaTessera().split("\n");
                    riga1.append(righeTessera[0]).append(" ");
                    riga2.append(righeTessera[1]).append(" ");
                    riga3.append(righeTessera[2]).append(" ");
               }  else if (celleAmmesse[i][j]) {
                    riga1.append("|     | ");
                    riga2.append("|     | ");
                    riga3.append("|_____| ");
                } else {
                    riga1.append("        ");
                    riga2.append("        ");
                    riga3.append("        ");
                }
            }

            // riga 1
            stringBuilder.append("    ").append(riga1).append("\n");

            // Aggiungo indice riga all'inizio di riga2 per chiarezza
            stringBuilder.append(" " + i + "  ");
            stringBuilder.append(riga2).append("\n");
            
            // riga 3
            stringBuilder.append("    ").append(riga3).append("\n");
        }

        return stringBuilder.toString();
    }
    /**
     * Stampa una rappresentazione ASCII della nave, mostrando solo i componenti piazzati.
     * @return una stringa multi-linea che rappresenta la nave.
     */
    public String stampaNave() {
        int righe = matrice.length;
        int colonne = matrice[0].length;
        StringBuilder stringBuilder = new StringBuilder();

        // Intestazione colonne
        stringBuilder.append("    ");  // Spazio per indice riga
        for (int j = 0; j < colonne; j++) {
        	stringBuilder.append("   ").append(j).append("    ");
        }
        stringBuilder.append("\n");

        for (int i = 0; i < righe; i++) {
            // Ogni tessera stampa 3 righe
            StringBuilder riga1 = new StringBuilder();
            StringBuilder riga2 = new StringBuilder();
            StringBuilder riga3 = new StringBuilder();

            for (int j = 0; j < colonne; j++) {
                Tessera t = matrice[i][j];
                if (t != null) {
                    String[] righeTessera = t.stampaTessera().split("\n");
                    riga1.append(righeTessera[0]).append(" ");
                    riga2.append(righeTessera[1]).append(" ");
                    riga3.append(righeTessera[2]).append(" ");
                }  else {
                    riga1.append("        ");
                    riga2.append("        ");
                    riga3.append("        ");
                }
            }

            // riga 1
            stringBuilder.append("    ").append(riga1).append("\n");

            // Aggiungo indice riga all'inizio di riga2 per chiarezza
            stringBuilder.append(" " + i + "  ");
            stringBuilder.append(riga2).append("\n");
            
            // riga 3
            stringBuilder.append("    ").append(riga3).append("\n");
        }

        return stringBuilder.toString();
    }
    
    /**
     * Rimuove una tessera dalla nave alla posizione specificata.
     * Aggiorna le statistiche della nave e il conteggio dei componenti persi.
     *
     * @param riga la riga della tessera da rimuovere.
     * @param colonna la colonna della tessera da rimuovere.
     * @throws IllegalArgumentException se le coordinate non sono valide o la cella è già vuota.
     */
    public void rimuoviTessera(int riga, int colonna) {
        if (!inMatrice(riga, colonna)) {
            throw new IllegalArgumentException("Posizione fuori dalla griglia");
        }
        
        Tessera tesseraDaRimuovere = matrice[riga][colonna];

        if (tesseraDaRimuovere == null) {
            throw new IllegalArgumentException("Nessuna tessera da rimuovere in questa posizione");
        }
        
        sottraiStatisticheTessera(tesseraDaRimuovere);
        
        matrice[riga][colonna] = null; // Rimuove la tessera dalla griglia
        tessereNave.remove(tesseraDaRimuovere); // Rimuovi anche dalla lista delle tessere
        numeroComponentiRimossi ++; //incremento numero di componenti rimossi dalla nave
        
        
    }
    
    /* * Metodo per aggiungere una tessera alla nave e modificare i parametri di essa
     * */
    public void aggiungiTessera(Tessera tessera) {
        tessereNave.add(tessera);
        
        // Aggiorna i valori per ogni tipo di tessera
        if (tessera instanceof TesseraBatteria) {
            this.caricheBatterie += ((TesseraBatteria) tessera).getInfo();
        }
//        //Verifica che il Motore sia singolo
//        //
//        if (tessera instanceof TesseraMotore && tessera.getInfo() == 1) {
//            this.potenzaMotoriSingoli += ((TesseraMotore) tessera).getInfo();
//        }
//        //Verifica che il Cannone sia singolo
//        if (tessera instanceof TesseraCannone && tessera.getInfo() == 1) {
//        	switch (tessera.getRotazione()) {
//            case 0 -> potenzaCannoniSingoli += 1.0;
//            case 1, 2, 3 -> potenzaCannoniSingoli += 0.5;
//        }
//        }
        
        if (tessera instanceof TesseraCabina) {
        	this.numeroPersonale += ((TesseraCabina) tessera).getInfo();
        }
        if (tessera instanceof TesseraCabinaCentrale) {
        	this.numeroPersonale += ((TesseraCabinaCentrale) tessera).getInfo();
        }
        
        if (tessera instanceof TesseraStiva) {
            this.spazioStiva += ((TesseraStiva) tessera).getInfo();
        }
        
        if (tessera instanceof TesseraStivaSpeciale) {
            this.spazioStivaSpeciale += ((TesseraStivaSpeciale) tessera).getInfo();
        }
        
        if (tessera instanceof TesseraAlieno) {
        }

    }
    
    

    /**
     * Controlla se la nave ha almeno una carica di batteria disponibile.
     * @return {@code true} se ci sono cariche, {@code false} altrimenti.
     */
public boolean haBatteriaDisponibile() {
    return caricheBatterie > 0;
    }

/**
 * Tenta di consumare una carica di batteria.
 * @return {@code true} se una carica è stata consumata, {@code false} se le batterie erano scariche.
 */
public boolean usaBatteria() {
    if (caricheBatterie > 0) {
        caricheBatterie--;
        return true;
    }
    return false;
    }
    
/**
 * Calcola la potenza di fuoco totale della nave per il turno corrente.
 * Somma la potenza dei cannoni singoli e chiede all'utente se vuole attivare
 * i cannoni doppi consumando batterie.
 *
 * @param inputProvider l'interfaccia per ottenere l'input dall'utente.
 * @param nomeGiocatore il nome del giocatore, per personalizzare i messaggi.
 * @return la potenza di fuoco totale.
 */
public double calcolaPotenzaDiFuoco(UserInputProvider inputProvider, String nomeGiocatore) {
    System.out.println(nomeGiocatore + ", calcola la tua potenza di fuoco.");
    double potenza = 0.0;

    // Calcolo potenza base da cannoni singoli
    for (int i = 0; i < matrice.length; i++) {
        for (int j = 0; j < matrice[i].length; j++) {
            Tessera tessera = matrice[i][j];
            if (tessera instanceof TesseraCannone cannone && cannone.getInfo() == 1) { // Cannone singolo
                // La potenza dei cannoni singoli dipende dalla direzione
                // Assumiamo che "0" sia la direzione principale (es. avanti)
                // e le altre (1,2,3 laterali/dietro) contino meno.
                // Questa logica era già presente e sembra corretta.
                int rotazione = cannone.getRotazione();
                switch (rotazione) {
                    case 0: potenza += 1.0; break; // Avanti
                    case 1: case 3: potenza += 0.5; break; // Est, Ovest
                    case 2: potenza += 0.5; break; // Sud (se i cannoni possono sparare indietro)
                                                     // Se non possono sparare indietro, case 2 potrebbe essere 0.0
                    // Assumiamo la logica esistente: 1.0 fronte, 0.5 lati/retro
                }
            }
        }
    }
    System.out.println("  Potenza base da cannoni singoli: " + potenza);

    List<ComponenteAttivabile> cannoniDoppi = new ArrayList<>(); // Usa ComponenteAttivabile
    for (int i = 0; i < matrice.length; i++) {
        for (int j = 0; j < matrice[i].length; j++) {
            Tessera tessera = matrice[i][j];
            if (tessera instanceof TesseraCannone cannone && cannone.getInfo() == 2) { // Cannone doppio
                int rotazione = cannone.getRotazione();
                double potenzaCannoneDoppio = 0;
                 switch (rotazione) {
                    case 0: potenzaCannoneDoppio = 2.0; break;
                    case 1: case 3: potenzaCannoneDoppio = 1.0; break;
                    case 2: potenzaCannoneDoppio = 1.0; break; // Come sopra per potenza
                }
                if (potenzaCannoneDoppio > 0) {
                   cannoniDoppi.add(new ComponenteAttivabile(i, j, potenzaCannoneDoppio)); // Usa ComponenteAttivabile
                }
            }
        }
    }

    // --- MODIFICA: Scelta singola per ogni cannone doppio ---
    if (!cannoniDoppi.isEmpty()) {
        System.out.println("  Hai " + cannoniDoppi.size() + " cannoni doppi e " + getCaricheBatterie() + " cariche batteria.");
        if (getCaricheBatterie() > 0) {
            for (ComponenteAttivabile cDoppio : cannoniDoppi) { // Usa ComponenteAttivabile
                if (getCaricheBatterie() == 0) {
                    System.out.println("    Batterie esaurite. Non si possono attivare altri cannoni doppi.");
                    break;
                }
                // Chiedi per ogni cannone doppio singolarmente
                String domanda = String.format("  Vuoi attivare il cannone doppio in posizione (%d,%d)? Potenza aggiunta: %.1f (s/n): ", cDoppio.r, cDoppio.c, cDoppio.potenza); // Usa r e c
                if (inputProvider.askYesNo(domanda)) {
                    if (usaBatteria()) { // usaBatteria decrementa caricheBatterie
                        potenza += cDoppio.potenza;
                        System.out.println("    Cannone doppio (" + cDoppio.r + "," + cDoppio.c + ") attivato! Potenza aggiunta: " + cDoppio.potenza); // Usa r e c
                    } else {
                        System.out.println("    Batterie esaurite durante l'attivazione.");
                        break;
                    }
                } else {
                    System.out.println("    Cannone doppio (" + cDoppio.r + "," + cDoppio.c + ") non attivato."); // Usa r e c
                }
            }
        } else {
            System.out.println("  Cannoni doppi non attivati con batterie.");
        }
    }
    System.out.println("  Potenza di fuoco totale per " + nomeGiocatore + ": " + potenza);
    return potenza;
}

/**
 * Calcola la potenza motrice totale della nave per il turno corrente.
 * Somma la potenza dei motori singoli e chiede all'utente se vuole attivare
 * i motori doppi consumando batterie.
 *
 * @param inputProvider l'interfaccia per ottenere l'input dall'utente.
 * @param nomeGiocatore il nome del giocatore, per personalizzare i messaggi.
 * @return la potenza motrice totale.
 */
public double calcolaPotenzaMotrice(UserInputProvider inputProvider, String nomeGiocatore) {
    System.out.println(nomeGiocatore + ", calcola la tua potenza motrice.");
    double potenza = 0.0;

    // Potenza base da motori singoli (i motori doppi contribuiscono solo se attivati)
    for (int i = 0; i < matrice.length; i++) {
        for (int j = 0; j < matrice[i].length; j++) {
            Tessera tessera = matrice[i][j];
            if (tessera instanceof TesseraMotore motore && motore.getInfo() == 1) { // Motore singolo
                potenza += 1.0; // Assumiamo che i motori singoli diano sempre 1.0
            }
        }
    }
    System.out.println("  Potenza base da motori singoli: " + potenza);

    List<ComponenteAttivabile> motoriDoppi = new ArrayList<>(); // Usa ComponenteAttivabile
    for (int i = 0; i < matrice.length; i++) {
        for (int j = 0; j < matrice[i].length; j++) {
            Tessera tessera = matrice[i][j];
            if (tessera instanceof TesseraMotore motore && motore.getInfo() == 2) { // Motore doppio
                // Assumiamo che un motore doppio dia 2.0 di potenza SE attivato
                motoriDoppi.add(new ComponenteAttivabile(i, j, 2.0)); // Usa ComponenteAttivabile
            }
        }
    }

    // --- MODIFICA: Scelta singola per ogni motore doppio ---
    if (!motoriDoppi.isEmpty()) {
        System.out.println("  Hai " + motoriDoppi.size() + " motori doppi e " + getCaricheBatterie() + " cariche batteria.");
        if (getCaricheBatterie() > 0) {
            for (ComponenteAttivabile mDoppio : motoriDoppi) { // Usa ComponenteAttivabile
                if (getCaricheBatterie() == 0) {
                    System.out.println("    Batterie esaurite. Non si possono attivare altri motori doppi.");
                    break;
                }
                String domanda = String.format("  Vuoi attivare il motore doppio in posizione (%d,%d)? Potenza aggiunta: %.1f (s/n): ", mDoppio.r, mDoppio.c, mDoppio.potenza); // Usa r e c
                if (inputProvider.askYesNo(domanda)) {
                    if (usaBatteria()) {
                        potenza += mDoppio.potenza;
                        System.out.println("    Motore doppio (" + mDoppio.r + "," + mDoppio.c + ") attivato! Potenza aggiunta: " + mDoppio.potenza); // Usa r e c
                    } else {
                        System.out.println("    Batterie esaurite durante l'attivazione.");
                        break;
                    }
                } else {
                    System.out.println("    Motore doppio (" + mDoppio.r + "," + mDoppio.c + ") non attivato."); // Usa r e c
                }
            }
        } else {
            System.out.println("  Motori doppi non attivati con batterie.");
        }
    }
    System.out.println("  Potenza motrice totale per " + nomeGiocatore + ": " + potenza);
    return potenza;
}




/**
 * Carica una data quantità di merci di un certo colore nelle stive disponibili.
 * Riempie prima le stive che hanno spazio, rispettando i vincoli (es. merci rosse
 * solo in stive speciali).
 *
 * @param colore il colore della merce da caricare.
 * @param quantita la quantità da caricare.
 */
public void caricaMerce(String colore, int quantita) {
    int caricate = 0;
    for (Tessera t : tessereNave) {
        if (t instanceof InterfacciaMerci contenitore)  // Considera solo le tessere che implementano InterfacciaMerci
        {
            caricate += caricaInStiva(contenitore, colore, quantita - caricate);
        }
        if (caricate >= quantita) break; //caricato tutto, usciamo dal ciclo
    }
    if (caricate < quantita) {
        System.out.println("Non c'è abbastanza spazio per caricare tutte le merci " + colore + ". Caricate: " + caricate + "/" + quantita);
    }
}


    private int caricaInStiva(InterfacciaMerci stiva, String colore, int quantitaRichiesta) {
    	// Se la merce è "rossa", può essere caricata solo in una TesseraStivaSpeciale
        if ("rosso".equals(colore) && !(stiva instanceof TesseraStivaSpeciale)) {
            return 0;  //// Bloccata: la stiva non è speciale
        }
        List<String> merci = stiva.getMerci();
        int spaziLiberi = stiva.getNumeroScomparti() - merci.size();
        int daCaricare = Math.min(spaziLiberi, quantitaRichiesta); // Calcola quante ne può effettivamente caricare

        for (int i = 0; i < daCaricare; i++) {
            merci.add(colore);
        }
        return daCaricare;
    }



// Metodo per scaricare merci dalla nave (Effetto carta Contrabbandieri)
    
    /**
     * Rimuove un certo numero delle merci di maggior valore dalla nave.
     * La priorità di rimozione è: rosse > gialle > verdi > blu.
     *
     * @param numero il numero di merci da scaricare.
     */
    public void scaricaMerciPiuPreziose(int numero) {
        String[] priorita = {"rosso", "giallo", "verde", "blu"};
        int rimosse = 0;

        for (String colore : priorita) {
            for (Tessera t : tessereNave) {
                if (t instanceof InterfacciaMerci contenitore) {
                    List<String> merci = contenitore.getMerci();
                    Iterator<String> iterator = merci.iterator();
                    while (iterator.hasNext()) {
                        String merce = iterator.next();
                        if (merce.equals(colore)) {
                            iterator.remove();
                            rimosse++;
                            System.out.println(" Merce " + colore + " sequestrata.");
                            break; // passa alla prossima tessera
                        }
                    }
                }
                if (rimosse >= numero) break;
            }
            if (rimosse >= numero) break;
        }
        if (rimosse < numero) {
            System.out.println("Tutte le merci sequestrate");
        }
    }
    
 // Mappa statica dei valori delle merci per colore
    private static final Map<String, Integer> VALORE_MERCI = Map.of(
        "rosso", 4,
        "giallo", 3,
        "verde", 2,
        "blu", 1
    );

    /**
     * Calcola il valore totale in crediti di tutte le merci a bordo della nave.
     * @return il totale dei crediti.
     */
    public int calcolaCreditiMerci() {
        int crediti = 0;
        for (Tessera t : tessereNave) {
            if (t instanceof InterfacciaMerci contenitore) {
                for (String merce : contenitore.getMerci()) {
                    crediti += VALORE_MERCI.getOrDefault(merce, 0);
                }
            }
        }
        return crediti;
    }


    /**
     * Restituisce il numero totale di membri dell'equipaggio a bordo (umani + alieni).
     * @return il totale dell'equipaggio.
     */
    public int getTotaleEquipaggio() {
    return this.numeroPersonale + this.numeroAlieniViola + this.numeroAlieniMarroni;
    }
    
    /**
     * Restituisce il numero totale di membri dell'equipaggio umano a bordo.
     * @return il totale degli umani.
     */
    public int getTotaleUmani() {
    return this.numeroPersonale;
    }

    /**
     * Consuma una certa quantità di equipaggio, dando priorità al personale umano
     * e poi agli alieni.
     *
     * @param quantita il numero di membri dell'equipaggio da consumare.
     */
    public void consumaEquipaggio(int quantita) {
    for (int i = 0; i < quantita; i++) {
        if (numeroPersonale > 0) {
            numeroPersonale--;
        } else if (numeroAlieniViola > 0) {
            numeroAlieniViola--;
        }
        else if(numeroAlieniMarroni > 0) {
            numeroAlieniMarroni--;
        } else {
            System.out.println("Nessun equipaggio disponibile");
        }
    }
    }
    
    /**
     * Simula l'impatto di una cannonata leggera su una colonna casuale della nave.
     * Il colpo può essere bloccato da uno scudo attivo, altrimenti distrugge il primo
     * componente incontrato.
     *
     * @return {@code true} se il danno è stato evitato (colpo a vuoto o scudo attivo),
     *         {@code false} se un componente è stato distrutto.
     */
    public boolean risolviCannonataLeggera() {

        Dado dado = new Dado();
        int colonnaColpo = (dado.lancia() - 2) * matrice[0].length / 11;  //include estremo

        for (int riga = matrice.length-1; riga >= 0; riga--) //Stessa correzione come nella pesante
        {  // Dal basso verso l’alto (SUD)
            Tessera tessera = matrice[riga][colonnaColpo];
            if (tessera != null) {
                if (tessera instanceof TesseraScudo scudo && scudo.proteggeDa(EnumDirezione.SUD)) {
                    if (caricheBatterie > 0) {
                        caricheBatterie--;
                        return true; // Danno evitato
                    }
                }

                rimuoviTessera(riga, colonnaColpo);
                return false; // Danno subito, tessera tolta
            }
        }

        return true; // Nessun danno (nessuna tessera colpita)
    }

    /**
     * Simula l'impatto di una cannonata pesante su una colonna casuale.
     * Questa cannonata non può essere bloccata da scudi e distrugge il primo
     * componente incontrato.
     *
     * @return {@code true} se un componente è stato distrutto, {@code false} se il colpo
     *         è andato a vuoto.
     */
    public boolean risolviCannonataPesante() {

        Dado dado = new Dado();
        int colonna = (dado.lancia() - 2) * matrice[0].length / 11;  //include estremo

        // Scorriamo le righe dal basso verso l’alto
        for (int riga = matrice.length-1; riga >= 0; riga--) //Adesso dovrebbe scorrere in maniera corretta
        {
            Tessera tessera = matrice[riga][colonna];
            if (tessera != null) {
            	
            	rimuoviTessera(riga, colonna); 
            	
                return true;
            }
        }

        return false; // Colpo a vuoto
    }
    
    /**
     * Calcola il numero totale di connettori esposti (non collegati ad altre tessere)
     * su tutta la nave.
     *
     * @return il numero di connettori esposti.
     */
    public int getConnettoriEsposti() {
        int esposti = 0;

        for (int r = 0; r < matrice.length; r++) {
            for (int c = 0; c < matrice[0].length; c++) //Cicla su tutta la matrice
            	{
                Tessera tessera = matrice[r][c]; //Controllo tessera per tessera
                if (tessera == null) continue;  //Se cella vuota, va avanti

                for (EnumDirezione dir : EnumDirezione.values()) //Controllo tutti e quattro i lati
                {
                    if (isConnettoreEsposto(r, c, dir)) //Chiama metodo sul singolo connettore
                     esposti++;                
                }
            }
        }
        return esposti;
    }

    
    /**
     * Simula l'impatto di un meteorite piccolo.
     * Cerca di difendersi con uno scudo; se fallisce, distrugge il componente.
     *
     * @param direzione La direzione da cui proviene il meteorite.
     */
    public void risolviMeteoritePiccolo(EnumDirezione direzione) {
    	Dado dado = new Dado();
        Tessera bersaglio = null;
        int rTrovato = 0;
		int cTrovato = 0;
		int indiceRigaColonna = (dado.lancia() - 2) * matrice[0].length / 11;  //include estremo

        // 1. Trova la prima tessera colpita nella direzione corretta
        if (direzione == EnumDirezione.NORD) {
            for (int r = 0; r < matrice.length; r++) {
                if (matrice[r][indiceRigaColonna] != null) {
                    bersaglio = matrice[r][indiceRigaColonna];
                    rTrovato = r;
                    cTrovato = indiceRigaColonna;
                    break;
                }
            }
        } 
        	else if (direzione == EnumDirezione.EST) {
        		for (int c = matrice[0].length - 1; c >= 0; c--) {
        			if (matrice[indiceRigaColonna][c] != null) {
        				bersaglio = matrice[indiceRigaColonna][c];
        				rTrovato = indiceRigaColonna;
        				cTrovato = c;
        				break;
                }
            }
        }

        if (bersaglio == null) {
            return; //Il meteorite non colpisce nulla
        }

        // 2. Verifica se il connettore è esposto
        if (!isConnettoreEsposto(rTrovato, cTrovato, direzione)) {
            return; //Connettore non esposto, esce dal metodo
        }  

        // 3. Cerca uno scudo valido sulla nave
        for (int r = 0; r < matrice.length; r++) {
            for (int c = 0; c < matrice[r].length; c++) {
                Tessera t = matrice[r][c];
                if (t instanceof TesseraScudo scudo && scudo.proteggeDa(direzione)) {
                    if (usaBatteria()) 
                        return;
                     else 
                        break; // esci dal ciclo: non si attiva lo scudo
                    
                }
            }
        }

        // 4. Scudo non usato -> tessera distrutta
        rimuoviTessera(rTrovato, cTrovato);
    }

    /**
     * Controlla se un connettore su un lato specifico di una tessera è esposto.
     *
     * @param r Riga della tessera.
     * @param c Colonna della tessera.
     * @param direzione Il lato da controllare.
     * @return {@code true} se il connettore è esposto, altrimenti {@code false}.
     */
    public boolean isConnettoreEsposto(int r, int c, EnumDirezione direzione) {
        Tessera tessera = matrice[r][c];
        int lato = direzione.getAbbreviazioneNum();

        // Se non c'è connettore su questo lato, non è esposto; se non c'è tessera non è esposto
        if (tessera == null || tessera.getConnettore(lato) == 0) return false;

        // Calcola la cella adiacente nella direzione
        int rAd = r, cAd = c;
        switch (direzione) {
            case NORD -> rAd--; //salgo di una riga (decremento r)
            case EST  -> cAd++; //vado a destra (incremento c)
            case SUD  -> rAd++; //scendo di una riga (incremento r)
            case OVEST -> cAd--; //vado a sinistra (decremento c) 
        }

        // Se la cella adiacente è fuori matrice -> connettore è esposto
        if (rAd < 0 || rAd >= matrice.length || cAd < 0 || cAd >= matrice[0].length) {
            return true;  //Ovviamnete solo nel caso in cui la tessera è sul bordo matrice
        }

        // Se la cella adiacente è vuota -> connettore è esposto
        return matrice[rAd][cAd] == null;
    }
    



    /**
     * Simula l'impatto di un meteorite grande.
     * Cerca di difendersi con un cannone; se fallisce, distrugge il componente.
     *
     * @param direzione La direzione da cui proviene il meteorite.
     */
    public void risolviMeteoriteGrande(EnumDirezione direzione) {
    	Dado dado = new Dado();
        Tessera bersaglio = null;
        int rTrovato = 0;
		int cTrovato = 0;
		int indiceRigaColonna = (dado.lancia() - 2) * matrice[0].length / 11;  //include estremo

        // 1. Trova la prima tessera colpita in base alla direzione
        if (direzione == EnumDirezione.NORD) {
            for (int r = 0; r < matrice.length; r++) {
                if (matrice[r][indiceRigaColonna] != null) {
                    bersaglio = matrice[r][indiceRigaColonna];
                    rTrovato = r;
                    cTrovato = indiceRigaColonna;
                    break;
                }
            }
        } else if (direzione == EnumDirezione.EST) {
            for (int c = matrice[0].length - 1; c >= 0; c--) {
                if (matrice[indiceRigaColonna][c] != null) {
                    bersaglio = matrice[indiceRigaColonna][c];
                    rTrovato = indiceRigaColonna;
                    cTrovato = c;
                    break;
                }
            }
        }

        if (bersaglio == null) {
            return; //Uguale al meteorite piccolo
        }

        // 2. Cerca un cannone puntato nella direzione, sulla stessa riga/colonna
        for (int r = 0; r < matrice.length; r++) {
            for (int c = 0; c < matrice[0].length; c++) {
                Tessera t = matrice[r][c];
                if (t instanceof TesseraCannone cannone && cannone.puntaVerso(direzione)) {
                    boolean sullaLinea = (direzione == EnumDirezione.NORD && c == indiceRigaColonna)
                                      || (direzione == EnumDirezione.EST && r == indiceRigaColonna);
                    if (sullaLinea) {
                        if (cannone.isDoppio()) {
                            if (usaBatteria()) 
                                return; //Meteorite distrutto da cannone doppio
                             else 
                        		return;                            
                        } 
                        else 
                            return; //Meteorite distrutto da cannone piccolo                   
                    }
                }
            }
        }

        // 3. Nessun cannone -> tessera distrutta
        rimuoviTessera(rTrovato, cTrovato);
    }
    
    //LOGICA PER LA VERIFICA DEI COMPONENTI ------------------
    
  //Una classe di supporto interna per gestire le coordinate.
    //Rende il codice molto più leggibile che usare array di interi.
    //poteva tornare utile anche prima, ma ho scoperto solo ora le classi annidate.
    public static class Posizione { //l'ho resa pubblica per chiamarla anche in gamecli.
    public final int r, c;
    Posizione(int r, int c) {
      this.r = r;
      this.c = c;
    }
    }
    
    /**
     * Analizza l'integrità strutturale della nave per trovare pezzi disconnessi.
     * Utilizza un algoritmo di esplorazione (BFS) per identificare tutti i gruppi
     * di tessere connesse tra loro. Non modifica lo stato della nave.
     *
     * @return un {@link RisultatoVerifica} che descrive lo stato della nave e le azioni necessarie.
     */
    public static class RisultatoVerifica {
        public final String log;
        public final List<List<Posizione>> pezziDaRimuovere; //lista di liste di coordinate
        public final List<List<Posizione>> opzioniSceltaUtente; // lo stesos di sopra
        private final boolean sceltaNecessaria;

        // Costruttore privato (mi serve una factory perchè ho più casi)
        private RisultatoVerifica(String log, List<List<Posizione>> pezziDaRimuovere, List<List<Posizione>> opzioniSceltaUtente, boolean sceltaNecessaria) {
            this.log = log;
            this.pezziDaRimuovere = pezziDaRimuovere;
            this.opzioniSceltaUtente = opzioniSceltaUtente;
            this.sceltaNecessaria = sceltaNecessaria;
        }

     //METODI STATICI DI FABBRICA ---

        // Crea un risultato per una nave integra.
        public static RisultatoVerifica naveIntegra(String log) {
            return new RisultatoVerifica(log, Collections.<List<Posizione>>emptyList(), Collections.<List<Posizione>>emptyList(), false);
        }

        //Crea un risultato quando ci sono solo detriti da rimuovere automaticamente.
        public static RisultatoVerifica conDetriti(String log, List<List<Posizione>> detriti) {
            return new RisultatoVerifica(log, detriti, Collections.<List<Posizione>>emptyList(), false);
        }

        //Crea un risultato quando è necessaria una scelta da parte dell'utente.
        public static RisultatoVerifica conSceltaUtente(String log, List<List<Posizione>> opzioni) {
            return new RisultatoVerifica(log, Collections.<List<Posizione>>emptyList(), opzioni, true);
        }
        
        public boolean isSceltaUtenteNecessaria() {
            return this.sceltaNecessaria;
        }
    }
 


    /**
     * Analizza l'integrità della nave, identifica componenti disconnessi e
     * restituisce un oggetto RisultatoVerifica con le azioni da intraprendere.
     * Questo metodo NON modifica lo stato della nave.
     *
     * @return Un oggetto RisultatoVerifica che descrive lo stato della nave.
     */
    public RisultatoVerifica verificaIntegrita() {
        StringBuilder log = new StringBuilder();
        Posizione cabinaPos = trovaCabinaCentrale();

        if (tessereNave.isEmpty()) {
            return RisultatoVerifica.naveIntegra("  -> Nave completamente distrutta.\n");
        }
        
        boolean[][] visitato = new boolean[righe][colonne];
        List<List<Posizione>> componenti = new ArrayList<>();

        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                if (matrice[i][j] != null && !visitato[i][j]) {
                    List<Posizione> nuovoComponente = new ArrayList<>();
                    esploraComponente(new Posizione(i, j), visitato, nuovoComponente);
                    componenti.add(nuovoComponente);
                }
            }
        }

        if (componenti.size() <= 1) {
            log.append("  -> Nave integra.\n");
            return RisultatoVerifica.naveIntegra(log.toString());
        }
        
        log.append("  -> ATTENZIONE: La nave si è frammentata in ").append(componenti.size()).append(" pezzi!\n");

        if (cabinaPos != null) {
            List<List<Posizione>> pezziDaRimuovere = new ArrayList<>();
            for (List<Posizione> comp : componenti) {
                boolean contieneCabina = false;
                for (Posizione pos : comp) {
                    if (pos.r == cabinaPos.r && pos.c == cabinaPos.c) {
                        contieneCabina = true;
                        break;
                    }
                }
                if (!contieneCabina) {
                    pezziDaRimuovere.add(comp);
                }
            }
            int numTessereDaRimuovere = 0;
            for(List<Posizione> pezzo : pezziDaRimuovere) numTessereDaRimuovere += pezzo.size();
            log.append("  -> Il pezzo con la cabina è salvo. Verranno rimosse ").append(numTessereDaRimuovere).append(" tessere alla deriva.\n");
            return RisultatoVerifica.conDetriti(log.toString(), pezziDaRimuovere);

        } else {
            Collections.sort(componenti, new Comparator<List<Posizione>>() {
                @Override
                public int compare(List<Posizione> o1, List<Posizione> o2) {
                    return Integer.compare(o2.size(), o1.size());
                }
            });

            log.append("  -> La cabina è distrutta! È necessario scegliere quale pezzo tenere.\n");
            List<List<Posizione>> opzioni = new ArrayList<>();
            opzioni.add(componenti.get(0));
            if (componenti.size() > 1) {
                opzioni.add(componenti.get(1));
            }
            return RisultatoVerifica.conSceltaUtente(log.toString(), opzioni);
        }
    }

    /**
     * Esegue la rimozione effettiva delle tessere.
     */
    public void eseguiRimozione(List<List<Posizione>> pezziDaRimuovere) {
        for (List<Posizione> pezzo : pezziDaRimuovere) {
            for (Posizione pos : pezzo) {
            	Tessera rimossa = matrice[pos.r][pos.c];
                if (matrice[pos.r][pos.c] != null) {
                	 sottraiStatisticheTessera(rimossa);
                    tessereNave.remove(rimossa);
                    matrice[pos.r][pos.c] = null;
                    numeroComponentiRimossi++;
                }
            }
        }
    }

    /**
     * Algoritmo di esplorazione per trovare un componente connesso (BFS).
     */
    private void esploraComponente(Posizione start, boolean[][] visitato, List<Posizione> componente) {
        
    	Queue<Posizione> coda = new LinkedList<>(); 
        coda.add(start);
        visitato[start.r][start.c] = true;
        componente.add(start);

        while (!coda.isEmpty()) {
            Posizione attuale = coda.poll();
            int[] dr = {-1, 0, 1, 0}; int[] dc = {0, 1, 0, -1};
            for (int i = 0; i < 4; i++) {
                int nr = attuale.r + dr[i]; int nc = attuale.c + dc[i];

                if (inMatrice(nr, nc) && matrice[nr][nc] != null && !visitato[nr][nc]) {
                    Tessera tesseraAttuale = matrice[attuale.r][attuale.c];
                    Tessera tesseraVicina = matrice[nr][nc];
                    
                    int latoAttuale = i; int latoVicino = (i + 2) % 4;

                    if (connettoriCompatibili(tesseraAttuale.getConnettore(latoAttuale), tesseraVicina.getConnettore(latoVicino))) {
                        Posizione vicinoPos = new Posizione(nr, nc);
                        visitato[nr][nc] = true;
                        coda.add(vicinoPos);
                        componente.add(vicinoPos);
                    }
                }
            }
        }
    }

    /**
     * Metodo di supporto per trovare la cabina centrale.
     */
    private Posizione trovaCabinaCentrale() {
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                if (matrice[i][j] != null && matrice[i][j].getTipoTessera() == TipoTessera.CABINA_CENTRALE) {
                    return new Posizione(i, j);
                }
            }
        }
        return null;
    }
    
    /*
     * Verifica la presenza di almeno un motore sulla nave
     * * Restituisce true se sulla nave è presente almeno un motore
     * Altrimenti restituisce false
     * */
    public Boolean potenzaMotriceNonNulla() {

        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[i].length; j++) {
                Tessera tessera = matrice[i][j];
                if (tessera instanceof TesseraMotore && tessera.getInfo() == 1) {
                	return true;
                }
            }
        }
        return false;
    }
    
    /*
     * Verifica la presenza di almeno un componente sulla nave
     * * Restituisce true se sulla nave è presente almeno un componente
     * Altrimenti restituisce false
     * */
    public Boolean naveNonVuota() {

        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[i].length; j++) {
                if (matrice[i][j] != null) {
                	return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Sottrae le statistiche di una tessera dai totali della nave quando viene rimossa.
     * @param tessera La tessera che sta per essere rimossa.
     */
    private void sottraiStatisticheTessera(Tessera tessera) {
        if (tessera instanceof TesseraBatteria) {
            this.caricheBatterie -= tessera.getInfo();
        } 
        else if (tessera instanceof TesseraCabina) {
            this.numeroPersonale -= tessera.getInfo();
        } 
        else if (tessera instanceof TesseraCabinaCentrale) {
            this.numeroPersonale -= tessera.getInfo();
        } 
        else if (tessera instanceof TesseraStiva) {
            this.spazioStiva -= tessera.getInfo();
        } 
        else if (tessera instanceof TesseraStivaSpeciale) {
            this.spazioStivaSpeciale -= tessera.getInfo();
        }
     // Nessun'altra azione necessaria per cannoni o motori,
        // perché la loro potenza viene calcolata """dinamicamente""".
    }
    
    
}