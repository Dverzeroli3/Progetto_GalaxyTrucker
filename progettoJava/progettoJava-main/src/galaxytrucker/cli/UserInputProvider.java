package galaxytrucker.cli;


/**
 * Definisce un'interfaccia per fornire un meccanismo di input utente astratto.
 * <p>
 * Questa interfaccia disaccoppia la logica di gioco (es. nelle carte avventura)
 * dalla specifica implementazione dell'input (es. {@link GameCLI}).
 * Permette alla logica di gioco di richiedere dati all'utente (numeri, sì/no, stringhe)
 * senza conoscere i dettagli su come questi dati vengono ottenuti.
 */

public interface UserInputProvider {
	//Interfaccia di accrocco per una soluzione intermedia sensa stravolgere le carte
	//e i due metodi problematici in nave
	
	 /**
     * Richiede all'utente di inserire un numero intero in un dato intervallo.
     *
     * @param msg il messaggio da visualizzare all'utente.
     * @param min il valore minimo accettabile (incluso).
     * @param max il valore massimo accettabile (incluso).
     * @return l'intero valido inserito dall'utente.
     */
 int askInt(String msg, int min, int max);
 
 /**
  * Pone una domanda sì/no all'utente.
  *
  * @param msg il messaggio da visualizzare all'utente.
  * @return {@code true} se la risposta è affermativa ('sì'), {@code false} altrimenti ('no').
  */
 boolean askYesNo(String msg);
 
 /**
  * Richiede all'utente di inserire una stringa generica.
  *
  * @param msg il messaggio da visualizzare all'utente.
  * @return la stringa inserita dall'utente.
  */
 String askString(String msg); // Utile per input testuali generici
}
