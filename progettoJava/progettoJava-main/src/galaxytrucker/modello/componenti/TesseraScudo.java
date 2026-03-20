package galaxytrucker.modello.componenti;

/**
 * Rappresenta una tessera di tipo "Scudo" nel gioco Galaxy Trucker.
 * Questa tessera fornisce protezione all'astronave su due lati consecutivi.
 *
 * Estende la classe {@link Tessera}, ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * La posizione degli scudi è determinata dalla rotazione della tessera.
 *
 * @see Tessera
 * @see TipoTessera
 */
public class TesseraScudo extends Tessera {
	/**
	 * Costruisce una nuova tessera Scudo con i collegamenti ai lati specificati.
	 * Le tessere Scudo hanno sempre gli scudi rivolti a NORD ed EST quando la rotazione è 0.
	 * La posizione degli scudi si adatta automaticamente con la rotazione della tessera:
	 * uno scudo è rivolto verso la direzione corrispondente a {@code getRotazione()}
	 * e l'altro verso la direzione successiva in senso orario ({@code (getRotazione() + 1) % 4}).
	 *
	 * @param nord   Il tipo di collegamento sul lato Nord della tessera.
	 * @param est    Il tipo di collegamento sul lato Est della tessera.
	 * @param sud    Il tipo di collegamento sul lato Sud della tessera.
	 * @param ovest  Il tipo di collegamento sul lato Ovest della tessera.
	 */
	public TesseraScudo(int nord, int est, int sud, int ovest) {
		super(TipoTessera.SCUDO, nord, est, sud, ovest);	
	}

	/**
	 * Restituisce una rappresentazione testuale delle due direzioni verso cui gli scudi sono rivolti.
	 * Le direzioni sono separate da una virgola.
	 *
	 * @return Una stringa che indica le due direzioni di protezione degli scudi (es. "NORD,EST").
	 */
	@Override
	public String getInfoTestuale() {
		return EnumDirezione.values()[getRotazione()] + "," +
				EnumDirezione.values()[(getRotazione() + 1) % 4];
    }

	/**
	 * Restituisce una rappresentazione in stringa di questa tessera Scudo,
	 * includendo le informazioni della tessera base e le direzioni verso cui gli scudi sono rivolti.
	 * Questo metodo tiene conto dell'orientamento attuale della tessera.
	 *
	 * @return Una stringa che descrive la tessera Scudo e il suo stato attuale.
	 */
	@Override
	public String toString() {
		String[] direzione = getInfoTestuale().split(",");
	    return super.toString() + " [Scudi rivolti verso " + direzione[0] + 
	    		" e " + direzione[1] + "]";
	}	

	/**
	 * Restituisce una rappresentazione in stringa della tessera Scudo
	 * per come era stata inizializzata (rotazione 0), senza valutare eventuali rotazioni avvenute.
	 *
	 * @return Una stringa che descrive la tessera Scudo nel suo stato originale (scudi a Nord ed Est).
	 */
	@Override
	 public String toStringOriginale() {
	    return super.toStringOriginale() + " [Scudi rivolti verso " + EnumDirezione.NORD + 
	    		" e " + EnumDirezione.EST + "]";	    
	 }

	/**
	 * Restituisce una rappresentazione testuale della tessera Scudo,
	 * includendo i connettori sui lati e un simbolo (#) per indicare i lati protetti dagli scudi.
	 * Gli scudi sono posizionati sui due lati consecutivi determinati dalla rotazione corrente.
	 *
	 * @return Una stringa formattata che visualizza la tessera e la posizione degli scudi.
	 * @throws IllegalArgumentException Se la rotazione corrente non è valida.
	 */
	@Override
	public String stampaTessera() {

	    // Scudi su due lati consecutivi: rotazione 0 = Nord-Est, 1 = Est-Sud, 2 = Sud-Ovest, 3 = Ovest-Nord
	    boolean scudoNord = false;
	    boolean scudoEst = false;
	    boolean scudoSud = false;
	    boolean scudoOvest = false;

	    switch (getRotazione()) {
	        case 0: scudoNord = true; scudoEst = true; break;
	        case 1: scudoEst = true; scudoSud = true; break;
	        case 2: scudoSud = true; scudoOvest = true; break;
	        case 3: scudoOvest = true; scudoNord = true; break;
	        default: throw new IllegalArgumentException("Rotazione non valida: " + getRotazione());
	    }

	    // Riga superiore
	    String riga1 = "|" + (scudoNord || scudoOvest ? "#" : "-") + " " + getConnettore(0) 
	    				+ " " + (scudoNord || scudoEst ? "#" : "-") + "|";

	    // Riga centrale
	    String riga2 =  getConnettore(3) + " "+ stampaSigla() +" " + getConnettore(1);

	    // Riga inferiore
	    String riga3 = "|" + (scudoSud || scudoOvest ? "#" : "-") + " " + getConnettore(2) 
	    				+ " " + (scudoSud || scudoEst ? "#" : "-") + "|";

	    return riga1 + "\n" + riga2 + "\n" + riga3;
	}

	/**
	 * Verifica se questa tessera Scudo fornisce protezione verso la direzione specificata.
	 *
	 * @param direzione L'enumerazione {@link EnumDirezione} che rappresenta la direzione da controllare.
	 * @return {@code true} se la tessera protegge verso la direzione indicata, {@code false} altrimenti.
	 */
	public boolean proteggeDa(EnumDirezione direzione) {
	    EnumDirezione scudo1 = EnumDirezione.values()[getRotazione()];
	    EnumDirezione scudo2 = EnumDirezione.values()[(getRotazione() + 1) % 4];
	    return direzione == scudo1 || direzione == scudo2;
	}

}