package kuvadekryptaaja;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Luokkaolio sisältää kuvatiedosto-objektin ja purkuun
 * käytettävän avainkartan.
 */
class KuvaAvaimet {
	
	/**
	 * Kuvatiedosto.
	 */
	BufferedImage kuva;
	
	/**
	 * Lista kartan arvopareista.
	 */
	ArrayList<int[][]> kartta;
	
	void lisaaKuva(BufferedImage kuva){
		this.kuva = kuva;
	}
	
	void lisaaKartta(ArrayList<int[][]> kartta) {
		this.kartta = kartta;
	}
}
