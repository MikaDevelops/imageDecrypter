package kuvadekryptaaja;

import java.awt.image.BufferedImage;

class KuvaDekryptaaja {
	
	private KuvaAvaimet kuvaJaAvaimet;
	
	protected KuvaDekryptaaja(KuvaAvaimet kuvaAvaimet) {
		this.kuvaJaAvaimet = kuvaAvaimet;
	}
	
	protected BufferedImage dekryptaa() {
		
		BufferedImage dekryptattuKuva = new BufferedImage(
				kuvaJaAvaimet.kuva.getWidth()
				, kuvaJaAvaimet.kuva.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < kuvaJaAvaimet.kuva.getHeight(); i++){
			
			for (int j = 0; j < kuvaJaAvaimet.kuva.getWidth(); j++) {
				int[] pikseliKoordinaatti = kuvaJaAvaimet.kartta.get(i)[j];
				dekryptattuKuva.setRGB(j, i,
						kuvaJaAvaimet.kuva.getRGB(pikseliKoordinaatti[0], pikseliKoordinaatti[1]));
				
			}
			
		}
		
		return dekryptattuKuva;
		
	}
}
