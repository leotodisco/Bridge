import { useState } from "react";

// Constants for available languages
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

// Constants for available categories
const CATEGORIA = {
    LINGUA: "LINGUA",
    INFORMATICA: "INFORMATICA",
    ALTRO: "ALTRO",
};

const CreaCorso = () => {
    const [titolo, setTitolo] = useState("");
    const [descrizione, setDescrizione] = useState("");
    const [categoria, setCategoria] = useState("");
    const [lingua, setLingua] = useState("");

    /*
     * Funzioni di aggiornamento dei campi
     */
    const aggiornaTitolo = (event) => {
        setTitolo(event.target.value);
    };

    const aggiornaDescrizione = (event) => {
        setDescrizione(event.target.value);
    };

    const aggiornaCategoria = (event) => {
        setCategoria(event.target.value);
    };

    const aggiornaLingua = (event) => {
        setLingua(event.target.value);
    };

    const handleSubmit = async (event) => {
        // Previene il comportamento di default del form
        event.preventDefault();

        // Creazione dell'oggetto da inviare
        const corsoDTO = {
            titolo: titolo,
            descrizione: descrizione,
            categoria: categoria,
            lingua: lingua
        };

        console.log("Corso da creare:", corsoDTO);

        try {
            const response = await fetch("http://localhost:8080/api/corsi/crea", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify({
                    titolo: titolo,
                    descrizione: descrizione,
                    categoria: categoria,
                    lingua: lingua,
                    proprietario: "FiguraSpecializzata@example.com"
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Errore HTTP: ${response.status} - ${errorText}`);
            }

            const result = await response.json();
            console.log("Corso creato con successo:", result);
            alert("Corso creato con successo!");
        } catch (error) {
            console.error("Errore durante la creazione del corso:", error);
            alert(`Errore durante la creazione del corso: ${error.message}`);
        }
    };

    return (
        <div className="formContainer">
            <h2>Crea un Corso</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Titolo del corso"
                    className="formEditText"
                    value={titolo}
                    onChange={aggiornaTitolo}
                    required
                />

                <textarea
                    placeholder="Descrizione"
                    className="formEditText"
                    value={descrizione}
                    onChange={aggiornaDescrizione}
                    required
                />

                <select
                    className="formEditText"
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

                <select
                    className="formEditText"
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

                <button type="submit" className="formButton">
                    Crea Corso
                </button>
            </form>
        </div>
    );
};

export default CreaCorso;
