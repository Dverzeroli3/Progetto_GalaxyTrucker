package galaxytrucker.gioco;

/*Con questa enum il resto del progetto (CLI, test, renderer) 
 * può controllare in modo sicuro lo stato della partita senza 
 * usare numeri magici o stringhe.
 */

/**
 * Stato “macro” in cui può trovarsi una partita.
 * <p>
 * La logica di cambio fase rimane tutta dentro {@link Gioco}.
 * </p>
 */
public enum Fase {
    /** I giocatori pescano e piazzano tessere, oppure passano. */
    COSTRUZIONE,

    /** Si rivelano le carte Avventura e si muovono le astronavi. */
    VOLO,

    /** Fine partita: calcolo dei punteggi e schermata riassuntiva. */
    FINE;
}
