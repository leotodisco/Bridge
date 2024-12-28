import { useState, useEffect } from "react";
import {useNavigate, useParams} from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart, faHeartBroken } from '@fortawesome/free-solid-svg-icons'; // Importa le icone del cuore
import "../css/alloggio.css";

const DettaglioAlloggio = () => {
    const { titolo } = useParams();
    const [alloggio, setAlloggio] = useState({});
    const [fotoAlloggio, setFotoAlloggio] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [fotoIngrandita, setFotoIngrandita] = useState(null); // Stato per foto ingrandita
    const [isFavorito, setIsFavorito] = useState(false); // Stato per tracciare se l'alloggio è nei preferiti
    const nav = useNavigate();
    const emailRifugiato = localStorage.getItem("email");
    const token = localStorage.getItem("authToken");

    // Funzione per gestire il click sull'icona del cuore
    const handleClickCuore2 = async () => {
            try {

            const idAlloggio = alloggio.id;

            console.log("Email Rifugiato:", emailRifugiato);
            console.log("ID Alloggio:", idAlloggio);

            if (!emailRifugiato) {
                setError("Email mancante.");
                return;
            }

            if (!idAlloggio || isNaN(idAlloggio)) {
                setError("ID alloggio non valido.");
                return;
            }

            if (!emailRifugiato || !token) {
                alert("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const response = await fetch(`http://localhost:8080/alloggi/interesse?emailRifugiato=${encodeURIComponent(emailRifugiato)}&idAlloggio=${idAlloggio}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Accept": "application/json",
                },
            });

            console.log("Response status:", response.status);

            if (response.ok) {
                const newFavoritoState = !isFavorito; // Cambia lo stato
                setIsFavorito(newFavoritoState);

                // Salva o rimuovi lo stato nel localStorage
                if (newFavoritoState) {
                    localStorage.setItem(`favorito_${idAlloggio}`, "true");
                } else {
                    localStorage.removeItem(`favorito_${idAlloggio}`);
                }

                console.log("Interesse manifestato con successo.");
            } else {
                const errorData = await response.text();
                setError(`Errore durante l'aggiunta ai preferiti: ${errorData}`);
                console.error("Errore:", errorData);
            }
        } catch (error) {
            setError(`Errore: ${error.message}`);
            console.error("Errore:", error.message);
        }
    };

    useEffect(() => {
        const checkFavorito = async () => {
            try {
                const emailRifugiato = localStorage.getItem("email");
                const idAlloggio = alloggio.id;

                console.log("Email Rifugiato (checkFavorito):", emailRifugiato);
                console.log("ID Alloggio (checkFavorito):", idAlloggio);

                // Controlla se lo stato è già salvato nel localStorage
                    // Altrimenti, fai una richiesta API per verificare se è nei preferiti
                const response = await fetch(`http://localhost:8080/alloggi/isFavorito?email=${emailRifugiato}&idAlloggio=${idAlloggio}`);
                console.log("Response status (isFavorito):", response.status);

                if (response.ok) {
                    const isFavorito = await response.json();
                    setIsFavorito(isFavorito);
                    console.log("Favorito status from server:", isFavorito);
                    // Salva il risultato nel localStorage per evitare future richieste
                    if (isFavorito) {
                       setIsFavorito(true);
                    }
                } else {

                    // check su token:
                    if (!token) {
                        alert("Non sei autenticato. Effettua il login.");
                        nav('/login');
                        return;
                    }

                    console.warn("Errore durante il controllo dello stato dei preferiti");
                    const response = await fetch(`http://localhost:8080/alloggi/isFavorito?email=${emailRifugiato}&idAlloggio=${idAlloggio}`, {
                        method: "GET",
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            "Content-Type": "application/json",
                            "Accept": "application/json"
                        },
                    });

                    if (response.ok) {
                        const isFavorito = await response.json();
                        setIsFavorito(isFavorito);
                        // Salva il risultato nel localStorage per evitare future richieste
                        if (isFavorito) {
                            localStorage.setItem(`favorito_${idAlloggio}`, "true");
                        }
                    } else {
                        console.warn("Errore durante il controllo dello stato dei preferiti");
                    }
                }

            } catch (error) {
                console.error("Errore:", error.message);
            }
        };

        if (alloggio.id) {
            checkFavorito();
        }
    }, [alloggio.id]); // Assicurati che alloggio.id sia disponibile prima di fare la chiamata

    useEffect(() => {
        const fetchAlloggio = async () => {
            try {
                // check su token:
                if (!token) {
                    alert("Non sei autenticato. Effettua il login.");
                    nav('/login');
                    return;
                }

                const response = await fetch(`http://localhost:8080/alloggi/SingoloAlloggio/${titolo}`, {
                    method: "GET",
                    headers: {
                        'Authorization': `Bearer ${token}`,
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
            <div className="cuore-container2" onClick={handleClickCuore2}>
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

