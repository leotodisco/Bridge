import { useState } from "react";
import { FaTimes } from 'react-icons/fa';
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
const CreaCorso = ({ onClose }) => {
    const [titolo, setTitolo] = useState("");
    const [descrizione, setDescrizione] = useState("");
    const [categoria, setCategoria] = useState("");
    const [lingua, setLingua] = useState("");
    const [pdfFile, setPdfFile] = useState(null);
    const [loading, setLoading] = useState(false);

    const aggiornaTitolo = (e) => setTitolo(e.target.value);
    const aggiornaDescrizione = (e) => setDescrizione(e.target.value);
    const aggiornaCategoria = (e) => setCategoria(e.target.value);
    const aggiornaLingua = (e) => setLingua(e.target.value);
    const aggiornaPdfFile = (e) => setPdfFile(e.target.files[0]);

    const emailUtenteLoggato = localStorage.getItem("email");

    const validateFields = () => {
        const errors = [];
        if (!titolo.trim()) errors.push("Titolo non valido");
        if (!descrizione.trim()) errors.push("Descrizione non valida");
        if (!categoria.trim()) errors.push("Categoria non valida");
        if (!lingua.trim()) errors.push("Lingua non valida");
        if (!pdfFile || pdfFile.type !== "application/pdf") errors.push("File PDF non valido");
        return errors;
    };

    const handlePdfUpload = async (file) => {
        const formData = new FormData();
        formData.append("nome", file.name);
        formData.append("pdf", file);

        const token = localStorage.getItem('authToken');
        const response = await fetch("http://localhost:8080/api/corsi/upload", {
            method: 'POST',
            body: formData,
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            throw new Error("Errore durante l'upload del PDF");
        }

        return await response.text();
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const errors = validateFields();
        if (errors.length > 0) {
            alert(errors.join("\n"));
            return;
        }

        setLoading(true);

        try {
            const pdfId = await handlePdfUpload(pdfFile);

            const token = localStorage.getItem("authToken");
            const response = await fetch("http://localhost:8080/api/corsi/crea", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({
                    titolo,
                    descrizione,
                    categoria,
                    lingua,
                    pdf: pdfId,
                    proprietario: emailUtenteLoggato
                }),
            });

            if (!response.ok) {
                const errorDetails = await response.text();
                throw new Error(errorDetails);
            }

            window.location.reload(); // Ricarica la pagina
        } catch (error) {
            console.error("Errore durante la creazione del corso:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="formContainer">
            <button
                type="button"
                className="closeIcon"
                onClick={onClose}
                aria-label="Chiudi"
            >
                <FaTimes />
            </button>

            <h2>Crea un Corso</h2>
            <form onSubmit={handleSubmit}>
                <div className="formField">
                    <input
                        className="InputField"
                        type="text"
                        placeholder="Titolo del corso"
                        value={titolo}
                        onChange={aggiornaTitolo}
                        required
                    />
                </div>

                <div className="formField">
                    <textarea
                        className="InputField"
                        placeholder="Descrizione"
                        value={descrizione}
                        onChange={aggiornaDescrizione}
                        required
                    />
                </div>

                <div className="formField">
                    <select
                        className="InputField"
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
                </div>

                <div className="formField">
                    <select
                        className="InputField"
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
                        {pdfFile && <p className="filePreview">File selezionato: {pdfFile.name}</p>}
                    </div>
                </div>

                <button className="fileButton" type="submit" disabled={loading}>
                    {loading ? "Caricamento..." : "Crea Corso"}
                </button>
            </form>
        </div>
    );
};

export default CreaCorso;
