import { useNavigate } from 'react-router-dom';
import './IntroPageStyle.css';

const IntroPage = () => {
    const nav = useNavigate();

    const redirectToLogin = () => {
        nav('/login');
    };

    return (
        <div className="intro-container">
            <header className="intro-header">
                <h1>Benvenuto su Bridge</h1>
                <p>La piattaforma digitale per l&#39;integrazione sociale e lavorativa dei rifugiati in Italia.</p>
            </header>

            <section className="intro-mission">
                <h2>La nostra missione</h2>
                <p>
                    Bridge nasce per promuovere l&#39;accoglienza e l&#39;integrazione dei rifugiati, coinvolgendo cittadini e
                    volontari. Offriamo soluzioni concrete per il reintegro sociale ed economico, facilitando l&#39;accesso a risorse come
                    alloggi, corsi di formazione e opportunità lavorative.
                </p>
            </section>

            <section className="intro-features">
                <h2>Cosa offriamo</h2>
                <ul>
                    <li><strong>Alloggi:</strong> Trova un posto sicuro grazie al supporto dei volontari.</li>
                    <li><strong>Opportunità lavorative:</strong> Accedi a offerte di lavoro adatte alle tue competenze.</li>
                    <li><strong>Corsi di formazione:</strong> Impara nuove abilità per migliorare il tuo futuro.</li>
                    <li><strong>Consulenze:</strong> Ricevi assistenza legale, sanitaria e burocratica.</li>
                    <li><strong>Chatbot:</strong> Ottieni risposte rapide a domande su vari argomenti.</li>
                </ul>
            </section>

            <section className="intro-success">
                <h2>Perché scegliere Bridge?</h2>
                <p>
                    Crediamo in un futuro inclusivo. Con il supporto di intelligenza artificiale e un team dedicato, Bridge semplifica il
                    processo di integrazione e crea opportunità per tutti.
                </p>
            </section>

            <div className="cta-container">
                <button onClick={redirectToLogin} className="cta-button">Accedi per scoprire di più</button>
            </div>

            <footer className="intro-footer">
                <p>&copy; 2025 Bridge</p>
            </footer>
        </div>
    );
};

export default IntroPage;
