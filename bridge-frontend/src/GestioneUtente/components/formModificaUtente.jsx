import { useState } from "react";

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

const regexPatterns = {
    nome: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/,
    cognome: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/,
    nazionalita: /^[a-zA-Z\s]{5,30}$/,
    lingueParlate: /^[a-zA-Z ,]{5,30}$/,
    disponibilita: /^[A-Za-z0-9 ì,.:;-]+$/,
    skill: /^[^<>{}[\]]{1,255}$/,
};

const ModificaUtente = () => {
    const [nome, setNome] = useState("");
    const [cognome, setCognome] = useState("");
   const [nazionalita, setNazionalita] = useState("");
    const [titolodistudio, setTitolodistudio] = useState([]);
    const [genere, setGenere] = useState([]);
     const [skill, setSkill] = useState("");
    const [lingueParlate, setLingue] = useState("");
     const [disponibilita, setDisponibilita] = useState("");
    const [dataDiNascita, setDataDiNascita] = useState("");
    const [errorMessages, setErrorMessages] = useState({});

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

    const aggiornaDisponibilita = (event) => {
        const value = event.target.value;
        setDisponibilita(value);
        validateField("disponibilita", value);
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
            disponibilitaUtente: disponibilita
        };


        const email = localStorage.getItem('email');

        console.log(utenteDTO);
        try {
            const response = await fetch(`http://localhost:8080/areaPersonale/modificaUtente/${email}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify({
                    "nomeUtente": nome,
                    "cognomeUtente": cognome,
                    "genderUtente": genere,
                    "titoloDiStudioUtente": titolodistudio,
                    "skillUtente": skill,
                    "nazionalitaUtente": nazionalita,
                    "lingueParlateUtente": lingueParlate,
                    "dataNascitaUtente": dataDiNascita,
                    "disponibilitaUtente": disponibilita
                })
            });

            if (response.ok) {
                const data = await response.text();
                console.log("Modifica dati avvenuta con successo:", data);
                alert("Modifica dati avvenuta con successo");
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
                <input
                    type="text"
                    placeholder={"Nome"}
                    className={`formEditText field ${errorMessages.nome ? 'error-field' : ''}`}
                    value={nome}
                    onChange={aggiornaNome}
                    required={true}
                />
                {errorMessages.nome && <p className="error">{errorMessages.nome}</p>}

                <input
                    type="text"
                    placeholder={"Cognome"}
                    className={`formEditText field ${errorMessages.cognome ? 'error-field' : ''}`}
                    value={cognome}
                    onChange={aggiornaCognome}
                    required={true}
                />
                {errorMessages.cognome && <p className="error">{errorMessages.cognome}</p>}

                <input
                    type="date"
                    className={`formEditText field ${errorMessages.dataNascita ? 'error-field' : ''}`}
                    value={dataDiNascita}
                    onChange={aggiornaDataDiNascita}
                    required={true}
                />
                {errorMessages.dataNascita && <p className="error">{errorMessages.dataNascita}</p>}

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

                <input
                    type="text"
                    placeholder={"Nazionalità"}
                    className={`formEditText field ${errorMessages.nazionalita ? 'error-field' : ''}`}
                    value={nazionalita}
                    onChange={aggiornaNazionalita}
                />
                {errorMessages.nazionalita && <p className="error">{errorMessages.nazionalita}</p>}
                <input
                    type="text"
                    placeholder={"Lingue Parlate"}
                    className={`formEditText field ${errorMessages.lingueParlate ? 'error-field' : ''}`}
                    value={lingueParlate}
                    onChange={aggiornaLingueParlate}
                    required={true}
                />
                {errorMessages.lingueParlate && <p className="error">{errorMessages.lingueParlate}</p>}
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
                    <input
                        type="text"
                        placeholder={"Disponibilità"}
                        className={`formEditText field ${errorMessages.disponibilita ? 'error-field' : ''}`}
                        value={disponibilita}
                        onChange={aggiornaDisponibilita}
                        required={true}
                    />
                )}
                {errorMessages.disponibilita && <p className="error">{errorMessages.disponibilita}</p>}

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
export default ModificaUtente;