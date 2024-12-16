import { useEffect, useState } from "react";
import PropTypes from "prop-types";
import "../../GestioneCorso/css/corsoView.css"; // Import del file CSS

const LINGUA = {
    ITALIANO: "ITALIANO",
    INGLESE: "INGLESE",
    FRANCESE: "FRANCESE",
    TEDESCO: "TEDESCO",
    SPAGNOLO: "SPAGNOLO",
    PORTOGHESE: "PORTOGHESE",
    UCRAINO: "UCRAINO",
    RUSSO: "RUSSO",
    CINESE: "CINESE",
    ARABO: "ARABO",
};

const CATEGORIA = {
    LINGUA: "LINGUE",
    INFORMATICA: "INFORMATICA",
    ALTRO: "ALTRO",
};

const CorsoView = ({ id, onClose }) => {
    const [corso, setCorso] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [editMode, setEditMode] = useState(false);
    const [formData, setFormData] = useState({
        titolo: "",
        descrizione: "",
        lingua: "",
        categoriaCorso: "",
        pdf: ""
    });
    const [pdfFile, setPdfFile] = useState(null);

    // Recupera l'email dell'utente loggato
    const emailUtenteLoggato = localStorage.getItem("email");

    // Funzione per verificare se l'utente loggato è il proprietario
    const isOwner = (loggedInEmail, ownerEmail) => {
        return loggedInEmail === ownerEmail;
    };

    const fetchCorso = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/corsi/cerca/${id}`);
            if (!response.ok) {
                throw new Error("Corso non trovato");
            }
            const data = await response.json();
            setCorso(data);
            setFormData({
                titolo: data.titolo,
                descrizione: data.descrizione,
                lingua: data.lingua,
                categoriaCorso: data.categoriaCorso,
                pdf: data.pdf
            });
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const handlePdfUpload = async (file) => {
        const formData = new FormData();
        formData.append("nome", file.name);
        formData.append("pdf", file);

        try {
            const response = await fetch("http://localhost:8080/api/corsi/upload", {
                method: "POST",
                body: formData,
            });

            if (!response.ok) {
                throw new Error("Errore durante il caricamento del PDF");
            }
            return await response.text();
        } catch (error) {
            console.error("Errore durante l'upload del PDF:", error);
            alert(`Errore durante il caricamento del PDF: ${error.message}`);
            throw error;
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Controllo del proprietario
        if (!isOwner(emailUtenteLoggato, corso.proprietario.email)) {
            alert("Non sei autorizzato a modificare questo corso.");
            return;
        }

        if (pdfFile) {
            try {
                const uploadedPdfId = await handlePdfUpload(pdfFile);
                setFormData((prevData) => ({
                    ...prevData,
                    pdf: uploadedPdfId
                }));
            } catch {
                return;
            }
        }

        try {
            const response = await fetch(`http://localhost:8080/api/corsi/modifica/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    ...formData,
                    proprietario: emailUtenteLoggato
                })
            });

            if (!response.ok) {
                const errorDetails = await response.text();
                throw new Error(`Errore nella modifica del corso: ${errorDetails}`);
            }

            const updatedCorso = await response.json();
            setCorso(updatedCorso);
            setEditMode(false);
            alert("Corso modificato con successo!");
        } catch (error) {
            setError(error.message);
        }
    };

    const closeError = () => setError(null);

    useEffect(() => {
        fetchCorso();
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
                    <button className="popup-close-error" onClick={closeError}>
                        Chiudi
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="popup-overlay">
            <div className="popup-container">
                <button className="popup-close" onClick={onClose}>
                    &times;
                </button>
                <div className="popup-header">
                    <h1 className="popup-title">{editMode ? "Modifica Corso" : corso.titolo}</h1>
                    {!editMode && <p className="popup-subtitle">{corso.descrizione}</p>}
                </div>
                {editMode ? (
                    <form onSubmit={handleSubmit} className="edit-form">
                        <div>
                            <label>Titolo:</label>
                            <input
                                type="text"
                                name="titolo"
                                value={formData.titolo}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div>
                            <label>Descrizione:</label>
                            <textarea
                                name="descrizione"
                                value={formData.descrizione}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div>
                            <label>Lingua:</label>
                            <select
                                name="lingua"
                                value={formData.lingua}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Seleziona una lingua</option>
                                {Object.values(LINGUA).map((lang) => (
                                    <option key={lang} value={lang}>{lang}</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>Categoria:</label>
                            <select
                                name="categoriaCorso"
                                value={formData.categoriaCorso}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Seleziona una categoria</option>
                                {Object.values(CATEGORIA).map((cat) => (
                                    <option key={cat} value={cat}>{cat}</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>PDF:</label>
                            <input
                                type="file"
                                accept="application/pdf"
                                onChange={(e) => setPdfFile(e.target.files[0])}
                            />
                        </div>
                        <div className="popup-actions">
                            <button className="popup-button" type="submit">
                                Salva
                            </button>
                            <button
                                className="popup-button"
                                type="button"
                                onClick={() => setEditMode(false)}
                            >
                                Annulla
                            </button>
                        </div>
                    </form>
                ) : (
                    <div className="popup-body">
                        <h3>Dettagli</h3>
                        <div className="popup-details">
                            <div className="popup-row">
                                <span className="popup-label">Lingua:</span>
                                <span className="popup-value">{corso.lingua}</span>
                            </div>
                            <div className="popup-row">
                                <span className="popup-label">Categoria:</span>
                                <span className="popup-value">{corso.categoriaCorso}</span>
                            </div>
                            <div className="popup-row">
                                <span className="popup-label">Proprietario:</span>
                                <span className="popup-value">{corso.proprietario.nome} {corso.proprietario.cognome}</span>
                            </div>
                        </div>
                        <h3>Azioni</h3>
                        <div className="popup-actions">
                            {isOwner(emailUtenteLoggato, corso.proprietario.email) && (
                                <button className="popup-button" onClick={() => setEditMode(true)}>
                                    Modifica
                                </button>
                            )}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

CorsoView.propTypes = {
    id: PropTypes.number.isRequired,
    onClose: PropTypes.func.isRequired,
};

export default CorsoView;
