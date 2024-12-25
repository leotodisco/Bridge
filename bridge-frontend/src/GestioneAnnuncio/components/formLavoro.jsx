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
    const [orariLavoro, setOrariLavoro] = useState([{ start: "", end: "" }]);
    const [tipoContratto, setTipoContratto] = useState("");
    const [retribuzione, setRetribuzione] = useState("");
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

    const aggiornaOrarioLavoro = (index, campo, valore) => {
        const nuoviOrari = [...orariLavoro];
        nuoviOrari[index][campo] = valore;
        setOrariLavoro(nuoviOrari);
    };

    const aggiornaIndirizzo = (campo) => (event) => {
        setIndirizzo({
            ...indirizzo,
            [campo]: event.target.value,
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        // Validazione del CAP
        if (indirizzo.cap && !/^\d{5}$/.test(indirizzo.cap)) {
            alert("Il CAP deve contenere esattamente 5 cifre.");
            return;
        }

        // Validazione della retribuzione
        const retribuzioneNumerica = parseFloat(retribuzione.replace(",", "."));

        if (isNaN(retribuzioneNumerica) || retribuzioneNumerica <= 0) {
            alert("La retribuzione deve essere un numero valido e positivo.");
            return;
        }

        // Validazione e trasformazione dell'orario di lavoro
        const orarioLavoroFormatted = orariLavoro
            .filter(({ start, end }) => start && end) // Rimuove intervalli incompleti
            .map(({ start, end }) => `${start}-${end}`) // Trasforma in formato `HH:mm-HH:mm`
            .join(", "); // Unisce intervalli multipli separandoli con una virgola

        console.log("Orario Formattato:", orarioLavoroFormatted);

        if (!orarioLavoroFormatted) {
            alert("Devi inserire almeno un intervallo di orario di lavoro completo.");
            return;
        }

        const email = localStorage.getItem("email");

        // Costruzione del DTO
        const lavoroDTO = {
            titolo,
            disponibilita,
            maxCandidature: parseInt(maxCandidature, 10),
            candidati: candidati.filter((email) => email.trim() !== ""),
            posizioneLavorativa,
            nomeAzienda,
            orarioLavoro: orarioLavoroFormatted, // Stringa formattata
            tipoContratto,
            retribuzione: retribuzioneNumerica,
            nomeSede,
            infoUtili,
            indirizzo,
            proprietario: email,
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
        setOrarioLavoro([{ start: "", end: "" }]); // Reset degli orari di lavoro
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
                        <label htmlFor="disponibilita" className="toggle-switch">
                            <input
                                type="checkbox"
                                id="disponibilita"
                                checked={disponibilita}
                                onChange={(e) => setDisponibilita(e.target.checked)}
                            />
                            <span className="switch-slider"></span>
                        </label>
                        <span>Spunta se l'annuncio deve risultare disponibile</span>
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

                {/* Orario di Lavoro */}
                <div className="form-group">
                    <label>Orario di Lavoro</label>
                    {orariLavoro.map((orario, index) => (
                        <div key={index} className="orari-row">
                            <div className="inlineCityDetails">
                                <input
                                    type="time"
                                    value={orario.start}
                                    onChange={(e) =>
                                        aggiornaOrarioLavoro(index, "start", e.target.value)
                                    }
                                    required
                                />
                                <input
                                    type="time"
                                    value={orario.end}
                                    onChange={(e) =>
                                        aggiornaOrarioLavoro(index, "end", e.target.value)
                                    }
                                    required
                                />
                            </div>
                        </div>
                    ))}
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
                        placeholder="Inserisci la retribuzione annua lorda"
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
