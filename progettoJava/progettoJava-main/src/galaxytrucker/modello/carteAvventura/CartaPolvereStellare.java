package galaxytrucker.modello.carteAvventura;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

import java.util.*;

/**
 * Rappresenta la carta avventura "Polvere Stellare" nel gioco Galaxy Trucker.
 * Questa carta penalizza i giocatori facendoli perdere giorni di volo
 * in base al numero di connettori esposti sulla loro nave.
 */
public class CartaPolvereStellare extends CartaAvventura {

    /**
     * Costruisce una nuova istanza della carta "Polvere Stellare".
     */
    public CartaPolvereStellare() {
        super("Polvere Stellare");
    }

    /**
     * Applica l'effetto della carta "Polvere Stellare".
     * Ogni giocatore perde un numero di giorni di volo pari al numero di connettori
     * esposti sulla propria nave. L'effetto viene applicato ai giocatori
     * in ordine inverso di rotta (dal giocatore più indietro al leader).
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco per aggiornare le posizioni.
     * @param inputProvider Un'interfaccia per fornire input utente (non utilizzato direttamente da questa carta).
     * @return Una stringa che descrive quanti giorni ha perso ogni giocatore a causa della polvere stellare.
     */
    @Override
    public String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider) {
       StringBuilder ret = new StringBuilder();

        ret.append("Polvere Stellare! Ogni connettore esposto ti fa perdere 1 giorno di volo. \n");

        // Applica l'effetto in ordine inverso di rotta
        List<Giocatore> ordineInverso = new ArrayList<>(ordineDiGioco);
        Collections.reverse(ordineInverso); // Inverte l'ordine dei giocatori

        for (Giocatore g : ordineInverso) {
            // Ottiene il numero di connettori esposti dalla nave del giocatore
            int connettoriEsposti = g.getNave().getConnettoriEsposti();
            // Sposta il giocatore indietro sul tabellone
            tabellone.muoviGiocatore(g, -connettoriEsposti);
            ret.append(g.getNome()).append(" ha ").append(connettoriEsposti).append(" connettori esposti, quindi perde ").append(connettoriEsposti).append(" giorni di volo. \n");
        }
        return ret.toString();
    }

    /**
     * Restituisce una descrizione concisa dell'effetto della carta "Polvere Stellare".
     *
     * @return Una stringa che descrive l'effetto della carta.
     */
    @Override
    public String getDescrizione() {
        return "Polvere Stellare: ogni giocatore perde 1 giorno di volo per ogni connettore esposto (ordine inverso di rotta). \n";
    }
}