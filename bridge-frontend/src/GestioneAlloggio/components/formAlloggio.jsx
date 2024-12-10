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

        // Crea l'oggetto da inviare
        const data = {
            descrizione,
            maxPersone,
            metratura,
            emailProprietario: "jedi@jedi.it",
            servizi,
        };

        try {
            const response = await fetch("http://localhost:8080/alloggi/aggiungi", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json", // Indica il tipo di dati
                },
                body: JSON.stringify(data), // Converte l'oggetto in JSON
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
                    onChange={(event) => setDescrizione(event.target.value)}
                    required
                />

                <input
                    type="number"
                    placeholder="Numero massimo di persone"
                    className="formMaxPersone"
                    value={maxPersone}
                    onChange={(event) => setMaxPersone(event.target.value)}
                    min="1"
                    required
                />

                <input
                    type="number"
                    placeholder="Metratura (min 15 mq)"
                    className="formMetratura"
                    value={metratura}
                    onChange={(event) => setMetratura(event.target.value)}
                    min="15"
                    required
                />

                <select
                    className="servizi"
                    value={servizi}
                    onChange={(event) => setServizi(event.target.value)}
                    required={true}
                >
                    <option value="">Seleziona i servizi dell alloggio</option>
                    {Object.entries(Servizi).map(([value, label]) => (
                        <option key={value} value={value}>
                            {label}
                        </option>
                    ))}
                </select>

                <button type="submit" className="formButtonAlloggio">
                    Crea Alloggio
                </button>
            </form>
        </div>
    );
};

export default CreaAlloggio;
