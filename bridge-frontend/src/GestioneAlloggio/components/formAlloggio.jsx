import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import "../css/formAlloggioStyle.css";

const Servizi = {
    WIFI: "WIFI",
    ARIACONDIZIONATA: "ARIACONDIZIONATA",
    RISCALDAMENTO: "RISCALDAMENTO"
};

const CreaAlloggio = () => {
    const [descrizione, setDescrizione] = useState("");
    const nav = useNavigate();
    const [maxPersone, setMaxPersone] = useState(1);
    const [metratura, setMetratura] = useState("");
    const [fotos, setFotos] = useState([]);
    const [servizi, setServizi] = useState("");
    const [titolo, setTitolo] = useState("");
    const [volontario, setVolontario] = useState(false);
    const [indirizzo, setIndirizzo] = useState({
        via: "",
        cap: "",
        numCivico: "",
        provincia: "",
        citta: ""
    });

    const [errori, setErrori] = useState({});

    const aggiornaCampo = (setter) => (event) => setter(event.target.value);
    const aggiornaIndirizzo = (campo) => (event) => {
        setIndirizzo({
            ...indirizzo,
            [campo]: event.target.value
        });
    };

    const aggiornaFotoAlloggio = (event) => {
        const files = Array.from(event.target.files);

        if (files.length < 1 || files.length > 3) {
            setErrori((prev) => ({ ...prev, fotos: "Devi caricare almeno 1 foto e un massimo di 3 foto." }));
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
            .then((base64Images) => {
                setFotos(base64Images);
                setErrori((prev) => ({ ...prev, fotos: "" }));
            })
            .catch((error) => {
                console.error("Errore nella lettura delle immagini:", error);
                setErrori((prev) => ({ ...prev, fotos: "Errore nella lettura delle immagini." }));
            });
    };

    const validaCampi = () => {
        const nuoviErrori = {};

        if (!titolo || titolo.length < 3 || titolo.length > 100) {
            nuoviErrori.titolo = "Il titolo deve essere tra 3 e 100 caratteri.";
        }

        if (!/^[A-Za-z0-9]{0,400}$/.test(descrizione)) {
            nuoviErrori.descrizione = "La descrizione deve essere tra 1 e 400 caratteri.";
        }

        if (!/^\d{1,2}$/.test(maxPersone)) {
            nuoviErrori.maxPersone = "Il numero massimo di persone deve essere tra 1 e 99.";
        }

        if (!/^\d{1,5}$/.test(metratura)) {
            nuoviErrori.metratura = "La metratura deve essere un numero valido.";
        }


        if (!servizi) {
            nuoviErrori.servizi = "Devi selezionare un servizio.";
        }

        if (!/^[A-zÀ-ù ‘]{2,50}$/.test(indirizzo.via)) {
            nuoviErrori.via = "La via deve avere almeno 2 caratteri e massimo 50.";
        }

        if (!/^\d{1,3}$/.test(indirizzo.numCivico)) {
            nuoviErrori.numCivico = "Il numero civico deve essere un numero valido.";
        }

        if (!/^[A-zÀ-ù ‘]{2,50}$/.test(indirizzo.citta)) {
            nuoviErrori.citta = "Errore nell'inserimento della cità.";
        }

        if (!/^[0-9]{5}$/.test(indirizzo.cap)) {
            nuoviErrori.cap = "Il CAP deve essere di 5 cifre.";
        }

        if (!/^[A-Za-z]{2}$/.test(indirizzo.provincia)) {
            nuoviErrori.provincia = "La provincia deve avere esattamente 2 caratteri alfabetici.";
        }

        if (fotos.length === 0 && !/^.+\.jpeg$/.test(fotos)) {
            nuoviErrori.fotos = "Devi caricare almeno 1 foto in formato JPEG.";
        }

        setErrori(nuoviErrori);
        return Object.keys(nuoviErrori).length === 0;
    };

    // Verifica se l'utente è loggato come Volontario
    useEffect(() => {
        const ruoloUtente = localStorage.getItem('ruolo'); // Recupera il ruolo dal localStorage

        if (ruoloUtente === 'Volontario') {
            setVolontario(true); // Imposta che l'utente è un volontario
        }
    }, []);


    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!validaCampi()) {
            console.log("Errore nei campi inseriti");
            return;
        }

        try {

            if (!volontario) {
                throw new Error("L'utente non è autorizzato a creare un alloggio.");
            }

            const emailProprietario = localStorage.getItem('email'); // Recupera l'email del volontario da localStorage

            if (!emailProprietario) {
                throw new Error("Email del volontario non trovata");
            }

            const email = localStorage.getItem('email');
            const token = localStorage.getItem('authToken');

            if (!email || !token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await fetch("http://localhost:8080/alloggi/aggiungi", {
                method: "POST",
                headers: {
                    'Authorization': `Bearer ${token}`,
                    "Content-Type": "application/json",
                    Accept: "application/json"
                },

                body: JSON.stringify({
                    titolo : titolo,
                    descrizione : descrizione,
                    metratura : metratura,
                    maxPersone : maxPersone,
                    servizi : servizi,
                    foto: fotos,
                    emailProprietario: emailProprietario,
                    indirizzo: {
                        via: indirizzo.via,
                        numCivico: indirizzo.numCivico,
                        citta: indirizzo.citta,
                        cap: indirizzo.cap,
                        provincia: indirizzo.provincia
                    },
                })
            });

            if (!response.ok) {
                throw new Error(`Errore http: ${response.status}`);
            }

            const result = await response.text();
            alert("Alloggio creato con successo: " + result);

            nav("/area-personale");
        } catch (error) {
            console.error("Errore creazione alloggio: ", error);
            alert("Errore durante la creazione dell'alloggio: " + error.message);
        }
    };

    return (
        <div className= "form-generale">
            <form className="formContainer" onSubmit={handleSubmit}>
                <h2>Crea un nuovo alloggio</h2>

                <div className="formField">
                    <input
                        type="text"
                        placeholder="Titolo alloggio"
                        value={titolo}
                        onChange={aggiornaCampo(setTitolo)}
                    />
                    {errori.titolo && <span className="errore">{errori.titolo}</span>}
                </div>

                <div className="formField">
                    <textarea
                        placeholder="Descrizione"
                        value={descrizione}
                        onChange={aggiornaCampo(setDescrizione)}
                    />
                    {errori.descrizione && <span className="errore">{errori.descrizione}</span>}
                </div>

                <div className="formField">
                    <select value={servizi} onChange={aggiornaCampo(setServizi)}>
                        <option value="" disabled>Seleziona un servizio</option>
                        {Object.values(Servizi).map((servizio) => (
                            <option key={servizio} value={servizio}>{servizio}</option>
                        ))}
                    </select>
                    {errori.servizi && <span className="errore">{errori.servizi}</span>}
                </div>

                <div className="formField">
                    <input
                        type="number"
                        placeholder="Max persone"
                        value={maxPersone}
                        onChange={aggiornaCampo(setMaxPersone)}
                    />
                    {errori.maxPersone && <span className="errore">{errori.maxPersone}</span>}
                </div>

                <div className="formField">
                    <input
                        type="number"
                        placeholder="Metratura"
                        value={metratura}
                        onChange={aggiornaCampo(setMetratura)}
                    />
                    {errori.metratura && <span className="errore">{errori.metratura}</span>}
                </div>

                <h3>Indirizzo</h3>

                <div className="formField">
                    <input
                        type="text"
                        placeholder="Via"
                        value={indirizzo.via}
                        onChange={aggiornaIndirizzo("via")}
                    />
                    {errori.via && <span className="errore">{errori.via}</span>}
                </div>

                <div className="formField">
                    <input
                        type="text"
                        placeholder="Numero Civico"
                        value={indirizzo.numCivico}
                        onChange={aggiornaIndirizzo("numCivico")}
                    />
                    {errori.numCivico && <span className="errore">{errori.numCivico}</span>}
                </div>

                <div className="formField">
                    <input
                        type="text"
                        placeholder="Città"
                        value={indirizzo.citta}
                        onChange={aggiornaIndirizzo("citta")}
                    />
                    {errori.citta && <span className="errore">{errori.citta}</span>}
                </div>

                <div className="formField">
                    <input
                        type="text"
                        placeholder="CAP"
                        value={indirizzo.cap}
                        onChange={aggiornaIndirizzo("cap")}
                    />
                    {errori.cap && <span className="errore">{errori.cap}</span>}
                </div>

                <div className="formField">
                    <input
                        type="text"
                        placeholder="Provincia"
                        value={indirizzo.provincia}
                        onChange={aggiornaIndirizzo("provincia")}
                    />
                    {errori.provincia && <span className="errore">{errori.provincia}</span>}
                </div>

                <div className="formField">
                    <input
                        type="file"
                        multiple
                        accept="image/*"
                        onChange={aggiornaFotoAlloggio}
                    />
                    {errori.fotos && <span className="errore">{errori.fotos}</span>}
                </div>

                <button type="submit">Crea Alloggio</button>
            </form>
        </div>
    );
};

export default CreaAlloggio;
