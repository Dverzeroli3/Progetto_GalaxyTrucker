package galaxytrucker.modello.carteAvventura;

import java.util.List;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

/**
 * Rappresenta una carta Avventura generica nel gioco Galaxy Trucker.
 * Questa è una classe astratta che definisce l'interfaccia comune per tutte le carte Avventura,
 * le quali hanno un nome, una descrizione e un effetto che viene applicato durante il gioco.
 * <p>
 * Le classi che estendono {@code CartaAvventura} devono implementare i metodi
 * {@link #applicaEffetto(List, Tabellone, UserInputProvider)} e {@link #getDescrizione()}
 * per definire il comportamento specifico della carta.
 * </p>
 */
public abstract class CartaAvventura {
    /**
     * Il nome della carta avventura.
     */
    private final String nome;

    /**
     * Costruisce una nuova istanza di {@code CartaAvventura} con il nome specificato.
     *
     * @param nome Il nome univoco della carta avventura.
     */
    public CartaAvventura(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il nome della carta avventura.
     *
     * @return Il nome della carta avventura.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Applica l'effetto specifico di questa carta avventura sul gioco.
     * Questo metodo deve essere implementato dalle sottoclassi per definire l'azione della carta.
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco su cui agire.
     * @param inputProvider Un'interfaccia per fornire input utente, utile per carte che richiedono scelte.
     * @return Una stringa che descrive l'esito o l'azione intrapresa dall'applicazione dell'effetto.
     */
    public abstract String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider);

    /**
     * Restituisce una descrizione dettagliata dell'effetto della carta.
     * Questo metodo deve essere implementato dalle sottoclassi per fornire una spiegazione
     * leggibile dell'effetto della carta.
     *
     * @return Una stringa che descrive l'effetto della carta.
     */
    public abstract String getDescrizione();

    /**
     * Restituisce una rappresentazione in stringa della carta avventura, includendo il nome e la descrizione.
     *
     * @return Una stringa formattata con il nome e la descrizione della carta.
     */
    @Override
    public String toString() {
        return "Carta pescata: " + nome + " - " + getDescrizione();
    }
}