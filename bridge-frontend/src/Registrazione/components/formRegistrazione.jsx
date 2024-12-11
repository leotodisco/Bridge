import { useState } from "react";
import './CreaUtente.css';

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
    const [disponibilita, setDisponibilita] = useState("");  // Nuovo stato per la disponibilità

    const aggiornaNome = (event) => {
        setNome(event.target.value);
    }

    const aggiornaCognome = (event) => {
        setCognome(event.target.value);
    };

    const aggiornaEmail = (event) => {
        setEmail(event.target.value);
    };

    const aggiornaPassword = (event) => {
        setPassword(event.target.value);
    };

    const aggiornaConfermaPW = (event) => {
        setConfermaPW(event.target.value);
    };

    const aggiornaNazionalita = (event) => {
        setNazionalita(event.target.value);
    }

    const aggiornaSkill = (event) => {
        setSkill(event.target.value);
    };

    const aggiornaLingueParlate = (event) => {
        setLingue(event.target.value);
    };

    const aggiornaDataDiNascita = (event) => {
        setDataDiNascita(event.target.value);
    };

    const aggiornaFotoProfilo = (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = () => {
                setFotoProfilo(reader.result); // Salva solo il Base64
            };
            reader.readAsDataURL(file);
        } else {
            setFotoProfilo(null);
        }
    };

    const aggiornaDisponibilita = (event) => {
        setDisponibilita(event.target.value);
    };

    const gestisciSubmit = async (event) => {
        event.preventDefault();
        const utenteDTO = {
            nomeUtente: nome,
            cognomeUtente: cognome,
            emailUtente: email,
            passwordUtente: password,
            confermaPWUtente: confermaPW,
            dataNascitaUtente: dataDiNascita,
            genderUtente: genere, // Singolo valore, es: "Maschio"
            titoloDiStudioUtente: titolodistudio, // Singolo valore, es: "Laurea"
            ruoloUtente: ruolo, // Singolo valore, es: "Volontario"
            skillUtente: skill,
            nazionalitaUtente: nazionalita,
            lingueParlateUtente: lingueParlate,
            fotoUtente: fotoProfilo,
            disponibilitaUtente: disponibilita  // Aggiunto campo disponibilità
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
            } else {
                console.error("Errore durante la registrazione");
                alert("Errore, riprova!");
            }
        } catch (error) {
            console.error("Errore nella richiesta di registrazione:", error);
            alert("Errore nel collegamento al server.");
        }
    };

    return (
        //Contenitore del form
        <div className="formRegistrazione">
            <h2>Registrazione nuovo utente</h2>
            <form onSubmit={gestisciSubmit}>
                <input
                    type="text"
                    placeholder={"Nome"}
                    className={"formEditText"}
                    value={nome}
                    onChange={aggiornaNome}
                    required={true}
                />

                <input
                    type="text"
                    placeholder={"Cognome"}
                    className={"formEditText"}
                    value={cognome}
                    onChange={aggiornaCognome}
                    required={true}
                />

                <input
                    type="date"
                    className={"formEditText"}
                    value={dataDiNascita}
                    onChange={aggiornaDataDiNascita}
                    required={true}
                />

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
                    className={"formEditText"}
                    value={nazionalita}
                    onChange={aggiornaNazionalita}
                />

                <input
                    type="text"
                    placeholder={"Lingue Parlate"}
                    className={"formEditText"}
                    value={lingueParlate}
                    onChange={aggiornaLingueParlate}
                    required={true}
                />

                <input
                    type="text"
                    placeholder={"Skill"}
                    className={"formEditText"}
                    value={skill}
                    onChange={aggiornaSkill}
                    required={true}
                />

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

                {/* Campo Disponibilità che appare solo se il ruolo è "Figura Specializzata" */}
                {ruolo === "FiguraSpecializzata" && (
                    <input
                        type="text"
                        placeholder={"Disponibilità"}
                        className={"formEditText"}
                        value={disponibilita}
                        onChange={aggiornaDisponibilita}
                        required={true}
                    />
                )}

                <input
                    type="text"
                    placeholder={"Email"}
                    className={"formEditText"}
                    value={email}
                    onChange={aggiornaEmail}
                    required={true}
                />

                <input
                    type="text"
                    placeholder={"Password"}
                    className={"formEditText"}
                    value={password}
                    onChange={aggiornaPassword}
                    required={true}
                />
                <input
                    type="text"
                    placeholder={"Conferma Password"}
                    className={"formEditText"}
                    value={confermaPW}
                    onChange={aggiornaConfermaPW}
                    required={true}
                />


                <input
                    type="file"
                    accept="image/*"
                    className="formEditText"
                    onChange={aggiornaFotoProfilo}
                    required={false}
                />

                <button type="submit" className="formButton">
                    Invio
                </button>
            </form>
        </div>
    );
};

export default CreaUtente;
