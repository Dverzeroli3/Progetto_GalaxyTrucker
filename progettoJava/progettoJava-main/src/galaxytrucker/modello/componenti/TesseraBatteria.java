package galaxytrucker.modello.componenti;

/**
 * Rappresenta una tessera di tipo "Batteria" nel gioco Galaxy Trucker.
 * Questa tessera è caratterizzata da un numero specifico di cariche.
 *
 * Estende la classe {@link Tessera}, ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * Il numero di cariche della batteria è immutabile dopo la creazione della tessera.
 *
 * @see Tessera
 * @see TipoTessera
 */
public class TesseraBatteria extends Tessera {

    /**
     * Il numero di cariche della batteria.
     */
    private final int cariche;

    /**
     * Costruisce una nuova tessera Batteria con il numero di cariche specificato e i collegamenti ai lati.
     *
     * @param cariche Il numero di cariche della batteria (2 o 3).
     * @param nord    Il tipo di collegamento sul lato Nord della tessera.
     * @param est     Il tipo di collegamento sul lato Est della tessera.
     * @param sud     Il tipo di collegamento sul lato Sud della tessera.
     * @param ovest   Il tipo di collegamento sul lato Ovest della tessera.
     * @throws IllegalArgumentException Se il numero di cariche non è né 2 né 3.
     */
    public TesseraBatteria(int cariche, int nord, int est, int sud, int ovest) {
        super(TipoTessera.BATTERIA, nord, est, sud, ovest);
        if (cariche < 2 || cariche > 3) {
            throw new IllegalArgumentException("Una batteria può avere 2 o 3 cariche");
        }
        this.cariche = cariche;
    }

    /**
     * Restituisce il numero di cariche di questa tessera Batteria.
     *
     * @return Il numero di cariche della batteria.
     */
    @Override
    public int getInfo() {
        return cariche;
    }

    /**
     * Restituisce una rappresentazione in stringa di questa tessera Batteria,
     * includendo le informazioni della tessera base e il numero di cariche.
     * Questo metodo tiene conto dell'orientamento attuale della tessera.
     *
     * @return Una stringa che descrive la tessera Batteria e il suo stato attuale.
     */
    @Override
    public String toString() {
        return super.toString() + " [" + getInfo() + " cariche]";
    }

    /**
     * Restituisce una rappresentazione in stringa della tessera Batteria
     * per come era stata inizializzata, senza valutare eventuali rotazioni.
     *
     * @return Una stringa che descrive la tessera Batteria nel suo stato originale.
     */
    @Override
    public String toStringOriginale() {
        return super.toStringOriginale() + " [" + getInfo() + " cariche]";
    }

    /**
     * Restituisce una sigla formata dall'abbreviazione del tipo di tessera (es. "BA")
     * con alla fine il numero di cariche.
     * Ad esempio, una batteria con due cariche sarà "BA2", e con tre cariche "BA3".
     *
     * @return Una stringa che rappresenta la sigla della tessera Batteria.
     */
    @Override
    protected String stampaSigla() {
        return super.getTipoTessera().getAbbreviazione() + getInfo();
    }
}