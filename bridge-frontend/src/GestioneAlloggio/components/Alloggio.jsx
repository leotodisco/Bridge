import { useState, useEffect } from "react";
import {useNavigate, useParams} from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart, faHeartBroken } from '@fortawesome/free-solid-svg-icons'; // Importa le icone del cuore
import "../css/alloggio.css";
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const DettaglioAlloggio = () => {
    const { titolo } = useParams();
    const [alloggio, setAlloggio] = useState({});
    const [fotoAlloggio, setFotoAlloggio] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errore, setError] = useState(null);
    const [fotoIngrandita, setFotoIngrandita] = useState(null); // Stato per foto ingrandita
    const [isFavorito, setIsFavorito] = useState(false); // Stato per tracciare se l'alloggio è nei preferiti
    const nav = useNavigate();
    const emailRifugiato = localStorage.getItem("email");
    const token = localStorage.getItem("authToken");

    // Funzione per gestire il click sull'icona del cuore
    const handleClickCuore2 = async () => {
        try {
            const idAlloggio = alloggio.id;

            if (!emailRifugiato || !token) {
                toast.error("Non sei autenticato. Effettua il login.");
                nav('/login');
                return;
            }

            const url = isFavorito
                ? `http://localhost:8080/alloggi/rimuoviInteresse?emailRifugiato=${encodeURIComponent(emailRifugiato)}&idAlloggio=${idAlloggio}`
                : `http://localhost:8080/alloggi/interesse?emailRifugiato=${encodeURIComponent(emailRifugiato)}&idAlloggio=${idAlloggio}`;

            const response = await fetch(url, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Accept": "application/json",
                },
            });

            if (response.ok) {
                const newFavoritoState = !isFavorito;
                setIsFavorito(newFavoritoState);

                if (newFavoritoState) {
                    localStorage.setItem(`favorito_${idAlloggio}`, "true");
                    toast.error("Manifestazione interesse aggiunta con successo");
                } else {
                    localStorage.removeItem(`favorito_${idAlloggio}`);
                    toast.error("Manifestazione interesse rimossa con successo");
                }

                console.log(newFavoritoState ? "Interesse aggiunto" : "Interesse rimosso");
            } else {
                const errorData = await response.text();
                setError(`Errore durante l'operazione: ${errorData}`);
                console.error("Errore:", errorData);
            }
        } catch (errore) {
            setError(`Errore: ${errore.message}`);
            console.error("Errore:", errore.message);
        }
    };


    useEffect(() => {
        const checkFavorito = async () => {
            try {
                const response = await fetch(`http://localhost:8080/alloggi/isFavorito?email=${emailRifugiato}&idAlloggio=${alloggio.id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                if (response.ok) {
                    const isFavorito = await response.json();
                    setIsFavorito(isFavorito);
                } else {
                    console.warn("Errore durante il controllo dello stato dei preferiti.");
                }
            } catch (errore) {
                console.error("Errore:", errore.message);
            }
        };

        if (alloggio.id) {
            checkFavorito();
        }
    }, [alloggio.id]);
    [alloggio.id]; // Assicurati che alloggio.id sia disponibile prima di fare la chiamata

    useEffect(() => {
        const fetchAlloggio = async () => {
            try {
                // check su token:
                if (!token) {
                   toast.error("Non sei autenticato. Effettua il login.");
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
            } catch (errore) {
                setError(errore.message);
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
    if (errore) return <p>Errore: {errore}</p>;
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

