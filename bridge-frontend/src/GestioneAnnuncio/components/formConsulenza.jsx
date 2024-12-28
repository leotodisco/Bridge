import { useState } from "react";
import {useNavigate} from "react-router-dom";

const Tipo = {
    SANITARIA: "SANITARIA",
    LEGALE: "LEGALE",
    COMMERCIALE: "COMMERCIALE",
    PSICOLOGICA: "PSICOLOGICA",
    TRADUTTORE: "TRADUTTORE"
};

const giorniSettimana = ["Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato", "Domenica"];
const nav = useNavigate;

const CreaConsulenza = () => {
    const [titolo, setTitolo] = useState("");
    const [descrizione, setDescrizione] = useState("");
    const [orariDisponibili, setOrariDisponibili] = useState([]);
    const [numero, setNumero] = useState("");
    const [tipo, setTipo] = useState("");
    const [maxCandidature, setMaxCandidature] = useState(1);
    const [indirizzo, setIndirizzo] = useState({
        via: "",
        numCivico: "",
        citta: "",
        cap: "",
        provincia: ""
    });

    //funzione per errori
    const [errori, setErrori] = useState({
        numero: "",
        orariDisponibili: "",
        cap: "",
        titolo:"",
    });

    const emailUtenteLoggato = localStorage.getItem("email");

    // Funzioni di aggiornamento dei campi
    const aggiornaCampo = (setter) => (event) => {
        setter(event.target.value);
    };

    const aggiornaIndirizzo = (campo) => (event) => {
        setIndirizzo({
            ...indirizzo,
            [campo]: event.target.value
        });
    };

    //per controllare che non vi siano sovrapposizioni orarie
    const controllaSovrapposizioniOrari = () => {
        for (let i = 0; i < orariDisponibili.length; i++) {
            for (let j = i + 1; j < orariDisponibili.length; j++) {
                if (orariDisponibili[i].giorno === orariDisponibili[j].giorno) {
                    const start1 = orariDisponibili[i].start;
                    const end1 = orariDisponibili[i].end;
                    const start2 = orariDisponibili[j].start;
                    const end2 = orariDisponibili[j].end;

                    if (
                        (start1 < end2 && start2 < end1) || // Sovrapposizione oraria
                        start1 === start2 || end1 === end2 // Intervalli identici
                    ) {
                        return false;
                    }
                }
            }
        }
        return true;
    };


    //logica per gestire gli orari di disponibilità
    const aggiungiOrario = () => {
        setOrariDisponibili([...orariDisponibili, { giorno: "", start: "", end: "" }]);
    };

    const aggiornaOrario = (index, campo, valore) => {
        const nuoviOrari = [...orariDisponibili];
        nuoviOrari[index][campo] = valore;
        setOrariDisponibili(nuoviOrari);
    };

    const rimuoviOrario = (index) => {
        setOrariDisponibili(orariDisponibili.filter((_, i) => i !== index));
    };


    const validaCampi = () => {
        const nuoviErrori = { numero: "", orariDisponibili: "", cap: "" };
        let valido = true;

        //validazione titolo
        if (titolo.length<=2 && titolo.length<=100) {
            nuoviErrori.titolo = "Il titolo deve contenere più di due caratteri";
            valido = false;
        }

        // Validazione numero telefono
        if (numero && !/^((00|\+)39[. ]??)??3\d{2}[. ]??\d{6,7}$/.test(numero)) {
            nuoviErrori.numero = "Numero di telefono non valido. Deve seguire il formato italiano (+39 o 0039 opzionali).";
            valido = false;
        }

        // Validazione orari disponibili
        if (orariDisponibili.length === 0) {
            nuoviErrori.orariDisponibili = "Devi inserire almeno un orario disponibile.";
            valido = false;
        } else if (orariDisponibili.some(orario => !orario.giorno || !orario.start || !orario.end)) {
            nuoviErrori.orariDisponibili = "Completa tutti i campi degli orari disponibili.";
            valido = false;
        } else if (!controllaSovrapposizioniOrari()) {
            nuoviErrori.orariDisponibili = "Gli orari disponibili non possono sovrapporsi.";
            valido = false;
        }

        // Validazione formato orari disponibili
        //const orariPattern = /^(\w+\s+\d{2}:\d{2}-\d{2}:\d{2})(,\s*\w+\s+\d{2}:\d{2}-\d{2}:\d{2})*$/;
        //if (orariDisponibili && !orariPattern.test(orariDisponibili)) {
        //    nuoviErrori.orariDisponibili = "Formato errato: 'Giorno hh:mm-hh:mm'.";
        //    valido = false;
        //}

        // Validazione CAP
        if (indirizzo.cap && !/^\d{5}$/.test(indirizzo.cap)) {
            nuoviErrori.cap = "Il CAP deve contenere esattamente 5 cifre.";
            valido = false;
        }

        setErrori(nuoviErrori);
        return valido;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!emailUtenteLoggato) {
            alert("Sessione scaduta. Esegui di nuovo il login.");
            return;
        }

        if (!validaCampi()) {
            return; // Non procedere se ci sono errori
        }


        //costruisco la stringa per gli orari di disponibilità
        const orariStringa = orariDisponibili
            .map(orario => `${orario.giorno} ${orario.start}-${orario.end}`)
            .join(", ")
            .replace(/\s*,\s*/g, ',')
            .trim();


        console.log("orario generato " + orariStringa);

        const consulenzaDTO = {
            titolo,
            descrizione,
            orariDisponibili:orariStringa,
            numero,
            tipo,
            maxCandidature,
            indirizzo: {
                via: indirizzo.via,
                numCivico: indirizzo.numCivico,
                citta: indirizzo.citta,
                cap: indirizzo.cap,
                provincia: indirizzo.provincia
            },
            disponibilita: true,
            proprietario: emailUtenteLoggato
        };

        console.log("Dati della consulenza: ", consulenzaDTO);

        try {
            const token = localStorage.getItem("authToken");
            // check su token:
            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await fetch("http://localhost:8080/api/annunci/creaConsulenza", {
                method: "POST",
                headers: {
                    'Authentication': `Bearer ${token}`,
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(consulenzaDTO)
            });

            if (!response.ok) {
                throw new Error(`Errore HTTP: ${response.status}`);
            }

            const result = await response.json();
            console.log("Consulenza creata con successo: ", result);
            alert("Consulenza creata con successo!");
        } catch (error) {
            console.error("Errore durante la creazione della consulenza: ", error);
            alert("Errore durante la creazione della consulenza: " + error.message);
        }
    };

    return (
        <div className="formContainer">
            <h2>Crea una Consulenza</h2>
            <hr/>
            <p className="introText">
                Segui questi passaggi per creare un nuova consulenza:
            </p>
            <ul className="introList">
                <li>Compila i campi sottostanti con i dettagli per la tua consulenza.</li>
                <li>Assicurati di riempire tutti i campi richiesti.</li>
                <li>Quando hai completato, clicca sul pulsante <a href="#creaConsulenzaButton"
                                                                  className="scrollLink"><strong>Crea
                    Consulenza</strong></a> per pubblicare.
                </li>
            </ul>
            <form onSubmit={handleSubmit}>
                <h3>Informazioni Principali:</h3>
                <hr/>
                <input
                    type="text"
                    placeholder="Titolo"
                    className="formEditText"
                    value={titolo}
                    onChange={aggiornaCampo(setTitolo)}
                    required
                />
                {errori.titolo && <p className="error">{errori.titolo}</p>}

                <textarea
                    placeholder="Descrizione"
                    className="formEditText"
                    value={descrizione}
                    onChange={aggiornaCampo(setDescrizione)}
                    required
                />

                <h3>Orari Disponibili</h3>
                {orariDisponibili.map((orario, index) => (
                    <div key={index}>
                        <div className="inlineCityDetails">
                            <select value={orario.giorno}
                                    onChange={(e) => aggiornaOrario(index, "giorno", e.target.value)}>
                                <option value="">Seleziona Giorno</option>
                                {giorniSettimana.map(giorno => (
                                    <option key={giorno} value={giorno}>{giorno}</option>
                                ))}
                            </select>
                            <input type="time" value={orario.start}
                                   onChange={(e) => aggiornaOrario(index, "start", e.target.value)}/>
                            <input type="time" value={orario.end}
                                   onChange={(e) => aggiornaOrario(index, "end", e.target.value)}/>
                        </div>

                        <button type="button" onClick={() => rimuoviOrario(index)}>Rimuovi</button>
                    </div>
                ))}
                <button
                    type="button"
                    onClick={aggiungiOrario}
                    disabled={orariDisponibili.some(orario => !orario.giorno || !orario.start || !orario.end)}
                    className={orariDisponibili.some(orario => !orario.giorno || !orario.start || !orario.end) ? "buttonDisabled" : ""}
                >
                    Aggiungi Orario
                </button>
                {errori.orariDisponibili && <p className="error">{errori.orariDisponibili}</p>}

                <input
                    type="text"
                    placeholder="Numero di contatto"
                    className="formEditText"
                    value={numero}
                    onChange={aggiornaCampo(setNumero)}
                />
                {errori.numero && <p className="error">{errori.numero}</p>}

                <select
                    className="formEditText"
                    value={tipo}
                    onChange={aggiornaCampo(setTipo)}
                    required
                >
                    <option value="">Seleziona Tipo Consulenza</option>
                    {Object.values(Tipo).map((tipoValue) => (
                        <option key={tipoValue} value={tipoValue}>
                            {tipoValue}
                        </option>
                    ))}
                </select>

                <div className="inlineCityDetails">
                    <input
                        type="number"
                        placeholder="Massimo candidature"
                        className="formEditText candidatureInput"
                        value={maxCandidature}
                        onChange={aggiornaCampo(setMaxCandidature)}
                        min="1"
                        required
                    />
                </div>

                <h3>Indirizzo</h3>
                <hr/>
                <div className="inlineCityDetails">
                    <input
                        type="text"
                        placeholder="Via"
                        className="formEditText"
                        value={indirizzo.via}
                        onChange={aggiornaIndirizzo("via")}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Numero Civico"
                        className="formEditText"
                        value={indirizzo.numCivico}
                        onChange={aggiornaIndirizzo("numCivico")}
                        required
                    />
                </div>

                <div className="inlineCityDetails">
                    <input
                        type="text"
                        placeholder="Città"
                        className="formEditText"
                        value={indirizzo.citta}
                        onChange={aggiornaIndirizzo("citta")}
                        required
                    />
                    <input
                        type="text"
                        placeholder="CAP"
                        className="formEditText"
                        value={indirizzo.cap}
                        onChange={aggiornaIndirizzo("cap")}
                        required
                    />

                    <input
                        type="text"
                        placeholder="Provincia"
                        className="formEditText"
                        value={indirizzo.provincia}
                        onChange={aggiornaIndirizzo("provincia")}
                        required
                    />
                </div>
                {errori.cap && <p className="error">{errori.cap}</p>}


                <button id="creaConsulenzaButton" type="submit" className="formButton">
                    Crea Consulenza
                </button>
            </form>
        </div>
    );
};

export default CreaConsulenza;
