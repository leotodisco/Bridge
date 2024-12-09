import {useState} from "react";
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
    Admin: "Admin",
    FiguraSpecializzata: "Figura Specializzata",
};

const CreaUtente = () => {
    const [nome, setNome] = useState("");
    const [cognome, setCognome] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [nazionalita, setNazionalita] = useState("");
    const [titolodistudio, setTitolodistudio] = useState([]);
    const [genere, setGenere] = useState([]);
    const [ruolo, setRuolo] = useState([]);
    const [skill, setSkill] = useState("");
    const [lingueParlate, setLingue] = useState("");
    const [dataDiNascita, setDataDiNascita] = useState("");
    //const [fotoProfilo, setFotoProfilo] = useState([]);

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

    const aggiornaNazionalita = (event) => {
        setNazionalita(event.target.value);
    };

    /*const aggiornaTitoloDiStudio = (event) => {
        const options = event.target.selectedOptions;
        const values = Array.from(options).map(option => option.value);
        setTitolodistudio(values);
    };*/

    /*const aggiornaGenere = (event) => {
        const options = event.target.selectedOptions;
        const values = Array.from(options).map(option => option.value);
        setGenere(values);
    };*/

    /*const aggiornaRuolo = (event) => {
        const options = event.target.selectedOptions;
        const values = Array.from(options).map(option => option.value);
        setRuolo(values);
    };*/

    const aggiornaSkill = (event) => {
        setSkill(event.target.value);
    };

    const aggiornaLingueParlate = (event) => {
        setLingue(event.target.value);
    };

    const aggiornaDataDiNascita = (event) => {
        setDataDiNascita(event.target.value);
    };

    /*const aggiornaFotoProfilo = (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = () => {
                setFotoProfilo(reader.result); // Salva la foto come base64
            };
            reader.readAsDataURL(file);
        }
    };*/


    const gestisciSubmit = async (event) => {
        event.preventDefault();
        const utenteDTO = {
            nomeUtente: nome,
            cognomeUtente: cognome,
            emailUtente: email,
            passwordUtente: password,
            dataNascitaUtente: dataDiNascita,
            genderUtente: genere, // Singolo valore, es: "Maschio"
            titoloDiStudioUtente: titolodistudio, // Singolo valore, es: "Laurea"
            ruoloUtente: ruolo, // Singolo valore, es: "Volontario"
            skillUtente: skill,
            nazionalitaUtente: nazionalita,
            lingueParlateUtente: lingueParlate,
            fotoUtente: null
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
                    "dataNascitaUtente": dataDiNascita,
                    "genderUtente": genere,
                    "titoloDiStudioUtente": titolodistudio,
                    "ruoloUtente": ruolo,
                    "skillUtente": skill,
                    "nazionalitaUtente": nazionalita,
                    "lingueParlateUtente": lingueParlate,
                    "fotoProfiloUtente": null
                })
            });

            if (response.ok) {
                const data = await response.text();
                console.log("Login avvenuto con successo:", data);
                alert("Login avvenuto con successo!");
                // Puoi salvare il token o l'utente autenticato nello stato o in localStorage
            } else {
                console.error("Errore durante il login");
                alert("Email o password errati.");
            }
        } catch (error) {
            console.error("Errore nella richiesta di login:", error);
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
                    className={"formEditText"}
                    value={genere}
                    onChange={(event) => setGenere(event.target.value)}
                    required={true}
                >
                    <option value="">Seleziona Genere</option>
                    {Object.values(Genere).map((genere) => (
                        <option key={genere} value={genere}>
                            {genere}
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
                    className={"formEditText"}
                    value={titolodistudio}
                    onChange={(event) => setTitolodistudio(event.target.value)}
                    required={true}
                >
                    <option value="">Seleziona Titolo di Studio</option>
                    {Object.values(TitolodiStudio).map((titolodistudio) => (
                        <option key={titolodistudio} value={titolodistudio}>
                            {titolodistudio}
                        </option>
                    ))}
                </select>

                <select
                    className={"formEditText"}
                    value={ruolo}
                    onChange={(event) => setRuolo(event.target.value)}
                    required={true}
                >
                    <option value="">Seleziona Ruolo</option>
                    {Object.values(Ruolo).map((ruolo) => (
                        <option key={ruolo} value={ruolo}>
                            {ruolo}
                        </option>
                    ))}
                </select>


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


                <button type="submit" className="formButton">
                    Invio
                </button>
            </form>
        </div>
    );
};
/*
<input
    type="file"
    accept="image/*"
    className={"formEditText"}
    value={fotoProfilo}
    onChange={aggiornaFotoProfilo}
    required={true}
/>*/

export default CreaUtente;