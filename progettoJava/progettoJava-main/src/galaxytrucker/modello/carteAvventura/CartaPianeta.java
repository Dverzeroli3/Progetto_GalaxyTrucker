package galaxytrucker.modello.carteAvventura;

import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Rappresenta la carta avventura "Sbarco su Pianeti" nel gioco Galaxy Trucker.
 * Questa carta offre ai giocatori la possibilità di atterrare su diversi pianeti
 * per caricare merci, a fronte di una perdita di giorni di volo. Ogni pianeta può
 * essere occupato da un solo giocatore. I dettagli dei pianeti (colore merce, quantità,
 * giorni di volo persi) sono generati casualmente.
 */
public class CartaPianeta extends CartaAvventura {

    /**
     * Classe interna statica che rappresenta un singolo pianeta disponibile per lo sbarco.
     * Contiene le informazioni sulla merce offerta, la quantità, i giorni di volo persi
     * e un riferimento al giocatore che lo ha eventualmente occupato.
     */
    private static class Pianeta {
        /** Il colore della merce disponibile sul pianeta. */
        String colore;
        /** La quantità di merce disponibile sul pianeta. */
        int quantita;
        /** Il numero di giorni di volo persi atterrando su questo pianeta. */
        int giorniVolo;
        /** Il giocatore che ha occupato questo pianeta, o {@code null} se è libero. */
        Giocatore occupatoDa = null;

        /**
         * Costruisce un'istanza di Pianeta.
         * @param colore Il colore della merce.
         * @param quantita La quantità di merce.
         * @param giorniVolo Il numero di giorni di volo persi.
         */
        Pianeta(String colore, int quantita, int giorniVolo) {
            this.colore = colore;
            this.quantita = quantita;
            this.giorniVolo = giorniVolo;
        }
    }

    /**
     * La lista dei pianeti generati per questa specifica carta.
     */
    private final List<Pianeta> pianeti = new ArrayList<>();

    /**
     * Costruisce una nuova istanza della carta "Sbarco su Pianeti".
     * Al momento della creazione, vengono generati casualmente un numero di pianeti
     * (da 2 a 4) con caratteristiche (colore merce, quantità, giorni di volo) variabili.
     */
    public CartaPianeta() {
        super("Sbarco su Pianeti");

        String[] colori = {"blu", "verde", "giallo", "rosso"};
        int numeroPianeti = new Random().nextInt(3) + 2; // da 2 a 4

        for (int i = 0; i < numeroPianeti; i++) {
            String colore = colori[i % colori.length]; // Cicla tra i colori disponibili
            int quantita = new Random().nextInt(3) + 1; // 1-3 merci
            int giorni = new Random().nextInt(3) + 1;   // 1-3 giorni
            pianeti.add(new Pianeta(colore, quantita, giorni));
        }
    }

    /**
     * Applica l'effetto della carta "Sbarco su Pianeti".
     * I giocatori, in ordine di rotta, hanno la possibilità di scegliere un pianeta disponibile.
     * <ul>
     * <li>Prima vengono mostrati i pianeti disponibili e le loro caratteristiche.</li>
     * <li>Ogni giocatore, a partire dal leader, decide se sbarcare.</li>
     * <li>Se un giocatore decide di sbarcare, gli viene chiesto di scegliere un pianeta tra quelli non ancora occupati.</li>
     * <li>Dopo che tutti i giocatori hanno fatto la loro scelta, le merci vengono caricate e i giorni di volo persi
     * applicati in ordine inverso (dal giocatore più indietro al leader).</li>
     * </ul>
     *
     * @param ordineDiGioco La lista dei giocatori nell'ordine di turno attuale.
     * @param tabellone Il {@link galaxytrucker.modello.tabellone.Tabellone Tabellone} di gioco per aggiornare le posizioni.
     * @param inputProvider Un'interfaccia per fornire input utente per le scelte dei pianeti.
     * @return Una stringa che riassume l'esito dello sbarco sui pianeti per ogni giocatore coinvolto.
     */
    @Override
    public String applicaEffetto(List<Giocatore> ordineDiGioco, Tabellone tabellone, UserInputProvider inputProvider) {
        StringBuilder ret = new StringBuilder(); // Per il resoconto finale
        StringBuilder outputTemp = new StringBuilder(); // Per i messaggi intermedi da mostrare all'utente

        Giocatore leader = tabellone.getLeader();
        int indiceLeader = ordineDiGioco.indexOf(leader);

        // Riordina i giocatori a partire dal leader per la fase di scelta
        List<Giocatore> ordineSbarco = new ArrayList<>();
        ordineSbarco.addAll(ordineDiGioco.subList(indiceLeader, ordineDiGioco.size()));
        ordineSbarco.addAll(ordineDiGioco.subList(0, indiceLeader));

        // Mostra l'introduzione e i pianeti disponibili inizialmente
        outputTemp.append("Carta Pianeta: Opportunità di sbarco su pianeti!\n");
        for (int i = 0; i < pianeti.size(); i++) {
            Pianeta p = pianeti.get(i);
            outputTemp.append("[").append(i).append("] Pianeta ").append(p.colore)
                      .append(": ").append(p.quantita).append(" merci, -")
                      .append(p.giorniVolo).append(" giorni\n");
        }

        // Scelte dei giocatori
        for (Giocatore g : ordineSbarco) {
            List<Integer> opzioniDisponibili = new ArrayList<>();
            for (int i = 0; i < pianeti.size(); i++) {
                if (pianeti.get(i).occupatoDa == null) {
                    opzioniDisponibili.add(i);
                }
            }

            if (opzioniDisponibili.isEmpty()) {
                outputTemp.append("Nessun pianeta disponibile per ").append(g.getNome()).append(".\n");
                continue; // Passa al prossimo giocatore
            }

            outputTemp.append("\n").append(g.getNome()).append(", vuoi atterrare su un pianeta? (s/n):\n");
            // Mostra il messaggio all'utente e ottieni la risposta
            boolean vuoleSbarcare = inputProvider.askYesNo(outputTemp.toString());
            outputTemp.setLength(0); // Pulisce il buffer per il prossimo ciclo

            // Registra la risposta nel log finale
            ret.append(g.getNome()).append(" ha risposto ").append(vuoleSbarcare ? "s\n" : "n\n");

            if (vuoleSbarcare) {
                // Mostra i pianeti disponibili per la scelta
                for (int i : opzioniDisponibili) {
                    Pianeta p = pianeti.get(i);
                    outputTemp.append("[").append(i).append("] Pianeta ").append(p.colore)
                              .append(": ").append(p.quantita).append(" merci, -")
                              .append(p.giorniVolo).append(" giorni\n");
                }

                outputTemp.append(g.getNome()).append(", scegli un pianeta tra quelli disponibili:\n");
                // Richiedi la scelta del pianeta, con validazione dell'input
                int scelta = inputProvider.askInt(outputTemp.toString(),
                                                  opzioniDisponibili.get(0), // Minimo indice
                                                  opzioniDisponibili.get(opzioniDisponibili.size() - 1)); // Massimo indice
                outputTemp.setLength(0); // Pulisce il buffer

                // Ricontrolla se la scelta è valida nel caso l'input provider non abbia una validazione forte
                while (!opzioniDisponibili.contains(scelta)) {
                    scelta = inputProvider.askInt("Scelta non valida. Riprova: ",
                                                  opzioniDisponibili.get(0),
                                                  opzioniDisponibili.get(opzioniDisponibili.size() - 1));
                }

                Pianeta scelto = pianeti.get(scelta);
                scelto.occupatoDa = g; // Segna il pianeta come occupato
                ret.append(g.getNome()).append(" ha scelto il pianeta ").append(scelto.colore).append(".\n");
            }
        }

        // Fase di caricamento merci e penalità: in ordine inverso di rotta (dall'ultimo al leader)
        List<Giocatore> ordineInverso = new ArrayList<>(ordineSbarco);
        Collections.reverse(ordineInverso); // Inverte l'ordine dei giocatori

        for (Giocatore g : ordineInverso) {
            for (Pianeta p : pianeti) {
                if (p.occupatoDa == g) { // Trova il pianeta che questo giocatore ha occupato
                    g.getNave().caricaMerce(p.colore, p.quantita); // Carica la merce
                    tabellone.muoviGiocatore(g, -p.giorniVolo);    // Applica la penalità di giorni
                    ret.append(g.getNome())
                       .append(" ha caricato ")
                       .append(p.quantita).append(" merci ")
                       .append(p.colore)
                       .append(" e perso ")
                       .append(p.giorniVolo).append(" giorni di volo.\n");
                }
            }
        }

        // Restituisce il resoconto completo delle interazioni
        return ret.toString();
    }

    /**
     * Restituisce una descrizione concisa dell'effetto della carta "Sbarco su Pianeti".
     *
     * @return Una stringa che descrive l'effetto della carta.
     */
    @Override
    public String getDescrizione() {
        return "Puoi atterrare su pianeti per caricare merci, ma perdi giorni di volo. Solo uno per pianeta.";
    }
}