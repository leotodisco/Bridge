import { useEffect, useState } from "react";
import PropTypes from "prop-types"; // Importa PropTypes per validare le props
import { format } from "date-fns"; // Importa date-fns per la formattazione
import "../../GestioneEvento/css/eventoView.css";
import {useNavigate} from "react-router-dom";

// Funzione per formattare la data
const formatDate = (dateString) => {
    return format(new Date(dateString), "dd-MM-yyyy"); // Converte nel formato dd-MM-yyyy
};

// Funzione per formattare l'ora
const formatTime = (timeString) => {
    const [hours, minutes] = timeString.split(":");
    return `${hours}:${minutes}`; // HH:mm
};

const EventView = ({ id, onClose }) => {
    const [eventData, setEventData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isSubscribed, setIsSubscribed] = useState(false);
    const [showSecondPopup, setShowSecondPopup] = useState(false); // Stato per il secondo popup
    const [popupTransition, setPopupTransition] = useState(false); // Stato per il movimento del popup
    const nav = useNavigate();
    const [partecipanti, setPartecipanti] = useState([]); // Stato per i partecipanti
    const [partecipantiError, setPartecipantiError] = useState(null);
    const [partecipantiLoading, setPartecipantiLoading] = useState(false);

    // Recupera l'email dell'utente loggato dal localStorage
    const emailPartecipante = localStorage.getItem("email");
    const token = localStorage.getItem("authToken");

    // Quando il secondo popup deve essere visibile
    useEffect(() => {
        if (showSecondPopup) {
            // Aggiungi la classe per far comparire il secondo popup
            setTimeout(() => {
                const secondPopup = document.querySelector('.second-popup');
                if (secondPopup) {
                    secondPopup.classList.add('visible');
                }
            }, 200); // Ritardo per sincronizzare la comparsa
            fetchPartecipanti(); // Carica i partecipanti
        }
    }, [showSecondPopup]);

    const fetchEvent = async (id) => {
        try {

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await fetch(`http://localhost:8080/api/eventi/retrieve/${id}?emailPartecipante=${encodeURIComponent(emailPartecipante)}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error("Evento non trovato");
            }
            const data = await response.json();
            setEventData(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Funzione per verificare l'iscrizione
    const checkSubscription = async () => {
        if (!emailPartecipante) {
            console.error("Email non presente nel localStorage.");
            return;
        }
        try {
            const url = `http://localhost:8080/api/eventi/${id}/iscrizione?emailPartecipante=${encodeURIComponent(emailPartecipante)}`;

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                throw new Error(`Errore nella richiesta: ${response.status} - ${response.statusText}`);
            }
            const isIscritto = await response.json();
            setIsSubscribed(isIscritto);
        } catch (error) {
            console.error("Errore durante la verifica dell'iscrizione:", error.message);
        }
    };

    const handleSubscription = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/eventi/${id}/iscrivi?emailPartecipante=${encodeURIComponent(emailPartecipante)}`, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
            });
            if (!response.ok) {
                throw new Error("Errore durante l'iscrizione");
            }
            alert("Iscrizione avvenuta con successo!");
            setIsSubscribed(true); // Aggiorna lo stato
        } catch (error) {
            console.error(error);
            alert("Errore durante l'iscrizione.");
        }
    };

    const handleUnsubscription = async () => {
        try {
            const token = localStorage.getItem('authToken');

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch(`http://localhost:8080/api/eventi/${id}/disiscrivi?emailPartecipante=${encodeURIComponent(emailPartecipante)}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });

            if (!response.ok) {
                throw new Error("Errore durante la disiscrizione");
            }
            alert("Disiscrizione avvenuta con successo!");
            setIsSubscribed(false); // Aggiorna lo stato
        } catch (error) {
            console.error(error);
            alert("Errore durante la disiscrizione.");
        }
    };

    const fetchPartecipanti = async () => {
        try {
            setPartecipantiLoading(true);

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch(`http://localhost:8080/api/eventi/partecipanti/${id}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

            if (!response.ok) {
                throw new Error("Errore durante il recupero dei partecipanti.");
            }
            const data = await response.json();
            setPartecipanti(data);
        } catch (err) {
            setPartecipantiError(err.message);
        } finally {
            setPartecipantiLoading(false);
        }
    };

    useEffect(() => {
        fetchEvent(id);
        checkSubscription();
    }, [id]);

    const luogoConcatenato = eventData
        ? `${eventData.luogo.via}, ${eventData.luogo.numCivico}, ${eventData.luogo.citta} (${eventData.luogo.provincia}) ${eventData.luogo.cap}`
        : "";

    return (
        <div className="popup-overlay">
            {loading ? (
                <div className="loading-message">Caricamento...</div>
            ) : error ? (
                <div className="error-message">{error}</div>
            ) : eventData ? (
                <div
                    className={`popup-container ${popupTransition ? "move-left" : ""}`}
                >
                    <button className="popup-close" onClick={onClose}>
                        &times;
                    </button>
                    <div className="popup-header">
                        <h1 className="popup-title">{eventData.nome}</h1>
                        <p className="popup-subtitle">{eventData.descrizione}</p>
                    </div>
                    <div className="popup-body">
                        <h3>Dettagli</h3>
                        <div className="popup-details">
                            <div className="popup-row">
                                <span className="popup-label">Data:</span>
                                <span className="popup-value">{formatDate(eventData.data)}</span>
                            </div>
                            <div className="popup-row">
                                <span className="popup-label">Ora:</span>
                                <span className="popup-value">{formatTime(eventData.ora)}</span>
                            </div>
                            <div className="popup-row">
                                <span className="popup-label">Lingua:</span>
                                <span className="popup-value">{eventData.linguaParlata}</span>
                            </div>
                            <div className="popup-row">
                                <span className="popup-label">Max Partecipanti:</span>
                                <span className="popup-value">{eventData.maxPartecipanti}</span>
                            </div>
                        </div>
                        <h3>Luogo</h3>
                        <p className="popup-location">{luogoConcatenato}</p>
                        <h3>Organizzatore</h3>
                        <div className="popup-details">
                            <div className="popup-row">
                                <span className="popup-label">Nome:</span>
                                <span className="popup-value">{eventData.organizzatore.nome} {eventData.organizzatore.cognome}</span>
                            </div>
                        </div>
                        {emailPartecipante !== eventData.organizzatore.email && (
                            <div className="popup-actions">
                                {isSubscribed ? (
                                    <button className="popup-button" onClick={handleUnsubscription}>
                                        Disiscriviti
                                    </button>
                                ) : (
                                    <button className="popup-button" onClick={handleSubscription}>
                                        Iscriviti
                                    </button>
                                )}
                            </div>
                        )}
                        <button
                            className="popup-button"
                            onClick={() => {
                                setPopupTransition(true);
                                setTimeout(() => setShowSecondPopup(true), 500);
                            }}
                        >
                            Mostra Partecipanti
                            <i className="fa-solid fa-arrow-right" style={{marginLeft: '8px'}}></i>
                        </button>
                    </div>
                </div>
            ) : null}

            {showSecondPopup && (
                <div className="second-popup">
                    <button className="popup-close" onClick={() => { setPopupTransition(false);setShowSecondPopup(false)}}>
                        &times;
                    </button>
                    <h2>Partecipanti</h2>
                    {partecipantiLoading ? (
                        <p>Caricamento partecipanti...</p>
                    ) : partecipantiError ? (
                        <p>Errore: {partecipantiError}</p>
                    ) : (
                        <ul>
                            {partecipanti.map((partecipante) => (
                                <li key={partecipante.id} className="partecipante-item">
                                <span className="dati">
                                    {partecipante.nome} {partecipante.cognome}
                                </span>
                                    <span className="email">{partecipante.email}</span>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}

        </div>
    );
};

EventView.propTypes = {
    id: PropTypes.string.isRequired,
    onClose: PropTypes.func.isRequired,
};

export default EventView;
