import {useEffect, useState} from "react";
import '../css/CreaUtente.css';
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


const giorniSettimana = ["Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato", "Domenica"];

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
    const [fotoProfilo, setFotoProfilo] = useState("/immagineProfiloVuota.jpg");
    const [errorMessages, setErrorMessages] = useState({});
    const [orariDisponibili, setOrariDisponibili] = useState([]);
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

    const convertiImmagineInBase64 = (percorsoImmagine) => {
        return new Promise((resolve, reject) => {
            const immagine = new Image();
            immagine.src = percorsoImmagine;

            immagine.onload = () => {
                const canvas = document.createElement("canvas");
                const ctx = canvas.getContext("2d");
                canvas.width = immagine.width;
                canvas.height = immagine.height;
                ctx.drawImage(immagine, 0, 0);
                resolve(canvas.toDataURL("image/jpeg"));
            };

            immagine.onerror = reject;
        });
    };



    useEffect(() => {
        const caricaImmaginePredefinita = async () => {
            const immagineBase64 = await convertiImmagineInBase64('/immagineProfiloVuota.jpg');
            setFotoProfilo(immagineBase64);  // Imposta l'immagine predefinita in Base64
        };

        caricaImmaginePredefinita();
    }, []);

    const aggiornaFotoProfilo = (event) => {
        const file = event.target.files[0];

        if (file) {
            // Controlla se il file è di tipo JPEG/JPG
            if (file.type === "image/jpeg" || file.type === "image/jpg") {
                const reader = new FileReader();
                reader.onload = () => {
                    setFotoProfilo(reader.result); // Aggiorna l'immagine con il file caricato
                };
                reader.readAsDataURL(file);

                // Rimuovi l'eventuale messaggio di errore
                setErrorMessages((prev) => {
                    const updatedErrors = { ...prev };
                    delete updatedErrors.fotoProfilo;
                    return updatedErrors;
                });
            } else {
                // Imposta un messaggio di errore per tipi di file non validi
                setErrorMessages((prev) => ({
                    ...prev,
                    fotoProfilo: "Il file deve essere in formato JPG o JPEG.",
                }));
                setFotoProfilo('/immagineProfiloVuota.jpg'); // Reimposta l'immagine predefinita
                console.log("Immagine predefinita impostata: /immagineProfiloVuota.jpg");
            }
        } else {
            // Se non è stato selezionato alcun file, imposta l'immagine predefinita
            setFotoProfilo('/immagineProfiloVuota.jpg');
            setErrorMessages((prev) => ({
                ...prev,
                fotoProfilo: "Nessun file selezionato. È stata impostata l'immagine di default.",
            }));
        }
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

    const gestisciSubmit = async (event) => {
            if (!isFormValid()) {
                alert("Correggi i campi non validi prima di continuare.");
                return;
            }
        event.preventDefault();

        //costruisco la stringa per gli orari di disponibilità
        const orariStringa = orariDisponibili
            .map(orario => `${orario.giorno} ${orario.start}-${orario.end}`)
            .join(", ")
            .replace(/\s*,\s*/g, ',')
            .trim();
        console.log(orariStringa);

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
            disponibilitaUtente: orariStringa
        };

        console.log(utenteDTO);
        try {

            const response = await fetch("http://localhost:8080/authentication/registrazioneUtente", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(utenteDTO)
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

    console.log(passwordConditions);
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
                    <button type="button" className= "showPW"  onClick={togglePasswordVisibility}>
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
