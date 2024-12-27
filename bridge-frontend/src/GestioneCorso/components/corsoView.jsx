import { useEffect, useState } from "react";
import PropTypes from "prop-types";
import "../../GestioneCorso/css/corsoView.css"; // Import del file CSS
import "../../GestioneCorso/css/formCorsoStyle.css";
import {useNavigate} from "react-router-dom"; // Import del file CSS

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
        categoria: "",
        pdf: "",
    });
    const [pdfFile, setPdfFile] = useState(null);
    const [titoloError, setTitoloError] = useState("");
    const [descrizioneError, setDescrizioneError] = useState("");
    const [categoriaError, setCategoriaError] = useState("");
    const [linguaError, setLinguaError] = useState("");
    const [pdfFileError, setPdfFileError] = useState("");
    const nav = useNavigate();

    // Recupera l'email dell'utente loggato
    const emailUtenteLoggato = localStorage.getItem("email");

    // Funzione per verificare se l'utente loggato è il proprietario
    const isOwner = (loggedInEmail, ownerEmail) => loggedInEmail === ownerEmail;

    const fetchCorso = async () => {
        try {
            const token = localStorage.getItem('authToken');

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch(`http://localhost:8080/api/corsi/cerca/${id}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });

            if (!response.ok) throw new Error("Corso non trovato");

            const data = await response.json();
            setCorso(data);
            setFormData({
                titolo: data.titolo,
                descrizione: data.descrizione,
                lingua: data.lingua,
                categoria: data.categoriaCorso,
                pdf: data.pdf,
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
            [name]: value,
        }));
    };

    const validateForm = () => {
        let isValid = true;

        if (formData.titolo.trim().length < 3) {
            setTitoloError("Il titolo deve contenere almeno 3 caratteri.");
            isValid = false;
        } else {
            setTitoloError("");
        }

        if (formData.descrizione.trim().length < 3) {
            setDescrizioneError("La descrizione deve contenere almeno 3 caratteri.");
            isValid = false;
        } else {
            setDescrizioneError("");
        }

        if (formData.categoria.trim() === "") {
            setCategoriaError("Seleziona una categoria.");
            isValid = false;
        } else {
            setCategoriaError("");
        }

        if (formData.lingua.trim() === "") {
            setLinguaError("Seleziona una lingua.");
            isValid = false;
        } else {
            setLinguaError("");
        }

        if (pdfFile && pdfFile.type !== "application/pdf") {
            setPdfFileError("Il file deve essere un PDF valido.");
            isValid = false;
        } else {
            setPdfFileError("");
        }

        return isValid;
    };

    const handlePdfUpload = async (file) => {
        const uploadFormData = new FormData();
        uploadFormData.append("nome", file.name);
        uploadFormData.append("pdf", file);

        try {

            const token = localStorage.getItem('authToken');

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch("http://localhost:8080/api/corsi/upload", {
                    method: 'POST',
                    body: uploadFormData,
                    headers: {
                        'Authorization': `Bearer ${token}`,

                    },
                });

            if (!response.ok) {
                throw new Error("Errore durante il caricamento del PDF.");
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

        if (!validateForm()) return;

        let pdfId = formData.pdf;
        if (pdfFile) {
            try {
                pdfId = await handlePdfUpload(pdfFile);
            } catch {
                return;
            }
        }

        try {
            const response = await fetch(`http://localhost:8080/api/corsi/modifica/${id}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ ...formData, pdf: pdfId, proprietario: emailUtenteLoggato }),
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

    // Funzione per scaricare il PDF del corso
    const downloadPDF = async (courseId, courseTitle) => {
        try {

            const token = localStorage.getItem('authToken');

            if (!token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await
                fetch(`http://localhost:8080/api/corsi/download/${courseId}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

            if (!response.ok) {
                throw new Error(`Errore durante il download del PDF: ${response.status} ${response.statusText}`);
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);

            // Sostituisci i caratteri non alfanumerici per evitare problemi nei nomi dei file
            const sanitizedTitle = courseTitle.replace(/[^a-z0-9]/gi, '_').toLowerCase();

            const link = document.createElement("a");
            link.href = url;
            link.download = `${sanitizedTitle}.pdf`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Errore durante il download del PDF:", error);
            alert(`Errore durante il download del PDF: ${error.message}`);
        }
    };


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
                    <button className="popup-close" onClick={() => setError(null)}>
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
                    <form onSubmit={handleSubmit} className="formContainer">
                        <div className="formField">
                            <input
                                className={`InputField ${titoloError ? "error" : ""}`}
                                type="text"
                                placeholder="Titolo del corso"
                                name="titolo"
                                value={formData.titolo}
                                onChange={handleChange}
                                required
                            />
                            {titoloError && <p className="errorText">{titoloError}</p>}
                        </div>

                        <div className="formField">
                            <textarea
                                className={`InputField ${descrizioneError ? "error" : ""}`}
                                placeholder="Descrizione"
                                name="descrizione"
                                value={formData.descrizione}
                                onChange={handleChange}
                                required
                            />
                            {descrizioneError && <p className="errorText">{descrizioneError}</p>}
                        </div>

                        <div className="formField">
                            <select
                                className={`InputField ${categoriaError ? "error" : ""}`}
                                name="categoria"
                                value={formData.categoria}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Seleziona una categoria</option>
                                {Object.values(CATEGORIA).map((cat) => (
                                    <option key={cat} value={cat}>
                                        {cat}
                                    </option>
                                ))}
                            </select>
                            {categoriaError && <p className="errorText">{categoriaError}</p>}
                        </div>

                        <div className="formField">
                            <select
                                className={`InputField ${linguaError ? "error" : ""}`}
                                name="lingua"
                                value={formData.lingua}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Seleziona una lingua</option>
                                {Object.values(LINGUA).map((lang) => (
                                    <option key={lang} value={lang}>
                                        {lang}
                                    </option>
                                ))}
                            </select>
                            {linguaError && <p className="errorText">{linguaError}</p>}
                        </div>

                        <div className="formField">
                            <div className="fileInputWrapper">
                                <label className="fileButton">
                                    Seleziona un file PDF
                                    <input
                                        type="file"
                                        accept="application/pdf"
                                        onChange={(e) => setPdfFile(e.target.files[0])}
                                        style={{display: "none"}}
                                    />
                                </label>
                                {pdfFile && (
                                    <p className="filePreview"> File selezionato: {pdfFile.name}</p>
                                )}
                            </div>
                            {pdfFileError && <p className="errorText">{pdfFileError}</p>}
                        </div>

                        <div className="formActions">
                            <button className="fileButton" type="submit">
                                Salva
                            </button>
                            <button
                                className="fileButton cancelButton"
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
                                <span className="popup-value">
                                    {corso.proprietario.nome} {corso.proprietario.cognome}
                                </span>
                            </div>
                        </div>
                        <h3>Azioni</h3>
                        <div className="popup-actions">
                            {isOwner(emailUtenteLoggato, corso.proprietario.email) && (
                                <button className="popup-button modifica-button" onClick={() => setEditMode(true)}>
                                    Modifica
                                </button>
                            )}
                            {corso.pdf && (
                                <button className="popup-button scarica-button"
                                        onClick={() => downloadPDF(corso.id, corso.titolo)}>
                                    Scarica PDF
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
