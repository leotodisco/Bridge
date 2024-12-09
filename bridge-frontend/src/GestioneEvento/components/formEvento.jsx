import { useState } from "react";
import '../../GestioneEvento/css/formEventoStyle.css';

// Oggetto contenente le lingue disponibili
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

// Funzione per creare l'evento
const CreaEvento = () => {
    // Stato per gestire i dati del modulo
    const [nome, setNome] = useState(""); // Nome dell'evento
    const [data, setData] = useState(""); // Data dell'evento
    const [ora, setOra] = useState(""); // Ora dell'evento
    const [linguaParlata, setLinguaParlata] = useState(""); // Lingua parlata durante l'evento
    const [descrizione, setDescrizione] = useState(""); // Descrizione dell'evento
    const [luogo, setLuogo] = useState({ //Oggetto per gestire i dettagli del luogo
        via: "",
        numCivico: "",
        citta: "",
        cap: "",
        provincia: ""
    });
    const [maxPartecipanti, setMaxPartecipanti] = useState(1); // Numero massimo di partecipanti

    // Funzioni per aggiornare i valori dello stato

    // Aggiorna il nome dell'evento
    const aggiornaNome = (event) => setNome(event.target.value);

    // Aggiorna la data dell'evento
    const aggiornaData = (event) => setData(event.target.value);

    // Aggiorna l'ora dell'evento
    const aggiornaOra = (event) => setOra(event.target.value);

    //Aggiorna la lingua parlata durante l'evento
    const aggiornaLinguaParlata = (event) => {
        setLinguaParlata(event.target.value);
    };

    // Aggiorna la descrizione dell'evento
    const aggiornaDescrizione = (event) => setDescrizione(event.target.value);

    // Aggiorna la via del luogo dell'evento
    const aggiornaVia = (event) => {
        setLuogo({
            ...luogo,
            via: event.target.value
        });
    }

    // Aggiorna il numero civico del luogo dell'evento
    const aggiornaNumCivico = (event) => {
        setLuogo({
            ...luogo,
            numCivico: event.target.value
        });
    }

    // Aggiorna la città del luogo dell'evento
    const aggiornaCitta = (event) => {
        setLuogo({
            ...luogo,
            citta: event.target.value
        });
    }

    // Aggiorna il CAP del luogo dell'evento
    const aggiornaCap = (event) => {
        setLuogo({
            ...luogo,
            cap: event.target.value
        });
    }

    // Aggiorna la provincia del luogo dell'evento
    const aggiornaProvincia = (event) => {
        setLuogo({
            ...luogo,
            provincia: event.target.value
        });
    }

    // Aggiorna il numero massimo di partecipanti
    const aggiornaMaxPartecipanti = (event) => setMaxPartecipanti(event.target.value);

    // Funzione per gestire la creazione dell'evento
    const handleSubmit = async (event) => {
        // Impedisce il comportamento predefinito del form
        event.preventDefault();

        // Oggetto contenente i dati dell'evento
        const eventoDTO = {
            nome: nome,
            data: data,
            ora: ora,
            linguaParlata: linguaParlata,
            descrizione: descrizione,
            luogo: luogo,
            maxPartecipanti: maxPartecipanti
        };

        console.log("Evento da creare: ", eventoDTO);

        try {
            // Richiesta POST per creare l'evento
            const response = await fetch("http://localhost:8080/eventi/crea", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                // Converte l'oggetto in formato JSON
                body: JSON.stringify({
                    nome: nome,
                    data: data,
                    ora: ora,
                    linguaParlata: linguaParlata,
                    descrizione: descrizione,
                    luogo: {
                        via: luogo.via,
                        numCivico: luogo.numCivico,
                        cap: luogo.cap,
                        citta: luogo.citta,
                        provincia: luogo.provincia
                    },
                    organizzatore: {
                        "email": "volontario@example.com" //todo: prendere l'email dell'utente loggato
                    },
                    maxPartecipanti: maxPartecipanti
                })
            });

            // Se la richiesta non va a buon fine, lancia un errore
            if (!response.ok) {
                throw new Error(`Errore HTTP: ${response.status}`);
            }

            // Se la richiesta va a buon fine, stampa il risultato
            const result = await response.json();
            console.log("Evento creato con successo: ", result);
        } catch (error) {
            console.error("Errore durante la creazione dell'evento: ", error);
            alert("Errore durante la creazione dell'evento");
        }
    };

    // Form per la creazione dell'evento
    return (
        // Form per la creazione dell'evento
        <div className="formContainer">
            <h2>Crea un nuovo evento</h2>
            <hr/>
            <p className="introText">
                Segui questi passaggi per creare un nuovo evento:
            </p>
            <ul className="introList">
                <li>Compila i campi sottostanti con i dettagli del tuo evento.</li>
                <li>Assicurati di riempire tutti i campi richiesti.</li>
                <li>Quando hai completato, clicca sul pulsante <a href="#creaEventoButton" className="scrollLink"><strong>Crea Evento</strong></a> per pubblicare.
                </li>
            </ul>

            <form onSubmit={handleSubmit}>
                <h3>Informazioni Principali:</h3>
                <hr/>
                <input
                    type="text"
                    placeholder="Nome evento"
                    title="Digita il nome dell'evento"
                    className="formEditText"
                    value={nome}
                    onChange={aggiornaNome}
                    required
                />
                <textarea
                    placeholder="Descrizione"
                    title="Digita la descrizione dell'evento"
                    value={descrizione}
                    onChange={aggiornaDescrizione}
                    required
                />

                <select
                    className="formEditText"
                    value={linguaParlata}
                    title="Seleziona la lingua parlata durante l'evento"
                    onChange={aggiornaLinguaParlata}
                    required
                >
                    <option value="" disabled>
                        Seleziona una lingua
                    </option>
                    {Object.values(Lingua).map((lang) => (
                        <option key={lang} value={lang}>
                            {lang}
                        </option>
                    ))}
                </select>

                <input
                    type="number"
                    placeholder="Max partecipanti"
                    title="Seleziona il numero massimo di partecipanti"
                    value={maxPartecipanti}
                    onChange={aggiornaMaxPartecipanti}
                    required
                />
                <h3>Data e Ora</h3>
                <hr/>
                <div className="inlineDateTime">
                    <input
                        type="date"
                        title="Seleziona la data dell'evento"
                        className="formEditText"
                        value={data}
                        onChange={aggiornaData}
                        required
                    />
                    <input
                        type="time"
                        title="Seleziona l'ora dell'evento"
                        className="formEditText"
                        value={ora}
                        onChange={aggiornaOra}
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
                        value={luogo.via}
                        onChange={aggiornaVia}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Numero Civico"
                        title="Inserisci il numero civico del luogo dell'evento"
                        value={luogo.numCivico}
                        onChange={aggiornaNumCivico}
                        required
                    />
                </div>

                <div className="inlineCityDetails">
                    <input
                        type="text"
                        placeholder="Città"
                        title="Inserisci la città del luogo dell'evento"
                        value={luogo.citta}
                        onChange={aggiornaCitta}
                        required
                    />
                    <input
                        type="text"
                        placeholder="CAP"
                        title="Inserisci il CAP del luogo dell'evento"
                        value={luogo.cap}
                        onChange={aggiornaCap}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Provincia"
                        title="Inserisci la provincia del luogo dell'evento"
                        value={luogo.provincia}
                        onChange={aggiornaProvincia}
                        required
                    />
                </div>

                <button id="creaEventoButton" type="submit">Crea Evento</button>
            </form>
        </div>
    );
};

// Esporta il componente CreaEvento
export default CreaEvento;