import { useEffect, useState } from "react";
import PropTypes from "prop-types"; // Importa PropTypes per validare le props

import "../../GestioneEvento/css/eventoView.css";

const EventView = ({ id, onClose }) => {
    const [eventData, setEventData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

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

    useEffect(() => {
        fetchEvent(id);
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
                <h1>Dettagli di Evento</h1>
                {eventData && (
                    <div>
                        <p>
                            <strong>Nome:</strong> {eventData.nome}
                        </p>
                        <div className="popup-details">
                            <p>
                                <strong>Data:</strong> {eventData.data}
                            </p>
                            <p>
                                <strong>Ora:</strong> {eventData.ora}
                            </p>
                            <p>
                                <strong>Lingua Parlata:</strong> {eventData.linguaParlata}
                            </p>
                            <p>
                                <strong>Descrizione:</strong> {eventData.descrizione}
                            </p>
                        </div>
                        <h3>Luogo</h3>
                        <p>
                            <strong>Indirizzo:</strong> {luogoConcatenato}
                        </p>
                        <h3>Organizzatore</h3>
                        <div className="popup-details">
                            <p>
                                <strong>Email:</strong> {eventData.organizzatore.email}
                            </p>
                            <p>
                                <strong>Numero Massimo Partecipanti:</strong> {eventData.maxPartecipanti}
                            </p>
                        </div>
                    </div>
                )}
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
