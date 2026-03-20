package galaxytrucker.modello.componenti;

/**
 * Classe di supporto che rappresenta un componente della nave che può essere attivato,
 * come un motore doppio o un cannone doppio.
 * <p>
 * Incapsula le coordinate del componente (riga, colonna) e la potenza che
 * fornisce se attivato. Viene utilizzata nella logica di calcolo della potenza
 * per gestire le scelte dell'utente.
 */

public class ComponenteAttivabile {
	
	/** La riga in cui si trova il componente sulla griglia della nave. */
	 public final int r;
	 /** La colonna in cui si trova il componente sulla griglia della nave. */
	 public final int c;
	 
	 /** La potenza fornita dal componente se attivato. */
	    public final double potenza;

	    /**
	     * Costruisce un nuovo componente attivabile.
	     *
	     * @param r la riga del componente.
	     * @param c la colonna del componente.
	     * @param potenza la potenza fornita all'attivazione.
	     */
	    public ComponenteAttivabile(int r, int c, double potenza) {
	        this.r = r;
	        this.c = c;
	        this.potenza = potenza;
	    }

}
