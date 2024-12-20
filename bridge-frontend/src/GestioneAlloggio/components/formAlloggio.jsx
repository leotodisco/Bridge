import { useState } from "react";


const Servizi = {
    WIFI : "WIFI",
    ARIACONDIZIONATA : "ARIACONDIZIONATA",
    RISCALDAMENTO : "RISCALDAMENTO"
}

const CreaAlloggio = () => {
    const [descrizione , setDescrizione] = useState("");
    const [maxPersone, setMaxPersone] = useState(1);
    const [metratura, setMetratura] = useState("");
    const [fotos, setFotos] = useState([]);
    const [servizi, setServizi] = useState("");
    const [titolo, setTitolo] = useState("");
    const [indirizzo , setIndirizzo] = useState({
        via: "",
        cap: "",
        numCivico : "",
        provincia : "",
        citta : ""
    });

    const aggiornaDescrizione = (event) => setDescrizione(event.target.value);
    const aggiornaMaxPeersone = (event) => setMaxPersone(event.target.value);
    const aggiornaMetratura = (event) => setMetratura(event.target.value);
    const aggiornaServizi = (event) => setServizi(event.target.value);
    const aggiornaTitolo = (event) => setTitolo(event.target.value);

    // Funzione per aggiornare le foto
    const aggiornaFotoAlloggio = (event) => {
        const files = Array.from(event.target.files);

        if (files.length < 1 || files.length > 4) {
            alert("Devi caricare almeno 1 foto e un massimo di 3 foto.");
            return;
        }

        const readers = files.map((file) => {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.onload = () => resolve(reader.result);
                reader.onerror = (error) => reject(error);
                reader.readAsDataURL(file);
            });
        });
        Promise.all(readers)
            .then((base64Images) => setFotos(base64Images))
            .catch((error) => console.error("Errore nella lettura delle immagini:", error));
    };

    const aggiornaVia = (event) => {
        setIndirizzo({
            ...indirizzo,
            via : event.target.value
        });
    }

    const aggiornaNumCivico = (event) => {
        setIndirizzo({
            ...indirizzo,
            numCivico : event.target.value
        });
    }
    const aggiornaCitta = (event) => {
        setIndirizzo({
            ...indirizzo,
            citta : event.target.value
        });
    }
    const aggiornaProvincia = (event) => {
        setIndirizzo({
            ...indirizzo,
            provincia : event.target.value
        });
    }
    const aggiornaCap = (event) => {
        setIndirizzo({
            ...indirizzo,
            cap : event.target.value
        });
    }


    const handleSubmit = async (event) => {
        event.preventDefault();


        const alloggioDTO = {
            titolo : titolo, // Inviato insieme agli altri dati
            descrizione : descrizione,
            metratura : metratura,
            maxPersone : maxPersone,
            servizi : servizi,
            foto : fotos,
            emailProprietario: "root@mail.it",
            indirizzo : indirizzo
        };

        console.log("Alloggio da creare ", alloggioDTO);

        try {
            const response = await fetch("http://localhost:8080/alloggi/aggiungi", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify({
                    titolo: titolo,
                    descrizione: descrizione,
                    metratura: metratura,
                    maxPersone: maxPersone,
                    servizi: servizi,
                    foto: fotos,
                    emailProprietario: "root@mail.it",
                    indirizzo: {
                        via: indirizzo.via,
                        numCivico: indirizzo.numCivico,
                        citta: indirizzo.citta,
                        cap: indirizzo.cap,
                        provincia: indirizzo.provincia
                    },
                })
            });

            console.log("Risposta del server:", response);  // Log della risposta del server

            if (!response.ok) {
                throw new Error(`Errore http: ${response.status}`);
            }

            const result = await response.text();
            console.log("Successo: ", result)
            alert("Mi sento una tvoria ", result);
        } catch (error) {
            console.error("Errore creazione alloggio: ", error);
            alert("Errore durante la creazione dell'alloggio. Dettagli: " + error.message);
        }
    };

    return (
        // Form per la creazione dell'evento
        <div className="formContainer">
            <h2>Crea un nuovo alloggio</h2>
            <hr/>

            <form onSubmit={handleSubmit}>
                <h3>Informazioni Principali:</h3>
                <hr/>
                <div className="formField">
                    <input
                        type="text"
                        placeholder="Titolo alloggio"
                        title="Digita il titolo"
                        //className={`formEditText ${nomeErrore ? "erroreInput" : ""}`}
                        value={titolo}
                        onChange={aggiornaTitolo}
                        required
                    />
                </div>

                <div className="formField">
                    <textarea
                        placeholder="Descrizione"
                        title="Digita la descrizione dell'evento"
                        //className={`formEditText ${descrizioneErrore ? "erroreInput" : ""}`}
                        value={descrizione}
                        onChange={aggiornaDescrizione}
                        required
                    />
                </div>

                <div className="formField">
                    <select
                        //className={`formEditText ${linguaParlataErrore ? "erroreInput" : ""}`}
                        value={servizi}
                        title="Seleziona un servizio"
                        onChange={aggiornaServizi}
                        required
                    >
                        <option value="" disabled>
                            Seleziona un servizio
                        </option>
                        {Object.values(Servizi).map((lang) => (
                            <option key={lang} value={lang}>
                                {lang}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="formField">
                    <input
                        type="number"
                        placeholder="Max persone"
                        title="Seleziona il numero massimo di partecipanti"
                        //className={`formEditText ${maxPartecipantiErrore ? "erroreInput" : ""}`}
                        value={maxPersone}
                        onChange={aggiornaMaxPeersone}
                        required
                    />
                </div>

                <div className="formField">
                    <input
                        type="number"
                        placeholder="Metratura"
                        title=" Metratura camera"
                        //className={`formEditText ${maxPartecipantiErrore ? "erroreInput" : ""}`}
                        value={metratura}
                        onChange={aggiornaMetratura}
                        required
                    />
                </div>

                <h3>Luogo</h3>
                <hr/>
                <div className="inlineAddress">
                    <input
                        type="text"
                        placeholder="Via"
                        title="Inserisci la via del luogo dell'evento"
                        //className={`formEditText ${viaErrore ? "erroreInput" : ""}`}
                        value={indirizzo.via}
                        onChange={aggiornaVia}
                        required
                    />

                    <input
                        type="text"
                        placeholder="Numero Civico"
                        title="Inserisci il numero civico del luogo dell'evento"
                        //className={`formEditText ${numCivicoErrore ? "erroreInput" : ""}`}
                        value={indirizzo.numCivico}
                        onChange={aggiornaNumCivico}
                        required
                    />
                </div>

                <div className="inlineCityDetails">
                    <div>
                        <input
                            type="text"
                            placeholder="Città"
                            title="Inserisci la città del luogo dell'evento"
                            //className={`formEditText ${cittaErrore ? "erroreInput" : ""}`}
                            value={indirizzo.citta}
                            onChange={aggiornaCitta}
                            required
                        />
                    </div>

                    <div>
                        <input
                            type="text"
                            placeholder="CAP"
                            title="Inserisci il CAP del luogo dell'evento"
                            //className={`formEditText ${capErrore ? "erroreInput" : ""}`}
                            value={indirizzo.cap}
                            onChange={aggiornaCap}
                            required
                        />
                    </div>

                    <div>
                        <input
                            type="text"
                            placeholder="Provincia"
                            title="Inserisci la provincia del luogo dell'evento"
                            value={indirizzo.provincia}
                            onChange={aggiornaProvincia}
                            required
                        />
                    </div>
                </div>

                <input
                    type= "file"
                    multiple
                    accept= "image/*"
                    onChange={aggiornaFotoAlloggio}
                    required
                />
                <button id="creaEventoButton" type="submit">Crea Alloggio</button>
            </form>
        </div>
    );
};

export default CreaAlloggio;