import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

const DettaglioAlloggio = () => {
    const { titolo } = useParams(); // Recupera il titolo dall'URL
    const [alloggio, setAlloggio] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchAlloggio = async () => {
            try {
                console.log("prova");
                const response = await fetch(`http://localhost:8080/alloggi/SingoloAlloggio/${titolo}`,{
                    method:"GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                })
                if (!response.ok) {
                    throw new Error('Errore durante il caricamento dei dati');
                }
                const data = await response.json(); // Usa json() per ottenere i dati in formato JSON
                console.log(data, "dati ricevuti");  // Verifica i dati ricevuti
                setAlloggio(data);  // Imposta i dati ricevuti nello stato
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAlloggio();
    }, [titolo]);

    if (loading) return <p>Caricamento in corso...</p>;
    if (error) return <p>Errore: {error}</p>;
    if (!alloggio) return <p>Dettagli non disponibili.</p>;

    return (
        <div className="alloggio-container">
            <h1>{alloggio?.titolo}</h1>
            <p>
                {alloggio.indirizzo.via}, {alloggio.indirizzo.numCivico}, {alloggio.indirizzo.cap}, {alloggio.indirizzo.citta}, {alloggio.indirizzo.provincia}
            </p>

            <div className="immagini">
                {alloggio?.immagini?.map((img, index) => (
                    <img key={index} src={img} alt={`Immagine ${index + 1}`} />
                ))}
            </div>

            <div className="descrizione">
                <h2>DESCRIZIONE</h2>
                <p><strong>Proprietario:</strong> {alloggio.proprietario.nome}</p>
                <p><strong>Telefono:</strong> {alloggio.proprietario.telefono}</p>
                <p><strong>Email:</strong> {alloggio.proprietario.email}</p>
            </div>
        </div>
    );
};

export default DettaglioAlloggio;
