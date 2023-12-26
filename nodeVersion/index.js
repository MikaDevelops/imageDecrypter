import * as fs from 'node:fs';
import {PNG} from 'pngjs';


// Luetaan tiedostot muistiin.
const pictureFile = fs.readFileSync('inputFiles/mixed.png');
const picture = PNG.sync.read(pictureFile);
const mapFile = fs.readFileSync('inputFiles/data.csv',{encoding:"utf-8"});


// Käsitellään kartta
const kartta = kasitteleKartta(mapFile);
let jotain = typeof kartta;
const kasiteltyKuva = kasitteleKuva(picture, kartta);

function kasitteleKartta(kartta){
    if (typeof(kartta) == "string") {

        // Rakenne johon arvot tallennetaan
        const karttaArray = [];

        // puretaan tiedosto riveihin
        const rivit = kartta.split('\n');

        // puretaan rivi pari-merkkijonoiksi jokaisen rivin kohdalla
        for (let i = 0; i < rivit.length; i++){

            // uusi rivi tallennettavaksi
            const uusiRivi =[];

            // puretaan rivi pareihin
            const rivi = rivit[i].split(';');

            // puretaan parit arvoiksi merkkijonosta
            for (let j = 0; j < rivi.length; j++) {
                const arvopari = rivi[j].split(',');
                
                const uusiArvopari = [];

                // parit muutetaan numeroksi ja tallennetaan taulukkoon
                for (let k = 0; k < arvopari.length; k++){
                    uusiArvopari.push(parseInt(arvopari[k]));
                }

                // laitetaan int muotoinen arvopari uuteen riviin
                uusiRivi.push(uusiArvopari);
            }

            // tallennetaan rivi karttaan
            karttaArray.push(uusiRivi);
        }
        return karttaArray;
    }
    else throw new Error("Kartta-tiedosto ei ole merkkijono!");
}

function kasitteleKuva(kuva, kartta){

    if ( ! kuva.toString()=='[object Object]' && kuva.width>0 && kuva.height >0 ) throw new Error('kasitteleKuva fun: kuvadata virheellinen!');
    if ( ! typeof kartta=='object' && kartta.length>0) throw new Error('kasitteleKuva fun: karttadata virheellinen!');

    // Uusi kuva purettavalle tiedolle.
    let val = new PNG({width: picture.width, height: picture.height});

}
