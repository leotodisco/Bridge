import React, { useState } from "react";

const TipoContratto = {
    FULL_TIME: "Full Time",
    PART_TIME: "Part Time",
    APPRENDISTATO: "Apprendistato",
    INTERNSHIP: "Stage/Tirocinio",
    PROGETTO: "Contratto a Progetto",
    COLLABORAZIONE: "Collaborazione",
    COOPERATIVA: "Cooperativa",
    ALTRO: "Altro"
};

const Tipologia = {
    LAVORO: "LAVORO",
    CONSULENZA: "CONSULENZA"
};

const CreaLavoro = () => {
    const [titolo, setTitolo] = useState("");
    const [disponibilita, setDisponibilita] = useState(false);
    const [tipologia, setTipologia] = useState(Tipologia.LAVORO); // Impostato a Lavoro
    const [maxCandidature, setMaxCandidature] = useState(1);
    const [candidati, setCandidati] = useState([]);
    const [posizioneLavorativa, setPosizioneLavorativa] = useState("");
    const [nomeAzienda, setNomeAzienda] = useState("");
    const [orarioLavoro, setOrarioLavoro] = useState("");
    const [tipoContratto, setTipoContratto] = useState("");
    const [retribuzione, setRetribuzione] = useState("");
    const [nomeSede, setNomeSede] = useState("");
    const [infoUtili, setInfoUtili] = useState("");
    const [indirizzo, setIndirizzo] = useState({
        via: "",
        numCivico: "",
        citta: "",
        cap: "",
        provincia: ""
    });

    const aggiornaCampo = (setter) => (event) => {
        setter(event.target.value);
    };

    const aggiornaIndirizzo = (campo) => (event) => {
        setIndirizzo({
            ...indirizzo,
            [campo]: event.target.value
        });
    };

    const resetForm = () => {
        setTitolo("");
        setDisponibilita(false);
        setTipologia(Tipologia.LAVORO);
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
            provincia: ""
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (indirizzo.cap && !/^\d{5}$/.test(indirizzo.cap)) {
            alert("Il CAP deve contenere esattamente 5 cifre.");
            return;
        }

        const lavoroDTO = {
            titolo,
            disponibilita,
            tipologia,
            maxCandidature: parseInt(maxCandidature, 10),
            candidati: candidati.filter((email) => email.trim() !== ""),
            posizioneLavorativa,
            nomeAzienda,
            orarioLavoro,
            tipoContratto,
            retribuzione: parseFloat(retribuzione),
            nomeSede,
            infoUtili,
            indirizzo
        };

        try {
            const response = await fetch("http://localhost:8080/api/lavori/creaLavoro", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json"
                },
                body: JSON.stringify(lavoroDTO)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(`Errore: ${errorData.message || response.status}`);
            }

            alert("Annuncio di lavoro creato con successo!");
            resetForm();
        } catch (error) {
            console.error("Errore durante la creazione del lavoro: ", error);
            alert("Errore: " + error.message);
        }
    };

    return (
        <div className="formContainer">
            <h2>Crea un Annuncio di Lavoro</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="Titolo" value={titolo} onChange={aggiornaCampo(setTitolo)} required />
                <label>
                    Disponibilità:
                    <input type="checkbox" checked={disponibilita} onChange={(e) => setDisponibilita(e.target.checked)} />
                </label>
                <input type="hidden" value={tipologia} />
                <input
                    type="number"
                    placeholder="Massimo candidature"
                    value={maxCandidature}
                    onChange={aggiornaCampo(setMaxCandidature)}
                    min="1"
                    required
                />
                <input
                    type="text"
                    placeholder="Posizione Lavorativa"
                    value={posizioneLavorativa}
                    onChange={aggiornaCampo(setPosizioneLavorativa)}
                    required
                />
                <input
                    type="text"
                    placeholder="Nome Azienda"
                    value={nomeAzienda}
                    onChange={aggiornaCampo(setNomeAzienda)}
                    required
                />
                <input
                    type="text"
                    placeholder="Orario Lavoro"
                    value={orarioLavoro}
                    onChange={aggiornaCampo(setOrarioLavoro)}
                    required
                />
                <select value={tipoContratto} onChange={aggiornaCampo(setTipoContratto)} required>
                    <option value="">Seleziona Tipo Contratto</option>
                    {Object.entries(TipoContratto).map(([key, value]) => (
                        <option key={key} value={key}>
                            {value}
                        </option>
                    ))}
                </select>
                <input
                    type="number"
                    step="0.01"
                    placeholder="Retribuzione"
                    value={retribuzione}
                    onChange={aggiornaCampo(setRetribuzione)}
                    min="0.01"
                    required
                />
                <input type="text" placeholder="Nome Sede" value={nomeSede} onChange={aggiornaCampo(setNomeSede)} required />
                <textarea placeholder="Informazioni Utili" value={infoUtili} onChange={aggiornaCampo(setInfoUtili)} required />
                <h3>Indirizzo</h3>
                <input
                    type="text"
                    placeholder="Via"
                    value={indirizzo.via}
                    onChange={aggiornaIndirizzo("via")}
                    required
                />
                <input
                    type="text"
                    placeholder="Numero Civico"
                    value={indirizzo.numCivico}
                    onChange={aggiornaIndirizzo("numCivico")}
                    required
                />
                <input
                    type="text"
                    placeholder="Città"
                    value={indirizzo.citta}
                    onChange={aggiornaIndirizzo("citta")}
                    required
                />
                <input
                    type="text"
                    placeholder="CAP"
                    value={indirizzo.cap}
                    onChange={aggiornaIndirizzo("cap")}
                    required
                />
                <input
                    type="text"
                    placeholder="Provincia"
                    value={indirizzo.provincia}
                    onChange={aggiornaIndirizzo("provincia")}
                    required
                />
                <button type="submit">Crea Annuncio di Lavoro</button>
            </form>
        </div>
    );
};

export default CreaLavoro;
