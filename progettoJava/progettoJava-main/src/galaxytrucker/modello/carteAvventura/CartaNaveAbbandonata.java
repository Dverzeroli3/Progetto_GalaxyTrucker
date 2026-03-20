package galaxytrucker.modello.carteAvventura;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

/**
 * Rappresenta la carta avventura "Nave Abbandonata" nel gioco Galaxy Trucker.
 * Questa carta offre ai giocatori la possibilità di scambiare membri dell'equipaggio
 * per ottenere crediti, a fronte di una perdita di giorni di viaggio.
 * I requisiti e le ricompense variano casualmente ogni volta che la carta viene giocata.
 */
public class CartaNaveAbbandonata extends CartaAvventura {

    /**
     * Costruisce una nuova istanza della carta "Nave Abbandonata".
     */
    public CartaNaveAbbandonata() {
        super("Nave Abbandonata");
    }

    /**
     * Applica l'effetto della carta "Nave Abbandonata".
     * L'evento si sviluppa come segue:
     * <ul>
     * <li>Vengono generati casualmente i requisiti (equipaggio richiesto, crediti offerti, giorni persi).</li>
     * <li>I giocatori, a partire dal leader e seguendo l'ordine di rotta, hanno la possibilità di accettare l'offerta.</li>
     * <li>Se un giocatore accetta e ha sufficiente equipaggio:
     * <ul>
     * <li>Spende l'equipaggio richiesto.</li>
     * <li>Guadagna i crediti offerti.</li>
     * <li>Retrocede sul tabellone del numero di giorni persi.</li>
     * <li>L'evento termina (solo il primo giocatore che accetta beneficia della nave).</li>
     * </ul>
     * </li>
     * <li>Se un giocatore non ha sufficiente equipaggio o rifiuta, si passa al giocatore successivo.</li>
     * </ul>
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco per aggiornare le posizioni.
     * @param inputProvider Un'interfaccia per fornire input utente, usata per chiedere la decisione del giocatore.
     * @return Una stringa che riassume l'esito dell'interazione con la nave abbandonata.
     */
    @Override
    public String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider) {
        Random rand = new Random();
        final int equipaggioRichiesto = rand.nextInt(3) + 1;  // 1-3
        final int creditiOfferti = rand.nextInt(8) + 5;       // 5-12
        final int giorniPersi = rand.nextInt(2) + 1;          // 1-2

        StringBuilder resocontoFinale = new StringBuilder();

        Giocatore leader = tabellone.getLeader();
        int indiceLeader = ordineDiGioco.indexOf(leader);
        
        List<Giocatore> ordineOfferta = new ArrayList<>();
        ordineOfferta.addAll(ordineDiGioco.subList(indiceLeader, ordineDiGioco.size()));
        ordineOfferta.addAll(ordineDiGioco.subList(0, indiceLeader));

        boolean qualcunoHaAccettato = false;

        for (Giocatore g : ordineOfferta) {
            int equipaggioTotale = g.getNave().getTotaleEquipaggio();

            if (equipaggioTotale >= equipaggioRichiesto) {
                String domanda =  "\nTocca a " + g.getNome() + ". Hai " + equipaggioTotale + " membri di equipaggio."
                    		+ "\nVuoi spendere " + equipaggioRichiesto + " equipaggio per riparare "
                    		+ "la nave e guadagnare " + creditiOfferti + " crediti? (s/n): ";
                
                boolean risposta = inputProvider.askYesNo(domanda);

                if (risposta) {
                    g.getNave().consumaEquipaggio(equipaggioRichiesto);
                    g.aggiungiCrediti(creditiOfferti);
                    tabellone.muoviGiocatore(g, -giorniPersi);

                    resocontoFinale.append(String.format(
                    		"\n" + g.getNome() + " ha riparato la nave! Ha ottenuto " + creditiOfferti 
                    		+ " crediti scambiando " + equipaggioRichiesto + " membri di equipaggio"
                    		+ " e perdendo " + giorniPersi + " giorni."
                    ));
                    
                    qualcunoHaAccettato = true;
                    break;
                }
            } else {
                System.out.println("\nTocca a " + g.getNome() + ". Non hai abbastanza equipaggio (" 
                + equipaggioTotale + "/" + equipaggioRichiesto + ").");
            }
        }

        if (!qualcunoHaAccettato) {
            resocontoFinale.append("\nNessuno ha riparato la Nave Abbandonata.");
        }

        return resocontoFinale.toString();
    }

    /**
     * Restituisce una descrizione concisa dell'effetto della carta "Nave Abbandonata".
     *
     * @return Una stringa che descrive l'effetto della carta.
     */
    @Override
    public String getDescrizione() {
        return "Hai trovato una Nave Abbandonata! Puoi scambiare un certo numero di membri equipaggio "
        		+ "per crediti (il valore sarà casuale ogni volta che la carta viene giocata).";
    }
}