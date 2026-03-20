package galaxytrucker.modello.giocatori;

import java.util.Objects;
import galaxytrucker.cli.UserInputProvider;
import galaxytrucker.modello.componenti.Tessera;
import galaxytrucker.modello.nave.Nave;
import galaxytrucker.modello.tabellone.Tabellone;
/**
 * Rappresenta un giocatore all'interno della partita.
 * <p>
 * Ogni giocatore ha un nome, un colore univoco, una nave ({@link Nave}) da
 * costruire e pilotare, un ammontare di crediti e un registro dei progressi
 * come i giri completati sul tabellone.
 */
public class Giocatore {

		private final Colore  colore;
	    private final String nome;
	    private Nave nave;
		private int crediti;
		private int giriCompletati;
		//private Int posizione;
		//private final Int id;

		/**
	     * Costruisce un nuovo giocatore.
	     *
	     * @param nome il nome del giocatore.
	     * @param colore il {@link Colore} associato al giocatore.
	     */
	    public Giocatore(String nome, Colore colore) {
	        this.nome   = nome;
	        this.colore = colore;
	        this.nave = new Nave(1);
	    }

	    /* --- GETTER pubblici --- */
	    /** Restituisce il nome del giocatore. @return il nome. */
	    public String getNome()   { return nome; }
	    /** Restituisce il colore del giocatore. @return il {@link Colore}. */
	    public Colore getColore() { return colore; }
	    /** Restituisce la nave del giocatore. @return la {@link Nave}. */
		public Nave getNave()     { return nave; }
	    /** Restituisce i crediti del giocatore. @return i crediti. */
	    public int    getCrediti(){ return crediti; }

	    /* --- Metodi che userà il motore di gioco più avanti --- */
	  //  public void aggiungiCrediti(int c) { crediti += c; }
	  //  public void spostaCrediti(int c)   { crediti -= c; }

	    
	    /**
	     * Restituisce una rappresentazione testuale del giocatore.
	     * @return una stringa del tipo "Nome (COLORE)".
	     */
	    @Override
	    public String toString() {
	        return nome + " (" + colore + ")";
	    }

	    
//INIZIO LOGICA METODI CONTRABBANDIERE
	    
	    /**
	     * Calcola la potenza di fuoco totale della nave del giocatore.
	     * Delega il calcolo alla nave stessa.
	     *
	     * @param inputProvider l'interfaccia per richiedere input all'utente.
	     * @return la potenza di fuoco calcolata.
	     */
	    public double calcolaPotenzaDiFuoco(UserInputProvider inputProvider) {
	        return nave.calcolaPotenzaDiFuoco(inputProvider, getNome());
	    }

	    /**
	     * Carica merci di ricompensa sulla nave del giocatore.
	     * Delega l'operazione alla nave.
	     *
	     * @param colore il colore della merce da caricare.
	     * @param quantita la quantità di merce da caricare.
	     */
	public void caricaMerciRicompensa(String colore, int quantita) {
        nave.caricaMerce(colore, quantita);
    }

	/**
     * Rimuove le merci più preziose dalla nave del giocatore.
     * Delega l'operazione alla nave.
     *
     * @param numero il numero di merci da rimuovere.
     */
    public void perdiMerciPiuPreziose(int numero) {
        nave.scaricaMerciPiuPreziose(numero);
    }

    /**
     * Fa retrocedere il giocatore sul tabellone.
     * Delega il movimento al tabellone.
     *
     * @param quanti il numero di giorni (caselle) da perdere.
     * @param tabellone il tabellone di gioco.
     */
	public void perdiGiorniDiVolo(int quanti, Tabellone tabellone) {
    for (int i = 0; i < quanti; i++) {
        tabellone.muoviGiocatore(this, -1); // Retrocede di 1 posizione
    }
}

	/**
     * Controlla se la nave del giocatore ha almeno una batteria disponibile.
     * @return {@code true} se c'è almeno una carica, {@code false} altrimenti.
     */
public boolean haBatteriaDisponibile() {
    return nave.haBatteriaDisponibile();
}

/**
 * Tenta di consumare una carica di batteria dalla nave.
 * @return {@code true} se la batteria è stata usata, {@code false} altrimenti.
 */
public boolean usaBatteria() {
    return nave.usaBatteria();
}

//FINE LOGICA METODI CONTRABBANDIERE
	


//INIZIO LOGICA GIRI E PROGRESSIONE SUL TABELLONE

/** Restituisce il numero di giri completati dal giocatore sul tabellone. @return i giri completati. */
public int getGiriCompletati() {
    return giriCompletati;
}

/** Imposta il numero di giri completati dal giocatore. @param giri il nuovo numero di giri. */
public void setGiriCompletati(int giri) {
    this.giriCompletati = giri;
}


/** Incrementa il conteggio dei giri completati. @param quanti il numero di giri da aggiungere. */
public void incrementaGiri(int quanti) {
    giriCompletati += quanti;
}

/** Decrementa il conteggio dei giri completati. @param quanti il numero di giri da togliere. */
public void decrementaGiri(int quanti) {
    if (giriCompletati+1 < quanti) {
        throw new IllegalStateException("Non si possono togliere più giri di quanti ne sono stati percorsi.");
    }
    giriCompletati -= quanti;
}
//FINE LOGICA GIRI E PROGRESSIONE SUL TABELLONE



//INIZIO LOGICA SPAZIO APERTO

/**
 * Calcola la potenza motrice totale della nave del giocatore.
 * Delega il calcolo alla nave.
 *
 * @param inputProvider l'interfaccia per richiedere input all'utente.
 * @return la potenza motrice calcolata.
 */
public double calcolaPotenzaMotrice(UserInputProvider inputProvider) {
    return nave.calcolaPotenzaMotrice(inputProvider, getNome());
}
//FINE LOGICA SPAZIO APERTO

	
	
//INIZIO LOGICA NAVE ABBANDONATA

/** Restituisce il totale dell'equipaggio sulla nave (umani + alieni). @return il numero totale di membri equipaggio. */
	public int getTotaleEquipaggio() {
    return nave.getTotaleEquipaggio();
	}

	/** Consuma una certa quantità di equipaggio dalla nave. @param quantita il numero di membri da consumare. */
	public void consumaEquipaggio(int quantita) {
    nave.consumaEquipaggio(quantita);
	}

	 /** Aggiunge crediti al totale del giocatore. @param quantita il numero di crediti da aggiungere. */
	public void aggiungiCrediti(int quantita) {
    this.crediti += quantita;
	}

	/** Sottrae crediti dal totale del giocatore. @param quantita il numero di crediti da togliere. */
	public void togliCrediti(int quantita) {
		if(this.crediti > 0){
    		this.crediti -= quantita;
		}
	}
//FINE LOGICA NAVE ABBANDONATA
	
//LOGICA NECESSARIA A GIOCO

	 /**
     * Piazza una tessera sulla nave del giocatore.
     * Delega l'operazione alla nave.
     *
     * @param t la tessera da piazzare.
     * @param x la riga di destinazione.
     * @param y la colonna di destinazione.
     * @param rot il numero di rotazioni da applicare.
     */
	 public void piazzaTessera(Tessera t, int x, int y, int rot) {     
	       
	        nave.piazzaTessera(t, x, y, rot);// (riga,colonna) nell’API Nave
	    }
	 
	 
	 
	 //li ho realizzati per correggere un'errore nel hashset che ho usato per i giocatori che hanno già passato
	 
	 /**
	     * Confronta questo giocatore con un altro oggetto per l'uguaglianza.
	     * Due giocatori sono considerati uguali se hanno lo stesso nome e colore.
	     *
	     * @param o l'oggetto da confrontare.
	     * @return {@code true} se gli oggetti sono uguali, {@code false} altrimenti.
	     */
	 @Override
	 public boolean equals(Object o) {
	  if (this == o) return true;
	  if (o == null || getClass() != o.getClass()) return false;
	  Giocatore giocatore = (Giocatore) o;
	  return Objects.equals(nome, giocatore.nome) && // Assumendo che il nome sia un identificatore unico
	         colore == giocatore.colore;
	 }

	 
	 /**
	     * Calcola il codice hash per questo giocatore.
	     * Basato su nome e colore per coerenza con {@link #equals(Object)}.
	     *
	     * @return il codice hash.
	     */
	 @Override
	 public int hashCode() {
	  return Objects.hash(nome, colore); // Basato sugli stessi campi di equals
	 }

	
	}



