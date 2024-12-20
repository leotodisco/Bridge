import { useState } from "react";
import './CreaUtente.css';
import {useNavigate} from "react-router";

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

const Ruolo = {
    Volontario: "Volontario",
    Rifugiato: "Rifugiato",
    FiguraSpecializzata: "Figura Specializzata",
};

const regexPatterns = {
    nome: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/,
    cognome: /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/,
    nazionalita: /^[a-zA-Z\s]{5,30}$/,
    lingueParlate: /^[a-zA-Z ,]{5,30}$/,
    email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    disponibilita: /^[A-Za-z0-9 ì,.:;-]+$/,
    password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/,
    skill: /^[^<>{}[\]]{1,255}$/,
};

const CreaUtente = () => {
    const [nome, setNome] = useState("");
    const [cognome, setCognome] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confermaPW, setConfermaPW] = useState("");
    const [nazionalita, setNazionalita] = useState("");
    const [titolodistudio, setTitolodistudio] = useState([]);
    const [genere, setGenere] = useState([]);
    const [ruolo, setRuolo] = useState([]);
    const [skill, setSkill] = useState("");
    const [lingueParlate, setLingue] = useState("");
    const [dataDiNascita, setDataDiNascita] = useState("");
    const [fotoProfilo, setFotoProfilo] = useState(null);
    const [disponibilita, setDisponibilita] = useState("");
    const [errorMessages, setErrorMessages] = useState({});
    const navigate = useNavigate();

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

    const aggiornaEmail = (event) => {
        const value = event.target.value;
        setEmail(value);
        validateField("email", value);
    };

    const aggiornaPassword = (event) => {
        const value = event.target.value;
        setPassword(value);
        validatePasswordConditions(value);
        validateField("password", value);
    };

    const aggiornaConfermaPW = (event) => {
        const value = event.target.value;
        setConfermaPW(value);
        if (password !== value) {
            setErrorMessages((prev) => ({
                ...prev,
                confermaPW: "Le password non corrispondono.",
            }));
        } else {
            setErrorMessages((prev) => {
                const updatedErrors = { ...prev };
                delete updatedErrors.confermaPW;
                return updatedErrors;
            });
        }
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

    const aggiornaFotoProfilo = (event) => {
        const file = event.target.files[0];
        if (file && (file.type === "image/jpeg" || file.type === "image/jpg")) {
            const reader = new FileReader();
            reader.onload = () => {
                setFotoProfilo(reader.result);
            };
            reader.readAsDataURL(file);
            setErrorMessages((prev) => {
                const updatedErrors = { ...prev };
                delete updatedErrors.fotoProfilo;
                return updatedErrors;
            });
        } else {
            setErrorMessages((prev) => ({
                ...prev,
                fotoProfilo: "Il file deve essere in formato JPG o JPEG.",
            }));
        }
    };


    const aggiornaDisponibilita = (event) => {
        const value = event.target.value;
        setDisponibilita(value);
        validateField("disponibilita", value);
    };

    const isFormValid = () => {
        return Object.keys(errorMessages).length === 0;
    };

    const gestisciSubmit = async (event) => {
            if (!isFormValid()) {
                alert("Correggi i campi non validi prima di continuare.");
                return;
            }
        event.preventDefault();
        const utenteDTO = {
            nomeUtente: nome,
            cognomeUtente: cognome,
            emailUtente: email,
            passwordUtente: password,
            confermaPWUtente: confermaPW,
            dataNascitaUtente: dataDiNascita,
            genderUtente: genere,
            titoloDiStudioUtente: titolodistudio,
            ruoloUtente: ruolo,
            skillUtente: skill,
            nazionalitaUtente: nazionalita,
            lingueParlateUtente: lingueParlate,
            fotoUtente: fotoProfilo,
            disponibilitaUtente: disponibilita
        };

        console.log(utenteDTO);
        try {
            const response = await fetch("http://localhost:8080/authentication/registrazioneUtente", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify({
                    "nomeUtente": nome,
                    "cognomeUtente": cognome,
                    "emailUtente": email,
                    "passwordUtente": password,
                    "confermaPWUtente": confermaPW,
                    "dataNascitaUtente": dataDiNascita,
                    "genderUtente": genere,
                    "titoloDiStudioUtente": titolodistudio,
                    "ruoloUtente": ruolo,
                    "skillUtente": skill,
                    "nazionalitaUtente": nazionalita,
                    "lingueParlateUtente": lingueParlate,
                    "fotoUtente": fotoProfilo,
                    "disponibilitaUtente": disponibilita
                })
            });

            if (response.ok) {
                const data = await response.text();
                console.log("Registrazione avvenuta con successo:", data);
                alert("Registrazione avvenuta con successo");
                navigate('/login');
            } else {
                console.error("Errore durante la registrazione");
                alert("Errore, riprova!");
            }
        } catch (error) {
            console.error("Errore nella richiesta di registrazione:", error);
            alert("Errore nel collegamento al server.");
        }
    };

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const [passwordConditions, setPasswordConditions] = useState({
        hasUpperCase: false,
        hasLowerCase: false,
        hasNumber: false,
        hasSpecialChar: false,
        isLengthValid: false
    });
    const [passwordError, setPasswordError] = useState("");
    const validatePasswordConditions = (password) => {
        const newConditions = {
            hasUpperCase: /[A-Z]/.test(password),
            hasLowerCase: /[a-z]/.test(password),
            hasNumber: /\d/.test(password),
            hasSpecialChar: /[@$!%*?&]/.test(password),
            isLengthValid: password.length >= 8 && password.length <= 16
        };
        setPasswordConditions(newConditions);

        if (!newConditions.hasUpperCase) {
            setPasswordError("La password deve contenere almeno una lettera maiuscola.");
        } else if (!newConditions.hasLowerCase) {
            setPasswordError("La password deve contenere almeno una lettera minuscola.");
        } else if (!newConditions.hasNumber) {
            setPasswordError("La password deve contenere almeno un numero.");
        } else if (!newConditions.hasSpecialChar) {
            setPasswordError("La password deve contenere almeno un carattere speciale.");
        } else if (!newConditions.isLengthValid) {
            setPasswordError("La password deve essere lunga tra 8 e 16 caratteri.");
        } else {
            setPasswordError(""); // Se tutte le condizioni sono soddisfatte, non ci sono errori
        }
    };

    return (
        <div className="formRegistrazione">
            <h2>Registrazione nuovo utente</h2>
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

                <select
                    className={"formEditText selectMulti"}
                    value={ruolo}
                    onChange={(event) => setRuolo(event.target.value)}
                    required={true}
                >
                    <option value="">Seleziona Ruolo</option>
                    {Object.entries(Ruolo).map(([value, label]) => (
                        <option key={value} value={value}>
                            {label}
                        </option>
                    ))}
                </select>


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
                <input
                    type="text"
                    placeholder={"Email"}
                    className={`formEditText field ${errorMessages.email ? 'error-field' : ''}`}
                    value={email}
                    onChange={aggiornaEmail}
                    required={true}
                />
                {errorMessages.email && <p className="error">{errorMessages.email}</p>}


                <div className="password-field">
                    <input
                        type={showPassword ? "text" : "password"}
                        placeholder="Password"
                        className={`formEditText field ${passwordError || errorMessages.password ? 'error-field' : ''}`}
                        value={password}
                        onChange={aggiornaPassword}
                        required={true}
                    />
                    <button type="button" onClick={togglePasswordVisibility}>
                        {showPassword ? "Nascondi Password" : "Mostra Password"}
                    </button>
                </div>
                {(passwordError || errorMessages.password) && (
                    <p className="error">
                        {passwordError || errorMessages.password}
                    </p>
                )}

                <div className="password-field">
                    <input
                        type={showConfirmPassword ? "text" : "password"}
                        placeholder="Conferma Password"
                        className={`formEditText  field ${errorMessages.confermaPW ? 'error-field' : ''}`}
                        value={confermaPW}
                        onChange={aggiornaConfermaPW}
                        required={true}
                    />
                    <button type="button" onClick={toggleConfirmPasswordVisibility}>
                        {showConfirmPassword ? "Nascondi Password" : "Mostra Password"}
                    </button>
                </div>
                {errorMessages.confermaPW && <p className="error">{errorMessages.confermaPW}</p>}


                <input
                    type="file"
                    accept="image/*"
                    className={`formEditText field ${errorMessages.fotoProfilo ? 'error-field' : ''}`}
                    onChange={aggiornaFotoProfilo}
                    required={false}
                />
                {errorMessages.fotoProfilo && <p className="error">{errorMessages.fotoProfilo}</p>}

                <button type="submit" className="formButton">
                    Invio
                </button>
            </form>
        </div>
    );
};

export default CreaUtente;
