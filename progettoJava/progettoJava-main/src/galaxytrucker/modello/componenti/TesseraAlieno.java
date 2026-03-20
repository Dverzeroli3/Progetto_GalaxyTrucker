package galaxytrucker.modello.componenti;

/**
 * Rappresenta una tessera di tipo "Alieno" nel gioco Galaxy Trucker.
 * Questa tessera è caratterizzata da un colore specifico, che può essere Viola o Marrone.
 *
 * Estende la classe {@link Tessera}, ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * Il colore dell'alieno è immutabile dopo la creazione della tessera.
 *
 * @see Tessera
 * @see TipoTessera
 */
public class TesseraAlieno extends Tessera {

    /**
     * Il colore dell'alieno.
     * 0 per Viola, 1 per Marrone.
     */
    private final int colore;

    /**
     * Costruisce una nuova tessera Alieno con il colore specificato e i collegamenti ai lati.
     *
     * @param colore L'ID del colore dell'alieno (0 per Viola, 1 per Marrone).
     * @param nord   Il tipo di collegamento sul lato Nord della tessera.
     * @param est    Il tipo di collegamento sul lato Est della tessera.
     * @param sud    Il tipo di collegamento sul lato Sud della tessera.
     * @param ovest  Il tipo di collegamento sul lato Ovest della tessera.
     * @throws IllegalArgumentException Se il valore del colore non è né 0 né 1.
     */
    public TesseraAlieno(int colore, int nord, int est, int sud, int ovest) {
        super(TipoTessera.ALIENO, nord, est, sud, ovest);
        if (colore < 0 || colore > 1) {
            throw new IllegalArgumentException("Una tessera alieno può essere solo viola o marrone. (valori 0 o 1)");
        }
        this.colore = colore;
    }

    /**
     * Restituisce l'ID numerico associato al colore dell'alieno di questa tessera.
     *
     * I valori possibili sono:
     * <ul>
     * <li>0: Viola</li>
     * <li>1: Marrone</li>
     * </ul>
     *
     * @return L'ID del colore dell'alieno (0 per Viola, 1 per Marrone).
     */
    @Override
    public int getInfo() {
        return colore;
    }

    /**
     * Restituisce la rappresentazione testuale del colore dell'alieno di questa tessera.
     *
     * @return Una stringa che rappresenta il colore ("Viola" o "Marrone").
     */
    @Override
    public String getInfoTestuale() {
        if (colore == 0) {
            return "Viola";
        } else {
            return "Marrone";
        }
    }

    /**
     * Restituisce una rappresentazione in stringa di questa tessera Alieno,
     * includendo le informazioni della tessera base e il colore dell'alieno.
     * Questo metodo tiene conto dell'orientamento attuale della tessera.
     *
     * @return Una stringa che descrive la tessera Alieno e il suo stato attuale.
     */
    @Override
    public String toString() {
        return super.toString() + " [" + getInfoTestuale() + "]";
    }

    /**
     * Restituisce una rappresentazione in stringa della tessera Alieno
     * per come era stata inizializzata, senza valutare eventuali rotazioni.
     *
     * @return Una stringa che descrive la tessera Alieno nel suo stato originale.
     */
    @Override
    public String toStringOriginale() {
        return super.toStringOriginale() + " [" + getInfoTestuale() + "]";
    }

    /**
     * Restituisce la sigla abbreviata della tessera Alieno,
     * composta dall'abbreviazione del tipo di tessera (es. "AL")
     * e la prima lettera del colore ("V" per Viola, "M" per Marrone).
     *
     * @return Una stringa che rappresenta la sigla della tessera Alieno.
     */
    @Override
    protected String stampaSigla() {
        return super.getTipoTessera().getAbbreviazione() + getInfoTestuale().charAt(0);
    }
}
