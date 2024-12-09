import { useState } from "react";

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

const CreaCorso = () => {
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

    const validateForm = () => {
        if (!titolo || !descrizione || !categoria || !lingua) {
            alert("Compila tutti i campi obbligatori!");
            return false;
        }
        if (pdfFile && pdfFile.type !== "application/pdf") {
            alert("Il file deve essere un PDF valido!");
            return false;
        }
        return true;
    };

    const handlePdfUpload = async (file) => {
        const formData = new FormData();
        formData.append("nome", file.name);  // nome del file
        formData.append("pdf", file);  // il file stesso

        try {
            const response = await fetch("http://localhost:8080/api/corsi/upload", {
                method: "POST",
                body: formData,  // Qui siamo comunque obbligati a usare FormData per il file
            });

            if (!response.ok) {
                const errorMessage = await response.text(); // Leggi il corpo della risposta qui
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
        if (!validateForm()) return;

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
            const response = await fetch("http://localhost:8080/api/corsi/crea", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    titolo: titolo,
                    descrizione: descrizione,
                    categoria: categoria,
                    lingua: lingua,
                    pdf: uploadedPdfId,
                    proprietario: "FiguraSpecializzata@example.come",  // Nota: Cambiato "example.come" in "example.com"
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

    return (
        <div className="formContainer">
            <h2>Crea un Corso</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="Titolo del corso" value={titolo} onChange={aggiornaTitolo} required />
                <textarea placeholder="Descrizione" value={descrizione} onChange={aggiornaDescrizione} required />
                <select value={categoria} onChange={aggiornaCategoria} required>
                    <option value="">Seleziona una categoria</option>
                    {Object.values(CATEGORIA).map((cat) => (
                        <option key={cat} value={cat}>{cat}</option>
                    ))}
                </select>
                <select value={lingua} onChange={aggiornaLingua} required>
                    <option value="">Seleziona una lingua</option>
                    {Object.values(LINGUA).map((lang) => (
                        <option key={lang} value={lang}>{lang}</option>
                    ))}
                </select>
                <input type="file" accept="application/pdf" onChange={aggiornaPdfFile} />
                <button type="submit" disabled={loading}>
                    {loading ? "Caricamento..." : "Crea Corso"}
                </button>
            </form>
        </div>
    );
};

export default CreaCorso;
