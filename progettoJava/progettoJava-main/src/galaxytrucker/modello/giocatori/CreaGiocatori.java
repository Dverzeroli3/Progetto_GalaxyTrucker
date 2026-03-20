package galaxytrucker.modello.giocatori;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe factory responsabile della creazione e inizializzazione dei giocatori.
 * <p>
 * Gestisce la logica di assegnazione dei colori, assicurando che ogni giocatore
 * scelga un colore unico tra quelli disponibili.
 */

public class CreaGiocatori {

    private List<Colore> coloriDisponibili;		//la lista serve per tenere traccia dei colori ancora disponibili
    
    /**
     * Costruttore, inizializza la lista dei colori disponibili
     * prendendoli dall'enum Colore.
     */
    public CreaGiocatori() {
        coloriDisponibili = new ArrayList<>();
      //aggiunge alla lista tutti i colori presenti nell'enum Colore
        for (Colore colore : Colore.values()) {
            coloriDisponibili.add(colore);
        }
    }
    
    /**
     * Restituisce la lista dei colori ancora disponibili per la scelta.
     *
     * @return una {@link List} di {@link Colore} disponibili.
     */
    public List<Colore> getColoriDisponibili() {
        return coloriDisponibili;
    }
    
    /**
     * Crea un nuovo giocatore con il nome e il colore specificati.
     * Rimuove il colore scelto dalla lista di quelli disponibili.
     *
     * @param nome il nome del giocatore.
     * @param colore il {@link Colore} scelto dal giocatore.
     * @return un nuovo oggetto {@link Giocatore}.
     * @throws IllegalArgumentException se il colore scelto non è più disponibile.
     */
    public Giocatore creaGiocatore(String nome, Colore colore) {
        if (!coloriDisponibili.contains(colore)) {
            throw new IllegalArgumentException("Colore non disponibile");
        }
        coloriDisponibili.remove(colore);
        return new Giocatore(nome, colore);
    }
}

    