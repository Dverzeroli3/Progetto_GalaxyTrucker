package galaxytrucker.modello.componenti;

import java.util.List;

/**
 * Interfaccia per componenti della nave che possono contenere merci.
 * <p>
 * Definisce un contratto comune per tessere come {@link TesseraStiva} e
 * {@link TesseraStivaSpeciale}, permettendo alla logica di gioco di
 * interagire con esse in modo uniforme per caricare e scaricare merci.
 */

public interface InterfacciaMerci {
	
	/**
     * Restituisce la lista delle merci attualmente contenute in questo componente.
     * La lista è modificabile.
     *
     * @return una {@link List} di stringhe, dove ogni stringa è il colore di una merce.
     */
    List<String> getMerci();
    
    /**
     * Restituisce il numero massimo di scomparti (e quindi di merci) che questo
     * componente può contenere.
     *
     * @return la capacità massima del componente.
     */
    int getNumeroScomparti();   // per sapere quanti può contenerne al massimo
} 
