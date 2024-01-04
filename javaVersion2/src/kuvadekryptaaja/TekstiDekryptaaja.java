package kuvadekryptaaja;

import java.awt.image.BufferedImage;

/**
 * Käsittelee alphakanavan arvot merkeiksi ja tallentaa ne merkkijonotaulukkoon.
 */
class TekstiDekryptaaja {
	
	private BufferedImage kuva;
	
	protected TekstiDekryptaaja (BufferedImage kuva) {
		this.kuva = kuva;
	}
	
	/**
	 * Aloittaa viestin purkamisen kuvan pikseleiden alpha-arvoista.
	 * @return String[], jossa tekstirivit.
	 */
	protected String[] dekryptaa() {
		
		String[] teksti = new String[kuva.getHeight()];
		
		for (int i = 0; i < kuva.getHeight(); i++) {
			
			String rivi ="";
			
			for (int j = 0; j < kuva.getWidth(); j++) {
				
				int pikseli = kuva.getRGB(j, i);
				int alpha = (pikseli >> 24) & 0xff;
				char merkki = (char)alpha;

				rivi += String.valueOf(merkki);
			}
			teksti[i] = rivi;
		}
		
		return teksti;
	}
	
}
