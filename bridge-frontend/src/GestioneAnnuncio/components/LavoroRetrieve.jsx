import { useState, useEffect } from "react";
import PropTypes from "prop-types";
import "../../GestioneAnnuncio/css/formLavoro.css";
import "../../GestioneEvento/css/eventoView.css"
import {useLocation} from "react-router-dom";

const LavoroView = ({ id, onClose }) => {
    const [lavoroData, setLavoroData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [candidati, setCandidati] = useState([]);
    const [error, setError] = useState(null);
    const [editing, setEditing] = useState(false);
    const location = useLocation(); // Ottieni la route corrente
    const isLavoriUtenteRoute = location.pathname.includes("lavori-utente"); // Verifica se è la route desiderata
    const [titolo, setTitolo] = useState("");
    const [posizioneLavorativa, setPosizioneLavorativa] = useState("");
    const [retribuzione, setRetribuzione] = useState(0);
    const [nomeSede, setNomeSede] = useState("");
    const [nomeAzienda, setNomeAzienda] = useState("");
    const [orarioLavoro, setOrarioLavoro] = useState("");
    const [infoUtili, setInfoUtili] = useState("");
    const [showSecondPopup, setShowSecondPopup] = useState(false); // Stato per il secondo popup
    const [popupTransition, setPopupTransition] = useState(false); // Stato per il movimento del popup
    const [indirizzo, setIndirizzo] = useState({
        via: "",
        numCivico: "",
        citta: "",
        cap: "",
        provincia: ""
    });


    // Recupera l'email dell'utente loggato dal localStorage
    const emailUtenteLoggato = localStorage.getItem("email");

    const token = localStorage.getItem("authToken"); // Recupera il token JWT

    // Funzione per ottenere i dettagli di un annuncio di lavoro
    const fetchLavoro = async (id) => {
        try {

            const response = await fetch(`http://localhost:8080/api/annunci/view_lavori/retrieve/${id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

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

    // Effettua il fetch dei dettagli dell'annuncio al primo rendering
    useEffect(() => {

        if (showSecondPopup) {
            // Aggiungi la classe per far comparire il secondo popup
            setTimeout(() => {
                const secondPopup = document.querySelector('.second-popup');
                if (secondPopup) {
                    secondPopup.classList.add('visible');
                }
            }, 200); // Ritardo per sincronizzare la comparsa
            fetchCandidati(); // Carica i partecipanti
        }
    }, [showSecondPopup] );


    // Se il caricamento è in corso, mostra un messaggio
    if (loading) {
        return (
            <div className="popup-overlay">
                <div className="popup-container">
                    <p>Caricamento in corso...</p>
                </div>
            </div>
        );
    }


    // Se si è verificato un errore, mostra un messaggio
    if (error) {
        return (
            <div className="popup-overlay">
                <div className="popup-container">
                    <p>Errore: {error}</p>
                </div>
            </div>
        );
    }


    // Creazione stringa concatenata per l'indirizzo
    const luogoConcatenato = lavoroData
        ? `${lavoroData.indirizzo.via}, 
           ${lavoroData.indirizzo.numCivico}, 
           ${lavoroData.indirizzo.citta} 
           (${lavoroData.indirizzo.provincia}) 
           ${lavoroData.indirizzo.cap}`
        : "";


    // Funzione per la modifica dell'annuncio di lavoro
    const handleEdit = async () => {
        if (!id) {
            console.error("ID dell'annuncio non trovato.");
            alert("Errore: ID dell'annuncio non trovato.");
            return;
        }

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

        console.log("Dati inviati per la modifica:", aggiornamenti);

        try {
            const token = localStorage.getItem("token");
            if (!token) {
                console.error("Token non trovato. Effettua nuovamente il login.");
                alert("Token non trovato. Effettua nuovamente il login.");
                return;
            }

            const response = await fetch(`http://localhost:8080/api/annunci/modifica_lavoro/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify(aggiornamenti)
            });

            if (!response.ok) {
                throw new Error(`Errore HTTP: ${response.status}`);
            }

            const data = await response.json();
            alert("Annuncio modificato con successo!");
            setLavoroData(data);
            setEditing(false);
        } catch (err) {
            console.error("Errore durante la modifica dell'annuncio:", err);
            alert(`Non è stato possibile modificare l'annuncio. Dettagli: ${err.message}`);
        }
    };

    // Funzione per l'eliminazione dell'annuncio di lavoro
    const handleDelete = async () => {
        if (!window.confirm("Sei sicuro di voler eliminare questo annuncio?")) {
            return;
        }

        if (!token) {
            console.error("Token non trovato. Effettua nuovamente il login.");
            alert("Token non trovato. Effettua nuovamente il login.");
            return;
        }

        const id = lavoroData?.id;
        if (!id) {
            console.error("Errore: L'ID dell'annuncio è null o undefined.");
            alert("Errore: L'ID dell'annuncio non è valido.");
            return;
        }

        try {
            console.log(`Tentativo di eliminare l'annuncio con ID: ${id}`);

            const response = await fetch(`http://localhost:8080/api/annunci/elimina_lavoro/${id}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                if (response.status === 403) {
                    alert("Non sei autorizzato a eliminare questo annuncio.");
                } else {
                    alert(`Errore durante l'eliminazione dell'annuncio. Codice: ${response.status}`);
                }
                return;
            }

            alert("Annuncio eliminato con successo!");
            onClose(); // Chiudi il popup dopo l'eliminazione
            fetchLavori(); // Aggiorna la lista degli annunci
        } catch (err) {
            console.error("Errore durante l'eliminazione dell'annuncio:", err);
            alert(`Non è stato possibile eliminare l'annuncio. Dettagli: ${err.message}`);
        }
    };

        const fetchCandidati = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/annunci/candidati_lavoro/${id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                if (!response.ok) {
                    throw new Error("Errore durante il recupero dei candidati.");
                }
                const data = await response.json();
                setCandidati(data); // Presumiamo che data sia un array di stringhe
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };


    return (
        <div className="popup-overlay">
            <div className={`popup-container ${popupTransition ? "move-left" : ""}`}>
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
                                {luogoConcatenato}
                            </p>
                        </div>
                        {isLavoriUtenteRoute && emailUtenteLoggato === lavoroData.proprietario.email && (
                            <div className="button-group">
                                <button onClick={() => setEditing(true)} className="edit-button">
                                    Modifica Annuncio
                                </button>
                                <button onClick={handleDelete} className="delete-button">
                                    Elimina Annuncio
                                </button>
                                <button className="popup-button"
                                        onClick={() => {
                                            setPopupTransition(true);
                                            setTimeout(() => setShowSecondPopup(true), 500);
                                        }}>
                                    Mostra Candidati
                                    <i className="fa-solid fa-arrow-right" style={{marginLeft: '8px'}}></i>
                                </button>
                            </div>
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
            {showSecondPopup && (
                <div className="second-popup">
                    <button className="popup-close" onClick={() => { setPopupTransition(false);setShowSecondPopup(false)}}>
                        &times;
                    </button>
                    <h2>Lista dei Candidati</h2>
                    <ul>
                        {candidati.map((candidato, index) => (
                            <li key={index}>{candidato}</li> // Stampa ogni candidato come stringa
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

LavoroView.propTypes = {
    id: PropTypes.string.isRequired,
    onClose: PropTypes.func.isRequired
};

export default LavoroView;
