package kuvadekryptaaja;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		long ohjelmanAloitus = System.nanoTime();
		
		// Luetaan tiedot tiedostoista
		long tiedonlukuAloitus = System.nanoTime();
		KuvaAvaimet kuvaAvaimet = null;
		try {
			
			kuvaAvaimet = TiedostojenKasittely.lataaTiedot(args[0], args[1]);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		long tiedonlukuLopetus = System.nanoTime();
		
		long purkuAloitus = System.nanoTime();
		// Dekryptataan kuva ja puretaan teksti alpha-kerroksesta.
		if (kuvaAvaimet != null) {
			KuvaDekryptaaja dekryptaaja = new KuvaDekryptaaja(kuvaAvaimet);
			BufferedImage dekryptattuKuva = dekryptaaja.dekryptaa();
			
			TekstiDekryptaaja tekstiDekryptaaja = new TekstiDekryptaaja(dekryptattuKuva);
			String[] salainenTeksti = tekstiDekryptaaja.dekryptaa();
			
			try {
				
				TiedostojenKasittely.tallennaKuva(dekryptattuKuva);
				TiedostojenKasittely.tallennaTeksti(salainenTeksti);
				
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		else System.out.println("Kuvan ja avainten lataus ei onnistunut.");
		long purkuLopetus = System.nanoTime();
		
		long ohjelmanLopetus = System.nanoTime();
		// Tulostetaan lopuksi hieman tilastotietoa.
		System.out.println("Tietojen luku kesti nanosekunttia:            " + (tiedonlukuLopetus - tiedonlukuAloitus));
		System.out.println("Kryptatun datan purku kesti nanosekunttia:    " + (purkuLopetus - purkuAloitus));
		System.out.println("Koko ohjelman suoritus kesti nanosekunttia:   " + (ohjelmanLopetus - ohjelmanAloitus));
		System.out.println("Kuva dekryptattu ja pääjehu nähtävissä output.png -tiedosto avaamalla");
		System.out.println("Salattu alphakanavan viesti luettavissa dekryptedText.txt -tiedosto avaamalla");
	}
}
