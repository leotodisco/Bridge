import { useState } from "react";
import '../../GestioneEvento/css/formEventoStyle.css';
import { useNavigate } from "react-router-dom";
import PropTypes from "prop-types";
import {toast} from "react-toastify";


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
const CreaEvento = ( {onClose} ) => {
    const nav = useNavigate();
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

    const emailUtenteLoggato = localStorage.getItem("email");

    // Funzione per chiudere il popup o reindirizzare
    const handleClose = () => {
        if (onClose) {
            onClose(); // Chiama la funzione di chiusura passata come prop
        }
    };

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

    // Const per validazione dei campi
    const [nomeErrore, setNomeErrore] = useState("");
    const[dataErrore, setDataErrore] = useState("");
    const[oraErrore, setOraErrore] = useState("");
    const[linguaParlataErrore, setLinguaParlataErrore] = useState("");
    const[descrizioneErrore, setDescrizioneErrore] = useState("");
    const[maxPartecipantiErrore, setMaxPartecipantiErrore] = useState("");
    const[luogoErrore, setLuogoErrore] = useState("");
    // Funzione per validare il campo nome
    const validaNome = () => {
        // Regex del campo nome
        const nomePattern = /^[A-Za-z0-9-ÿ .,'-]{3,100}$/;

        // Controllo: non vuoto
        if(nome.trim() === "") {
            setNomeErrore("Il nome dell'evento non può essere vuoto.");
            return false;
        }

        // Controllo: Pattern
        if(!nomePattern.test(nome)) {
            setNomeErrore("Il nome contiene caratteri non validi.");
            return false;
        }

        // Controllo: Lunghezza minima e massima
        if(nome.length < 3 || nome.length > 100) {
            setNomeErrore("Il nome deve essere tra 3 e 100 caratteri.");
            return false;
        }

        // Se tutti i controlli passano
        setNomeErrore("");
        return true;
    }

    // Funzione per validare il campo data
    const validaData = () => {
        // Controllo: non vuoto
        if(data.trim() === "") {
            setDataErrore("La data dell'evento non può essere vuota.");
            return false;
        }

        // Controllo: Data passata
        if (new Date(data) < new Date()) {
            setDataErrore("La data dell'evento non può essere nel passato.");
            return false;
        }

        // Se tutti i controlli passano
        setDataErrore("");
        return true;
    }

    // Funzione per validare il campo ora
    const validaOra = () => {
        // Controllo: non vuoto
        if(ora.trim() === "") {
            setOraErrore("L'ora dell'evento non può essere vuota.");
            return false;
        }

        // Se tutti i controlli passano
        setOraErrore("");
        return true;
    }

    // Funzione per validare il campo lingua parlata
    const validaLinguaParlata = () => {
        // Controllo: non vuoto
        if(linguaParlata.trim() === "") {
            setLinguaParlataErrore("Seleziona la lingua parlata durante l'evento.");
            return false;
        }

        // Se tutti i controlli passano
        setLinguaParlataErrore("");
        return true;
    }

    // Funzione per validare il campo descrizione
    const validaDescrizione = () => {
        // Regex del campo descrizione
        const descrizionePattern = /^[\wÀ-ÿ\s,.!?'-]{1,1000}$/;

        // Controllo: Pattern
        if(!descrizionePattern.test(descrizione)) {
            setDescrizioneErrore("La descrizione contiene caratteri non validi.");
            return false;
        }

        // Controllo: non vuoto
        if(descrizione.trim() === "") {
            setDescrizioneErrore("La descrizione dell'evento non può essere vuota.");
            return false;
        }

        // Controllo: Lunghezza minima
        if(descrizione.length < 1 || descrizione.length > 1000) {
            setDescrizioneErrore("Il nome deve essere tra 1 e 1000 caratteri.");
            return false;
        }

        // Se tutti i controlli passano
        setDescrizioneErrore("");
        return true;
    }

    // Funzione per validare il campo numero massimo di partecipanti
    const validaMaxPartecipanti = () => {
        // Regex del campo maxPartecipanti: solo numeri da 0 a 999
        const maxPartecipantiPattern = /^\d{1,3}$/;

        // Controllo: non vuoto
        if (!maxPartecipanti) {
            setMaxPartecipantiErrore("Il numero massimo di partecipanti è obbligatorio.");
            return false;
        }

        // Controllo: Pattern e range valido (1-999)
        const numero = parseInt(maxPartecipanti, 10);
        if (!maxPartecipantiPattern.test(maxPartecipanti) || numero < 1 || numero > 999) {
            setMaxPartecipantiErrore("Il numero massimo di partecipanti deve essere tra 1 e 999.");
            return false;
        }

        // Se tutti i controlli passano
        setMaxPartecipantiErrore("");
        return true;
    };

    // Funzione per validare il campo luogo
    const validaLuogo = () => {
        let errore = "";
        if (!/^[A-zÀ-ù ‘]{2,50}$/.test(luogo.via)) {
            errore = "La via non rispetta il formato corretto";
        } else if (!/^\d{1,5}$/.test(luogo.numCivico)) {
            errore = "Il numero civico deve essere valido.";
        } else if (!/^[A-zÀ-ù ‘]{2,50}$/.test(luogo.citta)) {
            errore = "La città non rispetta il corretto formato.";
        } else if (!/^\d{5}$/.test(luogo.cap)) {
            errore = "Il CAP deve essere di 5 cifre.";
        } else if (!/^[A-Z]{2}$/.test(luogo.provincia)) {
            errore = "La sigla della provincia non rispetta il formato corretto.";
        }

        if (errore) {
            setLuogoErrore(errore);
            return false;
        }
        setLuogoErrore("");
        return true;
    };

    // Funzione per gestire la creazione dell'evento
    const handleSubmit = async (event) => {
        // Impedisce il comportamento predefinito del form
        event.preventDefault();

        // Validazione dei campi
        const isNomeValido = validaNome();
        const isDataValida = validaData();
        const isOraValida = validaOra();
        const isLinguaParlataValida = validaLinguaParlata();
        const isDescrizioneValida = validaDescrizione();
        const isMaxPartecipantiValido = validaMaxPartecipanti();
        const isLuogoValido = validaLuogo();

        console.log("Validazione CAP: ", luogo.cap);
        console.log("Validazione campi: ", isNomeValido, isDataValida, isOraValida, isLinguaParlataValida, isDescrizioneValida, isMaxPartecipantiValido, isLuogoValido);

        if (!emailUtenteLoggato) {
            toast.error("Sessione scaduta. Esegui di nuovo il login.");
            return;
        }

        if(!isNomeValido || !isDataValida || !isOraValida || !isLinguaParlataValida || !isDescrizioneValida || !isMaxPartecipantiValido || !isLuogoValido) {
            console.log("Errore nella validazione dei campi!");
            return;
        }

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

            const token = localStorage.getItem("authToken");

            if (!token) {
                toast.error("Sessione scaduta. Esegui di nuovo il login.");
                nav('/login');
                return;
            }

            // Richiesta POST per creare l'evento
            const response = await fetch("http://localhost:8080/api/eventi/crea", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json",
                    "Authorization": `Bearer ${token}`
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
                        email: emailUtenteLoggato
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
            toast.success("Evento creato con successo!");
            console.log("Evento creato con successo: ", result);
            handleClose(); // Chiude il popup dopo la creazione
        } catch (error) {
            console.error("Errore durante la creazione dell'evento: ", error);
            toast.error("Errore nella creazione dell'evento. Riprova.");
        }
    };

    // Form per la creazione dell'evento
    return (
        <div className="FormPopup-overlay">
            <div className="FormPopup-container">
                <div className="header-FormContainer">
                    <h2>Crea un nuovo evento</h2>
                    <button className="close-button" onClick={handleClose}>
                        &times;
                    </button>
                </div>
                <div className="formContainer">
                <hr/>
                    <p className="introText">
                        Segui questi passaggi per creare un nuovo evento:
                    </p>
                    <ul className="introList">
                        <li>Compila i campi sottostanti con i dettagli del tuo evento.</li>
                        <li>Assicurati di riempire tutti i campi richiesti.</li>
                        <li>Quando hai completato, clicca sul pulsante <a href="#creaEventoButton"
                                                                          className="scrollLink"><strong>Crea
                            Evento</strong></a> per pubblicare.
                        </li>
                    </ul>

                    <form onSubmit={handleSubmit}>
                        <h3>Informazioni Principali:</h3>
                        <hr/>
                        <div className="formField">
                            <input
                                type="text"
                                placeholder="Nome evento"
                                title="Digita il nome dell'evento"
                                className={`formEditText ${nomeErrore ? "erroreInput" : ""}`}
                                value={nome}
                                onChange={aggiornaNome}
                                required
                            />
                            {nomeErrore ? <span className="errore">{nomeErrore}</span> : null}
                        </div>

                        <div className="formField">
                    <textarea
                        placeholder="Descrizione"
                        title="Digita la descrizione dell'evento"
                        className={`formEditText ${descrizioneErrore ? "erroreInput" : ""}`}
                        value={descrizione}
                        onChange={aggiornaDescrizione}
                        required
                    />
                            {descrizioneErrore ? <span className="errore">{descrizioneErrore}</span> : null}
                        </div>

                        <div className="formField">
                            <select
                                className={`formEditText ${linguaParlataErrore ? "erroreInput" : ""}`}
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
                            {linguaParlataErrore ? <span className="errore">{linguaParlataErrore}</span> : null}
                        </div>

                        <div className="formField">
                            <input
                                type="number"
                                placeholder="Max partecipanti"
                                title="Seleziona il numero massimo di partecipanti"
                                className={`formEditText ${maxPartecipantiErrore ? "erroreInput" : ""}`}
                                value={maxPartecipanti}
                                onChange={(e) => {
                                    const valore = parseInt(e.target.value, 10);
                                    if (valore >= 0 || e.target.value === "") {
                                        setMaxPartecipanti(e.target.value);
                                    }
                                }}
                                min="0"
                                required
                                onKeyDown={(e) => {
                                    if (e.key === "-" || e.key === "e") {
                                        e.preventDefault(); // Impedisce l'inserimento del simbolo meno o della notazione esponenziale
                                    }
                                }}
                            />
                            {maxPartecipantiErrore ? <span className="errore">{maxPartecipantiErrore}</span> : null}
                        </div>

                        <h3>Data e Ora</h3>
                        <hr/>
                        <div className="inlineDateTime">


                            <input
                                type="date"
                                title="Seleziona la data dell'evento"
                                className={`formEditText ${dataErrore ? "erroreInput" : ""}`}
                                value={data}
                                onChange={aggiornaData}
                                required
                            />
                            {dataErrore ? <span className="errore">{dataErrore}</span> : null}

                            <input
                                type="time"
                                title="Seleziona l'ora dell'evento"
                                className={`formEditText ${oraErrore ? "erroreInput" : ""}`}
                                value={ora}
                                onChange={aggiornaOra}
                                required
                            />
                            {oraErrore ? <span className="errore">{oraErrore}</span> : null}
                        </div>

                        <h3>Luogo</h3>
                        <hr/>
                        <div className="inlineAddress">
                            <input
                                type="text"
                                placeholder="Via"
                                title="Inserisci la via del luogo dell'evento"
                                //className={`formEditText ${viaErrore ? "erroreInput" : ""}`}
                                value={luogo.via}
                                onChange={aggiornaVia}
                                required
                            />

                            <input
                                type="text"
                                placeholder="Numero Civico"
                                title="Inserisci il numero civico del luogo dell'evento"
                                //className={`formEditText ${numCivicoErrore ? "erroreInput" : ""}`}
                                value={luogo.numCivico}
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
                                    value={luogo.citta}
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
                                    value={luogo.cap}
                                    onChange={aggiornaCap}
                                    required
                                />
                            </div>

                            <div>
                                <input
                                    type="text"
                                    placeholder="Provincia"
                                    title="Inserisci la provincia del luogo dell'evento"
                                    value={luogo.provincia}
                                    onChange={aggiornaProvincia}
                                    required
                                />
                            </div>
                        </div>
                        {luogoErrore ? <span className="errore">{luogoErrore}</span> : null}
                        <button id="creaEventoButton" type="submit">Crea Evento</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

// Definizione del tipo di prop richiesto
CreaEvento.propTypes = {
    onClose: PropTypes.func.isRequired, // onClose è obbligatorio
};

// Esporta il componente CreaEvento
export default CreaEvento;