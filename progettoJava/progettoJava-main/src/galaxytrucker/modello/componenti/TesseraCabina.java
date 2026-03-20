package galaxytrucker.modello.componenti;

/**
 * Rappresenta una tessera di tipo "Cabina" nel gioco Galaxy Trucker.
 * Questa tessera è un componente fondamentale dell'astronave e fornisce personale.
 *
 * Estende la classe {@link Tessera}, ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * Una cabina inizia sempre con 2 unità di personale.
 *
 * @see Tessera
 * @see TipoTessera
 */
public class TesseraCabina extends Tessera {
	/**
	 * Il numero di unità di personale presenti nella cabina.
	 * Inizializzato a 2 alla creazione.
	 */
	private int personale;

	/**
	 * Costruisce una nuova tessera Cabina con i collegamenti ai lati specificati.
	 * Il personale della cabina viene inizializzato a 2.
	 *
	 * @param nord   Il tipo di collegamento sul lato Nord della tessera.
	 * @param est    Il tipo di collegamento sul lato Est della tessera.
	 * @param sud    Il tipo di collegamento sul lato Sud della tessera.
	 * @param ovest  Il tipo di collegamento sul lato Ovest della tessera.
	 */
	public TesseraCabina(int nord, int est, int sud, int ovest) {
		super(TipoTessera.CABINA, nord, est, sud, ovest);
		this.personale = 2;
		}

	/**
	 * Restituisce il numero di unità di personale presenti in questa tessera Cabina.
	 *
	 * @return Il numero di unità di personale.
	 */
	@Override
	public int getInfo() {
		return personale;
    }
}