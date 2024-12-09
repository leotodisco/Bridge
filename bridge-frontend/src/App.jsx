import './App.css';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import CreaEvento from './GestioneEvento/components/formEvento.jsx';
import CreaConsulenza from './GestioneAnnuncio/components/formConsulenza.jsx';
import CreaCorso from './GestioneCorso/components/formCorso.jsx';
import logo from './assets/IMG_1580.PNG';

function App() {
    return (
        <Router>
            <main className="main-content">
                <div className="logo-container">
                    <img src={logo} alt="Logo" className="site-logo"/>
                </div>
                <nav className="nav-links">
                    <Link to="/" className="nav-link">Home</Link>
                    <Link to="/crea-evento" className="nav-link">Crea Evento</Link>
                    <Link to="/crea-consulenza" className="nav-link">Crea Consulenza</Link>
                    <Link to = "/crea-corso" className = "nav-link">Crea Corso</Link>
                </nav>
                <Routes>
                    <Route path="/" element={<p>Benvenuto nel sistema di gestione!</p>}/>
                    <Route path="/crea-evento" element={<CreaEvento/>}/>
                    <Route path="/crea-consulenza" element={<CreaConsulenza/>}/>
                    <Route path="/crea-corso" element={<CreaCorso/>}/>
                </Routes>
            </main>
        </Router>
    );
}

export default App;
