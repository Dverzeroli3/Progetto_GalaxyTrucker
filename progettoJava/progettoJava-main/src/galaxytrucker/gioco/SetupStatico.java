package galaxytrucker.gioco;
import galaxytrucker.modello.componenti.*;
import galaxytrucker.modello.giocatori.*;
import galaxytrucker.modello.nave.*;
import java.util.*;

/**
 * Classe di utilità per configurare una partita di test predefinita.
 * <p>
 * Fornisce un metodo statico per creare una lista di giocatori con navi già
 * costruite, utile per saltare la fase di costruzione e passare direttamente
 * alla fase di volo durante lo sviluppo o i test.
 */
public class SetupStatico {
	private static boolean chiamata = false;

	/**
     * Inizializza una partita pre-configurata con 4 giocatori e navi già costruite.
     * Questo metodo è statico e crea un set fisso di giocatori e piazze tessere
     * sulle loro navi.
     *
     * @return una lista di {@link Giocatore} pronti per la fase di volo.
     */
    public static List<Giocatore> inizializzaPartitaPreconfigurata() {
        List<Giocatore> giocatori = new ArrayList<>();
        // Creo 4 giocatori
            Giocatore g1 = new Giocatore("Luca", Colore.ROSSO);
            Giocatore g2 = new Giocatore("Sofia", Colore.BLU);
            Giocatore g3 = new Giocatore("Ilaria", Colore.GIALLO);
            Giocatore g4 = new Giocatore("Giulio", Colore.VERDE);
 //           Nave nave = new Nave(1);  // esempio matrice 5x5

            giocatori.add(g1);
            giocatori.add(g2);
            giocatori.add(g3);
            giocatori.add(g4);
        

        
         // Nomi abbreviazioni navi
            Nave n1 = g1.getNave();
            Nave n2 = g2.getNave();
            Nave n3 = g3.getNave();
            Nave n4 = g4.getNave();
            
            

         // ------------------------ GIOCATORE 1 ------------------------
            n1.piazzaTessera(new TesseraConnettore(1, 1, 3, 3), 2, 1, 0);
            n1.piazzaTessera(new TesseraCabina(1, 1, 1, 0), 1, 1, 0);
            n1.piazzaTessera(new TesseraCannone(1, 0, 1, 1, 3), 1, 2, 0); 
            n1.piazzaTessera(new TesseraBatteria(2, 2, 3, 2, 1), 3, 1, 0); 
            n1.piazzaTessera(new TesseraStiva(2, 2, 1, 2, 1), 3, 0, 0);
            n1.piazzaTessera(new TesseraStivaSpeciale(2, 3, 3, 0, 3), 4, 1, 0);
            n1.piazzaTessera(new TesseraConnettore(1, 1, 1, 1), 2, 3, 1);
            n1.piazzaTessera(new TesseraConnettore(1, 3, 3, 1), 3, 3, 3);
            n1.piazzaTessera(new TesseraStiva(2, 0, 1, 2, 1), 2, 4, 0);
            n1.piazzaTessera(new TesseraConnettore(3, 0, 3, 1), 1, 3, 3);
            n1.piazzaTessera(new TesseraCannone(1, 0, 2, 2, 3), 2, 0, 0);
            n1.piazzaTessera(new TesseraBatteria(2, 3, 3, 1, 1), 3, 4, 0); 
            n1.piazzaTessera(new TesseraCabina(1, 3, 3, 3), 3, 2, 0);
            n1.piazzaTessera(new TesseraStivaSpeciale(1, 2, 2, 0, 0), 4, 0, 0);
            n1.piazzaTessera(new TesseraStivaSpeciale(1, 1, 0, 0, 1), 4, 4, 0);


         // ------------------------ GIOCATORE 2 ------------------------
            n2.piazzaTessera(new TesseraConnettore(1, 1, 3, 3), 2, 1, 0);
            n2.piazzaTessera(new TesseraCannone(2, 0, 3, 3, 3), 1, 1, 0);
            n2.piazzaTessera(new TesseraStiva(2, 3, 1, 1, 2), 1, 2, 0); 
            n2.piazzaTessera(new TesseraCannone(2, 0, 1, 1, 2), 0, 2, 0); 
            n2.piazzaTessera(new TesseraBatteria(2, 2, 3, 2, 1), 3, 1, 0); 
            n2.piazzaTessera(new TesseraStiva(2, 2, 1, 2, 1), 3, 0, 0);
            n2.piazzaTessera(new TesseraMotore(2, 3, 3, 0, 3), 4, 1, 0);
            n2.piazzaTessera(new TesseraConnettore(1, 1, 1, 1), 2, 3, 1);
            n2.piazzaTessera(new TesseraScudo(1, 3, 3, 1), 3, 3, 3);
            n2.piazzaTessera(new TesseraCannone(1, 0, 0, 3, 3), 2, 4, 0);
            n2.piazzaTessera(new TesseraCannone(1, 0, 3, 1, 3), 1, 3, 1);
            n2.piazzaTessera(new TesseraCannone(1, 0, 2, 2, 3), 2, 0, 0);
            n2.piazzaTessera(new TesseraCannone(2, 0, 3, 3, 3), 3, 4, 1); 
            n2.piazzaTessera(new TesseraCabina(1, 3, 3, 3), 3, 2, 0);
            n2.piazzaTessera(new TesseraMotore(1, 2, 2, 0, 0), 4, 0, 0);
            n2.piazzaTessera(new TesseraMotore(1, 1, 0, 0, 1), 4, 4, 0);
            
         // ------------------------ GIOCATORE 3 ------------------------
            n3.piazzaTessera(new TesseraConnettore(1, 1, 3, 3), 2, 1, 0);
            n3.piazzaTessera(new TesseraCabina(1, 1, 1, 0), 1, 1, 0);
            n3.piazzaTessera(new TesseraStiva(2, 3, 1, 1, 3), 1, 2, 0); 
            n3.piazzaTessera(new TesseraCannone(1, 0, 1, 1, 2), 0, 2, 0); 
            n3.piazzaTessera(new TesseraBatteria(2, 2, 3, 2, 1), 3, 1, 0); 
            n3.piazzaTessera(new TesseraStiva(2, 2, 1, 2, 1), 3, 0, 0);
            n3.piazzaTessera(new TesseraMotore(2, 3, 3, 0, 3), 4, 1, 0);
            n3.piazzaTessera(new TesseraConnettore(1, 1, 1, 1), 2, 3, 1);
            n3.piazzaTessera(new TesseraScudo(1, 3, 3, 1), 3, 3, 0);
            n3.piazzaTessera(new TesseraStivaSpeciale(1, 0, 1, 3, 1), 2, 4, 0);
            n3.piazzaTessera(new TesseraConnettore(3, 0, 3, 1), 1, 3, 3);
            n3.piazzaTessera(new TesseraCannone(1, 0, 2, 2, 3), 2, 0, 0);
            n3.piazzaTessera(new TesseraBatteria(2, 3, 3, 1, 1), 3, 4, 0); 
            n3.piazzaTessera(new TesseraMotore(1, 1, 3, 0, 3), 3, 2, 0);
            n3.piazzaTessera(new TesseraMotore(1, 2, 2, 0, 0), 4, 0, 0);
            n3.piazzaTessera(new TesseraMotore(1, 1, 0, 0, 1), 4, 4, 0);
            n3.piazzaTessera(new TesseraMotore(1, 1, 1, 0, 1), 4, 3, 0);

            
         // ------------------------ GIOCATORE 4 ------------------------
            n4.piazzaTessera(new TesseraScudo(1, 1, 3, 3), 2, 1, 0);
            n4.piazzaTessera(new TesseraCannone(2, 0, 3, 3, 0), 1, 1, 0);
            n4.piazzaTessera(new TesseraStiva(2, 3, 1, 1, 2), 1, 2, 0); 
            n4.piazzaTessera(new TesseraCannone(2, 0, 0, 2, 0), 0, 2, 0); 
            n4.piazzaTessera(new TesseraBatteria(2, 2, 3, 2, 1), 3, 1, 0); 
            n4.piazzaTessera(new TesseraStiva(2, 2, 1, 2, 0), 3, 0, 0);
            n4.piazzaTessera(new TesseraMotore(2, 3, 0, 0, 3), 4, 1, 0);
            n4.piazzaTessera(new TesseraConnettore(1, 1, 1, 1), 2, 3, 1);
            n4.piazzaTessera(new TesseraScudo(0, 3, 3, 1), 3, 3, 2);
            n4.piazzaTessera(new TesseraStivaSpeciale(1, 0, 0, 3, 1), 2, 4, 0);
            n4.piazzaTessera(new TesseraCabina(3, 0, 0, 1), 1, 3, 3);
            n4.piazzaTessera(new TesseraCannone(1, 0, 2, 2, 0), 2, 0, 0);
            n4.piazzaTessera(new TesseraBatteria(2, 3, 0, 1, 1), 3, 4, 0); 
            n4.piazzaTessera(new TesseraCabina(1, 3, 0, 3), 3, 2, 0);
            n4.piazzaTessera(new TesseraMotore(1, 2, 2, 0, 0), 4, 0, 0);
            n4.piazzaTessera(new TesseraMotore(1, 1, 0, 0, 0), 4, 4, 0);
            
            chiamata = true;
            return giocatori;
        
    }
    
    /**
     * Controlla se la partita è stata inizializzata usando la configurazione statica.
     *
     * @return {@code true} se {@link #inizializzaPartitaPreconfigurata()} è stato chiamato,
     *         altrimenti {@code false}.
     */
    public static boolean isPreConfig() {
    	if (chiamata) {
    		return true;
    	} else {
    		return false;
    	}
    	
    }
}
