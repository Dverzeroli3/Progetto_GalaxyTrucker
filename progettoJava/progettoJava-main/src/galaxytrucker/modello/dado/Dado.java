package galaxytrucker.modello.dado;

import java.util.Random;

/**
 * Rappresenta un dado utilizzato nel gioco per introdurre elementi di casualità.
 * <p>
 * Questa classe incapsula un generatore di numeri casuali e fornisce un
 * metodo per "lanciare" il dado, ottenendo un risultato in un intervallo
 * predefinito (da 2 a 12).
 */

public class Dado {
    private static final Random dado = new Random();  //Crea un'istanza di Random per generare numeri casuali, unica, per evitare problemi di lag e casualità semi-stabilita

    /**
     * Simula il lancio di due dadi a sei facce.
     *
     * @return un numero intero casuale compreso tra 2 e 12 (inclusi).
     */
    public int lancia() {
        return dado.nextInt(11) + 2; 
    }
}
