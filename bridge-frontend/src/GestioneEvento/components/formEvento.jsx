import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router";

const Lingua = {
    ITALIANO: "ITALIANO",
    INGLESE: "INGLESE",
    FRANCESE: "FRANCESE",
    TEDESCO: "TEDESCO",
    SPAGNOLO: "SPAGNOLO",
    PORTOGHESE: "PORTOGHESE",
    UCRAINO: "UCRAINO",
    RUSSO: "RUSSO",
    CINESE: "CINESE",
    ARABO: "ARABO"
};

/*
 * Stati che memorizzano i dati inseriti nel form.
 * Crea una var con valore iniziale vuoto.
 * Crea la funzione che aggiorna i campi.
 */
const CreaEvento = () => {

    const [nome, setNome] = useState("");
    const[data, setData] = useState("");
    const[ora, setOra] = useState("");
    const[lingueParlate, setLingueParlate] = useState([]);
    const[descrizione, setDescrizione] = useState("");
    const[luogo, setLuogo] = useState({
        via: "",
        numCivico: "",
        citta: "",
        cap: "",
        provincia: ""
    });
    const[maxPartecipanti, setMaxPartecipanti] = useState(1);


    const nav = useNavigate();

    /*
     * Funzione che aggiorna il campo nome.
     */
    const aggiornaNome = (event) => {
        setNome(event.target.value);
    }

    /*
     * Funzione che aggiorna il campo data.
     */
    const aggiornaData = (event) => {
        setData(event.target.value);
    }

    /*
     * Funzione che aggiorna il campo ora.
     */
    const aggiornaOra = (event) => {
        setOra(event.target.value);
    }

    /*
     * Funzione che aggiorna il campo lingueParlate.
     */
    const aggiornaLingueParlate = (event) => {
        const options = event.target.selectedOptions;
        const values = Array.from(options).map(option => option.value);
        setLingueParlate(values);
    };

    /*
     * Funzione che aggiorna il campo descrizione.
     */
    const aggiornaDescrizione = (event) => {
        setDescrizione(event.target.value);
    }

    /*
     * Funzione che aggiorna il campo via.
     */
    const aggiornaVia = (event) => {
        setLuogo({
            ...luogo,
            via: event.target.value
        });
    }

    /*
     * Funzione che aggiorna il campo numCivico.
     */
    const aggiornaNumCivico = (event) => {
        setLuogo({
            ...luogo,
            numCivico: event.target.value
        });
    }

    /*
     * Funzione che aggiorna il campo citta.
     */
    const aggiornaCitta = (event) => {
        setLuogo({
            ...luogo,
            citta: event.target.value
        });
    }

    /*
     * Funzione che aggiorna il campo cap.
     */
    const aggiornaCap = (event) => {
        setLuogo({
            ...luogo,
            cap: event.target.value
        });
    }

    /*
     * Funzione che aggiorna il campo provincia.
     */
    const aggiornaProvincia = (event) => {
        setLuogo({
            ...luogo,
            provincia: event.target.value
        });
    }

    /*
     * Funzione che aggiorna il campo maxPartecipanti.
     */
    const aggiornaMaxPartecipanti = (event) => {
        setMaxPartecipanti(event.target.value);
    }

    const handleSubmit = (event) => {
        //Previene il comportamento di default del form
        event.preventDefault();

        //Creazione dell'oggetto eventoDTO
        const eventoDTO = {
            nome,
            data,
            ora,
            lingueParlate,
            descrizione,
            luogo,
            maxPartecipanti
        };

        //Invio della richiesta al backend
        axios
            //Richiesta POST al backend
            .post("http://localhost:8080/eventi/crea", eventoDTO)
            //Se la richiesta ha successo
            .then((response) => {
                //Stampa a console il messaggio di successo
                console.log("Evento creato con successo: ", response.data);
                nav("/overviewEventi");
            })
            //Se la richiesta fallisce
            .catch((error) => {
                //Stampa a console l'errore
                console.log("Errore durante la creazione dell'evento: ", error);
                alert("Errore durante la creazione dell'evento");
            });
    };

    return (
        //Contenitore del form
        <div className = "formContainer">
            <h2>Crea un Evento</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type = "text"
                    placeholder= {"Nome evento"}
                    className={"formEditText"}
                    value = {nome}
                    onChange = {aggiornaNome}
                    required={true}
                />

                <input
                    type = "date"
                    className={"formEditText"}
                    value = {data}
                    onChange = {aggiornaData}
                    required={true}
                />

                <input
                    type = "time"
                    className={"formEditText"}
                    value = {ora}
                    onChange = {aggiornaOra}
                    required={true}
                />

                <select
                    multiple
                    className={"formEditText"}
                    value = {lingueParlate}
                    onChange = {aggiornaLingueParlate}
                    required={true}
                >
                    <option value = "">Seleziona lingue</option>
                    {Object.values(Lingua).map((lingua) => (
                        <option key = {lingua} value = {lingua}>
                            {lingua}
                        </option>
                    ))}
                </select>

                <input
                    type = "text"
                    placeholder= {"Descrizione"}
                    className={"formEditText"}
                    value = {descrizione}
                    onChange = {aggiornaDescrizione}
                />

                <input
                    type = "text"
                    placeholder= {"Via"}
                    className={"formEditText"}
                    value = {luogo.via}
                    onChange = {aggiornaVia}
                    required={true}
                />

                <input
                    type = "text"
                    placeholder= {"Numero Civico"}
                    className={"formEditText"}
                    value = {luogo.numCivico}
                    onChange = {aggiornaNumCivico}
                    required={true}
                />

                <input
                    type = "text"
                    placeholder= {"Città"}
                    className={"formEditText"}
                    value = {luogo.citta}
                    onChange = {aggiornaCitta}
                    required={true}
                />

                <input
                    type = "text"
                    placeholder= {"CAP"}
                    className={"formEditText"}
                    value = {luogo.cap}
                    onChange = {aggiornaCap}
                    required={true}
                />

                <input
                    type = "text"
                    placeholder= {"Provincia"}
                    className={"formEditText"}
                    value = {luogo.provincia}
                    onChange = {aggiornaProvincia}
                    required={true}
                />

                <input
                    type = "number"
                    placeholder= {"Numero Massimo Partecipanti"}
                    className={"formEditText"}
                    value = {maxPartecipanti}
                    onChange = {aggiornaMaxPartecipanti}
                    required={true}
                />

                <button type = "submit" className = "formButton">
                    Crea Evento
                </button>
            </form>
        </div>
    );
};

export default CreaEvento;