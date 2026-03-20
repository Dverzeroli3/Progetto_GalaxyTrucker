package galaxytrucker.modello.componenti;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una tessera di tipo "Stiva" nel gioco Galaxy Trucker.
 * Questa tessera è progettata per immagazzinare merci.
 *
 * Estende la classe {@link Tessera} e implementa l'interfaccia {@link InterfacciaMerci},
 * ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * Una stiva è caratterizzata da un numero definito di scomparti per la merce.
 *
 * @see Tessera
 * @see TipoTessera
 * @see InterfacciaMerci
 */
public class TesseraStiva extends Tessera implements InterfacciaMerci {
    /**
     * Lista delle merci attualmente immagazzinate nella stiva.
     */
    private List<String> merce = new ArrayList<>(); // ??TODO
    /**
     * Il numero di scomparti disponibili in questa tessera Stiva.
     */
    private final int numeroScomparti;

    /**
     * Costruisce una nuova tessera Stiva con il numero di scomparti specificato e i collegamenti ai lati.
     *
     * @param numeroScomparti Il numero di scomparti della stiva (2 o 3).
     * @param nord            Il tipo di collegamento sul lato Nord della tessera.
     * @param est             Il tipo di collegamento sul lato Est della tessera.
     * @param sud             Il tipo di collegamento sul lato Sud della tessera.
     * @param ovest           Il tipo di collegamento sul lato Ovest della tessera.
     * @throws IllegalArgumentException Se il numero di scomparti non è né 2 né 3.
     */
    public TesseraStiva(int numeroScomparti, int nord, int est, int sud, int ovest) {
        super(TipoTessera.STIVA, nord, est, sud, ovest);
        if (numeroScomparti < 2 || numeroScomparti > 3) {
            throw new IllegalArgumentException("Una stiva può contenere 2 o 3 scomparti. ");
        }
        this.numeroScomparti = numeroScomparti;
    }

    /**
     * Restituisce il numero di scomparti disponibili in questa tessera Stiva.
     *
     * @return Il numero di scomparti.
     */
    @Override
    public int getInfo() {
        return numeroScomparti;
    }

    /**
     * Restituisce il numero di scomparti disponibili in questa tessera Stiva.
     *
     * @return Il numero di scomparti.
     */
    public int getNumeroScomparti() {
        return numeroScomparti;
    }

    /**
     * Restituisce una rappresentazione in stringa di questa tessera Stiva,
     * includendo le informazioni della tessera base e il numero di scomparti.
     * Questo metodo tiene conto dell'orientamento attuale della tessera.
     *
     * @return Una stringa che descrive la tessera Stiva e il suo stato attuale.
     */
    @Override
    public String toString() {
        return super.toString() + " [" + getInfo() + "  scomparti]";
    }

    /**
     * Restituisce una rappresentazione in stringa della tessera Stiva
     * per come era stata inizializzata, senza valutare eventuali rotazioni.
     *
     * @return Una stringa che descrive la tessera Stiva nel suo stato originale.
     */
    @Override
    public String toStringOriginale() {
        return super.toStringOriginale() + " [" + getInfo() + "  scomparti]";
    }

    /**
     * Restituisce una sigla formata dall'abbreviazione del tipo di tessera (es. "ST")
     * con alla fine il numero di scomparti.
     *
     * @return Una stringa che rappresenta la sigla della tessera Stiva.
     */
    @Override
    protected String stampaSigla() {
        return super.getTipoTessera().getAbbreviazione() + getInfo();
    }

    /**
     * Lista delle merci attualmente immagazzinate nella stiva (duplicato o uso alternativo di 'merce').
     * Si noti che questo campo è un duplicato o una variazione del campo 'merce' già dichiarato.
     * È consigliabile unificare la gestione delle merci in un'unica lista.
     */
    private List<String> merci = new ArrayList<>();

    /**
     * Restituisce la lista delle merci attualmente immagazzinate in questa tessera Stiva.
     *
     * @return Una {@link List} di {@link String} che rappresenta le merci.
     */
    public List<String> getMerci() {
        return merci;
    }
}