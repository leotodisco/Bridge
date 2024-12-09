import {useEffect, useState} from "react";
import {useNavigate} from "react-router";
import "../../GestioneLogin/css/loginStyle.css";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const nav = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            nav("/App");
        }
    }, [nav]);

    const aggiornaEmail = (event) => {
        setEmail(event.target.value);
    }

    const aggiornaPassword = (event) => {
        setPassword(event.target.value);
    }

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
            localStorage.setItem('token', data.token);
            nav("/App");
        } catch (error) {
            document.getElementById("spanErrore").style.display = "block";
            console.error("Error during login:", error);
        }
    }

    const sendToRegistrazione = () => {
        nav("/CreaUtente")
    }


    return (
        <div className="login-container">
            <input type="text" placeholder=" E-mail" className="formEditText" onChange={aggiornaEmail}/>
            <input type="password" placeholder=" Password" className="formEditText" onChange={aggiornaPassword}/>
            <span className="errore" id="spanErrore">Controlla i dati inseriti</span>
            <button className="formButton" onClick={handleSubmit}>Accedi</button>
            <span onClick={sendToRegistrazione}
                  className="formLink centerFlexItem">Non hai un account? Registrati</span>
        </div>
    );

}

export default Login;