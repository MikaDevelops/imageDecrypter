import * as fs from 'node:fs';
import {PNG} from 'pngjs';

class PixelIterator{

    // Osoitin, joka osoittaa käsiteltävä pikselin arvojen ensimmäistä arvoa.
    #lastPixel = false;
    #pixelPointer = 0;
    #linePointer = 0;
    #image;
    #map;

    constructor(image, map){
        this.#image = image;
        this.#map = map;
    }

    nextPixel(){

        if (this.#linePointer >= this.#image.height) 
            throw new Error ("image Y out of bound");
        if (this.#pixelPointer >= this.#image.width) 
            throw new Error ("image X out of bound");

        const newXY = this.#getXYfromMap(this.#pixelPointer, this.#linePointer);
        //const pixelFromImage = this.#getPixel(newXY);

        this.#pixelPointer++;

        if (this.#pixelPointer >= this.#image.width) {
            this.#linePointer++;
            if(this.#linePointer >=this.#image.height 
                && this.#pixelPointer >= this.#image.width){
                    this.#lastPixel = true;
                }
            this.#pixelPointer = 0;
        }
        
        return newXY;
        
    }

    hasNextPixel(){
 
        if (this.#lastPixel){
            return false;
        }
        else return true;
    }

    #getXYfromMap(x, y){
        let correctXY = this.#map[y][x];
        return correctXY;
    }

    #getPixel(XY){
        let calculatedY = XY[1] * this.#image.width;
        let calculatedX = XY[0] * 4;
        let pixelStart = calculatedX + calculatedY;
        const pixel = this.#image.data.slice(pixelStart, pixelStart+4);
        return pixel;
    }
}

const aloitusaika = process.hrtime();

// Luetaan tiedostot muistiin.
const pictureFile = fs.readFileSync('inputFiles/mixed.png');
const picture = PNG.sync.read(pictureFile);
const mapFile = fs.readFileSync('inputFiles/data.csv',{encoding:"utf-8"});

// Käsitellään kartta.
const kartta = kasitteleKartta(mapFile);

// Käsitellään kuva.
const kasiteltyKuva = kasitteleKuva(picture, kartta);

// Puretaan teksti käsitellystä kuvasta.
const teksti = puraTeksti(kasiteltyKuva);
fs.writeFileSync('output/teksti.txt', teksti);

// Kirjoitetaan buffer tiedostoon.
const buffer = PNG.sync.write(kasiteltyKuva,{colorType: 6});
fs.writeFileSync('output/out.png', buffer);

const lopetusaika = process.hrtime(aloitusaika);
const kesto = ((lopetusaika[0] * 1000000000 + lopetusaika[1])+ '\n');
fs.writeFileSync('output/tilasto.txt', kesto ,{flag: 'a'});


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
    let newImage = new PNG({width: picture.width, height: picture.height});

    const pixelIterator = new PixelIterator(kuva, kartta);

    let newX = 0;
    let newY = 0;

    while(pixelIterator.hasNextPixel()){

        // Get the next pixel.
        let pixel = pixelIterator.nextPixel();

        // Set the pixel to new image object.
        PNG.bitblt(kuva, newImage, pixel[0], pixel[1], 1, 1, newX, newY);

        newX++;

        if (newX >= newImage.width) {
            newY++;
            newX=0;
        }
    }

    return newImage;

}

function puraTeksti(pngKuva){
    let teksti = '';
    for (let i=3; i < pngKuva.data.length; i+=4){
        teksti += String.fromCharCode(parseInt(pngKuva.data[i], 10));
    }

    return teksti;
}

