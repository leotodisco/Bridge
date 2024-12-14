import { useState, useEffect } from "react";
import PropTypes from "prop-types"; // Per validare le props
import "../../GestioneEvento/css/eventoView.css";

const ConsulenzaView = ({ id, onClose }) => {
    const [consulenzaData, setConsulenzaData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Funzione per recuperare i dettagli della consulenza
    const fetchConsulenza = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/annunci/view_consulenze/retrive/${id}`);
            if (!response.ok) {
                throw new Error("Consulenza non trovata");
            }
            const data = await response.json();
            setConsulenzaData(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchConsulenza(id);
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
    const luogoConcatenato = consulenzaData
        ? `${consulenzaData.indirizzo.via}, 
        ${consulenzaData.indirizzo.numCivico}, 
        ${consulenzaData.indirizzo.citta} 
        (${consulenzaData.indirizzo.provincia}) 
        ${consulenzaData.indirizzo.cap}`
        : "";
    return (
        <div className="popup-overlay">
            <div className="popup-container">
                <button className="popup-close" onClick={onClose}>
                    &times;
                </button>
                <div className="popup-header">
                    <h1 className="popup-title">{consulenzaData.titolo}</h1>
                    <p className="popup-subtitle">{consulenzaData.descrizione}</p>
                </div>
                <div className="popup-body">
                    <h3>Dettagli</h3>
                    <div className="popup-details">
                        <div className="popup-row">
                            <span className="popup-label">Tipologia:</span>
                            <span className="popup-value">{consulenzaData.tipo}</span>
                        </div>
                        <div className="popup-row">
                            <span className="popup-label">Orari disponibili:</span>
                            <span className="popup-value">{consulenzaData.orariDisponibili}</span>
                        </div>
                    </div>
                </div>
                <div className="popup-body">
                    <h3>Contatti Diretti</h3>
                    <div className="popup-details">
                        <div className="popup-row">
                            <span className="popup-label">Email:</span>
                            <span className="popup-value">{consulenzaData.proprietario.email}</span>
                        </div>
                        <div className="popup-row">
                            <span className="popup-label">Numero Telefono:</span>
                            <span className="popup-value">{consulenzaData.numero}</span>
                        </div>
                    </div>
                </div>
                <div className="popup-body">
                    <h3>Sede</h3>
                   <p className="popup-location">{luogoConcatenato}</p>
                </div>
            </div>
        </div>
    );
};

ConsulenzaView.propTypes = {
    id: PropTypes.string.isRequired, // ID della consulenza
    onClose: PropTypes.func.isRequired, // Funzione per chiudere il popup
};

export default ConsulenzaView;
