import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart, faHeartBroken } from '@fortawesome/free-solid-svg-icons'; // Importa le icone del cuore
import "../css/alloggio.css";

const DettaglioAlloggio = () => {
    const { titolo } = useParams();
    const [alloggio, setAlloggio] = useState(null);
    const [fotoAlloggio, setFotoAlloggio] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [fotoIngrandita, setFotoIngrandita] = useState(null); // Stato per foto ingrandita
    const [isFavorito, setIsFavorito] = useState(false); // Stato per tracciare se l'alloggio è nei preferiti

    // Funzione per gestire il click sull'icona del cuore
    const handleClickCuore = async () => {
        try {
            const emailRifugiato = sessionStorage.getItem("emailUtente");
            const nomeAlloggio = alloggio.titolo;

            const response = await fetch("http://localhost:8080/alloggi/preferiti", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify({
                    emailRifugiato: emailRifugiato,
                    titoloAlloggio: nomeAlloggio
                })
            });

            if (response.ok) {
                setIsFavorito(true); // Segna l'alloggio come preferito
            } else if (response.status === 400) {
                setIsFavorito(true); // Segna comunque l'alloggio come già preferito
                const errorData = await response.text();
                console.warn(errorData);
            } else {
                throw Error("Errore durante l'aggiunta ai preferiti");
            }
        } catch (error) {
            setError(error.message);
        }
    };


    useEffect(() => {
        const checkFavorito = async () => {
            try {
                const emailRifugiato = sessionStorage.getItem("emailUtente");

                const response = await fetch(`http://localhost:8080/alloggi/isFavorito?email=${emailRifugiato}&titolo=${titolo}`);
                if (response.ok) {
                    const isFavorito = await response.json();
                    setIsFavorito(isFavorito);
                } else {
                    console.warn("Errore durante il controllo dello stato dei preferiti");
                }
            } catch (error) {
                console.error("Errore:", error.message);
            }
        };

        checkFavorito();
    }, [titolo]);



    useEffect(() => {
        const fetchAlloggio = async () => {
            try {
                const response = await fetch(`http://localhost:8080/alloggi/SingoloAlloggio/${titolo}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                });
                if (!response.ok) {
                    throw new Error('Errore durante il caricamento dei dati');
                }
                const data = await response.json();
                setAlloggio(data);
                setFotoAlloggio(data.foto);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAlloggio();
    }, [titolo]);

    // Funzione per portare la foto cliccata in prima posizione
    const handleClickFoto = (index) => {
        const fotoSelezionata = fotoAlloggio[index];
        const nuoveFoto = [fotoSelezionata, ...fotoAlloggio.filter((_, i) => i !== index)];
        setFotoAlloggio(nuoveFoto);
        setFotoIngrandita(fotoSelezionata); // Imposta la foto ingrandita
    };

    if (loading) return <p>Caricamento in corso...</p>;
    if (error) return <p>Errore: {error}</p>;
    if (!alloggio) return <p>Dettagli non disponibili.</p>;

    return (
        <div className="alloggio-container">
            <h1>{alloggio?.titolo}</h1>
            <p>
                {alloggio.indirizzo.via}, {alloggio.indirizzo.numCivico}, {alloggio.indirizzo.cap}, {alloggio.indirizzo.citta}, {alloggio.indirizzo.provincia}
            </p>

            {/* Icona cuore per manifestazione interesse */}
            <div className="cuore-container" onClick={handleClickCuore}>
                <FontAwesomeIcon
                    icon={isFavorito ? faHeart : faHeartBroken}
                    color={isFavorito ? "red" : "gray"}
                    size="2x"
                />
                <p>{isFavorito ? "Aggiunto ai preferiti" : "Aggiungi ai preferiti"}</p>
            </div>

            <div className="immagini">
                {fotoAlloggio.length === 0 ? (
                    <p>Nessuna foto disponibile</p>
                ) : (
                    fotoAlloggio.map((base64Image, index) => (
                        <div
                            key={index}
                            className={`foto-container ${fotoIngrandita === base64Image ? "ingrandita" : ""}`}
                            onClick={() => handleClickFoto(index)}
                        >
                            <img
                                src={`data:image/jpeg;base64,${base64Image}`}
                                alt={`Foto ${index + 1}`}
                                className="foto"
                            />
                        </div>
                    ))
                )}
            </div>

            {/* Descrizione sotto le immagini */}
            <div className="descrizione">
                <div className="colonna-sinistra">
                    <p><strong>Proprietario:</strong> {alloggio.proprietario.nome}</p>
                    <p><strong>Email:</strong> {alloggio.proprietario.email}</p>
                </div>

                <div className="colonna-destra">
                    <p><strong>Descrizione: </strong>{alloggio.descrizione}</p>
                </div>
            </div>

        </div>
    );
};

export default DettaglioAlloggio;
