import * as fs from 'node:fs';
import {PNG} from 'pngjs';

const aloitusaika = process.hrtime();

// Luetaan tiedostot muistiin.
const pictureFile = fs.readFileSync('inputFiles/mixed.png');
const picture = PNG.sync.read(pictureFile);
const mapFile = fs.readFileSync('inputFiles/data.csv',{encoding:"utf-8"});

// Käsitellään kartta.
const [kartta, purettuTeksti] = kasitteleKartta(mapFile, picture);

// Puretaan teksti käsitellystä kuvasta.
// const teksti = puraTeksti(kartta);
fs.writeFileSync('output/teksti.txt', purettuTeksti);

// Kirjoitetaan buffer tiedostoon.
const buffer = PNG.sync.write(kartta,{colorType: 6});
fs.writeFileSync('output/out.png', buffer);

const lopetusaika = process.hrtime(aloitusaika);
const kesto = ((lopetusaika[0] * 1000000000 + lopetusaika[1])+ '\n');
fs.writeFileSync('output/tilasto.txt', kesto ,{flag: 'a'});


function kasitteleKartta(kartta, picture){
    if (typeof(kartta) == "string") {

        // tehdään uusi kuva
        let newImage = new PNG({width: picture.width, height: picture.height});

        // salattu teksti
        let teksti = '';

        // puretaan tiedosto riveihin
        const rivit = kartta.split('\n');

        // puretaan rivi pari-merkkijonoiksi jokaisen rivin kohdalla
        for (let i = 0; i < picture.height; i++){

            // puretaan rivi pareihin
            const rivi = rivit[i].split(';');

            // puretaan parit arvoiksi merkkijonosta
            for (let j = 0; j < rivi.length; j++) {
                
                const arvopari = rivi[j].split(',');
                PNG.bitblt(picture, newImage, arvopari[0], arvopari[1], 1, 1, j, i);
                let arrayIndex = (j+1)*3 + j + i*newImage.width;
                teksti += String.fromCharCode(parseInt(newImage.data[arrayIndex]));

            }
        }
        return [newImage, teksti];
    }
    else throw new Error("Kartta-tiedosto ei ole merkkijono!");
}