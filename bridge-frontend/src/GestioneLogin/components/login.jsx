import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import "../../GestioneLogin/css/loginStyle.css";
import PropTypes from 'prop-types';
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Login = ({ onLogin }) => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const nav = useNavigate();

    const aggiornaEmail = (event) => {
        setEmail(event.target.value);
    };

    const aggiornaPassword = (event) => {
        setPassword(event.target.value);
    };

    useEffect(() => {
        const token = localStorage.getItem('authToken');

        // Controlla se l'utente è autenticato
        if (token) {
            nav('/homepage');
        }
    }, [nav]);

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/authentication/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('email', email);
            localStorage.setItem('ruolo', data.ruolo);
            onLogin(data.token);
            toast.success("Login avvenuto con successo!");
            nav("/homepage");
        } catch (error) {
            toast.error("Errore durante il login. Controlla i dati inseriti!");
            console.error("Error during login:", error);
        }
    };

    const sendToRegistrazione = () => {
        nav("/crea-utente");
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            <hr />
            <div className="formGroup">
                <label htmlFor="email" className="formLabel">Email</label>
                <input
                    type="text"
                    id="email"
                    placeholder="Inserisci la tua email"
                    className="formEditText"
                    value={email}
                    onChange={aggiornaEmail}
                    required
                />
            </div>
            <div className="formGroup">
                <label htmlFor="password" className="formLabel">Password</label>
                <input
                    type="password"
                    id="password"
                    placeholder="Inserisci la tua password"
                    className="formEditText"
                    value={password}
                    onChange={aggiornaPassword}
                    required
                />
            </div>
            <button className="formButton" onClick={handleSubmit}>Accedi</button>
            <span onClick={sendToRegistrazione}
                  className="formLink centerFlexItem">Non hai un account? Registrati</span>
            {/* Toast container */}
            <ToastContainer />
        </div>
    );
};

Login.propTypes = {
    onLogin: PropTypes.func.isRequired, // Deve essere una funzione e obbligatoria
};

export default Login;
