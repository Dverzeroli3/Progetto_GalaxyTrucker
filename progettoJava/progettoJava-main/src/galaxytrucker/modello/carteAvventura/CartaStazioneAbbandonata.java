package galaxytrucker.modello.carteAvventura;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Rappresenta la carta avventura "Stazione Abbandonata" nel gioco Galaxy Trucker.
 * Questa carta offre ai giocatori la possibilità di attraccare a una stazione
 * per caricare merci, a patto di avere un certo numero di membri dell'equipaggio
 * e subendo una perdita di giorni di volo.
 * I requisiti e le ricompense sono determinati casualmente.
 */
public class CartaStazioneAbbandonata extends CartaAvventura {

    /** Generatore di numeri casuali per i parametri della carta. */
    private final Random rand = new Random();
    /** Il numero di membri dell'equipaggio necessari per attraccare. */
    private final int equipaggioNecessario;
    /** Il numero di giorni di volo persi in caso di attracco. */
    private final int giorniPersi;

    /** Array di colori possibili per le merci di ricompensa (rosso e giallo). */
    private final String[] merciPossibili = {"rosso", "giallo"};
    /** Il colore della prima merce di ricompensa. */
    private final String merce1;
    /** Il colore della seconda merce di ricompensa. */
    private final String merce2;

    /**
     * Costruisce una nuova istanza della carta "Stazione Abbandonata".
     * Al momento della creazione, i parametri (equipaggio necessario, giorni persi,
     * e colori delle merci di ricompensa) vengono generati casualmente.
     */
    public CartaStazioneAbbandonata() {
        super("Stazione Abbandonata");
        this.equipaggioNecessario = rand.nextInt(3) + 2; // 2-4
        this.giorniPersi = rand.nextInt(2) + 1; // 1-2
        this.merce1 = merciPossibili[rand.nextInt(2)];
        this.merce2 = merciPossibili[rand.nextInt(2)];
    }

    /**
     * Applica l'effetto della carta "Stazione Abbandonata".
     * L'effetto si svolge come segue:
     * <ul>
     * <li>Vengono annunciati i requisiti (equipaggio necessario, merci ottenibili, giorni persi).</li>
     * <li>I giocatori, a partire dal leader e seguendo l'ordine di rotta, decidono se attraccare.</li>
     * <li>Se un giocatore ha sufficiente equipaggio e decide di attraccare:
     * <ul>
     * <li>Carica le due merci di ricompensa sulla sua nave.</li>
     * <li>Retrocede sul tabellone del numero di giorni persi.</li>
     * <li>L'evento si conclude per il primo giocatore che attracca, ma la decisione viene chiesta a tutti.</li>
     * </ul>
     * </li>
     * <li>Se un giocatore non ha equipaggio sufficiente o decide di non attraccare, l'opportunità passa al giocatore successivo.</li>
     * </ul>
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco per aggiornare le posizioni.
     * @param inputProvider Un'interfaccia per fornire input utente, usata per chiedere la decisione del giocatore.
     * @return Una stringa che riassume l'esito dell'interazione con la stazione abbandonata per ogni giocatore.
     */
    @Override
    public String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider) {
        StringBuilder ret = new StringBuilder();

        Giocatore leader = tabellone.getLeader();
        int indiceLeader = ordineDiGioco.indexOf(leader);

        // Riordina i giocatori a partire dal leader per la fase di decisione
        List<Giocatore> ordine = new ArrayList<>();
        ordine.addAll(ordineDiGioco.subList(indiceLeader, ordineDiGioco.size()));
        ordine.addAll(ordineDiGioco.subList(0, indiceLeader));

        // Annuncia le condizioni della stazione
        ret.append("Hai trovato una Stazione Abbandonata.\n");
        ret.append("Serve almeno ").append(equipaggioNecessario).append(" membri dell’equipaggio.\n");
        ret.append("Potrai caricare 2 merci: ").append(merce1).append(" e ").append(merce2).append(".\n");
        ret.append("Perderai anche ").append(giorniPersi).append(" giorni di volo.\n\n");

        boolean qualcunoAttracca = false;

        for (Giocatore g : ordine) {
            int equipaggioTotale = g.getNave().getTotaleEquipaggio();
            // Aggiunge informazioni per il giocatore corrente
            ret.append(g.getNome()).append(", hai ").append(equipaggioTotale).append(" membri dell’equipaggio.\n");

            if (equipaggioTotale >= equipaggioNecessario) {
                // Prepara il prompt completo per l'input utente
                String prompt = ret.toString() + g.getNome() + ", vuoi attraccare alla stazione? (s/n): ";
                String risposta = inputProvider.askString(prompt);

                // Resetta il StringBuilder per non ripetere messaggi nelle iterazioni successive
                ret.setLength(0);

                if (risposta.trim().equalsIgnoreCase("s")) {
                    g.caricaMerciRicompensa(merce1, 1);
                    g.caricaMerciRicompensa(merce2, 1);
                    tabellone.muoviGiocatore(g, -giorniPersi);
                    qualcunoAttracca = true;
                    ret.append(g.getNome()).append(" ha attraccato e caricato le merci.\n\n");
                } else {
                    ret.append(g.getNome()).append(" ha scelto di non attraccare.\n\n");
                }
            } else {
                ret.append(g.getNome()).append(" non ha abbastanza equipaggio per attraccare (richiesto: ").append(equipaggioNecessario).append(", disponibile: ").append(equipaggioTotale).append(").\n\n");
            }
        }

        if (!qualcunoAttracca) {
            ret.append("Nessuno ha attraccato alla stazione.\n");
        }

        return ret.toString();
    }

    /**
     * Restituisce una descrizione concisa dell'effetto della carta "Stazione Abbandonata".
     * La descrizione include i requisiti e le ricompense/penalità specifiche generate
     * per questa istanza della carta.
     *
     * @return Una stringa che descrive l'effetto della carta.
     */
    @Override
    public String getDescrizione() {
        return "Stazione Abbandonata: puoi caricare 2 merci (" + merce1 + " e " + merce2 +
               "), se hai almeno " + equipaggioNecessario + " membri equipaggio. Perderai " +
               giorniPersi + " giorni di volo.";
    }
}