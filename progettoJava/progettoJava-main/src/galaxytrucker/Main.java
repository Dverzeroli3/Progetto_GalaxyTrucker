package galaxytrucker;

import java.util.Scanner;

import galaxytrucker.cli.GameCLI;

/**
 * Classe principale che funge da punto di ingresso per l'applicazione
 * Galaxy Trucker.
 * <p>
 * Il suo unico scopo è inizializzare e avviare l'interfaccia a riga di comando
 * {@link GameCLI}, che gestirà l'intero flusso di gioco.
 */
public class Main {
	
	/**
     * Il metodo main dell'applicazione.
     * Crea uno {@link Scanner} per leggere l'input dell'utente da console e lo
     * passa al costruttore di {@link GameCLI} per avviare la partita.
     * Utilizza un blocco try-with-resources per garantire la chiusura sicura
     * dello Scanner al termine del programma.
     *
     * @param args argomenti passati a riga di comando (non utilizzati).
     */
	    public static void main(String[] args) {
	        try (Scanner in = new Scanner(System.in)) {
	            new GameCLI(in).start();   // <-- tutto parte da qui
	        }
	    	
	    }

	}
	
