import {useState} from "react";

const TitolodiStudio = {
    ScuolaPrimaria: "Scuola Primaria",
    ScuolaSecondariaPrimoGrado: "Scuola Secondaria Primo Grado",
    ScuolaSecondariaSecondoGrado: "Scuola Secondaria Secondo Grado",
    Laurea: "Laurea",
    Specializzazione: "Specializzazione"
};

const Genere = {
    Maschio : "Maschio",
    Femmina: "Femmina",
    NonSpecificato: "NonSpecificato"
};

const Ruolo = {
    Volontario: "Volontario",
    Rifugiato: "Rifugiato",
    Admin: "Admin",
    FiguraSpecializzata: "Figura Specializzata",
};

const CreaUtente = () => {
    const [nome, setNome] = useState("");
    const[cognome, setCognome] = useState("");
    const[email, setEmail] = useState("");
    const[password, setPassword] = useState("");
    const[nazionalita, setNazionalita] = useState("");
    const[titolodistudio, setTitolodistudio] = useState([]);
    const[genere, setGenere] = useState([]);
    const[ruolo, setRuolo] = useState([]);
    const[skill, setSkill] = useState("");
    const[lingueParlate, setLingue] = useState("");
    const [dataDiNascita, setDataDiNascita] = useState(null);
    const [fotoProfilo, setFotoProfilo] = useState([]);

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

    const aggiornaTitoloDiStudio = (event) => {
        const options = event.target.selectedOptions;
        const values = Array.from(options).map(option => option.value);
        setTitolodistudio(values);
    };

    const aggiornaGenere = (event) => {
        const options = event.target.selectedOptions;
        const values = Array.from(options).map(option => option.value);
        setGenere(values);
    };

    const aggiornaRuolo = (event) => {
        const options = event.target.selectedOptions;
        const values = Array.from(options).map(option => option.value);
        setRuolo(values);
    };

    const aggiornaSkill = (event) => {
        setSkill(event.target.value);
    };

    const aggiornaLingueParlate = (event) => {
        setLingue(event.target.value);
    };

    const aggiornaDataDiNascita = (data) => {
        setDataDiNascita(data);
    };

    const aggiornaFotoProfilo = (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = () => {
                setFotoProfilo(reader.result); // Salva la foto come base64
            };
            reader.readAsDataURL(file);
        }
    };


    const gestisciSubmit = async (event) =>{
        event.preventDefault();
        const utenteDTO = {
            nome: nome,
            cognome: cognome,
            email: email,
            password: password,
            dataDiNascita: dataDiNascita,
            genere: genere,
            titolodistudio: titolodistudio,
            ruolo: ruolo,
            skill: skill,
            nazionalita: nazionalita,
            lingueParlate: lingueParlate,
            fotoProfilo: fotoProfilo
        }

        console.log(utenteDTO);
        try{
            const response = await fetch("http://localhost:8080/authentication/registrazioneVolontario", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json", // Specifica che il corpo è in formato JSON
                    "Accept": "application/json" // Specifica che il server risponde in formato JSON
                },
                body: JSON.stringify({
                    "nome": nome,
                    "cognome": cognome,
                    "email": email,
                    "password": password,
                    "dataDiNascita": dataDiNascita,
                    "genere": genere,
                    "titolodistudio": titolodistudio,
                    "ruolo": ruolo,
                    "skill": skill,
                    "nazionalita": nazionalita,
                    "lingueParlate": lingueParlate,
                    "fotoProfilo": fotoProfilo
                })
            });

            const data = await response.json();
            console.log(data);
        }catch (error){
            console.error("Errore",error);
        }
    };

    return (
        //Contenitore del form
        <div className = "formRegistrazione">
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
                    multiple
                    className={"formEditText"}
                    value={genere}
                    onChange={aggiornaGenere}
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
                    multiple
                    className={"formEditText"}
                    value={titolodistudio}
                    onChange={aggiornaTitoloDiStudio}
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
                    multiple
                    className={"formEditText"}
                    value={ruolo}
                    onChange={aggiornaRuolo}
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

                <input
                    type="file"
                    accept="image/*"
                    className={"formEditText"}
                    value = {fotoProfilo}
                    onChange={aggiornaFotoProfilo}
                    required={true}
                />
                <button type="submit" className="formButton">
                    Invio
                </button>
            </form>
        </div>
    );
};

export default CreaUtente;