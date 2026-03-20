package galaxytrucker.modello.carteAvventura;

import java.util.List;
import java.util.Random;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.componenti.EnumDirezione;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

/**
 * Rappresenta la carta avventura "Pioggia di Meteoriti" nel gioco Galaxy Trucker.
 * Questa carta simula un evento in cui le navi dei giocatori vengono colpite da meteoriti,
 * con diverse dimensioni e direzioni d'impatto, e con possibilità di difesa.
 */
public class CartaMeteoriti extends CartaAvventura {

    /**
     * Generatore di numeri casuali per determinare il numero e il tipo di meteoriti.
     */
    private final Random rand = new Random();

    /**
     * Costruisce una nuova istanza della carta "Pioggia di Meteoriti".
     */
    public CartaMeteoriti() {
        super("Pioggia di Meteoriti");
    }

    /**
     * Applica l'effetto della carta "Pioggia di Meteoriti" a tutti i giocatori.
     * Ogni giocatore subisce un numero casuale di meteoriti (da 1 a 3).
     * Per ogni meteorite, viene determinato casualmente se è "piccolo" o "grande"
     * e da quale direzione (NORD o EST) colpisce.
     * La nave di ciascun giocatore tenterà di risolvere l'impatto in base ai suoi componenti (scudi, cannoni).
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco (non direttamente usato in questa implementazione, ma parte della firma).
     * @param inputProvider Un'interfaccia per fornire input utente 
     * @return Una stringa che descrive l'esito della pioggia di meteoriti per ogni giocatore.
     */
    @Override
    public String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider) {
    	StringBuilder ret = new StringBuilder();
    	ret.append("Una pioggia di meteoriti si abbatte sulle navi! \n");

        for(Giocatore giocatore : ordineDiGioco) {
            int numMeteoriti = rand.nextInt(3) + 1; // da 1 a 3 meteoriti per questo giocatore
            ret.append("\n" + giocatore.getNome() + " affronta " + numMeteoriti + " meteoriti:"+ "\n");
            for (int i = 0; i < numMeteoriti; i++) {
                boolean grande = rand.nextBoolean(); // true per meteorite grande, false per piccolo
                // Determina casualmente se colpisce da Nord (verticale) o Est (orizzontale)
                EnumDirezione direzione = rand.nextBoolean() ? EnumDirezione.NORD : EnumDirezione.EST; 

                ret.append("  Meteorite #" + (i+1) + ": ");
                if (direzione == EnumDirezione.NORD) {
                    if (grande) {
                        ret.append("GRANDE da NORD!");
                        // Il metodo risolviMeteoriteGrande gestisce la logica di difesa e danno
                        giocatore.getNave().risolviMeteoriteGrande(EnumDirezione.NORD);
                    } else {
                        ret.append("PICCOLO da NORD!");
                        // Il metodo risolviMeteoritePiccolo gestisce la logica di difesa e danno
                        giocatore.getNave().risolviMeteoritePiccolo(EnumDirezione.NORD);
                    }
                } else { // EnumDirezione.EST
                    if (grande) {
                        ret.append("GRANDE da EST!");
                        giocatore.getNave().risolviMeteoriteGrande(EnumDirezione.EST);
                    } else {
                    	ret.append("PICCOLO da EST!");
                        giocatore.getNave().risolviMeteoritePiccolo(EnumDirezione.EST);
                    }
                }
            }
        }
        return ret.toString();
    }

    /**
     * Restituisce una descrizione dell'effetto della carta "Pioggia di Meteoriti".
     *
     * @return Una stringa che descrive l'effetto della carta.
     */
    @Override
    public String getDescrizione() {
        return "Pioggia di Meteoriti: i giocatori subiscono casualmente da 1 a 3 meteoriti.";
    }
}