import {useEffect, useState} from "react";
import PropTypes from "prop-types";

const TitolodiStudio = {
    ScuolaPrimaria: "Scuola Primaria",
    ScuolaSecondariaPrimoGrado: "Scuola Secondaria Primo Grado",
    ScuolaSecondariaSecondoGrado: "Scuola Secondaria Secondo Grado",
    Laurea: "Laurea",
    Specializzazione: "Specializzazione"
};

const Genere = {
    Maschio: "Maschio",
    Femmina: "Femmina",
    NonSpecificato: "Non Specificato"
};

const giorniSettimana = ["Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato", "Domenica"];

const regexPatterns = {
    nome: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/,
    cognome: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/,
    nazionalita: /^[a-zA-Z\s]{5,30}$/,
    lingueParlate: /^[a-zA-Z ,]{5,30}$/,
    disponibilita: /^[A-Za-z0-9 ì,.:;-]+$/,
    skill: /^[^<>{}[\]]{1,255}$/,
};

const ModificaUtente = ({ userData , setUserData, onSuccess  }) => {
    const [nome, setNome] = useState("");
    const [cognome, setCognome] = useState("");
   const [nazionalita, setNazionalita] = useState("");
    const [titolodistudio, setTitolodistudio] = useState([]);
    const [genere, setGenere] = useState([]);
     const [skill, setSkill] = useState("");
    const [lingueParlate, setLingue] = useState("");
    const [orariDisponibili, setOrariDisponibili] = useState([]);
    const [dataDiNascita, setDataDiNascita] = useState("");
    const [errorMessages, setErrorMessages] = useState({});

    useEffect(() => {
        // Reimposta i valori quando userData cambia (utile se cambia dinamicamente)
        setNome(userData?.nomeUtente || "");
        setCognome(userData?.cognomeUtente || "");
        setNazionalita(userData?.nazionalitaUtente || "");
        setTitolodistudio(userData?.titoloDiStudioUtente || "");
        setGenere(userData?.genderUtente || "");
        setSkill(userData?.skillUtente || "");
        setLingue(userData?.lingueParlateUtente || "");
        setOrariDisponibili(userData?.disponibilitaUtente || []);
        setDataDiNascita(userData?.dataNascitaUtente || "");
    }, [userData]);

    const aggiornaDataDiNascita = (event) => {
        const value = event.target.value;
        setDataDiNascita(value);
        validateData("dataNascita", value);
    };

    const  validateData = (field, value) => {
        if (field === "dataNascita" && value) {
            const today = new Date();
            const dateValue = new Date(value);
            if (isNaN(dateValue.getTime())) {
                setErrorMessages((prev) => ({
                    ...prev,
                    dataNascita: "La data di nascita non è valida.",
                }));
                return;
            }

            if (dateValue >= today) {
                setErrorMessages((prev) => ({
                    ...prev,
                    dataNascita: "La data di nascita deve essere nel passato.",
                }));
                return;
            } else {
                setErrorMessages((prev) => {
                    const updatedErrors = { ...prev };
                    delete updatedErrors.dataNascita;
                    return updatedErrors;
                });
            }
        }
    }

    const aggiornaNome = (event) => {
        const value = event.target.value;
        setNome(value);
        validateField("nome", value);
    };

    const aggiornaCognome = (event) => {
        const value = event.target.value;
        setCognome(value);
        validateField("cognome", value);
    };

    const aggiornaNazionalita = (event) => {
        const value = event.target.value;
        setNazionalita(value);
        validateField("nazionalita", value);
    };

    const aggiornaSkill = (event) => {
        const value = event.target.value;
        setSkill(value);
        validateField("skill", value);
    };

    const aggiornaLingueParlate = (event) => {
        const value = event.target.value;
        setLingue(value);
        validateField("lingueParlate", value);
    };

    const aggiungiOrario = () => {
        setOrariDisponibili([...orariDisponibili, { giorno: "", start: "", end: "" }]);
    };


    const aggiornaOrario = (index, campo, valore) => {
        const nuoviOrari = [...orariDisponibili];  // Copia lo stato esistente
        nuoviOrari[index][campo] = valore;  // Modifica il campo specificato
        setOrariDisponibili(nuoviOrari);  // Aggiorna lo stato
    };


    const rimuoviOrario = (index) => {
        setOrariDisponibili(orariDisponibili.filter((_, i) => i !== index));
    };

    const isFormValid = () => {
        return Object.keys(errorMessages).length === 0;
    };

    const validateField = (field, value) => {
        if (!regexPatterns[field]?.test(value)) {
            setErrorMessages((prev) => ({
                ...prev,
                [field]: `Il campo ${field} non è valido.`,
            }));
        }else{
            setErrorMessages((prev) => {
                const updatedErrors = { ...prev };
                delete updatedErrors[field];
                return updatedErrors;
            });
        }
        if(value.trim().length === 0) {
            setErrorMessages((prev) => ({
                ...prev,
                [field]: `Il campo ${field} è vuoto.`,
            }));
        }
    };

    const ruolo = localStorage.getItem('ruolo');

    console.log(ruolo);
    const gestisciSubmit = async (event) => {
        if (!isFormValid()) {
            alert("Correggi i campi non validi prima di continuare.");
            return;
        }

        let orariStringa = null;

        // Se il ruolo è "Figura Specializzata", costruisco la stringa degli orari
        if (ruolo === "FiguraSpecializzata") {
            orariStringa = orariDisponibili
                .map(orario => `${orario.giorno} ${orario.start}-${orario.end}`)
                .join(", ")
                .replace(/\s*,\s*/g, ',')
                .trim();
            console.log(orariStringa);
        }

        event.preventDefault();
        const utenteDTO = {
            nomeUtente: nome,
            cognomeUtente: cognome,
            genderUtente: genere,
            titoloDiStudioUtente: titolodistudio,
            skillUtente: skill,
            nazionalitaUtente: nazionalita,
            lingueParlateUtente: lingueParlate,
            dataNascitaUtente: dataDiNascita,
            disponibilitaUtente: orariStringa
        };


        const email = localStorage.getItem('email');

        console.log(utenteDTO);
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                alert("Token non trovato. Effettua nuovamente il login.");
                return;
            }
            const response = await fetch(`http://localhost:8080/areaPersonale/modificaUtente/${email}`, {
                method: "POST",
                headers: {
                    'Authorization': `Bearer ${token}`,
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(utenteDTO)
            });

            if (response.ok) {
                const data = await response.text();
                setUserData(data);
                onSuccess();
                console.log("Modifica dati avvenuta con successo:", data);
            } else {
                console.error("Errore durante la modifica");
                alert("Errore, riprova!");
            }
        } catch (error) {
            console.error("Errore nella richiesta di modifica dati:", error);
            alert("Errore nel collegamento al server.");
        }
    };

    return (
        <div className="formRegistrazione">
            <h2>Modifica Dati Personali</h2>
            <form onSubmit={gestisciSubmit}>
                <label>Nome</label>
                <input
                    type="text"
                    placeholder={"Nome"}
                    className={`formEditText field ${errorMessages.nome ? 'error-field' : ''}`}
                    value={nome}
                    onChange={aggiornaNome}
                    required={true}
                />
                {errorMessages.nome && <p className="error">{errorMessages.nome}</p>}

                <label>Cognome</label>
                <input
                    type="text"
                    placeholder={"Cognome"}
                    className={`formEditText field ${errorMessages.cognome ? 'error-field' : ''}`}
                    value={cognome}
                    onChange={aggiornaCognome}
                    required={true}
                />
                {errorMessages.cognome && <p className="error">{errorMessages.cognome}</p>}

                <label>Data di Nascita</label>
                <input
                    type="date"
                    className={`formEditText field ${errorMessages.dataNascita ? 'error-field' : ''}`}
                    value={dataDiNascita}
                    onChange={aggiornaDataDiNascita}
                    required={true}
                />
                {errorMessages.dataNascita && <p className="error">{errorMessages.dataNascita}</p>}

                <label>Genere</label>
                <select
                    className={"formEditText selectMulti"}
                    value={genere}
                    onChange={(event) => setGenere(event.target.value)}
                    required={true}
                >
                    <option value="">Seleziona Genere</option>
                    {Object.entries(Genere).map(([value, label]) => (
                        <option key={value} value={value}>
                            {label}
                        </option>
                    ))}
                </select>

                <label>Nazionalità</label>
                <input
                    type="text"
                    placeholder={"Nazionalità"}
                    className={`formEditText field ${errorMessages.nazionalita ? 'error-field' : ''}`}
                    value={nazionalita}
                    onChange={aggiornaNazionalita}
                />
                {errorMessages.nazionalita && <p className="error">{errorMessages.nazionalita}</p>}
                <label>Lingue Parlate</label>
                <input
                    type="text"
                    placeholder={"Lingue Parlate"}
                    className={`formEditText field ${errorMessages.lingueParlate ? 'error-field' : ''}`}
                    value={lingueParlate}
                    onChange={aggiornaLingueParlate}
                    required={true}
                />
                {errorMessages.lingueParlate && <p className="error">{errorMessages.lingueParlate}</p>}
                <label>Skill</label>
                <input
                    type="text"
                    placeholder={"Skill"}
                    className={`formEditText field ${errorMessages.skill ? 'error-field' : ''}`}
                    value={skill}
                    onChange={aggiornaSkill}
                    required={true}
                />
                {errorMessages.skill && <p className="error">{errorMessages.skill}</p>}

                {ruolo === "FiguraSpecializzata" && (
                    <>
                        <h3>Disponibilità Settimanali</h3>
                        <hr/>
                        {orariDisponibili.map((orario, index) => (
                            <div key={index}>

                                <div className="inlineCityDetails">

                                    <select
                                        value={orario.giorno}
                                        onChange={(e) => aggiornaOrario(index, "giorno", e.target.value)}
                                    >
                                        <option value="">Seleziona Giorno</option>
                                        {giorniSettimana.map((giorno) => (
                                            <option key={giorno} value={giorno}>
                                                {giorno}
                                            </option>
                                        ))}
                                    </select>
                                    <input
                                        type="time"
                                        value={orario.start}
                                        onChange={(e) => aggiornaOrario(index, "start", e.target.value)}
                                    />
                                    <input
                                        type="time"
                                        value={orario.end}
                                        onChange={(e) => aggiornaOrario(index, "end", e.target.value)}
                                    />
                                </div>
                                <div className="buttonContainer">
                                    <button type="button" className="disponibilitaButton"
                                            onClick={() => rimuoviOrario(index)}>
                                        Rimuovi
                                    </button>
                                    {/* Pulsante "Aggiungi Orario" interno */}
                                    <button type="button" className="disponibilitaButton" onClick={aggiungiOrario}>
                                        Aggiungi Orario
                                    </button>
                                </div>
                            </div>
                        ))}
                        {/* Pulsante "Aggiungi Orario" esterno (visibile solo se orariDisponibili è vuoto) */}
                        {orariDisponibili.length === 0 && (
                            <div className="buttonContainer">
                                <button type="button" className="disponibilitaButton" onClick={aggiungiOrario}>
                                    Aggiungi Orario
                                </button>
                            </div>
                        )}
                    </>
                )}
                {errorMessages.disponibilita && <p className="error">{errorMessages.disponibilita}</p>}

                <label>Titolo di Studio</label>
                <select
                    className={"formEditText selectMulti"}
                    value={titolodistudio}
                    onChange={(event) => setTitolodistudio(event.target.value)}
                    required={true}
                >
                    <option value="">Seleziona Titolo di Studio</option>
                    {Object.entries(TitolodiStudio).map(([value, label]) => (
                        <option key={value} value={value}>
                            {label}
                        </option>
                    ))}
                </select>

                <button type="submit" className="formButton">
                    Invio
                </button>
            </form>
        </div>
    )
};

ModificaUtente.propTypes = {
    userData: PropTypes.object.isRequired,
    setUserData: PropTypes.func.isRequired,
    onSuccess: PropTypes.func.isRequired,
};
export default ModificaUtente;