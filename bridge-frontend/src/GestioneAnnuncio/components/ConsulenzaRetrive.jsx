import { useState, useEffect } from "react";
import PropTypes from "prop-types"; // Per validare le props
import "../../GestioneEvento/css/eventoView.css";

const ConsulenzaView = ({ id, onClose }) => {
    const [consulenzaData, setConsulenzaData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editing, setEditing] = useState(false);

    // Stati per i campi del form
    const [titolo, setTitolo] = useState("");
    const [descrizione, setDescrizione] = useState("");
    const [indirizzo, setIndirizzo] = useState({
        via: "",
        citta: "",
        cap: "",
        num_civico: "",
        provincia: ""
    });
    const [orariDisponibili, setOrariDisponibili] = useState("");
    const [numero, setNumero] = useState("");
    const [tipo, setTipo] = useState("");

    // Prendo da localStorage l'email dell'utente attualmente loggato
    const emailUtenteLoggato = localStorage.getItem('email');

    // Funzione per recuperare i dettagli della consulenza
    const fetchConsulenza = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/annunci/view_consulenze/retrive/${id}`);
            if (!response.ok) {
                throw new Error("Consulenza non trovata");
            }
            const data = await response.json();
            setConsulenzaData(data);

            // Inizializza gli stati del form con i dati ricevuti
            setTitolo(data.titolo);
            setDescrizione(data.descrizione);
            setIndirizzo({
                via: data.indirizzo.via,
                citta: data.indirizzo.citta,
                cap: data.indirizzo.cap,
                num_civico: data.indirizzo.numCivico,
                provincia: data.indirizzo.provincia
            });
            setOrariDisponibili(data.orariDisponibili);
            setNumero(data.numero);
            setTipo(data.tipo);

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

    // Funzione per modificare la consulenza
    const handleEdit = async () => {
        const aggiornamenti = {
            titolo,
            descrizione,
            indirizzo,
            orariDisponibili,
            numero,
            tipo,
        };
        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`http://localhost:8080/api/annunci/modifica_consulenza/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify(aggiornamenti),
            });

            if (!response.ok) {
                throw new Error("Errore durante la modifica");
            }

            const data = await response.json();
            alert("Consulenza modificata con successo!");
            setConsulenzaData(data);
        } catch (err) {
            console.error(err);
            alert("Non è stato possibile modificare la consulenza.");
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
                        {emailUtenteLoggato === consulenzaData.proprietario.email && (
                            <button onClick={() => setEditing(true)} className="edit-button">
                                Modifica Consulenza
                            </button>
                        )}
                    </>
                ) : (
                    <>
                        <h2>Modifica Consulenza</h2>
                        <div className="form-group">
                            <label>Titolo</label>
                            <input
                                type="text"
                                className="formEditText"
                                value={titolo}
                                onChange={(e) => setTitolo(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Descrizione</label>
                            <textarea
                                value={descrizione}
                                onChange={(e) => setDescrizione(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Indirizzo</label>
                            <input
                                type="text"
                                placeholder="Via"
                                value={indirizzo.via}
                                onChange={(e) =>
                                    setIndirizzo((prev) => ({ ...prev, via: e.target.value }))
                                }
                            />
                            <input
                                type="text"
                                placeholder="Città"
                                value={indirizzo.citta}
                                onChange={(e) =>
                                    setIndirizzo((prev) => ({ ...prev, citta: e.target.value }))
                                }
                            />
                            <input
                                type="text"
                                placeholder="CAP"
                                value={indirizzo.cap}
                                onChange={(e) =>
                                    setIndirizzo((prev) => ({ ...prev, cap: e.target.value }))
                                }
                            />
                            <input
                                type="text"
                                placeholder="Numero Civico"
                                value={indirizzo.num_civico}
                                onChange={(e) =>
                                    setIndirizzo((prev) => ({ ...prev, num_civico: e.target.value }))
                                }
                            />
                            <input
                                type="text"
                                placeholder="Provincia"
                                value={indirizzo.provincia}
                                onChange={(e) =>
                                    setIndirizzo((prev) => ({ ...prev, provincia: e.target.value }))
                                }
                            />
                        </div>
                        <div className="form-group">
                            <label>Orari Disponibili</label>
                            <input
                                type="text"
                                value={orariDisponibili}
                                onChange={(e) => setOrariDisponibili(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Numero</label>
                            <input
                                type="text"
                                value={numero}
                                onChange={(e) => setNumero(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Tipo</label>
                            <input
                                type="text"
                                value={tipo}
                                onChange={(e) => setTipo(e.target.value)}
                            />
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

ConsulenzaView.propTypes = {
    id: PropTypes.string.isRequired, // ID della consulenza
    onClose: PropTypes.func.isRequired, // Funzione per chiudere il popup
};

export default ConsulenzaView;
