import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Card from "../../GestioneEvento/components/Card.jsx";
import "../css/crea-alloggio.css";


const MostraAlloggi = () => {
    const [alloggi, setAlloggi] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [userImages, setUserImages] = useState({});
    const [isVolontario, setIsVolontario] = useState(false); // Stato per verificare se l'utente è volontario
    const navigate = useNavigate();

    const handleInfoClick = (titolo) => {
        navigate(`/alloggi/SingoloAlloggio/${titolo}`);
    };

    const handleGoToCreaAlloggi = () => {
        navigate('/crea-Alloggio');
    };

    const fetchAlloggi = async () => {
        try {
            const email = localStorage.getItem('email');
            const token = localStorage.getItem('authToken');

            if (!email || !token) {
                alert("Non sei autenticato. Effettua il login.");
                navigate('/login');
                return;
            }

            const response = await fetch("http://localhost:8080/alloggi/mostra", {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                console.error("Errore HTTP durante il fetch degli alloggi:", response.status);
                throw new Error(`Errore HTTP: ${response.status}`);
            }
            const data = await response.json();
            setAlloggi(data);

            const alloggioImagesData = {};
            for (const alloggio of data) {
                const email = alloggio.proprietario.email;
                try {
                    if (!token) {
                        alert("Non sei autenticato. Effettua il login.");
                        navigate('/login');
                        return;
                    }

                    const imgResponse = await fetch(`http://localhost:8080/areaPersonale/DatiFotoUtente/${email}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json',
                        },
                    });

                    if (imgResponse.ok) {
                        const imgBase64 = await imgResponse.text();
                        alloggioImagesData[email] = imgBase64;
                    } else {
                        alloggioImagesData[email] = "https://via.placeholder.com/150/cccccc/000000?text=No+Image";
                    }
                } catch (error) {
                    console.log(error);
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
        fetchAlloggi();

        // Controllo del ruolo utente
        const ruolo = localStorage.getItem('ruolo');
        if (ruolo === 'Volontario') {
            setIsVolontario(true);
        }
    }, []);

    if (loading) return <p>Caricamento in corso...</p>;
    if (error) return <p>Errore durante il caricamento degli alloggi: {error}</p>;

    return (
        <div>
            {/* Mostra il pulsante "Crea" solo se l'utente è un volontario */}
            {isVolontario && (
            <div className="headerForm-container">
                <h1 className="header-title">Tutti gli Alloggi</h1>
                    <button
                        className="btn btn-circle"
                        onClick={handleGoToCreaAlloggi} // Apri il popup
                    >
                        +
                    </button>
            </div>
            )}
            {loading ? (
                <p>Caricamento in corso...</p>
            ) : alloggi.length > 0 ? (
                <div className="cards-container">
                    {alloggi.map((alloggio) => {
                        const alloggioImage = userImages[alloggio.proprietario.email]
                            ? `data:image/jpeg;base64,${userImages[alloggio.proprietario.email]}`
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

export default MostraAlloggi;
