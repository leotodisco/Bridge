import { useEffect, useState } from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import CreaEvento from './GestioneEvento/components/formEvento.jsx';
import CreaConsulenza from './GestioneAnnuncio/components/formConsulenza.jsx';
import CreaCorso from './GestioneCorso/components/formCorso.jsx';
import CreaUtente from "./Registrazione/components/formRegistrazione.jsx";
import Login from "./GestioneLogin/components/login.jsx";
import CreaAlloggio from "./GestioneAlloggio/components/formAlloggio.jsx";

import logo from './assets/IMG_1580.PNG';
import LogoutButton from "./GestioneLogout/components/logout.jsx";

function App() {

    const [isAuthenticated, setIsAuthenticated] = useState(false);

    // Controlla se il token JWT è presente al caricamento
    useEffect(() => {
        const token = localStorage.getItem('authToken');
        setIsAuthenticated(!!token); // Aggiorna lo stato se il token esiste
    }, []);

    // Definizione di handleLogin
    const handleLogin = () => {
        setIsAuthenticated(true); // Imposta lo stato su true
    };

    const handleLogout = () => {
        setIsAuthenticated(false); // Imposta lo stato su false
    };

    return (
        <Router>
            <main className="main-content">
                <div className="logo-container">
                    <img src={logo} alt="Logo" className="site-logo"/>
                </div>
                {isAuthenticated ? (
                    <>
                        <nav className="nav-links">
                            <Link to="/" className="nav-link">Home</Link>
                            <Link to="/crea-evento" className="nav-link">Crea Evento</Link>
                            <Link to="/crea-consulenza" className="nav-link">Crea Consulenza</Link>
                            <Link to="/crea-corso" className="nav-link">Crea Corso</Link>
                            <Link to="/crea-alloggio" className="nav-link">Crea Alloggio</Link>
                            <LogoutButton onLogout={handleLogout}/>
                        </nav>
                        <Routes>
                            <Route path="/" element={<p>Benvenuto nel sistema di gestione!</p>}/>
                            <Route path="/crea-evento" element={<CreaEvento/>}/>
                            <Route path="/crea-consulenza" element={<CreaConsulenza/>}/>
                            <Route path="/crea-corso" element={<CreaCorso/>}/>
                            <Route path="/crea-utente" element={<CreaUtente/>}/>
                            <Route path="/login" element={<Login onLogin={handleLogin} />} />
                            <Route path="/crea-alloggio" element={<CreaAlloggio/>}/>
                        </Routes>
                    </> ) : (
                        <>
                            <nav className="nav-links">
                                <Link to="/login" className="nav-link">Login</Link>
                            </nav>
                            <Routes>
                                <Route path="/" element={<p>Benvenuto nel sistema di gestione!</p>}/>
                                <Route path="/login" element={<Login onLogin={handleLogin} />} />
                                <Route path="/crea-utente" element={<CreaUtente/>}/>
                            </Routes>
                        </>
                    )}

            </main>
        </Router>
    );
}

export default App;
