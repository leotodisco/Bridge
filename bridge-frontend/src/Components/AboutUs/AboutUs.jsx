import PropTypes from "prop-types"; // Importa PropTypes
import "./AboutUs.css";

const AboutUs = ({ onClose }) => {
    return (
        <div className="popup-overlay">
            <div className="popup-container">
                {/* Intestazione */}
                <header className="popup-header">
                    <h1 className="popup-title">Chi Siamo</h1>
                    <button className="popup-close" onClick={onClose}>
                        &times;
                    </button>
                </header>

                {/* Contenuto principale */}
                <main className="popup-body">
                    {/* Sezione Mission */}
                    <section className="about-us-section">
                        <h2>La Nostra Missione</h2>
                        <p>
                            Benvenuti su Bridge, una piattaforma creata per connettere le persone
                            con risorse e opportunità per raggiungere i propri obiettivi.
                        </p>
                    </section>

                    {/* Sezione Cosa Offriamo */}
                    <section className="about-us-section">
                        <h2>Cosa Offriamo</h2>
                        <ul>
                            <li>Opportunità di lavoro adatte alle tue competenze.</li>
                            <li>Eventi per migliorare le tue abilità.</li>
                            <li>Alloggi sicuri e confortevoli.</li>
                            <li>Consulenze per lo sviluppo personale e professionale.</li>
                        </ul>
                    </section>

                    {/* Sezione Vision */}
                    <section className="about-us-section">
                        <h2>La Nostra Visione</h2>
                        <p>
                            Crediamo in un mondo dove tutti possano accedere alle opportunità
                            che meritano per prosperare.
                        </p>
                    </section>

                    {/* Sezione Contatti */}
                    <section className="about-us-section">
                        <h2>Supporto</h2>
                        <p>
                            Hai domande o suggerimenti? Clicca sull&apos;icona dell&apos;assistente in basso a destra per ricevere
                            assistenza immediata. Siamo qui per aiutarti!
                        </p>
                    </section>

                </main>
            </div>
        </div>
    );
};

// Aggiungi PropTypes per validare le props
AboutUs.propTypes = {
    onClose: PropTypes.func.isRequired, // Definisce che onClose è una funzione obbligatoria
};

export default AboutUs;
