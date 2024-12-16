import { useState, useEffect } from "react";
import PropTypes from "prop-types";
import "../../GestioneAnnuncio/css/formLavoro.css";

const LavoroView = ({ id, onClose }) => {
    const [lavoroData, setLavoroData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editing, setEditing] = useState(false);

    const [titolo, setTitolo] = useState("");
    const [posizioneLavorativa, setPosizioneLavorativa] = useState("");
    const [retribuzione, setRetribuzione] = useState("");
    const [nomeSede, setNomeSede] = useState("");
    const [nomeAzienda, setNomeAzienda] = useState("");
    const [orarioLavoro, setOrarioLavoro] = useState("");
    const [infoUtili, setInfoUtili] = useState("");
    const [indirizzo, setIndirizzo] = useState({
        via: "",
        numCivico: "",
        citta: "",
        cap: "",
        provincia: ""
    });

    const emailUtenteLoggato = localStorage.getItem("email");

    const fetchLavoro = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/annunci/view_lavori/retrieve/${id}`);
            if (!response.ok) {
                throw new Error("Lavoro non trovato");
            }
            const data = await response.json();
            setLavoroData(data);
            console.log("Dati ricevuti: ",data);

            setTitolo(data.titolo);
            setPosizioneLavorativa(data.posizioneLavorativa);
            setRetribuzione(data.retribuzione);
            setNomeSede(data.nomeSede);
            setNomeAzienda(data.nomeAzienda);
            setOrarioLavoro(data.orarioLavoro);
            setInfoUtili(data.infoUtili);
            setIndirizzo({
                via: data.indirizzo.via,
                numCivico: data.indirizzo.numCivico,
                citta: data.indirizzo.citta,
                cap: data.indirizzo.cap,
                provincia: data.indirizzo.provincia
            });
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchLavoro(id);
    }, [id]);

    if (loading) {
        return (
            <div className="popup-overlay">
                <div className="popup-container">
                    <p>Caricamento in corso...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="popup-overlay">
                <div className="popup-container">
                    <p>Errore: {error}</p>
                </div>
            </div>
        );
    }

    // Creazione Indirizzo
    const luogoConcatenato = lavoroData
        ? `${lavoroData.indirizzo.via}, 
           ${lavoroData.indirizzo.numCivico}, 
           ${lavoroData.indirizzo.citta} 
           (${lavoroData.indirizzo.provincia}) 
           ${lavoroData.indirizzo.cap}`
        : "";

    // Funzione per la modifica dell'annuncio di lavoro
    const handleEdit = async () => {
        const aggiornamenti = {
            titolo,
            posizioneLavorativa,
            retribuzione,
            nomeSede,
            nomeAzienda,
            orarioLavoro,
            infoUtili,
            indirizzo
        };
        try {
            const token = localStorage.getItem("token");
            const response = await fetch(`http://localhost:8080/api/annunci/modifica_lavoro/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify(aggiornamenti)
            });

            if (!response.ok) {
                throw new Error("Errore durante la modifica dell'annuncio.");
            }

            const data = await response.json();
            alert("Annuncio modificato con successo!");
            setLavoroData(data);
            setEditing(false);
        } catch (err) {
            console.error(err);
            alert("Non è stato possibile modificare l'annuncio.");
        }
    };

    return (
        <div className="popup-overlay">
            <div className="popup-container">
                <button className="popup-close" onClick={onClose}>
                    &times;
                </button>
                {!editing ? (
                    <>
                        <div className="popup-header">
                            <h1 className="popup-title">{lavoroData.titolo}</h1>
                            <p className="popup-subtitle">{lavoroData.infoUtili}</p>
                        </div>
                        <div className="popup-body">
                            <h3>Dettagli</h3>
                            <div className="popup-details">
                                <div className="popup-row">
                                    <span className="popup-label">Posizione:</span>
                                    <span className="popup-value">{lavoroData.posizioneLavorativa}</span>
                                </div>
                                <div className="popup-row">
                                    <span className="popup-label">Retribuzione:</span>
                                    <span className="popup-value">
                                        {lavoroData.retribuzione ? `€ ${lavoroData.retribuzione}` : "Non specificata"}
                                    </span>
                                </div>
                                <div className="popup-row">
                                    <span className="popup-label">Azienda:</span>
                                    <span className="popup-value">{lavoroData.nomeAzienda}</span>
                                </div>
                                <div className="popup-row">
                                    <span className="popup-label">Nome Sede:</span>
                                    <span className="popup-value">{lavoroData.nomeSede}</span>
                                </div>
                                <div className="popup-row">
                                    <span className="popup-label">Orario:</span>
                                    <span className="popup-value">{lavoroData.orarioLavoro}</span>
                                </div>
                            </div>
                        </div>
                        <div className="popup-body">
                            <h3>Sede</h3>
                            <p>
                                {indirizzo.via}, {indirizzo.numCivico}, {indirizzo.citta} ({indirizzo.provincia}) -{" "}
                                {indirizzo.cap}
                            </p>
                        </div>
                        {emailUtenteLoggato === lavoroData.proprietario.email && (
                            <button onClick={() => setEditing(true)} className="edit-button">
                                Modifica Annuncio
                            </button>
                        )}
                    </>
                ) : (
                    <>
                        <h2>Modifica Annuncio</h2>
                        <div className="form-group">
                            <label>Titolo</label>
                            <input
                                type="text"
                                value={titolo}
                                onChange={(e) => setTitolo(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Posizione</label>
                            <input
                                type="text"
                                value={posizioneLavorativa}
                                onChange={(e) => setPosizioneLavorativa(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Retribuzione</label>
                            <input
                                type="text"
                                value={retribuzione}
                                onChange={(e) => setRetribuzione(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Azienda</label>
                            <input
                                type="text"
                                value={nomeAzienda}
                                onChange={(e) => setNomeAzienda(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Nome Sede</label>
                            <input
                                type="text"
                                value={nomeSede}
                                onChange={(e) => setNomeSede(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Orario</label>
                            <input
                                type="text"
                                value={orarioLavoro}
                                onChange={(e) => setOrarioLavoro(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Informazioni</label>
                            <textarea
                                value={infoUtili}
                                onChange={(e) => setInfoUtili(e.target.value)}
                            ></textarea>
                        </div>
                        <button onClick={handleEdit} className="save-button">
                            Salva Modifiche
                        </button>
                        <button onClick={() => setEditing(false)} className="cancel-button">
                            Annulla
                        </button>
                    </>
                )}
            </div>
        </div>
    );
};

LavoroView.propTypes = {
    id: PropTypes.string.isRequired,
    onClose: PropTypes.func.isRequired
};

export default LavoroView;
