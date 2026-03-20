package galaxytrucker.modello.giocatori;

/**
 * Colori da associare ad ogni singolo giocatore.
 */
public enum Colore {

	ROSSO(0), 
	BLU(1), 
	GIALLO(2), 
	VERDE(3);
	

	private final int abbreviazioneNum;
	
	Colore(int abbreviazioneNum){
		this.abbreviazioneNum = abbreviazioneNum;
	}
	
	public int getAbbreviazioneNum() {
        return abbreviazioneNum;
    }
}
