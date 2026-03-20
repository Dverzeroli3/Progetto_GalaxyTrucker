package galaxytrucker.modello.carteAvventura;

import java.util.List;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

/**
 * Rappresenta la carta avventura "Zona di Guerra" nel gioco Galaxy Trucker.
 * Questa carta penalizza i giocatori con le statistiche più basse (equipaggio,
 * potenza motrice, potenza di fuoco) in modo specifico per ciascun criterio.
 * In caso di parità, viene penalizzato il giocatore più avanti nella rotta.
 */
public class CartaZonaDiGuerra extends CartaAvventura {
	
    /** StringBuilder utilizzato per costruire il resoconto degli effetti della carta. */
	private StringBuilder ret = new StringBuilder();

    /**
     * Costruisce una nuova istanza della carta "Zona di Guerra".
     */
    public CartaZonaDiGuerra() {
        super("Zona di Guerra");
    }

    /**
     * Applica l'effetto della carta "Zona di Guerra".
     * L'effetto si divide in tre fasi, ognuna delle quali individua il giocatore
     * con la statistica più bassa (e più avanti in caso di parità) e gli applica una penalità:
     * <ol>
     * <li><b>Equipaggio:</b> Il giocatore con meno equipaggio perde 3 giorni di volo.</li>
     * <li><b>Motori:</b> Il giocatore con meno potenza motrice perde 2 membri dell'equipaggio.</li>
     * <li><b>Fuoco:</b> Il giocatore con meno potenza di fuoco subisce due cannonate (leggera e pesante),
     * gestite dalla sua nave.</li>
     * </ol>
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco per aggiornare le posizioni.
     * @param inputProvider Un'interfaccia per fornire input utente, usata per il calcolo della potenza motrice e di fuoco.
     * @return Una stringa che descrive dettagliatamente le penalità inflitte a ciascun giocatore.
     */
    @Override
    public String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider) {
        ret = new StringBuilder(); // resetta per ogni applicazione della carta

        // 1. Equipaggio: trova e penalizza il giocatore con meno equipaggio
        Giocatore peggiorEquipaggio = trovaPeggiore(ordineDiGioco, "equipaggio", tabellone, inputProvider);
        if (peggiorEquipaggio != null) {
            ret.append("Il giocatore con meno equipaggio è ").append(peggiorEquipaggio.getNome()).append(". Perde 3 giorni di volo.\n");
            tabellone.muoviGiocatore(peggiorEquipaggio, -3);
        }

        // 2. Motori: trova e penalizza il giocatore con meno potenza motrice
        Giocatore peggiorMotori = trovaPeggiore(ordineDiGioco, "motori", tabellone, inputProvider);
        if (peggiorMotori != null) {
            ret.append("Il giocatore con meno potenza motrice è ").append(peggiorMotori.getNome()).append(". Perde 2 membri dell’equipaggio.\n");
            peggiorMotori.getNave().consumaEquipaggio(2);
        }

        // 3. Fuoco: trova e penalizza il giocatore con meno potenza di fuoco
        Giocatore peggiorFuoco = trovaPeggiore(ordineDiGioco, "fuoco", tabellone, inputProvider);
        if (peggiorFuoco != null) {
            ret.append("Il giocatore con meno potenza di fuoco è ").append(peggiorFuoco.getNome()).append(". Subisce due cannonate (leggera e pesante).\n");

            // La nave gestisce la logica di difesa/danno
            boolean difesoLeggera = peggiorFuoco.getNave().risolviCannonataLeggera();
            ret.append("  Cannonata leggera: ").append(difesoLeggera ? "bloccata" : "colpita").append(".\n");

            boolean colpitaPesante = peggiorFuoco.getNave().risolviCannonataPesante();
            ret.append("  Cannonata pesante: ").append(colpitaPesante ? "colpita" : "non ha avuto effetto").append(".\n");
        }

        return ret.toString();
    }

    /**
     * Trova il giocatore "peggiore" in base a un criterio specificato.
     * In caso di parità nel valore del criterio, viene scelto il giocatore
     * che è più avanti nella rotta (ovvero, che appare prima nella lista
     * {@code ordineDiGioco}).
     *
     * @param ordine La lista dei giocatori nell'ordine di rotta.
     * @param criterio Il criterio da valutare ("equipaggio", "motori", "fuoco").
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco (utile per il leader, anche se non direttamente usato qui).
     * @param inputProvider Un'interfaccia per fornire input utente (passata per il calcolo di potenza motrice/fuoco).
     * @return Il {@link galaxytrucker.modello.giocatori.Giocatore Giocatore} peggiore secondo il criterio, o null se la lista è vuota.
     */
    private Giocatore trovaPeggiore(List<Giocatore> ordine, String criterio, Tabellone tabellone, UserInputProvider inputProvider) {
        double minimo = Double.MAX_VALUE; // Inizializza con un valore molto alto
        Giocatore peggiore = null;

        for (Giocatore g : ordine) {
            double valore = switch (criterio) {
                case "equipaggio" -> g.getNave().getTotaleEquipaggio();
                case "motori" -> g.getNave().calcolaPotenzaMotrice(inputProvider, g.getNome());
                case "fuoco" -> g.getNave().calcolaPotenzaDiFuoco(inputProvider, g.getNome());
                default -> 0.0; // Valore di default per criteri non riconosciuti
            };

            // Se il valore corrente è minore del minimo trovato finora, o
            // se i valori sono uguali ma il giocatore corrente è più avanti nella rotta
            if (valore < minimo || (valore == minimo && PiuAvanti(g, peggiore, ordine))) {
                minimo = valore;
                peggiore = g;
            }
        }
        return peggiore;
    }

    /**
     * Verifica se il giocatore {@code g1} è più avanti del giocatore {@code g2} nell'ordine di rotta.
     * Se {@code g2} è {@code null}, {@code g1} è considerato "più avanti" (o il primo).
     *
     * @param g1 Il primo giocatore.
     * @param g2 Il secondo giocatore da confrontare.
     * @param ordine La lista dei giocatori che definisce l'ordine di rotta.
     * @return {@code true} se {@code g1} è più avanti di {@code g2} (o se {@code g2} è null), {@code false} altrimenti.
     */
    private boolean PiuAvanti(Giocatore g1, Giocatore g2, List<Giocatore> ordine) 
    {
        if (g2 == null) 
    	return true; // Se g2 è null, g1 è il primo "peggiore" trovato finora
        return ordine.indexOf(g1) < ordine.indexOf(g2);  // g1 è più avanti se il suo indice è minore
    }
    
    /**
     * Restituisce una descrizione concisa dell'effetto della carta "Zona di Guerra".
     *
     * @return Una stringa che descrive l'effetto della carta.
     */
    @Override
    public String getDescrizione() {
        return "Zona di Guerra: penalizza i giocatori più deboli in equipaggio, motori e fuoco.";
    }
}