package kuvadekryptaaja;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
	protected static KuvaAvaimet lataaTiedot(String kuvaPolku, String dekryptausTiedosto) throws IOException {
		
		File kuvaTiedosto = new File(kuvaPolku);
		BufferedImage kuva = ImageIO.read(kuvaTiedosto);
		
		File dekrypTiedosto= new File(dekryptausTiedosto);
		Scanner skannaaja = new Scanner(dekrypTiedosto);
		
		ArrayList<int[][]> dekryptTaulu = new ArrayList<>();
		
		while (skannaaja.hasNextLine()) {
			
			// Rivin arvoparit erotellaan erottimen perusteella.
			String[] rivialkiot = skannaaja.nextLine().split(";");
			
			// Arvopareja varten taulukko, jossa taulut arvoille.
			int[][] arvoparit = new int[rivialkiot.length][2];
			
			for (int i = 0; i < rivialkiot.length; i++) {
				// Erotetaan arvoparit omiksi numeroarvoikseen.
				String[] alkio 		= rivialkiot[i].split(",");
				arvoparit[i][0] 	= Integer.parseInt(alkio[0]);
				arvoparit[i][1] 	= Integer.parseInt(alkio[1]);
			}
			
			// Lisätään rivikäsittelyn lopuksi rivin arvoparit taulukkoon.
			dekryptTaulu.add(arvoparit);
		}
		skannaaja.close();
		
		// Luodaan objekti, johon kootaan kuvaobjekti ja avaimet.
		KuvaAvaimet kuvaAvaimet = new KuvaAvaimet();
		kuvaAvaimet.lisaaKuva(kuva);
		kuvaAvaimet.lisaaKartta(dekryptTaulu);
		
		return kuvaAvaimet;
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
