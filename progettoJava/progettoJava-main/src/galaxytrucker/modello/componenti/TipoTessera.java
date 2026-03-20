package galaxytrucker.modello.componenti;

/**
 * Enumera tutti i possibili tipi di tessere componente con cui è possibile
 * costruire una nave.
 * <p>
 * Ogni tipo ha un'abbreviazione associata (es. "CC" per Cabina Centrale),
 * utilizzata per la rappresentazione grafica e testuale nel gioco.
 */

public enum TipoTessera {
	/**
	 * Postazione di partenza, la cabina centrale dell'equipaggio.
	 */
	CABINA_CENTRALE("CC"),
	/**
	 * Cabina standard per l'equipaggio.
	 */
	CABINA("CA"),
	/**
	 * Componente offensivo.
	 */
	CANNONE("CN"),
	/**
	 * Componente di propulsione.
	 */
	MOTORE("MO"),
	/**
	 * Componente per collegare altre tessere.
	 */
	CONNETTORE("CO"),
	/**
	 * Scomparti di stoccaggio per merci generiche.
	 */
	STIVA("ST"),
	/**
	 * Scomparti di stoccaggio per merci speciali.
	 */
	STIVA_SPECIALE("SS"),
	/**
	 * Componente per l'accumulo di energia.
	 */
	BATTERIA("BA"),
	/**
	 * Componente difensivo.
	 */
	SCUDO("SC"),
	/**
	 * Strutture aliene, con caratteristiche specifiche.
	 */
	ALIENO("AL");

	/**
	 * L'abbreviazione testuale del tipo di tessera.
	 */
	private final String abbreviazione;

	/**
	 * Costruisce un nuovo tipo di tessera con l'abbreviazione specificata.
	 *
	 * @param abbreviazione L'abbreviazione testuale del tipo di tessera.
	 */
	TipoTessera(String abbreviazione){
		this.abbreviazione = abbreviazione;
	}

	/**
	 * Restituisce l'abbreviazione associata a questo tipo di tessera.
	 *
	 * @return L'abbreviazione della tessera.
	 */
	public String getAbbreviazione() {
        return abbreviazione;
    }
}