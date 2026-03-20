package galaxytrucker.modello.componenti;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una tessera di tipo "Stiva Speciale" nel gioco Galaxy Trucker.
 * Questa tessera è una variante della stiva standard, con un numero ridotto di scomparti
 * e potenzialmente altre caratteristiche speciali non specificate in questa classe.
 *
 * Estende la classe {@link Tessera} e implementa l'interfaccia {@link InterfacciaMerci},
 * ereditando le proprietà e i comportamenti comuni a tutte le tessere,
 * come i collegamenti ai lati (Nord, Est, Sud, Ovest) e il tipo di tessera.
 * Una stiva speciale è caratterizzata da un numero definito di scomparti per la merce.
 *
 * @see Tessera
 * @see TipoTessera
 * @see InterfacciaMerci
 */
public class TesseraStivaSpeciale extends Tessera implements InterfacciaMerci {

	/**
	 * Il numero di scomparti disponibili in questa tessera Stiva Speciale.
	 */
	private final int numeroScomparti;

	/**
	 * Costruisce una nuova tessera Stiva Speciale con il numero di scomparti specificato e i collegamenti ai lati.
	 *
	 * @param numeroScomparti Il numero di scomparti della stiva speciale (1 o 2).
	 * @param nord            Il tipo di collegamento sul lato Nord della tessera.
	 * @param est             Il tipo di collegamento sul lato Est della tessera.
	 * @param sud             Il tipo di collegamento sul lato Sud della tessera.
	 * @param ovest           Il tipo di collegamento sul lato Ovest della tessera.
	 * @throws IllegalArgumentException Se il numero di scomparti non è né 1 né 2.
	 */
	public TesseraStivaSpeciale(int numeroScomparti, int nord, int est, int sud, int ovest) {
		super(TipoTessera.STIVA_SPECIALE, nord, est, sud, ovest);	

		if (numeroScomparti < 1 || numeroScomparti > 2) {
		throw new IllegalArgumentException("Una stiva può contenere 1 o 2 scomparti. ");
		}
		this.numeroScomparti = numeroScomparti;
	}

	/**
	 * Restituisce il numero di scomparti disponibili in questa tessera Stiva Speciale.
	 *
	 * @return Il numero di scomparti.
	 */
	@Override
	public int getInfo() {
		return numeroScomparti;
	}

	/**
	 * Restituisce il numero di scomparti disponibili in questa tessera Stiva Speciale.
	 *
	 * @return Il numero di scomparti.
	 */
	public int getNumeroScomparti() {
	    return numeroScomparti;
	}

	/**
	 * Restituisce una rappresentazione in stringa di questa tessera Stiva Speciale,
	 * includendo le informazioni della tessera base e il numero di scomparti.
	 * Questo metodo tiene conto dell'orientamento attuale della tessera.
	 *
	 * @return Una stringa che descrive la tessera Stiva Speciale e il suo stato attuale.
	 */
	@Override
	public String toString() {
		return super.toString() + " [" + getInfo() + "  scomparti]";
	}

	/**
	 * Restituisce una rappresentazione in stringa della tessera Stiva Speciale
	 * per come era stata inizializzata, senza valutare eventuali rotazioni.
	 *
	 * @return Una stringa che descrive la tessera Stiva Speciale nel suo stato originale.
	 */
	@Override
	public String toStringOriginale() {
		return super.toStringOriginale() + " [" + getInfo() + "  scomparti]";
	}

	/**
	 * Restituisce una sigla formata dall'abbreviazione del tipo di tessera (es. "SS")
	 * con alla fine il numero di scomparti.
	 *
	 * @return Una stringa che rappresenta la sigla della tessera Stiva Speciale.
	 */
	@Override
	protected String stampaSigla() {
		return super.getTipoTessera().getAbbreviazione() + getInfo();
	}

	/**
	 * Lista delle merci attualmente immagazzinate in questa stiva speciale.
	 */
	private List<String> merci = new ArrayList<>();

	/**
	 * Restituisce la lista delle merci attualmente immagazzinate in questa tessera Stiva Speciale.
	 *
	 * @return Una {@link List} di {@link String} che rappresenta le merci.
	 */
	public List<String> getMerci() {
	    return merci;
	}
}