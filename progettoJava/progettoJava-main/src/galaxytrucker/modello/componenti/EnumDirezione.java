package galaxytrucker.modello.componenti;
	
/**
 * Enumera le quattro direzioni cardinali (NORD, EST, SUD, OVEST).
 * <p>
 * Utilizzata per gestire l'orientamento di componenti come scudi e cannoni,
 * e per determinare la direzione di provenienza di minacce come meteoriti.
 * Ogni direzione ha un'abbreviazione testuale e numerica associata.
 */
public enum EnumDirezione {
	/** Direzione Nord, valore numerico 0. */
    NORD("N",0), 
    /** Direzione Est, valore numerico 1. */
    EST("E",1), 
    /** Direzione Sud, valore numerico 2. */
    SUD("S",2), 
    /** Direzione Ovest, valore numerico 3. */
    OVEST("O",3);
	
	private final String abbreviazione;
	private final int abbreviazioneNum;
	
	/**
     * Costruttore dell'enum.
     *
     * @param abbreviazione     abbreviazione testuale della direzione (es. "N" per Nord)
     * @param abbreviazioneNum  valore numerico associato alla direzione (0=NORD, 1=EST, 2=SUD, 3=OVEST)
     */
	EnumDirezione(String abbreviazione, int abbreviazioneNum){
		this.abbreviazione = abbreviazione;
		this.abbreviazioneNum = abbreviazioneNum;
	}
	
	/**
     * Restituisce l'abbreviazione testuale della direzione.
     *
     * @return abbreviazione della direzione (es. "N", "E", "S", "O")
     */
	public String getAbbreviazione() {
        return abbreviazione;
    }
	
	/**
     * Restituisce il valore numerico associato alla direzione.
     *
     * @return numero corrispondente alla direzione (0 per NORD, ..., 3 per OVEST)
     */
	public int getAbbreviazioneNum() {
        return abbreviazioneNum;
    }
}
