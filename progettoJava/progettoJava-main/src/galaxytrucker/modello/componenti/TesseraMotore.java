package galaxytrucker.modello.componenti;

/**
 * Rappresenta una tessera di tipo "Motore" nel gioco Galaxy Trucker.
 * Questa tessera fornisce propulsione all'astronave.
 *
 * Estende la classe {@link Tessera}, ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * Un motore può essere singolo o doppio.
 *
 * @see Tessera
 * @see TipoTessera
 */
public class TesseraMotore extends Tessera {

    /**
     * Il numero di motori su questa tessera (1 per singolo, 2 per doppio).
     */
    private final int numeroMotori;

    /**
     * Costruisce una nuova tessera Motore con il numero di motori specificato e i collegamenti ai lati.
     *
     * @param numeroMotori Il numero di motori (1 o 2).
     * @param nord         Il tipo di collegamento sul lato Nord della tessera.
     * @param est          Il tipo di collegamento sul lato Est della tessera.
     * @param sud          Il tipo di collegamento sul lato Sud della tessera.
     * @param ovest        Il tipo di collegamento sul lato Ovest della tessera.
     * @throws IllegalArgumentException Se il numero di motori non è né 1 né 2.
     */
    public TesseraMotore(int numeroMotori, int nord, int est, int sud, int ovest) {
        super(TipoTessera.MOTORE, nord, est, sud, ovest);
        if (numeroMotori < 1 || numeroMotori > 2) {
            throw new IllegalArgumentException("Un motore può essere singolo o doppio (1 o 2) ");
        }
        this.numeroMotori = numeroMotori;
    }

    /**
     * Restituisce il numero di motori su questa tessera.
     *
     * @return Il numero di motori.
     */
    @Override
    public int getInfo() {
        return numeroMotori;
    }

    /**
     * Restituisce una rappresentazione in stringa di questa tessera Motore,
     * includendo le informazioni della tessera base e il numero di motori.
     * Questo metodo tiene conto dell'orientamento attuale della tessera.
     *
     * @return Una stringa che descrive la tessera Motore e il suo stato attuale.
     */
    @Override
    public String toString() {
        return super.toString() + " [" + getInfo() + "  motore/i]";
    }

    /**
     * Restituisce una rappresentazione in stringa della tessera Motore
     * per come era stata inizializzata, senza valutare eventuali rotazioni.
     *
     * @return Una stringa che descrive la tessera Motore nel suo stato originale.
     */
    @Override
    public String toStringOriginale() {
        return super.toStringOriginale() + " [" + getInfo() + "  motore/i]";
    }

    /**
     * Restituisce una sigla formata dall'abbreviazione del tipo di tessera (es. "MO")
     * con alla fine il numero di motori.
     *
     * @return Una stringa che rappresenta la sigla della tessera Motore.
     */
    @Override
    protected String stampaSigla() {
        return super.getTipoTessera().getAbbreviazione() + getInfo();
    }

    /**
     * Restituisce una rappresentazione testuale della tessera Motore,
     * includendo i connettori sui lati e un simbolo che indica la direzione del motore
     * in base alla sua rotazione attuale.
     *
     * @return Una stringa formattata che visualizza la tessera e la direzione del motore.
     * @throws IllegalArgumentException Se la rotazione corrente non è valida.
     */
    @Override
    public String stampaTessera() {
        String n = String.valueOf(getConnettore(0));
        String e = String.valueOf(getConnettore(1));
        String s = String.valueOf(getConnettore(2));
        String o = String.valueOf(getConnettore(3));

        String simboloMotore = "#";

        switch (getRotazione()) {
            case 0: // Motore punta a Sud (lato opposto al Nord)
                s = simboloMotore;
                break;
            case 1: // Motore punta a Ovest (lato opposto all'Est)
                o = simboloMotore;
                break;
            case 2: // Motore punta a Nord (lato opposto al Sud)
                n = simboloMotore;
                break;
            case 3: // Motore punta a Est (lato opposto all'Ovest)
                e = simboloMotore;
                break;
            default:
                throw new IllegalArgumentException("Rotazione non valida: " + getRotazione());
        }

        return "|- " + n + " -|\n" +
                o + " " + stampaSigla() + " " + e + "\n" +
                "|- " + s + " -|";
    }

    /**
     * I motori non sono ruotabili. Questa implementazione sovrascrive il metodo {@code ruota()}
     * della classe base per impedire la rotazione, lasciando il corpo del metodo vuoto.
     */
    @Override
    public void ruota() {
        // Nessuna rotazione per i motori
        // System.out.println("Attenzione: i motori non possono essere ruotati.");
    }
}