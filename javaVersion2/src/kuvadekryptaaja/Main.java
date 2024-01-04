package kuvadekryptaaja;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		long ohjelmanAloitus = System.nanoTime();
		
		// Luetaan tiedot tiedostoista


		try {
			
			TiedostojenKasittely.lataaTiedot(args[0], args[1]);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}

		// Dekryptataan kuva ja puretaan teksti alpha-kerroksesta.

		long ohjelmanLopetus = System.nanoTime();
		// Tulostetaan lopuksi hieman tilastotietoa.

		System.out.println("Koko ohjelman suoritus kesti nanosekunttia:   " + (ohjelmanLopetus - ohjelmanAloitus));
		System.out.println("Kuva dekryptattu ja pääjehu nähtävissä output.png -tiedosto avaamalla");
		System.out.println("Salattu alphakanavan viesti luettavissa dekryptedText.txt -tiedosto avaamalla");
		
		FileWriter tiedostonNakuttaja = new FileWriter("tilasto.txt", true);
		tiedostonNakuttaja.write((ohjelmanLopetus-ohjelmanAloitus)+"\n");
		tiedostonNakuttaja.close();
	}
}
