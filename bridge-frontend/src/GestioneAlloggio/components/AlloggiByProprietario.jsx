import { useEffect, useState } from "react";
import Card from "../../GestioneEvento/components/Card.jsx";
import { useNavigate } from "react-router-dom";

const MostraMyAlloggi = () => {
    const [alloggi, setAlloggi] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [userImages, setUserImages] = useState({}); // Stato per le immagini degli alloggi
    const navigate = useNavigate();


    //inserire pop up con tutte le persone che hanno manifestato interesse
    const handleInfoClick = (titolo) => {
        navigate(`/alloggi/SingoloAlloggio/${titolo}`);
    };

    const fetchAlloggi = async () => {
        try {
            const token = localStorage.getItem('authToken');
            const proprietarioEmail = localStorage.getItem("email");

            if (!token) {
                setError("Token di autenticazione mancante.");
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
            console.log("Dati ricevuti:", data);

            const alloggi = Array.isArray(data) ? data : data.alloggi || [];
            setAlloggi(alloggi);

            // Recupera le immagini per ogni alloggio
            const alloggioImagesData = {};
            for (const alloggio of alloggi) {
                console.log(`Recupero immagine per l'alloggio ID: ${alloggio.id}`);
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
                    console.error(`Errore durante il recupero dell'immagine per l'alloggio ID ${alloggio.id}:`, error);
                    alloggioImagesData[alloggio.id] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                }
            }

            setUserImages(alloggioImagesData); // Salva le immagini nello stato
        } catch (error) {
            console.error("Errore durante il fetch degli alloggi:", error);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAlloggi();
    }, []);

    if (loading) return <p>Caricamento in corso...</p>;
    if (error) return <p>Errore durante il caricamento degli alloggi: {error}</p>;

    return (
        <div>
            <h1>Tutti i tuoi Alloggi</h1>
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
                                onClick={() => handleInfoClick(alloggio.titolo)}
                            />
                        );
                    })}
                </div>
            ) : (
                <p>Nessun alloggio trovato.</p>
            )}
        </div>
    );
};

export default MostraMyAlloggi;
