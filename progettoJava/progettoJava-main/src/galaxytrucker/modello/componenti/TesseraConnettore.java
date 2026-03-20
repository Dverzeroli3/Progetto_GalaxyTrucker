package galaxytrucker.modello.componenti;
	
/**
 * Rappresenta una tessera di tipo "Connettore" nel gioco Galaxy Trucker.
 * Questa tessera è utilizzata per collegare diverse parti dell'astronave.
 *
 * Estende la classe {@link Tessera}, ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * Una tessera connettore non ha informazioni aggiuntive specifiche oltre i suoi collegamenti.
 *
 * @see Tessera
 * @see TipoTessera
 */
public class TesseraConnettore extends Tessera {
	/**
     * Costruisce una nuova tessera Connettore con i collegamenti ai lati specificati.
     *
     * @param nord   Il tipo di collegamento sul lato Nord della tessera.
     * @param est    Il tipo di collegamento sul lato Est della tessera.
     * @param sud    Il tipo di collegamento sul lato Sud della tessera.
     * @param ovest  Il tipo di collegamento sul lato Ovest della tessera.
     */
	public TesseraConnettore(int nord, int est, int sud, int ovest) {
		super(TipoTessera.CONNETTORE, nord, est, sud, ovest);	}

}
