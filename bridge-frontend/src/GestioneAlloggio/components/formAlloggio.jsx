import { useState } from "react";
import "../css/formAlloggioStyle.css";

const Servizi = {
    WIFI: "WiFi",
    RISCALDAMENTO: "Riscaldamento",
    ARIACONDIZIONATA: "Aria Condizionata",
};

const CreaAlloggio = () => {
    const [metratura, setMetratura] = useState("");
    const [maxPersone, setMaxPersone] = useState("");
    const [descrizione, setDescrizione] = useState("");
    const [servizi, setServizi] = useState([]);
    const[fotos, setFotos] = useState([]);

    const aggiornaDescrizione = (event) => {
        setDescrizione(event.target.value);
    };

    const aggiornaMetratura = (event) => {
        setMetratura(event.target.value);
    };

    const aggiornaMaxPersone = (event) => {
        setMaxPersone(event.target.value);
    };

    const aggiornaFotoAlloggio = (event) => {
        const files = Array.from(event.target.files);

        if (files.length < 1 || files.length > 3) {
            alert("Devi caricare almeno 1 foto e un massimo di 3 foto.");
            return;
        }

        const readers = files.map((file) => {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.onload = () => resolve(reader.result);
                reader.onerror = (error) => reject(error);
                reader.readAsDataURL(file);
            });
        });
        Promise.all(readers)
            .then((base64Images) => setFotos(base64Images))
            .catch((error) => console.error("Errore nella lettura delle immagini:", error));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (metratura < 15) {
            alert("Il numero dei metri quadri deve essere almeno di 15");
            return;
        }

        if (maxPersone < 1) {
            alert("Inserire almeno 1 persona");
            return;
        }

        if (!descrizione.trim()) {
            alert("Inserire una descrizione");
            return;
        }

        if (!servizi) {
            alert("Selezionare almeno un servizio");
            return;
        }

        if (fotos.length === 0) {
            alert("Devi caricare almeno una foto");
            return;
        }
        // Crea l'oggetto da inviare
        const alloggioDTO = {
            descrizione,
            metratura,
            maxPersone,
            servizi,
            fotos, // Array di immagini in Base64
            emailProprietario: "jedi@jedi.it",
        };

        console.log(alloggioDTO);

        try {
            const response = await fetch("http://localhost:8080/alloggi/aggiungi", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json", // Indica il tipo di dati
                },
                body: JSON.stringify({
                    "descrizione": descrizione,
                    "metratura": metratura,
                    "maxPersone": maxPersone,
                    "servizi": servizi,
                    "fotos": fotos,
                    "emailProprietario": "root@mail.it",
                }), // Converte l'oggetto in JSON
            });

            if (!response.ok) {
                throw new Error(`Errore HTTP: ${response.status}`);
            }

            const result = await response.text();
            alert("Alloggio creato con successo!");
            console.log(result);
        } catch (error) {
            console.error("Errore durante la creazione dell'alloggio", error);
            alert(`Errore durante la creazione dell'alloggio: ${error.message}`);
        }
    };

    return (
        <div className="formContainerAlloggio">
            <h2>Crea un nuovo alloggio</h2>
            <form onSubmit={handleSubmit}>
                <textarea
                    placeholder="Inserisci una descrizione"
                    className="formDescrizione"
                    value={descrizione}
                    onChange={aggiornaDescrizione}
                    required
                />

                <input
                    type="number"
                    placeholder="Numero massimo di persone"
                    className="formMaxPersone"
                    value={maxPersone}
                    onChange={aggiornaMaxPersone}
                    min="1"
                    required
                />

                <input
                    type="number"
                    placeholder="Metratura (min 15 mq)"
                    className="formMetratura"
                    value={metratura}
                    onChange={aggiornaMetratura}
                    min="15"
                    required
                />

                <select
                    className="servizi"
                    value={servizi}
                    onChange={(event) => setServizi(event.target.value)}
                    required
                >
                    <option value="" disabled>
                        Seleziona i servizi dell alloggio
                    </option>
                    {Object.entries(Servizi).map(([value, label]) => (
                        <option key={value} value={value}>
                            {label}
                        </option>
                    ))}
                </select>

                <input
                    type="file"
                    multiple
                    accept="image/*"
                    onChange={aggiornaFotoAlloggio}
                    required
                />

                <button type="submit" className="formButtonAlloggio">
                    Crea Alloggio
                </button>
            </form>
        </div>
    );
};

export default CreaAlloggio;
