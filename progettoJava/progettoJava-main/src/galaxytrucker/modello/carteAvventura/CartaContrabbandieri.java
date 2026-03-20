package galaxytrucker.modello.carteAvventura;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

/**
 * Rappresenta la carta avventura "Contrabbandieri" nel gioco Galaxy Trucker.
 * Questa carta introduce un evento di combattimento contro un gruppo di contrabbandieri,
 * con esiti diversi in base alla potenza di fuoco dei giocatori.
 * La forza dei contrabbandieri e le penalità/ricompense sono determinate casualmente.
 */
public class CartaContrabbandieri extends CartaAvventura {

    /**
     * La forza di combattimento dei contrabbandieri.
     */
    private int forzaContrabbandieri;
    /**
     * Il numero di giorni (caselle sul tabellone) persi come penalità o ricompensa.
     */
    private int giorniPersi;
    /**
     * Il numero di merci più preziose perse in caso di sconfitta.
     */
    private int merciPerse;
    /**
     * Il colore della prima merce di ricompensa.
     */
    private String merce1;
    /**
     * Il colore della seconda merce di ricompensa.
     */
    private String merce2;

    /**
     * Generatore di numeri casuali per i parametri della carta.
     */
    private final Random random = new Random();

    /**
     * Costruisce una nuova istanza della carta "Contrabbandieri".
     * I parametri di forza, giorni persi e merci perse/ricompensa vengono
     * generati casualmente al momento della creazione della carta.
     */
    public CartaContrabbandieri() {
        super("Contrabbandieri");
        generaParametriCasuali();
    }

    /**
     * Genera casualmente i parametri specifici per questa istanza della carta "Contrabbandieri".
     * I valori sono:
     * <ul>
     * <li>Forza Contrabbandieri: tra 3 e 6.</li>
     * <li>Giorni Persi: tra 1 e 2.</li>
     * <li>Merci Perse: tra 1 e 2.</li>
     * <li>Merci di Ricompensa: "rosso" o "giallo".</li>
     * </ul>
     */
    private void generaParametriCasuali() {
        forzaContrabbandieri = 3 + random.nextInt(4); // 3–6
        giorniPersi = 1 + random.nextInt(2);          // 1–2
        merciPerse = 1 + random.nextInt(2);           // 1–2

        // Solo merci rosse e gialle
        String[] merciPossibili = {"rosso", "giallo"};
        merce1 = merciPossibili[random.nextInt(2)];
        merce2 = merciPossibili[random.nextInt(2)];
    }

    /**
     * Restituisce una descrizione dettagliata dell'effetto della carta "Contrabbandieri".
     * La descrizione include la forza dei contrabbandieri, le penalità in caso di sconfitta
     * e le ricompense in caso di vittoria.
     *
     * @return Una stringa formattata con la descrizione della carta.
     */
    @Override
    public String getDescrizione() {
        return String.format("""
            I Contrabbandieri attaccano ogni giocatore in ordine di rotta finché qualcuno li sconfigge.
            - Forza: %d
            - Penalità: perdi le %d merci più preziose (o batterie se non ne hai)
            - Ricompensa: carica 1 merce %s e 1 merce %s (perdendo %d giorn%s di volo)
            - In caso di pareggio, i contrabbandieri passano al giocatore successivo.
        """,
        forzaContrabbandieri,
        merciPerse,
        merce1,
        merce2,
        giorniPersi,
        giorniPersi > 1 ? "i" : "");
    }
   
    /**
     * Applica l'effetto della carta "Contrabbandieri".
     * I giocatori affrontano i contrabbandieri in ordine di rotta, a partire dal leader.
     * La battaglia continua finché un giocatore non sconfigge i contrabbandieri
     * o tutti i giocatori hanno tentato.
     * <ul>
     * <li><b>Vittoria:</b> Il giocatore ottiene merci di ricompensa e retrocede sul tabellone.</li>
     * <li><b>Sconfitta:</b> Il giocatore perde le merci più preziose (o batterie).</li>
     * <li><b>Pareggio:</b> I contrabbandieri passano al giocatore successivo senza effetti.</li>
     * </ul>
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco su cui agire.
     * @param inputProvider Un'interfaccia per fornire input utente, utile per chiedere l'attivazione dei cannoni doppi.
     * @return Una stringa che descrive l'esito dell'attacco dei contrabbandieri per ogni giocatore coinvolto.
     */
    @Override
    public String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider) {
        StringBuilder ret = new StringBuilder();
        Giocatore leader = tabellone.getLeader();

        ret.append("I Contrabbandieri attaccano con potenza " + forzaContrabbandieri + "!" +"\n");
        ret.append("Il leader è: " + leader.getNome()+ "\n");

        // Riordina l'ordine a partire dal leader
        int indiceLeader = ordineDiGioco.indexOf(leader);
        List<Giocatore> ordineAttacco = new ArrayList<>();
        ordineAttacco.addAll(ordineDiGioco.subList(indiceLeader, ordineDiGioco.size()));
        ordineAttacco.addAll(ordineDiGioco.subList(0, indiceLeader));

        boolean sconfitti = false;

        for (Giocatore giocatore : ordineAttacco) {
            ret.append("Tocca a " + giocatore.getNome());
            double potenzaGiocatore = giocatore.getNave().calcolaPotenzaDiFuoco(inputProvider, giocatore.getNome());
            ret.append(" Potenza di fuoco: " + potenzaGiocatore);

            if (potenzaGiocatore > forzaContrabbandieri) //vittoria
            {
                ret.append(" "+giocatore.getNome() + " ha sconfitto i Contrabbandieri! \n");

                // Ricompensa
                giocatore.caricaMerciRicompensa(merce1, 1);
                giocatore.caricaMerciRicompensa(merce2, 1);
                tabellone.muoviGiocatore(giocatore, -giorniPersi); // Retrocede di n caselle sul tabellone
                sconfitti = true;
                break;
            } else if (potenzaGiocatore < forzaContrabbandieri)  //sconfitta
            {
                ret.append(giocatore.getNome() + " ha perso contro i Contrabbandieri. \n");
                giocatore.perdiMerciPiuPreziose(merciPerse);
            } else {
                ret.append("Pareggio: i Contrabbandieri passano al prossimo giocatore. \n"); //pareggio
            }
        }

        if (!sconfitti) {
            ret.append("Nessun giocatore ha sconfitto i Contrabbandieri! \n");
        }
        return ret.toString();
    }
}