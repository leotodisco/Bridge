import { useEffect, useState } from "react";
import Card from "../../GestioneEvento/components/Card.jsx";
import "../css/PopUp.css";
import { useNavigate } from "react-router-dom";
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import "../../GestioneAnnuncio/css/PopUpForm.css";

const MostraMyAlloggi = () => {
    const [alloggi, setAlloggi] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [userImages, setUserImages] = useState({});
    const [interessati, setInteressati] = useState([]);
    const [showPopup, setShowPopup] = useState(false);
    const [selectedAlloggio, setSelectedAlloggio] = useState(null);
    const nav = useNavigate();
    const ruolo = localStorage.getItem("ruolo");

    const handleGoToAssegnaAlloggi = async (emailRifugiato) => {
        if (!selectedAlloggio) {
            toast.error("Errore: nessun alloggio selezionato.");
            return;
        }

        if (!emailRifugiato || emailRifugiato.trim() === "") {
            toast.error("Errore: nessuna email del rifugiato fornita.");
            return;
        }

        const token = localStorage.getItem('authToken');
        if (!token) {
            setError("Errore: token di autenticazione mancante.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/alloggi/assegnazione/${selectedAlloggio}?emailRifugiato=${emailRifugiato}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(errorMessage);
            }

            const responseData = await response.json();
            toast.info(`Assegnazione completata: ${responseData.titolo}`);

            // Svuota la lista dei rifugiati e chiudi il popup
            setInteressati([]);
            setShowPopup(false);

            // Ricarica gli alloggi per riflettere i cambiamenti
            fetchAlloggi();
        } catch (error) {
            console.error("Errore durante l'assegnazione dell'alloggio:", error);
            toast.error(`Errore durante l'assegnazione: ${error.message}`);
        }
    };


    const handleInfoClick = async (idAlloggio) => {
        try {
            const token = localStorage.getItem('authToken');

            if (!token) {
                setError("Token di autenticazione mancante.");
                return;
            }

            const response = await fetch(`http://localhost:8080/alloggi/interessati/${idAlloggio}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            const data = await response.json();

            // Se la lista dei rifugiati è vuota, mostra un alert e non aprire il pop-up
            if (data.length === 0) {
                toast.error("Non ci sono rifugiati candidati per questo alloggio.");
                return; // Esci dalla funzione
            }

            // Altrimenti, mostra il pop-up con la lista dei rifugiati
            setInteressati(data);
            setSelectedAlloggio(idAlloggio);
            setShowPopup(true);

        } catch (error) {
            console.error("Errore durante il caricamento dei rifugiati interessati:", error);
            setError(error.message);
        }
    };

    const fetchAlloggi = async () => {
        try {
            const token = localStorage.getItem('authToken');
            const proprietarioEmail = localStorage.getItem("email");

            if (!token) {
                toast.error("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            if (!proprietarioEmail || proprietarioEmail.trim() === "") {
                setError("Email non trovata. Impossibile recuperare gli alloggi.");
                return;
            }

            const response = await fetch(`http://localhost:8080/alloggi/alloggiByEmail/${proprietarioEmail}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error("Errore durante la richiesta:", response.status, errorDetails);
                throw new Error(`Errore HTTP: ${response.status} - ${errorDetails}`);
            }

            const data = await response.json();
            const alloggi = Array.isArray(data) ? data : data.alloggi || [];
            setAlloggi(alloggi);

            const alloggioImagesData = {};
            for (const alloggio of alloggi) {
                const email = alloggio.proprietario.email;
                try {
                    const imgResponse = await fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${email}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json',
                        },
                    });

                    if (imgResponse.ok) {
                        const imgBase64 = await imgResponse.text();
                        alloggioImagesData[alloggio.id] = imgBase64;
                    } else {
                        alloggioImagesData[alloggio.id] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                    }
                } catch (error) {
                    setError(error.message);
                    alloggioImagesData[alloggio.id] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                }
            }

            setUserImages(alloggioImagesData);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    const fetchAlloggiPreferiti = async () => {
        try{
        const token = localStorage.getItem('authToken');
        const email = localStorage.getItem("email");
        console.log(email);
        if (!token) {
            toast.error("Non sei autenticato. Effettua il login.");
            nav('/login');
            return;
        }

            const response = await fetch(`http://localhost:8080/alloggi/alloggiPreferitiUtente/${email}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });


            if (!response.ok) {
            const errorDetails = await response.text();
            console.error("Errore durante la richiesta:", response.status, errorDetails);
            throw new Error(`Errore HTTP: ${response.status} - ${errorDetails}`);
        }

        const data = await response.json();
        const alloggi = Array.isArray(data) ? data : data.alloggi || [];
        setAlloggi(alloggi);

        const alloggioImagesData = {};
        for (const alloggio of alloggi) {
            const email = alloggio.proprietario.email;
            try {
                const imgResponse = await fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${email}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                });

                if (imgResponse.ok) {
                    const imgBase64 = await imgResponse.text();
                    alloggioImagesData[alloggio.id] = imgBase64;
                } else {
                    alloggioImagesData[alloggio.id] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                }
            } catch (error) {
                setError(error.message);
                alloggioImagesData[alloggio.id] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
            }
        }

                setUserImages(alloggioImagesData);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
    };


    useEffect(() => {
        if(ruolo === "Rifugiato"){
            fetchAlloggiPreferiti();
        }else {
            fetchAlloggi();
        }
    }, []);

    const closePopup = () => {
        setShowPopup(false);
        setInteressati([]);
    };

    if (loading) return <p>Caricamento in corso...</p>;
    if (error) return <p>Errore durante il caricamento degli alloggi: {error}</p>;

    return (
        <div className="dashboardContainer">
            {ruolo === "Rifugiato" ? (
                <h1>Richieste Alloggi</h1>
            ) : (
                <h1>I miei Alloggi</h1>
            )}
            <hr/>
            {alloggi.length > 0 ? (
                <div className="cards-container">
                    {alloggi.map((alloggio) => {
                        const alloggioImage = userImages[alloggio.id]
                            ? `data:image/jpeg;base64,${userImages[alloggio.id]}`
                            : "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                        return (
                            <Card
                                key={alloggio.id}
                                data={{
                                    title: alloggio.titolo,
                                    image: alloggioImage,
                                    alloggiProprietario: `${alloggio.proprietario.nome}`,
                                    parameter1: alloggio.maxPersone,
                                    parameter2: alloggio.metratura,
                                    parameter3: alloggio.servizi,
                                }}
                                labels={{
                                    parameter1: "Max Persone",
                                    parameter2: "Metratura",
                                    parameter3: "Servizi",
                                }}
                                onClick={() => handleInfoClick(alloggio.id)}
                            />
                        );
                    })}
                </div>
            ) : (
                <p>Nessun alloggio trovato.</p>
            )}

            {ruolo !== "Rifugiato" && showPopup && selectedAlloggio && (
                <div className="popup">
                    <div className="popup-content">
                        <h2>Rifugiati che hanno manifestato interesse</h2>
                        <button onClick={closePopup}>Chiudi</button>
                        {interessati.length > 0 ? (
                            <ul>
                                {interessati.map((rifugiato, index) => (
                                    <li key={index} className="rifugiato-item">
                                        <span>{rifugiato.nome} - {rifugiato.email}</span>
                                        <button
                                            className="assegna-button"
                                            onClick={() => handleGoToAssegnaAlloggi(rifugiato.email)}
                                        >
                                            Assegna
                                        </button>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p>Nessun rifugiato ha manifestato interesse per questo alloggio.</p>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default MostraMyAlloggi;
