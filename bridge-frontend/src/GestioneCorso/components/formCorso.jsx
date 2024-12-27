// CreaCorso.jsx
import { useState } from "react";
import { FaTimes } from 'react-icons/fa'; // Importa l'icona
import { useNavigate } from 'react-router-dom'; // Importa useNavigate per la navigazione
import "../../GestioneCorso/css/formCorsoStyle.css";

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

// eslint-disable-next-line react/prop-types
const CreaCorso = ({ onClose }) => { // Accetta la prop onClose
    const [titolo, setTitolo] = useState("");
    const [descrizione, setDescrizione] = useState("");
    const [categoria, setCategoria] = useState("");
    const [lingua, setLingua] = useState("");
    const [pdfFile, setPdfFile] = useState(null);
    const [pdfId, setPdfId] = useState(null);
    const [loading, setLoading] = useState(false);

    const aggiornaTitolo = (e) => setTitolo(e.target.value);
    const aggiornaDescrizione = (e) => setDescrizione(e.target.value);
    const aggiornaCategoria = (e) => setCategoria(e.target.value);
    const aggiornaLingua = (e) => setLingua(e.target.value);
    const aggiornaPdfFile = (e) => setPdfFile(e.target.files[0]);
    const emailUtenteLoggato = localStorage.getItem("email");
    console.log(emailUtenteLoggato);

    const [titoloError, setTitoloError] = useState("");
    const [descrizioneError, setDescrizioneError] = useState("");
    const [categoriaError, setCategoriaError] = useState("");
    const [linguaError, setLinguaError] = useState("");
    const [pdfFileError, setPdfFileError] = useState("");

    const validateTitolo = () => {
        const titoloRegex = /^[A-Za-z0-9À-ÿ .,'-]{3,100}$/;
        if (!titoloRegex.test(titolo)) {
            setTitoloError("Il titolo deve contenere da 3 a 100 caratteri alfanumerici");
            return false;
        }
        setTitoloError("");
        return true;
    }

    const validateDescrizione = () => {
        const descrizioneRegex = /^[A-Za-z0-9À-ÿ .,'-]{3,500}$/;
        if (!descrizioneRegex.test(descrizione)) {
            setDescrizioneError("La descrizione deve contenere da 3 a 500 caratteri alfanumerici");
            return false;
        }
        setDescrizioneError("");
        return true;
    }

    const validateCategoria = () => {
        const categoriaRegex = /^[A-Za-zÀ-ÿ' -]{3,50}$/;
        if (!categoriaRegex.test(categoria)) {
            setCategoriaError("La categoria deve contenere da 3 a 50 caratteri alfabetici");
            return false;
        }
        setCategoriaError("");
        return true;
    }

    const validateLingua = () => {
        if (lingua.trim() === ""){
            setLinguaError("Seleziona una lingua");
            return false;
        }

        setLinguaError("");
        return true;
    }

    const validatePdfFile = () => {
        if (!pdfFile) {
            setPdfFileError("Nessun file selezionato");
            return false;
        }
        if (pdfFile.type !== "application/pdf") {
            setPdfFileError("Il file deve essere un PDF valido");
            return false;
        }
        setPdfFileError("");
        return true;
    }

    const handlePdfUpload = async (file) => {
        const formData = new FormData();
        formData.append("nome", file.name);  // nome del file
        formData.append("pdf", file);  // il file stesso

        try {
            const response = await fetch("http://localhost:8080/api/corsi/upload", {
                method: "POST",
                body: formData,  // Usare FormData per il file
            });

            if (!response.ok) {
                const errorMessage = await response.text(); // Leggi il corpo della risposta
                throw new Error(`Errore durante il caricamento del PDF: ${errorMessage}`);
            }

            // Leggi il corpo della risposta solo una volta
            const pdfId = await response.text();  // Ottenere l'ID del PDF dal corpo della risposta
            console.log("ID del PDF caricato:", pdfId);  // Stampare l'ID del PDF
            return pdfId;  // Restituire l'ID del PDF
        } catch (error) {
            console.error("Errore durante l'upload del PDF:", error);
            alert(`Errore durante il caricamento del PDF: ${error.message}`);
            throw error;
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        //if (!validateForm()) return;

        const isTitoloValid = validateTitolo();
        const isDescrizioneValid = validateDescrizione();
        const isCategoriaValid = validateCategoria();
        const isLinguaValid = validateLingua();
        const isPdfFileValid = validatePdfFile();

        console.log("Validazione campi:", isTitoloValid, isDescrizioneValid, isCategoriaValid, isLinguaValid, isPdfFileValid);

        if (!isTitoloValid || !isDescrizioneValid || !isCategoriaValid || !isLinguaValid || !isPdfFileValid) {
            console.log("Campi non validi, non posso procedere con la creazione del corso");
            return;
        }

        setLoading(true);
        let uploadedPdfId = pdfId;

        if (pdfFile && !pdfId) {
            console.log("PDF non ancora caricato, procedo con l'upload");
            try {
                uploadedPdfId = await handlePdfUpload(pdfFile);
                setPdfId(uploadedPdfId);
            } catch {
                setLoading(false);
                return;
            }
        }

        const corsoDTO = {
            titolo,
            descrizione,
            categoria,
            lingua,
            pdf: uploadedPdfId,
        };

        console.log("Corso da creare:", corsoDTO);  // Log dei dettagli del corso da creare
        try {
            const token = localStorage.getItem("authToken");  // Recupera il token JWT
            console.log(localStorage.getItem("authToken"));

            const response = await fetch("http://localhost:8080/api/corsi/crea", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json" ,
                    "Accept": "application/json",
                    "Authorization": `Bearer ${token}`  // Invia il token JWT nell'intestazione
                },
                body: JSON.stringify({
                    titolo: titolo,
                    descrizione: descrizione,
                    categoria: categoria,
                    lingua: lingua,
                    pdf: uploadedPdfId,
                    proprietario: emailUtenteLoggato
                }),
            });

            if (!response.ok) {
                const errorDetails = await response.text(); // Leggi il corpo della risposta
                console.error("Errore 403:", errorDetails);  // Log dei dettagli dell'errore
                throw new Error("Errore durante la creazione del corso: " + errorDetails);
            }
            const result = await response.json();
            console.log("Corso creato con successo:", result);  // Log del risultato
            alert("Corso creato con successo!");
        } catch (error) {
            console.error("Errore durante la creazione del corso:", error);
            alert(`Errore durante la creazione del corso: ${error.message}`);
        } finally {
            setLoading(false);
        }
    };

    const navigate = useNavigate(); // Hook per la navigazione

    const handleClose = () => {
        console.log("Pulsante di chiusura cliccato"); // Log di debug
        if (onClose) {
            onClose(); // Chiude il modal se onClose è fornito
        } else {
            navigate('/view-listacorsi'); // Reindirizza a /view-listacorsi
        }
    };

    return (
        <div className="formContainer">
            {/* Pulsante di chiusura */}
            <button
                type="button" // Specifica il tipo del pulsante
                className="closeIcon"
                onClick={handleClose}
                aria-label="Chiudi"
            >
                <FaTimes />
            </button>

            <h2>Crea un Corso</h2>
            <form onSubmit={handleSubmit}>
                <div className="formField">
                    <input
                        className={`InputField ${titoloError ? "error" : ""}`}
                        type="text"
                        placeholder="Titolo del corso"
                        value={titolo}
                        onChange={aggiornaTitolo}
                        required
                    />
                    {titoloError && <p className="errorText">{titoloError}</p>}
                </div>

                <div className="formField">
                    <textarea
                        className={`InputField ${descrizioneError ? "error" : ""}`}
                        placeholder="Descrizione"
                        value={descrizione}
                        onChange={aggiornaDescrizione}
                        required
                    />
                    {descrizioneError && <p className="errorText">{descrizioneError}</p>}
                </div>

                <div className="formField">
                    <select
                        className={`InputField ${categoriaError ? "error" : ""}`}
                        value={categoria}
                        onChange={aggiornaCategoria}
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
                        value={lingua}
                        onChange={aggiornaLingua}
                        required
                    >
                        <option value="">Seleziona una lingua</option>
                        {Object.values(LINGUA).map((lang) => (
                            <option key={lang} value={lang}>
                                {lang}
                            </option>
                        ))}
                    </select>
                </div>
                {linguaError && <p className="errorText">{linguaError}</p>}

                <div className="formField">
                    <div className="fileInputWrapper">
                        <label className="fileButton">
                            Seleziona un file PDF
                            <input
                                type="file"
                                accept="application/pdf"
                                onChange={aggiornaPdfFile}
                                style={{ display: "none" }}
                            />
                        </label>
                        {pdfFile && (
                            <p className="filePreview">File selezionato: {pdfFile.name}</p>
                        )}
                    </div>
                    <button className="fileButton" type="submit" disabled={loading}>
                        {loading ? "Caricamento..." : "Crea Corso"}
                    </button>
                    {pdfFileError && <p className="errorText">{pdfFileError}</p>}
                </div>
            </form>
        </div>
    );
};

export default CreaCorso;
