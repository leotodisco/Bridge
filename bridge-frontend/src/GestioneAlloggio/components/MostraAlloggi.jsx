import { useEffect, useState } from "react";
import Card from "../../GestioneEvento/components/Card.jsx";
import { useNavigate } from "react-router-dom";

const MostraAlloggi = () => {
    const [alloggi, setAlloggi] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleInfoClick = (titolo) => {
        navigate(`/alloggi/SingoloAlloggio/${titolo}`);  // Usa il percorso con il titolo
    };

    const fetchAlloggi = async () => {
        try {
            const response = await fetch("http://localhost:8080/alloggi/mostra");
            if (!response.ok) {
                console.log("Errore HTTP:", response.status, await response.text());
                throw new Error(`Errore HTTP: ${response.status}`);
            }
            const data = await response.json();
            console.log("Dati ricevuti:", data);
            setAlloggi(data);
        } catch (error) {
            console.log("Errore durante il caricamento:", error);
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
            <h1>Tutti gli Alloggi</h1>
            {alloggi.length > 0 ? (
                <div className="cards-container">
                    {alloggi.map((event) => (
                        <Card
                            key={event.id}
                            data={{
                                title: event.titolo,
                                image: event.fotos ? event.foto : "https://via.placeholder.com/150/cccccc/000000?text=No+Image",
                                alloggiProprietario: `${event.Proprietario}`,
                                parameter1: event.maxPersone,
                                parameter2: event.metratura,
                                parameter3: event.servizi,
                            }}
                            labels={{
                                parameter1: "MaxPersone",
                                parameter2: "Metratura",
                                parameter3: "Servizi",
                            }}
                            onClick={() => handleInfoClick(event.titolo)} // Reindirizza alla pagina dei dettagli
                        />
                    ))}
                </div>
            ) : (
                <p>Nessun alloggio trovato.</p>
            )}
        </div>
    );
};

export default MostraAlloggi;
