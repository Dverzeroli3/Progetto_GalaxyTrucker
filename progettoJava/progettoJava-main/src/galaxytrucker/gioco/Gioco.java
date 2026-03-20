package galaxytrucker.gioco;

import java.io.FileNotFoundException;
import java.util.*;

import galaxytrucker.modello.carteAvventura.CartaAvventura;
import galaxytrucker.modello.carteAvventura.CartaSpazioAperto;
import galaxytrucker.modello.carteAvventura.MazzoAvventuraBuilder;
import galaxytrucker.modello.componenti.Tessera;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.tabellone.Tabellone;
/**
 * Il motore principale del gioco Galaxy Trucker.
 * <p>
 * Questa classe orchestra le fasi della partita, gestisce lo stato del gioco
 * (giocatori, mazzi, tabellone) e contiene la logica centrale per le azioni
 * come pescare tessere, passare il turno e applicare gli effetti delle carte.
 * <p>
 * Agisce come "Model" nel pattern MVC, venendo manipolata dalla {@link galaxytrucker.cli.GameCLI}.
 * Controlla la validità delle azioni in base alle regole del gioco (es. un'azione
 * è permessa solo in una certa fase).
 */

//!!RICORDA; se l’azione può generare un errore di regola 
//(ordine, fase, limiti della griglia…), il controllo vive in Gioco ; 
//se è solo un errore di formato input (numero non intero, parola errata), vive in CLI.

public class Gioco {
	
	private List<Giocatore> giocatori; // Lista dei giocatori del gioco
	private List<Giocatore> giocatoriRitirati = new ArrayList<>(); 
	// Lista dei giocatori che hanno abbandonato la corsa
	private List<Giocatore> giocatoriEliminati = new ArrayList<>(); // Lista dei giocatori che sono stati eliminati
	
	// --- stato ---

    private final List<CartaAvventura> mazzo = new ArrayList<>();
	private final List<Tessera> sacchetto = new ArrayList<>();
	//Utilizzo list per 
	//utilizzo il generico per evitare problemi con i tipi delle carte 
	//e cast/ClassCastException
	
    private final Tabellone    tabellone;
    private int indiceTurno = 0;              // 0..giocatori-1
    private Fase fase = Fase.COSTRUZIONE;
    private boolean terminata = false;
 
    private final Set<Giocatore> hannoPassato = new HashSet<>();
   
    /**
     * Costruisce una nuova istanza di gioco.
     *
     * @param giocatori una lista di giocatori (da 2 a 4) già configurati.
     * @throws IllegalArgumentException se il numero di giocatori non è compreso tra 2 e 4.
     */
    public Gioco(List<Giocatore> giocatori) {
        if (giocatori == null || giocatori.size() < 2 || giocatori.size() > 4)
            throw new IllegalArgumentException("Devono essere 2-4 giocatori");
      //  this.giocatori = List.copyOf(giocatori);
        this.giocatori = giocatori;
		this.tabellone = new Tabellone();
        preparaSacchetto(); // riempi e mescola TODO
        preparaMazzo(); // riempi e mescola TODO
    }
    
    // --- Altri Metodi Utili al funzionamento di Gioco ---
    
    /**
     * Prepara il mazzo di carte avventura leggendole da un file di configurazione
     * e mescolandole.
     *
     * @throws IllegalStateException se il file delle carte non può essere letto o è vuoto.
     */
    private void preparaMazzo() {
    	
    
		try {
			mazzo.addAll(MazzoAvventuraBuilder.daFile("Carte.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Errore durante la lettura del file: ");
			e.printStackTrace();
		}
		if (mazzo.isEmpty()) {
			//errore di programmazione (strutturazine)
	        throw new IllegalStateException("Il file Componenti.txt è vuoto o mancante.");
	    }
	}
    

    /**
     * Prepara il sacchetto delle tessere componente leggendole da un file di
     * configurazione e mescolandole.
     *
     * @throws IllegalStateException se il file dei componenti non può essere letto o è vuoto.
     */
	private void preparaSacchetto() {
		
		sacchetto.clear(); // per sicurezza
	    // Riutilizzo il creatore già presente in Tessera
		sacchetto.addAll(Tessera.prendiTessereDaFile("Componenti.txt"));
	    if (sacchetto.isEmpty()) {
	        throw new IllegalStateException("Il file Componenti.txt è vuoto o mancante.");
	    }
	}
        
        

     // --- metodi usati dalla CLI ---
	/**
     * Restituisce la fase di gioco attuale.
     * @return la {@link Fase} corrente.
     */
	    public Fase   getFase()		{ return fase; }
	    /**
	     * Indica se la partita è terminata.
	     * @return {@code true} se la partita è finita, altrimenti {@code false}.
	     */
	    public boolean isTerminata()		{ return terminata; }
	    
	    /**
	     * Restituisce il giocatore il cui turno è attualmente in corso.
	     * @return il {@link Giocatore} attuale.
	     */
	    public Giocatore getGiocatoreAttuale()	{ return giocatori.get(indiceTurno); }
	    
	    /**
	     * Restituisce la lista di carte avventura rimanenti nel mazzo.
	     * @return una lista di {@link CartaAvventura}.
	     */
		public List<CartaAvventura> getMazzo() {
			
			return mazzo;
		}
		
		/**
	     * Restituisce la lista di tessere rimanenti nel sacchetto.
	     * @return una lista di {@link Tessera}.
	     */
		public List<Tessera> getSacchetto() {
			return sacchetto;
		}
		
		/**
	     * Restituisce la lista dei giocatori ancora in partita.
	     * @return una lista di {@link Giocatore}.
	     */
		public List<Giocatore> getGiocatori() {
			return giocatori;
		}
		
		/**
	     * Restituisce la lista dei giocatori che si sono ritirati.
	     * @return una lista di {@link Giocatore} ritirati.
	     */
		public List<Giocatore> getGiocatoriRitirati() {
			return giocatoriRitirati;
		}
		
		/**
	     * Restituisce l'istanza del tabellone di gioco.
	     * @return il {@link Tabellone} della partita.
	     */
		public Tabellone getTabellone() {
			return tabellone;
		}
	    
	 // Fase Costruzione
	    
		/**
	     * Pesca una tessera dal sacchetto.
	     * @return la {@link Tessera} pescata.
	     */
		 public Tessera pescaTessera() {
		    	
		    	Tessera tesseraPescata = sacchetto.remove(0); // "pescare"
		    	
		    	return tesseraPescata;
		    } 
		
		 /**
		     * Gestisce l'azione "passa" di un giocatore durante la fase di costruzione.
		     * Il giocatore attuale viene aggiunto all'insieme di coloro che hanno terminato
		     * la costruzione, e il turno passa al giocatore successivo.
		     */
	    public void passa() {
	        hannoPassato.add(getGiocatoreAttuale());   // segna che questo giocatore ha finito
	        prossimoGiocatore();                       // turno a quello dopo
	        
	    }
	    
	    /**
	     * Passa il turno al giocatore successivo nella fase di costruzione.
	     * Salta i giocatori che hanno già passato. Prima di passare il turno,
	     * controlla se è necessario cambiare fase.
	     */
		public void prossimoGiocatore() {
		
		
				controllaEcambiaFase(); // se tutti hanno passato ⇒ cambio fase
		
		do {
			
			if (indiceTurno<giocatori.size()-1) {
				
			indiceTurno++;
			
			} else {
				
			indiceTurno=0;
			
			}
			
		} while(hannoPassato.contains(getGiocatoreAttuale()));
		
	
			
		} 
	 
		/**
	     * Controlla le condizioni di cambio fase e, se soddisfatte, aggiorna lo stato del gioco.
	     * - Da {@link Fase#COSTRUZIONE} a {@link Fase#VOLO}: se tutti i giocatori hanno passato.
	     * - Da {@link Fase#VOLO} a {@link Fase#FINE}: se il mazzo di carte avventura è vuoto
	     *   o se non ci sono più giocatori in partita.
	     */
		 public void controllaEcambiaFase() {
			if (this.fase == Fase.COSTRUZIONE) {
				
				if (hannoPassato.size() == giocatori.size() || SetupStatico.isPreConfig()) {
				this.fase = Fase.VOLO;
				this.hannoPassato.clear(); // Resetta per un eventuale uso futuro
				
				// TODO Determinare l'ordine di gioco per il volo qui,
				// ad esempio basandomi sul leader sul tabellone.
				//per ora lasciamo quello di costruzione 
				 //(impone che il primo inserito è il leader)
				tabellone.preparaVolo(this.giocatori);
				
				 indiceTurno = 0;
				}
			} else {
				if (mazzo.isEmpty()) {
				this.fase = Fase.FINE;
				}
				if (giocatori.isEmpty()) {
					
					this.fase = Fase.FINE;
					
				}
				
			}
			
		}
		 
	 // Fase Volo
	    
		 /*La logica di pescaggio è qui perchè riguarda tutti i giocatori 
		  * e non uno singolo, e poi è in generale gestita in altre aree del programma*/
	   
		 /**
		     * Pesca una carta avventura dal mazzo.
		     * @return la {@link CartaAvventura} pescata, o null se il mazzo è vuoto.
		     */
		 public CartaAvventura pescaCarta() {
	         
		    	
		    	return(mazzo.remove(0)); // "pescare"
		    	
		        }
		 
		 
		 /**
		     * Esegue controlli preliminari sui giocatori all'inizio di un turno di volo.
		     * Verifica condizioni di eliminazione come:
		     * - Assenza di equipaggio umano.
		     * - Assenza di motori quando si pesca una carta "Spazio Aperto".
		     * - Essere doppiati dal leader.
		     * - Aver perso l'intera nave.
		     *
		     * @param pescata la carta avventura pescata per il turno.
		     * @return una stringa di log che descrive i giocatori eliminati e il motivo.
		     */
public String controlloGiocatori(CartaAvventura pescata) {
			 
			 StringBuilder log = new StringBuilder();
			 

			 for (Giocatore g : new ArrayList<>(giocatori)) {

				 
				 if(g.getNave().getTotaleUmani() == 0) {
					 
					 
					 log.append("\nGiocatore "+g.getNome()+" di colore "+g.getColore()
					 +" viene eliminato per assenza di personale umano nella nave\n");
					 ritiraGiocatore(g);
					 continue;

				 }
				 
				 if (pescata instanceof CartaSpazioAperto) {
					 
					if (!g.getNave().potenzaMotriceNonNulla()) {
						
						
						 log.append("\nGiocatore "+g.getNome()+" di colore "+g.getColore()
						 +" viene eliminato per assenza di potenza motrice nella nave all'arrivo dell'evento 'SpazioAperto'\n");
						 ritiraGiocatore(g);
						 continue;

					}
				}
				 
				 
			if (tabellone.confrontaProgressoGiocatori_doppiaggio(tabellone.getLeader(), g)) {
					
				
				 log.append("\nGiocatore "+g.getNome()+" di colore "+g.getColore()
				 +" viene eliminato perché il leader l'ha doppiato (che lumaca)\n");
				 ritiraGiocatore(g);

				 continue;
				 
				}
			
			if (!g.getNave().naveNonVuota()) {
				 
				
				 log.append("\nGiocatore "+g.getNome()+" di colore "+g.getColore()
				 +" viene eliminato perché non ha più una nave (- suoni di esplosioni -)\n");
				 ritiraGiocatore(g);

				 continue;
				 
			 }
			 
			 
		 }
	
			 return log.toString();
	}
	
	    
	 // Fase Fine
		
			//ordine di arrivo

/**
 * Assegna crediti ai giocatori in base alla loro posizione finale sulla rotta.
 * I giocatori più avanti ricevono più crediti.
 */
			public void assegnaCreditiPosizione() {
				List<Giocatore> classifica = new ArrayList<>(giocatori);  // array per ordinare
				int dimensione = Tabellone.DIMENSIONE;

				// Ordina i giocatori in base a numero giri e posizione sul tabellone
				classifica.sort((g1, g2) -> {
					int progresso1 = g1.getGiriCompletati() * dimensione + tabellone.getPosizioneGiocatore(g1);
					int progresso2 = g2.getGiriCompletati() * dimensione + tabellone.getPosizioneGiocatore(g2);
					return Integer.compare(progresso2, progresso1);
				});

				// Crediti da assegnare in base alla posizione
				int[] crediti = {4, 3, 2, 1};

				for (int i = 0; i < classifica.size(); i++) {
					Giocatore g = classifica.get(i);
					int c = crediti[i];
					g.aggiungiCrediti(c); //assegno crediti al giocatore
				}
			}

			//nave più bella (meno connettori esposti)
			
			/**
		     * Assegna crediti bonus al giocatore (o ai giocatori) con la "nave più bella",
		     * ovvero quella con il minor numero di connettori esposti.
		     */
			public void assegnaCreditiConnettoriEsposti() {
				int minConnettori = Integer.MAX_VALUE; //massimo intero possibile
				//minimo numero di connettori esposti tra i giocatori
				for (Giocatore g : giocatori) {
					int connettori = g.getNave().getConnettoriEsposti();
					if (connettori < minConnettori) {
						minConnettori = connettori;
					}
				}
				//assegno crediti al giocatore con il numero minimo di connettori esposti
				for (Giocatore g : giocatori) {
					int connettori = g.getNave().getConnettoriEsposti();
					if (connettori == minConnettori) {
						g.aggiungiCrediti(connettori);
					}
				}
			}

			//vendita delle merci
			
			/**
		     * Assegna crediti ai giocatori in base al valore delle merci che hanno
		     * trasportato fino alla fine.
		     */
			public void assegnaCreditiMerci(){
				for (Giocatore g : giocatori) {
					int creditiMerci = g.getNave().calcolaCreditiMerci();
					g.aggiungiCrediti(creditiMerci);
				}
			}
			
			//vendita delle merci dei giocatori ritirati (merci vendute a metà prezzo)
			
			/**
		     * Assegna crediti per le merci ai giocatori che si sono ritirati.
		     * Le loro merci vengono "svendute" a metà del loro valore.
		     */
			public void assegnaCreditiMerciRitirati(){
				for (Giocatore g : giocatoriRitirati) {
					double creditiMerci = g.getNave().calcolaCreditiMerci()/2;
					Math.ceil(creditiMerci);		// Arrotonda per eccesso
					g.aggiungiCrediti((int) creditiMerci);
				}
			}
			
			//componenti persi
			
			/**
		     * Sottrae crediti ai giocatori in base al numero di componenti della nave persi
		     * durante il volo.
		     */
			public void togliCreditiComponentiPersi() {
				//tolgo crediti in base al numero di componenti persi
				for (Giocatore g : giocatori) {
					int componentiPersi = g.getNave().getNumeroComponentiRimossi();
					g.togliCrediti(componentiPersi);
				}
			}
			
			//componenti persi da giocatori ritirati
			
			/**
		     * Sottrae crediti ai giocatori ritirati in base ai componenti persi.
		     */
			public void togliCreditiComponentiPersiRitirati() {
				//tolgo crediti in base al numero di componenti persi
				for (Giocatore g : giocatoriRitirati) {
					int componentiPersi = g.getNave().getNumeroComponentiRimossi();
					g.togliCrediti(componentiPersi);
				}
			}
			
			/**
		     * Sposta un giocatore dalla lista dei giocatori attivi a quella dei ritirati.
		     * Il giocatore viene anche rimosso dal tabellone.
		     *
		     * @param giocatore il giocatore da ritirare.
		     */
			public void ritiraGiocatore(Giocatore giocatore) {
				giocatoriRitirati.add(giocatore);
				giocatori.remove(giocatore);
				tabellone.rimuoviGiocatore(giocatore);
			}
			
			 /**
		     * Rimuove un giocatore dal gioco, aggiungendolo alla lista degli eliminati.
		     * @param giocatore il giocatore da eliminare.
		     */
			public void eliminaGiocatore(Giocatore giocatore) {
				giocatoriEliminati.add(giocatore);
				giocatori.remove(giocatore);
			}
			
}