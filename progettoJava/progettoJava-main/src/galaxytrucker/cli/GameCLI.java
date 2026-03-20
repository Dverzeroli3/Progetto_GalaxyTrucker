package galaxytrucker.cli;

import java.util.*;

import galaxytrucker.gioco.Gioco;
import galaxytrucker.gioco.SetupStatico;
import galaxytrucker.gioco.Fase;
import galaxytrucker.modello.componenti.Tessera;
import galaxytrucker.modello.giocatori.CreaGiocatori;
import galaxytrucker.modello.giocatori.Colore;
import galaxytrucker.modello.giocatori.Giocatore;
import galaxytrucker.modello.carteAvventura.CartaAvventura;
import galaxytrucker.modello.nave.Nave;

/**
 * Interfaccia a riga di comando per <em>Galaxy Trucker</em> con flusso guidato.<br/>
 * L’utente non deve ricordare comandi testuali: la CLI pone domande e accetta
 * risposte semplici (numeri oppure «s / n»). Si percorrono in ordine le quattro
 * fasi canoniche del gioco:
 * <p>
 * 1. **Lobby** – raccolta nomi e colori.<br>
 * 2. **Costruzione** – turni «Pesca tessera / Passa».<br>
 * 3. **Volo** – risoluzione sequenziale delle carte avventura.<br>
 * 4. **Fine** – calcolo e stampa dei punteggi.
 * <p>
 * La classe crea internamente l’oggetto {@link Gioco}: il <code>main</code>
 * istanzia solo la CLI passando lo {@link java.util.Scanner} da usare per gli
 * input.
 */

public final class GameCLI implements UserInputProvider{

    /* ===================================================================== */
    /*  Risorse condivise                                                    */
    /* ===================================================================== */

    private final Scanner in;   // Lettore (tipicamente System.in)
    private final Gioco   gioco; // Model – logica di gioco

    /* ===================================================================== */
    /*  Costruttore                                                          */
    /* ===================================================================== */

    /**
     * Crea la CLI, interroga l’utente per la lobby e costruisce il Model.
     * @param in sorgente di input (non chiuso da questa classe)
     */
    public GameCLI(Scanner in) {
        this.in = Objects.requireNonNull(in);
        List<Giocatore> lobby = setupLobby(in);
        this.gioco = new Gioco(lobby);
    }

    /* ===================================================================== */
    /*  Metodo d’avvio                                                      */
    /* ===================================================================== */

    /** Avvia il flusso completo finché la partita non termina. */
    public void start() {
        System.out.println("== GALAXY TRUCKER ⨯ CLI – guided flow ==");
        if (!SetupStatico.isPreConfig()) {
        phaseCostruzione();  // Fase 1
        } else { gioco.controllaEcambiaFase(); }
        
        phaseVolo();         // Fase 2
        phaseFine();         // Fase 3

        System.out.println("\nPartita conclusa. Grazie per aver giocato!");
    }

    /* ===================================================================== */
    /*  Fase COSTRUZIONE                                                    */
    /* ===================================================================== */
    
    /**
     * Gestisce la fase di costruzione della nave per tutti i giocatori.
     * Cicla attraverso i turni dei giocatori, permettendo a ciascuno di pescare
     * una tessera, piazzarla o passare il proprio turno di costruzione.
     * Il ciclo termina quando tutti i giocatori hanno passato, causando il
     * cambio di fase a {@link Fase#VOLO}.
     */
    private void phaseCostruzione() {
    	//le assersioni sono "assunzioni" permettono un controllo di premessa
    	//che se non è soddisfatto lancia un'eccezione e blocca l'esecuzione.
        assert gioco.getFase() == Fase.COSTRUZIONE;
        System.out.println("\n――― Fase di COSTRUZIONE ―――");

        while (gioco.getFase() == Fase.COSTRUZIONE) {
        	Boolean stato = true;
            Giocatore current = gioco.getGiocatoreAttuale();
            System.out.println("\nTurno di " + current.getNome());
            do {
            System.out.println("1) Pesca tessera"
            		+ "\n2) Termina la costruzione e passa"
            		+ "\n3) !Help! - non so cosa fare");

            int choice = askInt("Scegli un'opzione: ", 1, 3);
            
            if (choice == 1) {
            	stato = handlePesca(current); 
            	
            } 
            if (choice == 2){ 
            	
            	gioco.passa(); 
            	stato=true;
            }
            
            if (choice == 3) {
            	printConstructionHelp();
            }
            
            }while(Boolean.FALSE.equals(stato));
        }
    }
    
    /**
     * Stampa una guida dettagliata per la fase di costruzione, usando la terminologia
     * e le abbreviazioni specifiche dell'interfaccia a riga di comando.
     */
    private void printConstructionHelp() {
        System.out.println("\n====================== GUIDA ALLA COSTRUZIONE ======================");
        System.out.println("Ciao Camionista! L'obiettivo è costruire la miglior nave possibile."
        		+" Pesca le tessere dal centro e agganciale alla tua nave."
        		+"\nSe ti stai chiedendo perchè non ci sono i livelli 2 e 3, paga! così lavoriamo e li implementiamo più velocemente :)");

        System.out.println(
            """

            --- REGOLE FONDAMENTALI DI PIAZZAMENTO (da Pag. 4-5 del regolamento) ---
            1.  La prima tessera è la cabina di partenza (CC), già posizionata.
            2.  Ogni nuova tessera deve essere piazzata in una casella vuota ADIACENTE ad almeno una tessera già presente.
            3.  Una volta piazzata, una tessera è "saldata" e NON può più essere spostata.

            --- I CONNETTORI (Pag. 5) ---
            Per agganciare una tessera, i suoi lati devono connettersi legalmente.
            Nella nostra interfaccia, i connettori sono rappresentati da numeri:

            - '3' (Universale): Si connette con QUALSIASI altro connettore (1, 2, o 3). È il tuo migliore amico.
            - '2' (Doppio):     Si connette SOLO con altri connettori doppi (2) o universali (3).
            - '1' (Singolo):    Si connette SOLO con altri connettori singoli (1) o universali (3).
            - '0' (Lato Liscio): NON è un connettore. È un muro. NON può essere connesso a nulla.
                               Due lati lisci possono stare uno accanto all'altro, ma non contano come connessione.

            ATTENZIONE: Connettori singoli (1) e doppi (2) NON sono compatibili tra loro!

            --- I COMPONENTI DELLA NAVE (e le loro abbreviazioni) ---
            Sulla plancia, le tessere sono identificate da una sigla. Ecco cosa significano:

            - CC (Cabina Centrale):   È il tuo punto di partenza. Ospita 2 membri dell'equipaggio.
            - CA (Cabina):            Ospita altri 2 membri dell'equipaggio. Più ne hai, più personale puoi avere.
            
            - MO (Motore):            Aumenta la velocità della nave. IMPORTANTE: il loro scarico (indicato da '*')
                                      deve puntare verso il retro della nave (in basso) e la casella bersaglio deve essere VUOTA.
            - MO2 (Motore Doppio):    Vale quanto 2 motori singoli, ma per attivarlo durante il volo consumerà una batteria (BA).
                                      Senza batterie disponibili, è completamente inutile.
            
            - CN (Cannone):           Serve a distruggere meteoriti e nemici. IMPORTANTE: la loro bocca da fuoco (indicata da '*')
                                      può puntare ovunque, ma la casella bersaglio deve essere VUOTA.
            - CN2 (Cannone Doppio):   Ha la potenza di 2 cannoni singoli, ma per attivarlo consumerà una batteria (BA).
                                      Senza batterie, non può sparare.

            - BA (Batteria):          Fornisce energia. Ogni tessera BA contiene 2 o 3 cariche. È FONDAMENTALE per attivare
                                      scudi (SC) e/o componenti doppi (MO2, CN2).
            
            - ST (Stiva):             Serve a trasportare le merci che troverai durante il viaggio.
            - SS (Stiva Speciale):    Una stiva rinforzata che può trasportare anche le preziose (e pericolose) merci rosse.
            - SC (Scudo):             Protegge la nave da colpi leggeri e piccoli meteoriti. Consuma una batteria per attivarsi.
                                      L'orientamento è cruciale per coprire i lati esposti.
            - CO (Connettore):        Sono "moduli strutturali". Non fanno nulla di speciale, ma hanno tanti connettori.
                                      Usali per rendere la tua nave più robusta e tappare i buchi.
            - AL (Alieno):            Strutture aliene misteriose. Per il primo volo, considerale come dei Connettori (CO).

            --- ERRORI COMUNI DA EVITARE (da Pag. 8) ---
            - Connettere un '1' con un '2'.
            - Piazzare un motore o un cannone con la sua zona '*' (asterisco) occupata.
            - Piazzare un motore (MO o MO2) che non punta verso il basso.
            - Lasciare pezzi della nave non connessi al corpo principale.

            Buona fortuna, e che la tua nave non vada in mille pezzi al primo meteorite!
            P.S: Questa è una guida rapida, per una comprensione totale ti invito a cercare il regolamento completo di Galaxy Trucker in PDF.
            ======================================================================"""
        );
    }
    
    /**
     * Gestisce l'intera micro sequenza «pesca tessera → piazza/scarta».
     * Mostra al giocatore la tessera pescata e la sua nave attuale. Chiede se
     * desidera piazzarla. Se sì, richiede coordinate e rotazione e tenta di
     * piazzare la tessera. In caso di errore, permette al giocatore di riprovare.
     *
     * @param player il giocatore che sta eseguendo l'azione.
     * @return {@code true} se un'azione valida (piazza o passa) è stata completata,
     *         {@code false} se il giocatore ha solo scartato la tessera e deve
     *         scegliere una nuova azione.
     */
    private boolean handlePesca(Giocatore player) {
    	Boolean piazzatagiusta = true;
        Tessera t = gioco.pescaTessera();
        System.out.println(player.getNave().stampaNaveConGriglia());
        System.out.println("Hai pescato: " + t);
        System.out.println(t.stampaTessera());
        
        do {
        	
        boolean place = askYesNo("Vuoi piazzarla? (s/n): ");
        if (!place) {
            System.out.println("Tessera scartata.");
            return false;
        }
        
        
        	
        
        int r   = askInt("Riga: ", 0, 10);      // range placeholder
        int col = askInt("Colonna: ", 0, 10);
        int rot = askInt("Numero Rotazioni a Destra (0-3): ", 0, 3);

        try {
        	
        	piazzatagiusta = true;
            player.piazzaTessera(t, r, col, rot);
            
            } catch (RuntimeException ex) { //un pò troppo generico per tutti gli errori che ci sono TODO
            System.out.println("Errore:  " + ex.getMessage());//dovremmo fare tipi di errori personalizzati
            System.out.println("\n"+"- Riprova -"+"\n");
            piazzatagiusta = false;
        }
      
        } while(Boolean.FALSE.equals(piazzatagiusta));
        
        System.out.println(player.getNave().stampaNaveConGriglia());
        System.out.println("Tessera piazzata.");
        gioco.controllaEcambiaFase();
        gioco.prossimoGiocatore();
        
        return true;
    }

    /* ===================================================================== */
    /*  Fase VOLO                                                           */
    /* ===================================================================== */
    
    /**
     * Gestisce la fase di volo.
     * In un ciclo, pesca una carta avventura e ne applica gli effetti.
     * Prima di ogni carta, offre ai giocatori l'opzione di ritirarsi dalla corsa.
     * Dopo l'effetto di ogni carta, esegue un controllo di integrità su tutte
     * le navi per rimuovere pezzi disconnessi.
     * La fase termina quando il mazzo di carte avventura è esaurito o quando
     * tutti i giocatori sono stati eliminati o si sono ritirati.
     */
    private void phaseVolo() {
        assert gioco.getFase() == Fase.VOLO : "Non siamo nella fase di volo";
        System.out.println("\n――― Fase di VOLO ―――\n(premi Invio per la carta successiva, 'help' per aiuto, 'mostra navi' per mostrare le navi)");
        stampaTabellone();
        
        while (gioco.getFase() == Fase.VOLO) {
            System.out.print("\n [VOLO] > ");
            String line = in.nextLine().trim();
            if (line.equalsIgnoreCase("help")) { printHelp(); continue; }
            if (line.equalsIgnoreCase("mostra navi")) { 
                stampaNavi();
                continue; 
            }
            
            CartaAvventura pescata = gioco.pescaCarta();
            
            if (pescata == null) {
                System.out.println("Il mazzo di carte avventura è esaurito! La fase di volo è finita");
                gioco.controllaEcambiaFase();
                continue;
            } 

            System.out.println(gioco.controlloGiocatori(pescata));
            
            // Chiedo se qualcuno vuole ritirarsi dalla corsa
            boolean qualcunoVuoleRitirarsi = askYesNo("Qualcuno vuole ritirarsi dalla corsa? (s/n): ");

            if (qualcunoVuoleRitirarsi) {
                // Chiedo a ciascun giocatore se vuole ritirarsi
            
            	// 1. Raccogliamo chi vuole ritirarsi per non modificare la lista mentre la cicliamo
            	List<Giocatore> daRitirare = new ArrayList<>();
            	// È più sicuro iterare su una copia della lista se la si modifica, ma qui raccogliamo solo i nomi
            	for (Giocatore g : new ArrayList<>(gioco.getGiocatori())) {
            	    boolean abbandonaCorsa = askYesNo(g.getNome() + ", vuoi abbandonare la corsa? (s/n): ");
            	    if(abbandonaCorsa){
            	        System.out.println("Il giocatore " + g.getNome() + " si è ritirato dalla corsa.");
            	        daRitirare.add(g);
            	    }
            	}

            	// 2. Ora eseguiamo il ritiro usando il metodo centralizzato di Gioco
            	for (Giocatore g : daRitirare) {
            	    gioco.ritiraGiocatore(g); // Questo metodo ora fa tutto il lavoro necessario!
            	}
            }
 
            gioco.controllaEcambiaFase();
            if (gioco.getGiocatori().isEmpty()) {
            	System.out.println("\nTutti i giocatori si sono ritirati; La fase di volo è terminata\n");
            	continue;
            } 
            
            
            System.out.println(pescata.toString());
            
            // Applica l'effetto della carta
            System.out.println(pescata.applicaEffetto(gioco.getGiocatori(), gioco.getTabellone(), this));
            
            System.out.println("\n...turno concluso.\n"); 
            
         // Al posto del Thread.sleep, ora aspettiamo che l'utente prema Invio. molto meglio secondo me.
            System.out.print("\n[ Premi Invio per procedere al controllo delle navi... ]");
            in.nextLine(); // Questa chiamata blocca l'esecuzione finché l'utente non preme Invio
            
            //NUOVA SEZIONE DI CONTROLLO INTEGRITÀ----------------
            System.out.println("\n[CONTROLLO INTEGRITÀ NAVI POST-EVENTO]");
            for (Giocatore g : gioco.getGiocatori()) {
                System.out.println("Verifica per " + g.getNome() + ":");
                
                //1 La nave analizza se stessa e restituisce un risultato.
                //La CLI non sa COME avviene l'analisi, riceve solo il risultato.
                Nave.RisultatoVerifica risultato = g.getNave().verificaIntegrita();
                
                //2. La CLI stampa il log generato dalla nave.
                System.out.print(risultato.log);

                if (risultato.isSceltaUtenteNecessaria()) {
                    //3a La nave ha richiesto una scelta all'utente.
                    List<List<Nave.Posizione>> opzioni = risultato.opzioniSceltaUtente;
                    
                    // Presentiamo le opzioni all'utente in modo chiaro.
                    System.out.println("Opzione 1: pezzo da " + opzioni.get(0).size() + " tessere.");
                    if (opzioni.size() > 1) {
                        System.out.println("Opzione 2: pezzo da " + opzioni.get(1).size() + " tessere.");
                    }
                    
                    int scelta = askInt("Quale pezzo vuoi tenere? (1 o 2): ", 1, opzioni.size());

                    // Determiniamo quali pezzi rimuovere in base alla scelta.
                    // Creiamo una copia della lista delle opzioni e rimuoviamo il pezzo che l'utente vuole tenere.
                    // Quello che rimane nella lista è il pezzo (o i pezzi) da scartare.
                    List<List<Nave.Posizione>> daRimuovere = new ArrayList<>(opzioni);
                    daRimuovere.remove(scelta - 1); // Rimuoviamo il pezzo scelto.

                    // Ordiniamo alla nave di eseguire la rimozione.
                    g.getNave().eseguiRimozione(daRimuovere);
                    
                } else if (!risultato.pezziDaRimuovere.isEmpty()) {
                    // 3b. La nave ha trovato solo detriti (nessuna scelta necessaria).
                    // Ordiniamo alla nave di rimuoverli direttamente.
                    g.getNave().eseguiRimozione(risultato.pezziDaRimuovere);
                }
                // 3c. Se la nave è integra, non facciamo nulla.
            }
           
            
            stampaNavi();
            stampaTabellone();
   
        }
    }
    
    /**
     * Stampa a console lo stato attuale delle navi di tutti i giocatori attivi.
     * Per ogni giocatore, mostra il nome, il colore e la rappresentazione
     * grafica della sua nave.
     */
    private void stampaNavi() {
    	
    	for(Giocatore g : gioco.getGiocatori()) {
    		
    		System.out.println("\nStato della nave "+g.getColore()+" di: "+g.getNome());
    		System.out.println(g.getNave().stampaNave()+"\n");
    		
    	} 
    }

    /* ===================================================================== */
    /*  Fase FINE                                                           */
    /* ===================================================================== */
    
    /**
     * Gestisce la fase finale della partita.
     * Calcola e assegna i crediti finali ai giocatori in base a:
     * - Posizione di arrivo.
     * - "Bellezza" della nave (minor numero di connettori esposti).
     * - Merci trasportate.
     * - Detrazioni per i componenti persi.
     * Infine, stampa le statistiche e la classifica finale.
     */
    private void phaseFine() {
        assert gioco.getFase() == Fase.FINE;
        int crediti;
        System.out.println("\n――― Fase FINALE ―――");
       // System.out.println("Risultati finali: " + gioco.calcolaPunteggi());
        
        gioco.assegnaCreditiPosizione();
        gioco.assegnaCreditiConnettoriEsposti();
        gioco.assegnaCreditiMerci();
        gioco.assegnaCreditiMerciRitirati();
        gioco.togliCreditiComponentiPersi();
        gioco.togliCreditiComponentiPersiRitirati();
        
        
        
        List<Giocatore> classifica = new ArrayList<>(gioco.getGiocatori()); //copio giocatori
        classifica.addAll(gioco.getGiocatoriRitirati());
        classifica.sort((g1, g2) -> Integer.compare(g2.getCrediti(), g1.getCrediti())); //ordino in base ai crediti

        
      //stampo statistiche partita
        System.out.println("\nstatistiche giocatori:");
        for (int i = 0; i < classifica.size(); i++) {
            Giocatore g = classifica.get(i);
    	    System.out.println(g.getNome());
            System.out.println("giri completati:" + g.getGiriCompletati());
    	    System.out.println("componenti persi:" + g.getNave().getNumeroComponentiRimossi());
            System.out.println("connettori esposti:" + g.getNave().getConnettoriEsposti());
    	}
        
        System.out.println("\nla classifica:");
        //stampo classifica ordinata in base ai crediti
        for (int i = 0; i < classifica.size(); i++) {
            Giocatore g = classifica.get(i);
    		System.out.println((i + 1) + ") " + g.getNome() + ": " + g.getCrediti() + " crediti");
    	} 

        //stampo vincitori
        System.out.println("\ni vincitori sono:");
        for (int i = 0; i < classifica.size(); i++) {
            Giocatore g = classifica.get(i);
            if(g.getCrediti() > 0){
    		    System.out.println(g.getNome());
            }
    	} 

         
    }
        

    /* ===================================================================== */
    /*  Helper: help generale                                               */
    /* ===================================================================== */

    /**
     * Stampa a console un messaggio di aiuto generale con i comandi disponibili
     * e un riepilogo delle regole e dei componenti della nave.
     */
    private void printHelp() {
        System.out.println("""
Digita le opzioni numeriche quando richiesto.
Comando universale: 'help'  – mostra questa schermata.
Durante il volo premi Invio per rivelare la carta successiva o scrivi 'mostra navi'
per controllare di nuovo lo stato pietoso in cui si trova il tuo vascello e quello degli altri.
Le navi, come il tabellone di gioco, vengono comunque stampate ogni turno. 
Il gioco controlla in automatico se il tuo vascello è nelle condizioni di volare, e se non sei stato eliminato.
Se ti ritrovi senza il tuo turno, niente lamentele, il motivo è stato mostrato chiaramente (presta attenzione!).
Il volo finisce quando finisce il mazzo o quando tutti i giocatori sono stati "eliminati" o si sono ritirati.
Se ti ritiri o vieni eliminato, le tue merci verranno SVENDUTE a metà prezzo (ammesso tu le abbia) e diventerai 
un mero spettatore della partita. 
Nessuna garanzia sul tuo posizionamento finale in classifica :)

In caso fossi uno smemorato, e non ti ricordassi cosa vogliono dire le sigle dei componenti, ecco qui la guida:
--- I COMPONENTI DELLA NAVE (e le loro abbreviazioni) ---

            - CC (Cabina Centrale):   È il tuo punto di partenza. Ospita 2 membri dell'equipaggio.
            - CA (Cabina):            Ospita altri 2 membri dell'equipaggio. Più ne hai, più personale puoi avere.
            
            - MO (Motore):            Aumenta la velocità della nave. IMPORTANTE: il loro scarico (indicato da '*')
                                      deve puntare verso il retro della nave (in basso) e la casella bersaglio deve essere VUOTA.
            - MO2 (Motore Doppio):    Vale quanto 2 motori singoli, ma per attivarlo durante il volo consumerà una batteria (BA).
                                      Senza batterie disponibili, è completamente inutile.
            
            - CN (Cannone):           Serve a distruggere meteoriti e nemici. IMPORTANTE: la loro bocca da fuoco (indicata da '*')
                                      può puntare ovunque, ma la casella bersaglio deve essere VUOTA.
            - CN2 (Cannone Doppio):   Ha la potenza di 2 cannoni singoli, ma per attivarlo consumerà una batteria (BA).
                                      Senza batterie, non può sparare.

            - BA (Batteria):          Fornisce energia. Ogni tessera BA contiene 2 o 3 cariche. È FONDAMENTALE per attivare
                                      scudi (SC) e componenti doppi (MO2, CN2).
            
            - ST (Stiva):             Serve a trasportare le merci che troverai durante il viaggio.
            - SS (Stiva Speciale):    Una stiva rinforzata che può trasportare anche le preziose (e pericolose) merci rosse.
            - SC (Scudo):             Protegge la nave da colpi leggeri e piccoli meteoriti. Consuma una batteria per attivarsi.
                                      L'orientamento è cruciale per coprire i lati esposti.
            - CO (Connettore):        Sono "moduli strutturali". Non fanno nulla di speciale, ma hanno tanti connettori.
                                      Usali per rendere la tua nave più robusta e tappare i buchi.
            - AL (Alieno):            Strutture aliene misteriose. Per il primo volo, considerale come dei Connettori (CO).""");
    }

   
    /* ===================================================================== */
    /*  Lobby: creazione giocatori                                          */
    /* ===================================================================== */
    
    /**
     * Gestisce la fase di "lobby" iniziale, raccogliendo le informazioni sui giocatori.
     * Chiede se si desidera avviare una partita pre-configurata. Altrimenti,
     * chiede il numero di giocatori (da 2 a 4) e, per ciascuno, il nome e il
     * colore, assicurandosi che ogni colore sia unico.
     *
     * @param in lo {@link Scanner} da usare per l'input dell'utente.
     * @return una lista di oggetti {@link Giocatore} pronti per iniziare la partita.
     */
    private static List<Giocatore> setupLobby(Scanner in) {
        CreaGiocatori factory = new CreaGiocatori();
        List<Giocatore> players = new ArrayList<>();

        
        if (askYesNoS("Salve, Benvenuto in Galaxy Truker - desidera iniziare una partita pre-caricata? ", in)) {
            return SetupStatico.inizializzaPartitaPreconfigurata();
        }
        
        int n;
        do {
            System.out.print("\n" + "Quanti giocatori? (2 a 4): ");
            n = safeInt(in.nextLine());
        } while (n < 2 || n > 4);

        for (int i = 1; i <= n; i++) {
            System.out.print("Nome giocatore " + i + ": ");
            String nome = in.nextLine().trim();
            Colore colore = chooseColor(in, factory);
            players.add(factory.creaGiocatore(nome, colore));
        }
        return players;
    }
    
    /**
     * Gestisce la scelta del colore per un giocatore.
     * Mostra i colori ancora disponibili e continua a chiedere finché l'utente
     * non inserisce un nome di colore valido e non ancora scelto.
     *
     * @param in lo {@link Scanner} per leggere l'input.
     * @param factory l'oggetto {@link CreaGiocatori} che tiene traccia dei colori disponibili.
     * @return il {@link Colore} scelto dal giocatore.
     */
    private static Colore chooseColor(Scanner in, CreaGiocatori factory) {
        while (true) {
            System.out.println("Colori disponibili: " + factory.getColoriDisponibili());
            System.out.print("Scegli colore: ");
            String s = in.nextLine().trim().toUpperCase();
            try {
                Colore c = Colore.valueOf(s);
                if (!factory.getColoriDisponibili().contains(c)) {
                    System.out.println("Colore già scelto. Riprova.");
                    continue;
                }
                return c;
            } catch (IllegalArgumentException e) {
                System.out.println("Colore non riconosciuto. Riprova.");
            }
        }
    }
    
    //helper di imput statici
    
    /**
     * Converte in modo sicuro una stringa in un intero.
     * Se la stringa non è un numero valido, restituisce -1 invece di lanciare
     * un'eccezione.
     *
     * @param s la stringa da convertire.
     * @return l'intero corrispondente, o -1 in caso di errore di formato.
     */
    private static int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException e) { return -1; }
    }
    
    /**
     * Pone una domanda sì/no all'utente e restituisce la sua risposta.
     * Questo metodo statico richiede che lo {@link Scanner} sia passato come parametro.
     *
     * @param msg la domanda da mostrare.
     * @param scannerInput lo {@link Scanner} da usare.
     * @return {@code true} se l'utente risponde 's', {@code false} se risponde 'n'.
     */
    private static boolean askYesNoS(String msg, Scanner scannerInput) { // Aggiunto parametro Scanner
        while (true) {
            System.out.print(msg + " (s/n): "); // Aggiungo (s/n) per chiarezza all'utente
            String s = scannerInput.nextLine().trim().toLowerCase();
            if (s.equals("s")) return true;
            if (s.equals("n")) return false;
            System.out.println("\n" + "Rispondi 's' o 'n'.");
        }
    }

    /**
     * Stampa una rappresentazione grafica del tabellone di gioco nella console.
     * Mostra la posizione di ogni giocatore usando l'iniziale del suo colore.
     * Le caselle vuote sono rappresentate da "[.]".
     */
    private void stampaTabellone() {
        Giocatore[] spazi = gioco.getTabellone().getSpazi();
        System.out.println("=== STATO DEL TABELLONE ===");

        int larghezza = 11;
        int altezza = 6;

        // Riga superiore (celle 0-10)
        for (int i = 0; i < larghezza; i++) {
            System.out.print(formattaCella(spazi[i]));
        }
        System.out.println(); // Va a capo

        // Lati verticali (4 righe)
        for (int r = 0; r < altezza - 2; r++) {
            int indiceSinistra = 29 - r; // da 29 a 26
            int indiceDestra = 11 + r;   // da 11 a 14

            // Stampa cella sinistra
            System.out.print(formattaCella(spazi[indiceSinistra]));

            // Spazi vuoti centrali
            // (larghezza - 2) * 3 caratteri per cella "[.]"
            for (int i = 0; i < larghezza - 2; i++) {
                System.out.print("   ");
            }

            // Stampa cella destra
            System.out.print(formattaCella(spazi[indiceDestra]));

            System.out.println();
        }

        // Riga inferiore (celle 15-25, stampate in ordine inverso)
        for (int i = 25; i >= 15; i--) {
            System.out.print(formattaCella(spazi[i]));
        }
        System.out.println();
        System.out.println("===========================");
    }

    /**
     * Metodo di supporto per formattare una singola cella del tabellone.
     * @param g Il giocatore presente nella cella (può essere null).
     * @return La stringa formattata, es. "[R]" o "[.]".
     */
    private String formattaCella(Giocatore g) {
        if (g == null) {
            return "[.]";
        } else {
            // Prende l'iniziale del colore del giocatore, es. ROSSO -> 'R'
            char inizialeColore = g.getColore().name().charAt(0);
            return "[" + inizialeColore + "]";
        }
    }

 
        
        /* ===================================================================== */
        /*  Helpers di input                                                    */
        /* ===================================================================== */
    
    /**
     * Richiede all'utente di inserire un numero intero compreso in un intervallo specifico.
     * Continua a chiedere finché non viene fornito un input valido.
     *
     * @param msg il messaggio da mostrare all'utente.
     * @param min il valore minimo accettabile (incluso).
     * @param max il valore massimo accettabile (incluso).
     * @return l'intero inserito dall'utente.
     */
		@Override
		public int askInt(String msg, int min, int max) {
			while (true) {
	            System.out.print(msg);
	            String s = in.nextLine().trim();
	            if (s.equalsIgnoreCase("help")) { printHelp(); continue; }
	            try {
	                int v = Integer.parseInt(s);
	                if (v < min || v > max) throw new NumberFormatException();
	                return v;
	            } catch (NumberFormatException e) {
	                System.out.println("Inserisci un numero tra " + min + " e " + max + ".");
	            }
	        }
		}
		
		/**
	     * Pone una domanda sì/no all'utente e restituisce la sua risposta booleana.
	     * Continua a chiedere finché l'utente non risponde 's' o 'n'.
	     *
	     * @param msg la domanda da mostrare.
	     * @return {@code true} per 's', {@code false} per 'n'.
	     */
		@Override
		public boolean askYesNo(String msg) {
			 while (true) {
		            System.out.print(msg);
		            String s = in.nextLine().trim().toLowerCase();
		            if (s.equals("help")) { printHelp(); continue; }
		            if (s.equals("s")) return true;
		            if (s.equals("n")) return false;
		            System.out.println("Rispondi 's' o 'n'.");
		        }
		}

		/**
	     * Richiede all'utente di inserire una stringa di testo.
	     *
	     * @param msg il messaggio da mostrare all'utente.
	     * @return la stringa inserita dall'utente, dopo aver rimosso spazi iniziali e finali.
	     */
		@Override
		public String askString(String msg) {
			while (true) {
	            System.out.print(msg);
	            String s = in.nextLine().trim();
	            if (s.equalsIgnoreCase("help")) { printHelp(); continue; }
	            // Qui potresti aggiungere validazioni se la stringa non deve essere vuota, ecc.
	            return s;
			}
		}
}       
        


