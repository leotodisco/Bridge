import { useState } from "react";
import "../css/formLavoro.css";

const TipoContratto = {
    FULL_TIME: "Full Time",
    PART_TIME: "Part Time",
    APPRENDISTATO: "Apprendistato",
    INTERNSHIP: "Stage/Tirocinio",
    PROGETTO: "Contratto a Progetto",
    COLLABORAZIONE: "Collaborazione",
    COOPERATIVA: "Cooperativa",
    ALTRO: "Altro",
};

const CreaLavoro = () => {
    const [titolo, setTitolo] = useState("");
    const [disponibilita, setDisponibilita] = useState(false);
    const [maxCandidature, setMaxCandidature] = useState(1);
    const [candidati, setCandidati] = useState([]);
    const [posizioneLavorativa, setPosizioneLavorativa] = useState("");
    const [nomeAzienda, setNomeAzienda] = useState("");
    const [orarioLavoro, setOrarioLavoro] = useState("");
    const [tipoContratto, setTipoContratto] = useState("");
    const [retribuzione, setRetribuzione] = useState(""); // Gestito come stringa
    const [nomeSede, setNomeSede] = useState("");
    const [infoUtili, setInfoUtili] = useState("");
    const [indirizzo, setIndirizzo] = useState({
        via: "",
        numCivico: "",
        citta: "",
        cap: "",
        provincia: "",
    });

    const aggiornaCampo = (setter) => (event) => {
        setter(event.target.value);
    };

    const aggiornaRetribuzione = (event) => {
        const valore = event.target.value.replace(",", "."); // Sostituisce la virgola con il punto
        setRetribuzione(valore);
    };

    const aggiornaIndirizzo = (campo) => (event) => {
        setIndirizzo({
            ...indirizzo,
            [campo]: event.target.value,
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        // Validazione locale aggiuntiva per il CAP
        if (indirizzo.cap && !/^\d{5}$/.test(indirizzo.cap)) {
            alert("Il CAP deve contenere esattamente 5 cifre.");
            return;
        }

        // Convertire la retribuzione con la virgola in un numero
        const retribuzioneNumerica = parseFloat(retribuzione.replace(",", "."));

        if (isNaN(retribuzioneNumerica) || retribuzioneNumerica <= 0) {
            alert("La retribuzione deve essere un numero valido e positivo.");
            return;
        }

        const lavoroDTO = {
            titolo,
            disponibilita,
            maxCandidature: parseInt(maxCandidature, 10),
            candidati: candidati.filter((email) => email.trim() !== ""),
            posizioneLavorativa,
            nomeAzienda,
            orarioLavoro,
            tipoContratto,
            retribuzione: retribuzioneNumerica, // Inviare il valore numerico al backend
            nomeSede,
            infoUtili,
            indirizzo,
            proprietario: "mario.verdi@example.com",
        };

        console.log("Dati dell'annuncio di lavoro: ", lavoroDTO);

        try {
            const response = await fetch("http://localhost:8080/api/annunci/creaLavoro", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                },
                body: JSON.stringify(lavoroDTO),
            });

            console.log("Risposta del server: ", response);

            if (!response.ok) {
                throw new Error(`Errore durante la creazione del lavoro: ${response.status}`);
            }

            const result = await response.json();
            console.log("Annuncio di lavoro creato con successo!", result);
            alert("Annuncio di lavoro creato con successo!");
            resetForm();
        } catch (error) {
            console.error("Errore durante la creazione del lavoro: ", error);
            alert("Errore durante la creazione del lavoro: " + error.message);
        }
    };

    const resetForm = () => {
        setTitolo("");
        setDisponibilita(false);
        setMaxCandidature(1);
        setCandidati([]);
        setPosizioneLavorativa("");
        setNomeAzienda("");
        setOrarioLavoro("");
        setTipoContratto("");
        setRetribuzione("");
        setNomeSede("");
        setInfoUtili("");
        setIndirizzo({
            via: "",
            numCivico: "",
            citta: "",
            cap: "",
            provincia: "",
        });
    };

    return (
        <div className="form-container">
            <h3>Crea un Annuncio di Lavoro</h3>
            <form className="form-card" onSubmit={handleSubmit}>
                {/* Titolo */}
                <div className="form-group">
                    <label htmlFor="titolo">Titolo</label>
                    <input
                        id="titolo"
                        type="text"
                        value={titolo}
                        onChange={aggiornaCampo(setTitolo)}
                        placeholder="Inserisci il titolo"
                        required
                    />
                </div>

                {/* Disponibilità */}
                <div className="form-group">
                    <label htmlFor="disponibilita">Disponibilità</label>
                    <div className="checkbox-container">
                        <input
                            type="checkbox"
                            id="disponibilita"
                            checked={disponibilita}
                            onChange={(e) => setDisponibilita(e.target.checked)}
                        />
                        <span>Annuncio Disponibile</span>
                    </div>
                </div>

                {/* Max Candidature */}
                <div className="form-group">
                    <label htmlFor="maxCandidature">Numero massimo di Candidature</label>
                    <input
                        id="maxCandidature"
                        type="number"
                        value={maxCandidature}
                        onChange={aggiornaCampo(setMaxCandidature)}
                        min="1"
                        required
                    />
                </div>

                {/* Posizione Lavorativa */}
                <div className="form-group">
                    <label htmlFor="posizione">Posizione Lavorativa</label>
                    <input
                        id="posizione"
                        type="text"
                        value={posizioneLavorativa}
                        onChange={aggiornaCampo(setPosizioneLavorativa)}
                        placeholder="Inserisci la posizione"
                        required
                    />
                </div>

                {/* Nome Azienda */}
                <div className="form-group">
                    <label htmlFor="nomeAzienda">Nome Azienda</label>
                    <input
                        id="nomeAzienda"
                        type="text"
                        value={nomeAzienda}
                        onChange={aggiornaCampo(setNomeAzienda)}
                        placeholder="Inserisci il nome dell'azienda"
                        required
                    />
                </div>

                {/* Orario Lavoro */}
                <div className="form-group">
                    <label htmlFor="orarioLavoro">Orario di Lavoro</label>
                    <input
                        id="orarioLavoro"
                        type="text"
                        value={orarioLavoro}
                        onChange={aggiornaCampo(setOrarioLavoro)}
                        placeholder="Formato: 09:00-18:00"
                        required
                    />
                </div>

                {/* Tipo Contratto */}
                <div className="form-group">
                    <label htmlFor="tipoContratto">Tipo Contratto</label>
                    <select
                        id="tipoContratto"
                        value={tipoContratto}
                        onChange={aggiornaCampo(setTipoContratto)}
                        required
                    >
                        <option value="">Seleziona</option>
                        {Object.entries(TipoContratto).map(([key, value]) => (
                            <option key={key} value={key}>
                                {value}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Retribuzione */}
                <div className="form-group">
                    <label htmlFor="retribuzione">Retribuzione (€)</label>
                    <input
                        id="retribuzione"
                        type="text"
                        value={retribuzione || ""}
                        onChange={aggiornaRetribuzione}
                        placeholder="Es: 1500.00"
                        required
                    />
                </div>

                {/* Nome Sede */}
                <div className="form-group">
                    <label htmlFor="nomeSede">Nome Sede</label>
                    <input
                        id="nomeSede"
                        type="text"
                        value={nomeSede}
                        onChange={aggiornaCampo(setNomeSede)}
                        placeholder="Inserisci il nome della sede"
                        required
                    />
                </div>

                {/* Info Utili */}
                <div className="form-group">
                    <label htmlFor="infoUtili">Informazioni Utili</label>
                    <textarea
                        id="infoUtili"
                        value={infoUtili}
                        onChange={aggiornaCampo(setInfoUtili)}
                        placeholder="Inserisci informazioni aggiuntive"
                        required
                    ></textarea>
                </div>

                {/* Indirizzo */}
                <fieldset className="form-group">
                    <legend>Indirizzo</legend>
                    <label>Via</label>
                    <input
                        type="text"
                        value={indirizzo.via}
                        onChange={aggiornaIndirizzo("via")}
                        placeholder="Inserisci la via"
                        required
                    />
                    <label>Numero Civico</label>
                    <input
                        type="text"
                        value={indirizzo.numCivico}
                        onChange={aggiornaIndirizzo("numCivico")}
                        placeholder="Inserisci il numero civico"
                        required
                    />
                    <label>Città</label>
                    <input
                        type="text"
                        value={indirizzo.citta}
                        onChange={aggiornaIndirizzo("citta")}
                        placeholder="Inserisci la città"
                        required
                    />
                    <label>CAP</label>
                    <input
                        type="text"
                        value={indirizzo.cap}
                        onChange={aggiornaIndirizzo("cap")}
                        placeholder="Inserisci il CAP"
                        required
                    />
                    <label>Provincia</label>
                    <input
                        type="text"
                        value={indirizzo.provincia}
                        onChange={aggiornaIndirizzo("provincia")}
                        placeholder="Inserisci la provincia"
                        required
                    />
                </fieldset>

                <button type="submit">Conferma</button>
            </form>
        </div>
    );
};

export default CreaLavoro;
