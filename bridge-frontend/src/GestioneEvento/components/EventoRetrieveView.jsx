import { useEffect, useState } from "react";
import PropTypes from "prop-types"; // Importa PropTypes per validare le props
import { format } from "date-fns"; // Importa date-fns per la formattazione
import "../../GestioneEvento/css/eventoView.css";

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

    // Recupera l'email dell'utente loggato dal localStorage
    const emailPartecipante = localStorage.getItem("email");
    const token = localStorage.getItem("token");

    const fetchEvent = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/eventi/retrieve/${id}`);
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
        try {
            const response = await fetch(`http://localhost:8080/api/eventi/${id}/iscrizione?emailPartecipante=${encodeURIComponent(emailPartecipante)}`);
            if (!response.ok) {
                throw new Error("Errore durante la verifica dell'iscrizione");
            }
            const isIscritto = await response.json();
            setIsSubscribed(isIscritto);
        } catch (error) {
            console.error(error);
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

    // Funzione per gestire la disiscrizione
    const handleUnsubscription = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/eventi/${id}/disiscrivi?emailPartecipante=${encodeURIComponent(emailPartecipante)}`, {
                method: "DELETE",
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

    useEffect(() => {
        fetchEvent(id);
        checkSubscription();
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

    // Concatenazione del luogo in un'unica stringa
    const luogoConcatenato = eventData
        ? `${eventData.luogo.via}, ${eventData.luogo.numCivico}, ${eventData.luogo.citta} (${eventData.luogo.provincia}) ${eventData.luogo.cap}`
        : "";

    return (
        <div className="popup-overlay">
            <div className="popup-container">
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
                            <span
                                className="popup-value">{eventData.organizzatore.nome} {eventData.organizzatore.cognome}</span>
                        </div>
                    </div>
                    {/* Controlla che l'utente loggato non sia il proprietario dell'evento */}
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
                </div>
            </div>
        </div>
    );
};

// Definizione di PropTypes per validare le props
EventView.propTypes = {
    id: PropTypes.string.isRequired, // `id` deve essere una stringa e obbligatoria
    onClose: PropTypes.func.isRequired, // `onClose` deve essere una funzione e obbligatoria
};

export default EventView;
