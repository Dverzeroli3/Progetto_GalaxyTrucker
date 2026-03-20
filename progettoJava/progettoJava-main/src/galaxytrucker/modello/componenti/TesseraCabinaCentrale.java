package galaxytrucker.modello.componenti;

/**
 * Rappresenta la tessera della Cabina Centrale dell'astronave nel gioco Galaxy Trucker.
 * Questa è una tessera speciale e unica che funge da nucleo della nave.
 *
 * Estende la classe {@link Tessera}, ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * La cabina centrale inizia sempre con 2 unità di personale.
 *
 * @see Tessera
 * @see TipoTessera
 */
public class TesseraCabinaCentrale extends Tessera {
	/**
	 * Il numero di unità di personale presenti nella cabina centrale.
	 * Inizializzato a 2 alla creazione.
	 */
	private int personale;
	/**
	 * Costruisce una nuova tessera Cabina Centrale con i collegamenti ai lati specificati.
	 * Il personale della cabina centrale viene inizializzato a 2.
	 *
	 * @param nord   Il tipo di collegamento sul lato Nord della tessera.
	 * @param est    Il tipo di collegamento sul lato Est della tessera.
	 * @param sud    Il tipo di collegamento sul lato Sud della tessera.
	 * @param ovest  Il tipo di collegamento sul lato Ovest della tessera.
	 */
	public TesseraCabinaCentrale(int nord, int est, int sud, int ovest) {
		super(TipoTessera.CABINA_CENTRALE, nord, est, sud, ovest);	
		this.personale = 2;
	}

	/**
	 * Restituisce il numero di unità di personale presenti in questa tessera Cabina Centrale.
	 *
	 * @return Il numero di unità di personale.
	 */
	@Override
	public int getInfo() {
		return personale;
    }
}