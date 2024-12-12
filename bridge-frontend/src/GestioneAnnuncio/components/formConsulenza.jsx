import { useState } from "react";

const Tipo = {
    SANITARIA: "SANITARIA",
    LEGALE: "LEGALE",
    COMMERCIALE: "COMMERICIALE",
    PSICOLOGICA: "PSICOLOGICA",
    TRADUTTORE: "TRADUTTORE"
};

const CreaConsulenza = () => {
    const [titolo, setTitolo] = useState("");
    const [descrizione, setDescrizione] = useState("");
    const [orariDisponibili, setOrariDisponibili] = useState("");
    const [numero, setNumero] = useState("");
    const [tipo, setTipo] = useState("");
    const [maxCandidature, setMaxCandidature] = useState(1);
    const [indirizzo, setIndirizzo] = useState({
        via: "",
        numCivico: "",
        citta: "",
        cap: "",
        provincia: ""
    });

    // Funzioni di aggiornamento dei campi
    const aggiornaCampo = (setter) => (event) => {
        setter(event.target.value);
    };

    const aggiornaIndirizzo = (campo) => (event) => {
        setIndirizzo({
            ...indirizzo,
            [campo]: event.target.value
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        // Convalida del numero di telefono (deve essere composto da 10 cifre)
        if (numero && !/^\d{10}$/.test(numero)) {
            alert("Il numero di telefono deve contenere 10 cifre.");
            return;
        }

        //Convalida per la scrittura degli orari disponibili
        const orariPattern = /^(\w+\s+\d{2}:\d{2}-\d{2}:\d{2})(,\s*\w+\s+\d{2}:\d{2}-\d{2}:\d{2})*$/;
        if (!orariDisponibili.match(orariPattern)) {
            alert("Il formato degli orari deve essere 'Giorno hh:mm-hh:mm'.");
            return;
        }

        // Convalida del CAP (deve contenere esattamente 5 cifre)
        if (indirizzo.cap && !/^\d{5}$/.test(indirizzo.cap)) {
            alert("Il CAP deve contenere esattamente 5 cifre.");
            return;
        }

        // Verifica che il tipo di consulenza sia selezionato
        if (!tipo) {
            alert("Seleziona un tipo di consulenza.");
            return;
        }

        const consulenzaDTO = {
            titolo,
            descrizione,
            orariDisponibili,
            numero,
            tipo,
            maxCandidature,
            indirizzo: {
                via: indirizzo.via,
                numCivico: indirizzo.numCivico,
                citta: indirizzo.citta,
                cap: indirizzo.cap,
                provincia: indirizzo.provincia
            },
            disponibilita: true,
            proprietario: "negracciospecializzato@gmail.it"
        };

        console.log("Dati della consulenza: ", consulenzaDTO);

        try {
            const response = await fetch("http://localhost:8080/api/annunci/creaConsulenza", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(consulenzaDTO)
            });

            if (!response.ok) {
                throw new Error(`Errore HTTP: ${response.status}`);
            }

            const result = await response.json();
            console.log("Consulenza creata con successo: ", result);
            alert("Consulenza creata con successo!");
        } catch (error) {
            console.error("Errore durante la creazione della consulenza: ", error);
            alert("Errore durante la creazione della consulenza: " + error.message);
        }
    };

    return (
        <div className="formContainer">
            <h2>Crea una Consulenza</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Titolo"
                    className="formEditText"
                    value={titolo}
                    onChange={aggiornaCampo(setTitolo)}
                    required
                />

                <textarea
                    placeholder="Descrizione"
                    className="formEditText"
                    value={descrizione}
                    onChange={aggiornaCampo(setDescrizione)}
                    required
                />

                <input
                    type="text"
                    placeholder="Orari Disponibili"
                    className="formEditText"
                    value={orariDisponibili}
                    onChange={aggiornaCampo(setOrariDisponibili)}
                />

                <input
                    type="text"
                    placeholder="Numero di contatto"
                    className="formEditText"
                    value={numero}
                    onChange={aggiornaCampo(setNumero)}
                />

                <select
                    className="formEditText"
                    value={tipo}
                    onChange={aggiornaCampo(setTipo)}
                    required
                >
                    <option value="">Seleziona Tipo Consulenza</option>
                    {Object.values(Tipo).map((tipoValue) => (
                        <option key={tipoValue} value={tipoValue}>
                            {tipoValue}
                        </option>
                    ))}
                </select>

                <input
                    type="number"
                    placeholder="Massimo candidature"
                    className="formEditText"
                    value={maxCandidature}
                    onChange={aggiornaCampo(setMaxCandidature)}
                    min="1"
                    required
                />

                <h3>Indirizzo</h3>
                <input
                    type="text"
                    placeholder="Via"
                    className="formEditText"
                    value={indirizzo.via}
                    onChange={aggiornaIndirizzo("via")}
                    required
                />
                <input
                    type="text"
                    placeholder="Numero Civico"
                    className="formEditText"
                    value={indirizzo.numCivico}
                    onChange={aggiornaIndirizzo("numCivico")}
                    required
                />
                <input
                    type="text"
                    placeholder="Città"
                    className="formEditText"
                    value={indirizzo.citta}
                    onChange={aggiornaIndirizzo("citta")}
                    required
                />
                <input
                    type="text"
                    placeholder="CAP"
                    className="formEditText"
                    value={indirizzo.cap}
                    onChange={aggiornaIndirizzo("cap")}
                    required
                />
                <input
                    type="text"
                    placeholder="Provincia"
                    className="formEditText"
                    value={indirizzo.provincia}
                    onChange={aggiornaIndirizzo("provincia")}
                    required
                />

                <button type="submit" className="formButton">
                    Crea Consulenza
                </button>
            </form>
        </div>
    );
};

export default CreaConsulenza;
