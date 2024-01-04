package kuvadekryptaaja;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * Tietodostojen käsittely.
 */
class TiedostojenKasittely {
	
	/**
	 * Lataa annettujen tiedostopolkujen perusteella purettavan kuvan ja purkutaulukon levyltä.
	 * @param kuvaPolku Kryptatun kuvan tiedostopolku.
	 * @param dekryptausTiedosto Avaintaulu csv-tiedoston polku.
	 * @return KuvaAvaimet-objekti, joka sisältää kuvan ja avaintaulun
	 * @throws IOException
	 */
	protected static void lataaTiedot(String kuvaPolku, String dekryptausTiedosto) throws IOException {
		
		File kuvaTiedosto = new File(kuvaPolku);
		BufferedImage kuva = ImageIO.read(kuvaTiedosto);
		
		File dekrypTiedosto= new File(dekryptausTiedosto);
		Scanner skannaaja = new Scanner(dekrypTiedosto);
		
		// Uusi kuva, mihin pikselit asetellaan
		BufferedImage dekryptattuKuva = new BufferedImage(
				kuva.getWidth()
				, kuva.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		int yIndeksi=0;
		
		while (skannaaja.hasNextLine()) {
			
			// Rivin arvoparit erotellaan erottimen perusteella.
			String[] rivialkiot = skannaaja.nextLine().split(";");
			
			// Rivin käynti läpi
			for (int i = 0; i < rivialkiot.length; i++) {
				// Erotetaan arvoparit omiksi numeroarvoikseen.
				String[] alkio 		= rivialkiot[i].split(",");
				
				dekryptattuKuva.setRGB(i, yIndeksi, kuva.getRGB(Integer.parseInt(alkio[0]), Integer.parseInt(alkio[1])));
			}
			
			// Kasvatetaan yIndeksiä
			yIndeksi++;

		}
		skannaaja.close();
		
		TekstiDekryptaaja tekstiDekryptaaja = new TekstiDekryptaaja(dekryptattuKuva);
		String[] teksti = tekstiDekryptaaja.dekryptaa();
		
		tallennaKuva(dekryptattuKuva);
		tallennaTeksti(teksti);
		
	}
	
	/**
	 * Kirjoittaa kuvan levylle output.png tiedostoon.
	 * @param dekryptattuKuva kuva, joka dekryptattu ja halutaan kirjoittaa levylle.
	 * @throws IOException
	 */
	protected static void tallennaKuva(BufferedImage dekryptattuKuva) throws IOException {
		
		File tiedostoLevylle = new File("output.png");
		ImageIO.write(dekryptattuKuva, "png", tiedostoLevylle);
		
	}
	
	/**
	 * Tallentaa tekstin levylle.
	 * @param teksti String taulukko, jossa tekstirivit.
	 * @throws IOException
	 */
	protected static void tallennaTeksti(String[] teksti) throws IOException {
		
		FileWriter tiedosto = new FileWriter("dekryptedText.txt");
		PrintWriter printKirjoittelija = new PrintWriter(tiedosto);
		
		for (int i=0; i < teksti.length; i++) {
			printKirjoittelija.print(teksti[i]);
		}
		
		printKirjoittelija.close();
		
	}
}
