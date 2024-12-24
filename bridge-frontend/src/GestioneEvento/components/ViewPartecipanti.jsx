import { useEffect, useState } from "react";
import PropTypes from "prop-types"; // Per la validazione delle props
import { useParams } from "react-router-dom"; // Per ottenere i parametri della route

const ViewPartecipanti = () => {
    const { id } = useParams(); // Ottieni l'id dell'evento dalla route
    const [partecipanti, setPartecipanti] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPartecipanti = async () => {
            try {
                const token = localStorage.getItem("authToken"); // Recupera il token JWT
                const response = await fetch(`http://localhost:8080/api/eventi/partecipanti/${id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                if (!response.ok) {
                    throw new Error("Errore durante il recupero dei partecipanti.");
                }
                const data = await response.json();
                setPartecipanti(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };


        fetchPartecipanti();
    }, [id]);

    if (loading) {
        return <p>Caricamento partecipanti...</p>;
    }

    if (error) {
        return <p>Errore: {error}</p>;
    }

    return (
        <div>
            <h2>Partecipanti all

                evento</h2>
            <ul>
                {partecipanti.map((partecipante) => (
                    <li key={partecipante.id}>
                        {partecipante.nome} {partecipante.cognome} - {partecipante.email}
                    </li>
                ))}
            </ul>
        </div>
    );
};

ViewPartecipanti.propTypes = {
    id: PropTypes.string,
};

export default ViewPartecipanti;
