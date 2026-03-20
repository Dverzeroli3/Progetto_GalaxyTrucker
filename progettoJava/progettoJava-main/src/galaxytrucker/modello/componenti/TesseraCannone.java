package galaxytrucker.modello.componenti;

/**
 * Rappresenta una tessera di tipo "Cannone" nel gioco Galaxy Trucker.
 * Questa tessera fornisce capacità offensive all'astronave.
 *
 * Estende la classe {@link Tessera}, ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * Un cannone può essere singolo o doppio.
 *
 * @see Tessera
 * @see TipoTessera
 */
public class TesseraCannone extends Tessera {

    /**
     * Il numero di cannoni su questa tessera (1 per singolo, 2 per doppio).
     */
    private final int numeroCannoni;

    /**
     * Costruisce una nuova tessera Cannone con il numero di cannoni specificato e i collegamenti ai lati.
     *
     * @param numeroCannoni Il numero di cannoni (1 o 2).
     * @param nord          Il tipo di collegamento sul lato Nord della tessera.
     * @param est           Il tipo di collegamento sul lato Est della tessera.
     * @param sud           Il tipo di collegamento sul lato Sud della tessera.
     * @param ovest         Il tipo di collegamento sul lato Ovest della tessera.
     * @throws IllegalArgumentException Se il numero di cannoni non è né 1 né 2.
     */
    public TesseraCannone(int numeroCannoni, int nord, int est, int sud, int ovest) {
        super(TipoTessera.CANNONE, nord, est, sud, ovest);
        if (numeroCannoni < 1 || numeroCannoni > 2) {
            throw new IllegalArgumentException("Un cannone può essere singolo o doppio (1 o 2) ");
        }
        this.numeroCannoni = numeroCannoni;
    }

    /**
     * Restituisce il numero di cannoni su questa tessera.
     *
     * @return Il numero di cannoni.
     */
    @Override
    public int getInfo() {
        return numeroCannoni;
    }

    /**
     * Restituisce una rappresentazione in stringa di questa tessera Cannone,
     * includendo le informazioni della tessera base e il numero di cannoni.
     * Questo metodo tiene conto dell'orientamento attuale della tessera.
     *
     * @return Una stringa che descrive la tessera Cannone e il suo stato attuale.
     */
    @Override
    public String toString() {
        return super.toString() + " [" + getInfo() + "  cannone/i]";
    }

    /**
     * Restituisce una rappresentazione in stringa della tessera Cannone
     * per come era stata inizializzata, senza valutare eventuali rotazioni.
     *
     * @return Una stringa che descrive la tessera Cannone nel suo stato originale.
     */
    @Override
    public String toStringOriginale() {
        return super.toStringOriginale() + " [" + getInfo() + "  cannone/i]";
    }

    /**
     * Restituisce una sigla formata dall'abbreviazione del tipo di tessera (es. "CA")
     * con alla fine il numero di cannoni.
     *
     * @return Una stringa che rappresenta la sigla della tessera Cannone.
     */
    @Override
    protected String stampaSigla() {
        return super.getTipoTessera().getAbbreviazione() + numeroCannoni;
    }

    /**
     * Restituisce una rappresentazione testuale della tessera Cannone,
     * includendo i connettori sui lati e un simbolo che indica la direzione del cannone
     * in base alla sua rotazione attuale.
     *
     * @return Una stringa formattata che visualizza la tessera e la direzione del cannone.
     * @throws IllegalArgumentException Se la rotazione corrente non è valida.
     */
    @Override
    public String stampaTessera() {
        String n = String.valueOf(getConnettore(0));
        String e = String.valueOf(getConnettore(1));
        String s = String.valueOf(getConnettore(2));
        String o = String.valueOf(getConnettore(3));

        String simboloCannone = "@";

        switch (getRotazione()) {
            case 0: // Nord
                n = simboloCannone;
                break;
            case 1: // Est
                e = simboloCannone;
                break;
            case 2: // Sud
                s = simboloCannone;
                break;
            case 3: // Ovest
                o = simboloCannone;
                break;
            default:
                throw new IllegalArgumentException("Rotazione non valida: " + getRotazione());
        }

        return "|- " + n + " -|\n" +
                o + " " + stampaSigla() + " " + e + "\n" +
                "|- " + s + " -|";
    }

    /**
     * Verifica se il cannone di questa tessera punta verso la direzione specificata.
     *
     * @param direzione L'enumerazione {@link EnumDirezione} che rappresenta la direzione da controllare.
     * @return {@code true} se il cannone punta verso la direzione indicata, {@code false} altrimenti.
     */
    public boolean puntaVerso(EnumDirezione direzione) {
        return switch (direzione) {
            case NORD -> getRotazione() == 0;
            case EST -> getRotazione() == 1;
            case SUD -> getRotazione() == 2;
            case OVEST -> getRotazione() == 3;
        };
    }

    /**
     * Verifica se questa tessera Cannone è un cannone doppio.
     *
     * @return {@code true} se la tessera ha 2 cannoni, {@code false} altrimenti.
     */
    public boolean isDoppio() {
        return numeroCannoni == 2;
    }
}