import * as fs from 'node:fs';
import {PNG} from 'pngjs';

const aloitusaika = process.hrtime();

// Luetaan tiedostot muistiin.
const pictureFile = fs.readFileSync('inputFiles/mixed.png');
const picture = PNG.sync.read(pictureFile);
const mapFile = fs.readFileSync('inputFiles/data.csv',{encoding:"utf-8"});

// Käsitellään kartta.
const kartta = kasitteleKartta(mapFile, picture);

// Puretaan teksti käsitellystä kuvasta.
const teksti = puraTeksti(kartta);
fs.writeFileSync('output/teksti.txt', teksti);

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


        // Rakenne johon arvot tallennetaan
        const karttaArray = [];

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
                
            }
        }
        return newImage;
    }
    else throw new Error("Kartta-tiedosto ei ole merkkijono!");
}


function puraTeksti(pngKuva){
    let teksti = '';
    for (let i=3; i < pngKuva.data.length; i+=4){
        teksti += String.fromCharCode(parseInt(pngKuva.data[i], 10));
    }

    return teksti;
}