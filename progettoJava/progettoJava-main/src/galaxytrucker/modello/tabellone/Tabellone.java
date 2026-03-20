package galaxytrucker.modello.tabellone;

import galaxytrucker.modello.giocatori.Giocatore;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Tabellone {
    public static final int DIMENSIONE = 30; // O qualsiasi dimensione tu preferisca
    private final Giocatore[] spazi;

    /**
     * Costruttore base. Inizializza un tabellone vuoto.
     */
    public Tabellone() {
        this.spazi = new Giocatore[DIMENSIONE];
        Arrays.fill(spazi, null);
    }

    /**
     * Posiziona i giocatori sul tabellone all'inizio del volo.
     * Il primo giocatore della lista parte in posizione 1, gli altri a seguire dietro di lui.
     * @param giocatoriInOrdineDiArrivo La lista dei giocatori in ordine di partenza.
     */
    public void preparaVolo(List<Giocatore> giocatoriInOrdineDiArrivo) {
        // La posizione di partenza per il leader.
        final int POSIZIONE_LEADER = 1;

        for (int i = 0; i < giocatoriInOrdineDiArrivo.size(); i++) {
            Giocatore g = giocatoriInOrdineDiArrivo.get(i);
            
            // Calcola la posizione andando all'indietro a partire da quella del leader.
            // Usa il metodo mod per gestire correttamente il "giro" del tabellone.
            // i=0 -> mod(1 - 0, 30) = 1
            // i=1 -> mod(1 - 1, 30) = 0
            // i=2 -> mod(1 - 2, 30) = mod(-1, 30) = 29
            // i=3 -> mod(1 - 3, 30) = mod(-2, 30) = 28
            int posizioneIniziale = mod(POSIZIONE_LEADER - i, DIMENSIONE);

            // Controlla che la posizione sia valida (anche se mod dovrebbe garantirlo)
            if (posizioneIniziale >= 0 && posizioneIniziale < DIMENSIONE) {
                this.spazi[posizioneIniziale] = g;
                
                // I giocatori posizionati "dietro" la linea di partenza (con un indice di posizione alto)
                // iniziano al giro -1, perché devono ancora completare il primo giro.
                // I giocatori "davanti" (con un indice di posizione basso) iniziano al giro 0.
                // Usiamo POSIZIONE_LEADER come punto di riferimento.
                if (posizioneIniziale > POSIZIONE_LEADER) {
                    // Esempio: POSIZIONE_LEADER = 1. Se sei a 29, 28... sei "dietro".
                    g.setGiriCompletati(-1);
               } else {
                    // Esempio: POSIZIONE_LEADER = 1. Se sei a 1 o 0, sei "davanti".
                    g.setGiriCompletati(0);
               }
            }
        }
    }

    /**
     * Muove un giocatore di un certo numero di PASSI VUOTI, in avanti o all'indietro.
     * La logica implementa il "salto" delle caselle occupate e aggiorna lo stato
     * dei giri completati in modo persistente sul giocatore.
     * @param g Il giocatore da muovere.
     * @param passiIl numero di passi vuoti da compiere (positivo per avanti, negativo per indietro).
     */
    public void muoviGiocatore(Giocatore g, int passi) {
        int posizioneAttuale = trovaGiocatore(g);
        if (posizioneAttuale == -1) {
        	
        	//Eccezione da lanciare TODO
        	
            return;
        }

        spazi[posizioneAttuale] = null;

        int passiRimanenti = Math.abs(passi);
        int direzione = Integer.signum(passi);
        int posizioneCorrente = posizioneAttuale;

        // Variabile per contare i giri effettuati *in questa mossa*
        int giriVariazione = 0;

        // Calcola la posizione finale saltando gli spazi occupati
        // e conta quante volte attraversiamo la linea del traguardo
        while (passiRimanenti > 0) {
            
            // Salva la posizione prima del passo
            int posPrimaDelPasso = posizioneCorrente;
            
            // Avanza di una casella
            posizioneCorrente = mod(posizioneCorrente + direzione, DIMENSIONE);

            // Controlla se la linea del traguardo è stata attraversata
            if (direzione > 0 && posPrimaDelPasso == DIMENSIONE - 1 && posizioneCorrente == 0) {
                giriVariazione++;
            } else if (direzione < 0 && posPrimaDelPasso == 0 && posizioneCorrente == DIMENSIONE - 1) {
                giriVariazione--;
            }

            // Se la casella è vuota, consuma un passo
            if (spazi[posizioneCorrente] == null) {
                passiRimanenti--;
            }
        }

        // Applica la variazione totale dei giri al contatore persistente del giocatore
        if (giriVariazione > 0) {
            g.incrementaGiri(giriVariazione);
        } else if (giriVariazione < 0) {
            g.decrementaGiri(Math.abs(giriVariazione));
        }
        
        // Riposiziona il giocatore nella sua nuova casella
        spazi[posizioneCorrente] = g;
    }

    /**
     * Calcola il punteggio di progresso totale per un singolo giocatore.
     * Il progresso è dato dai giri completati moltiplicati per la dimensione del
     * tabellone, più la posizione corrente.
     * @param g Il giocatore di cui calcolare il progresso.
     * @return un intero che rappresenta il progresso totale.
     */
    private int calcolaProgresso(Giocatore g) {
        if (g == null) {
            return -1; // O lancia un'eccezione, a seconda di come vuoi gestire il caso
        }
        int posizione = trovaGiocatore(g);
        // Se il giocatore non è sul tabellone, il suo progresso è considerato minimo.
        if (posizione == -1) {
            return -1;
        }
        return g.getGiriCompletati() * DIMENSIONE + posizione;
    }


    //La funzione di comparazione "brutale"
    /**
     * Confronta il progresso di due giocatori su questo tabellone.
     * Restituisce un valore che indica la loro posizione relativa.
     *
     * @param g1 Il primo giocatore.
     * @param g2 Il secondo giocatore.
     * @return - un valore negativo se g1 è più avanti di g2.
     *         - un valore positivo se g1 è più indietro di g2.
     *         - zero se hanno lo stesso identico progresso.
     */
    public int confrontaProgressoGiocatori(Giocatore g1, Giocatore g2) {
        int progresso1 = calcolaProgresso(g1);
        int progresso2 = calcolaProgresso(g2);

        // Confrontiamo in ordine inverso (progresso2 - progresso1) per ottenere
        // un risultato negativo quando il progresso1 è maggiore (quindi g1 è davanti).
        return Integer.compare(progresso2, progresso1);
    }

    /**
     * Controlla se il giocatore g1 ha doppiato il giocatore g2.
     * Un doppiaggio avviene quando il progresso totale di g1 supera quello di g2
     * di almeno la dimensione dell'intero tabellone.
     *
     * @param g1 Il giocatore che potrebbe aver doppiato.
     * @param g2 Il giocatore che potrebbe essere stato doppiato.
     * @return true se g1 ha doppiato g2, altrimenti false.
     */
    public boolean confrontaProgressoGiocatori_doppiaggio(Giocatore g1, Giocatore g2) {
        int progresso1 = calcolaProgresso(g1);
        int progresso2 = calcolaProgresso(g2);

     // Se il progresso di uno dei due non è calcolabile (es. giocatore non sul tabellone),
       // non può avvenire un doppiaggio.
        if (progresso1 == -1 || progresso2 == -1) {
            return false;
        }
        // Un doppiaggio avviene se la differenza di progresso è almeno pari
        // alla dimensione del tabellone.
        return progresso1 - progresso2 >= DIMENSIONE;
    }
    
    /**
     * Restituisce una lista di giocatori ordinati secondo la loro posizione sulla rotta,
     * dal leader all'ultimo.
     * @return Una lista ordinata di giocatori.
     */
    public List<Giocatore> getOrdineDiRotta() {
        List<Giocatore> giocatoriInGioco = new ArrayList<>();
        for (Giocatore g : spazi) {
            if (g != null) {
                giocatoriInGioco.add(g);
            }
        }
        
        // La sintassi 'this::confrontaProgressoGiocatori' è un "riferimento a metodo",
        // un modo conciso per dire a sort() di usare la nostra funzione per confrontare
        // gli elementi a due a due.
        giocatoriInGioco.sort(this::confrontaProgressoGiocatori);
        
        return giocatoriInGioco;
    }

    /**
     * Trova il giocatore in testa alla corsa.
     * @return Il giocatore leader, o null se non ci sono giocatori.
     */
    public Giocatore getLeader() {
        List<Giocatore> ordine = getOrdineDiRotta();
        return ordine.isEmpty() ? null : ordine.get(0);
    }
    
    private int trovaGiocatore(Giocatore g) {
        for (int i = 0; i < DIMENSIONE; i++) {
            if (g.equals(spazi[i])) {
                return i;
            }
        }
        return -1;
    }
    
    private int mod(int x, int m) {
        return (x % m + m) % m;
    }

    public int getPosizioneGiocatore(Giocatore g) {
        return trovaGiocatore(g);
    }

    public Giocatore[] getSpazi() {
        return Arrays.copyOf(spazi, spazi.length);
    }
    
    /**
     * Rimuove un giocatore dal tabellone, liberando la sua casella.
     * Utile quando un giocatore si ritira o viene eliminato.
     * @param g Il giocatore da rimuovere.
     */
    public void rimuoviGiocatore(Giocatore g) {
        int posizione = trovaGiocatore(g);
        if (posizione != -1) {
            spazi[posizione] = null;
        }
    }
}