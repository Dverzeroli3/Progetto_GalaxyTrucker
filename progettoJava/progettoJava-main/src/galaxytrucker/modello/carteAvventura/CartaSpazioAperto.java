package galaxytrucker.modello.carteAvventura;

import java.util.*;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

/**
 * Rappresenta la carta avventura "Spazio Aperto" nel gioco Galaxy Trucker.
 * Questa carta consente ai giocatori di avanzare sul tabellone in base
 * alla potenza motrice della loro nave, offrendo la possibilità di attivare
 * motori doppi tramite batterie per un movimento aggiuntivo.
 */
public class CartaSpazioAperto extends CartaAvventura {

    /**
     * Costruisce una nuova istanza della carta "Spazio Aperto".
     */
    public CartaSpazioAperto() {
        super("Spazio Aperto");
    }

    /**
     * Applica l'effetto della carta "Spazio Aperto".
     * L'effetto si svolge come segue:
     * <ul>
     * <li>I giocatori vengono processati in ordine di rotta, a partire dal leader.</li>
     * <li>Per ogni giocatore, viene calcolata la potenza motrice della sua nave.
     * Durante questo calcolo, al giocatore viene chiesto se desidera attivare
     * eventuali motori doppi consumando batterie.</li>
     * <li>Il giocatore avanza sul tabellone di un numero di spazi pari alla sua
     * potenza motrice finale.</li>
     * </ul>
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco per aggiornare le posizioni.
     * @param inputProvider Un'interfaccia per fornire input utente, usata per chiedere l'attivazione dei motori doppi.
     * @return Una stringa che descrive l'avanzamento di ogni giocatore.
     */
    @Override
    public String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider) {
        StringBuilder ret= new StringBuilder();

        // Determina il leader
        Giocatore leader = tabellone.getLeader();
        int indiceLeader = ordineDiGioco.indexOf(leader);

        // Riordina i giocatori a partire dal leader
        List<Giocatore> ordineAttacco = new ArrayList<>();
        ordineAttacco.addAll(ordineDiGioco.subList(indiceLeader, ordineDiGioco.size()));
        ordineAttacco.addAll(ordineDiGioco.subList(0, indiceLeader));

        // A turno, ogni giocatore dichiara la propria potenza motrice e avanza
        for (Giocatore giocatore : ordineAttacco) {
            // Calcola la potenza motrice in modo autonomo, gestendo la richiesta di input per le batterie
            double potenzaMotrice = giocatore.getNave().calcolaPotenzaMotrice(inputProvider, giocatore.getNome());

            // Avanza il giocatore sulla rotta (n. di spazi in base alla potenza motrice)
            ret.append(giocatore.getNome()).append(" avanza di ").append((int)potenzaMotrice).append(" spazi! \n");
            tabellone.muoviGiocatore(giocatore, (int)potenzaMotrice);
        }
        return ret.toString();
    }

    /**
     * Restituisce una descrizione concisa dell'effetto della carta "Spazio Aperto".
     *
     * @return Una stringa che descrive l'effetto della carta.
     */
    @Override
    public String getDescrizione() {
        return """
                Carta: Spazio Aperto
                Ogni giocatore può avanzare sulla rotta dichiarando la propria potenza motrice.
                Ogni giocatore può decidere se usare una batteria per attivare i motori doppi (se disponibili).
                Non ci sono bonus, il numero di spazi in cui avanzi è pari alla tua potenza motrice.
                """;
    }
}