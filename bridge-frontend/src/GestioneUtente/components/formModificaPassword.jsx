import { useState } from "react";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const regexPatterns = {
    password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/
};

const ModificaPassword = () => {
    const [password, setPassword] = useState("");
    const [confermaPW, setConfermaPW] = useState("");

    const [errorMessages, setErrorMessages] = useState({});

    const validateField = (field, value) => {
        if (!regexPatterns[field]?.test(value)) {
            setErrorMessages((prev) => ({
                ...prev,
                [field]: `Il campo ${field} non è valido.`,
            }));
        }else{
            setErrorMessages((prev) => {
                const updatedErrors = { ...prev };
                delete updatedErrors[field];
                return updatedErrors;
            });
        }
        if(value.trim().length === 0) {
            setErrorMessages((prev) => ({
                ...prev,
                [field]: `Il campo ${field} è vuoto.`,
            }));
        }
    };

    const aggiornaPassword = (event) => {
        const value = event.target.value;
        setPassword(value);
        validatePasswordConditions(value);
        validateField("password", value);
    };

    const aggiornaConfermaPW = (event) => {
        const value = event.target.value;
        setConfermaPW(value);
        if (password !== value) {
            setErrorMessages((prev) => ({
                ...prev,
                confermaPW: "Le password non corrispondono.",
            }));
        } else {
            setErrorMessages((prev) => {
                const updatedErrors = { ...prev };
                delete updatedErrors.confermaPW;
                return updatedErrors;
            });
        }
    };

    const isFormValid = () => {
        if (password !== confermaPW) {
            setErrorMessages((prev) => ({
                ...prev,
                confermaPW: "Le password non corrispondono.",
            }));
            return false;
        }
        return Object.keys(errorMessages).length === 0;
    };


    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const [passwordConditions, setPasswordConditions] = useState({
        hasUpperCase: false,
        hasLowerCase: false,
        hasNumber: false,
        hasSpecialChar: false,
        isLengthValid: false
    });
    const [passwordError, setPasswordError] = useState("");
    const validatePasswordConditions = (password) => {
        const newConditions = {
            hasUpperCase: /[A-Z]/.test(password),
            hasLowerCase: /[a-z]/.test(password),
            hasNumber: /\d/.test(password),
            hasSpecialChar: /[@$!%*?&]/.test(password),
            isLengthValid: password.length >= 8 && password.length <= 16
        };
        setPasswordConditions(newConditions);

        if (!newConditions.hasUpperCase) {
            setPasswordError("La password deve contenere almeno una lettera maiuscola.");
        } else if (!newConditions.hasLowerCase) {
            setPasswordError("La password deve contenere almeno una lettera minuscola.");
        } else if (!newConditions.hasNumber) {
            setPasswordError("La password deve contenere almeno un numero.");
        } else if (!newConditions.hasSpecialChar) {
            setPasswordError("La password deve contenere almeno un carattere speciale.");
        } else if (!newConditions.isLengthValid) {
            setPasswordError("La password deve essere lunga tra 8 e 16 caratteri.");
        } else {
            setPasswordError(""); // Se tutte le condizioni sono soddisfatte, non ci sono errori
        }
    };

    const gestisciSubmit = async (event) => {

        event.preventDefault();
        if (!isFormValid()) {
            toast.error("Correggi i campi non validi prima di continuare.");
            return;
        }

        const token = localStorage.getItem('authToken');
        const email = localStorage.getItem('email');
        try {
            if (!token) {
                toast.error("Token non trovato. Effettua nuovamente il login.");
                return;
            }

            const response = await fetch(`http://localhost:8080/areaPersonale/modificaPassword/${email}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body:password
            });

            if (response.ok) {
                const data = await response.text();
                console.log(data)
                toast.info("Password modificata con successo! Verrai reinderizzato alla pagina di log-in");
                setTimeout(async () => {
                    // Effettua il logout
                    await fetch('/authentication/logout', {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                        },
                    });

                    localStorage.clear();
                    sessionStorage.clear();
                    window.location.href = "/login";
                }, 5000);
            } else {
                toast.error("Errore nella richiesta di modifica:");
            }
        } catch (error) {
            console.error("Errore nella richiesta di modifica:", error);
            toast.error("Errore nella richiesta di modifica:");
        }
    };

    return (
        <div className="formRegistrazione">
            <h2>Modifica Password</h2>
            <form onSubmit={gestisciSubmit}>

                <div className="password-field">
                    <input
                        type={showPassword ? "text" : "password"}
                        placeholder="Password"
                        className={`formEditText field ${passwordError || errorMessages.password ? 'error-field' : ''}`}
                        value={password}
                        onChange={aggiornaPassword}
                        required={true}
                    />
                    <button type="button" onClick={togglePasswordVisibility}>
                        {showPassword ? "Nascondi Password" : "Mostra Password"}
                    </button>
                </div>
                {(passwordError || errorMessages.password) && (
                    <p className="error">
                        {passwordError || errorMessages.password}
                    </p>
                )}

                <div className="password-field">
                    <input
                        type={showConfirmPassword ? "text" : "password"}
                        placeholder="Conferma Password"
                        className={`formEditText  field ${errorMessages.confermaPW ? 'error-field' : ''}`}
                        value={confermaPW}
                        onChange={aggiornaConfermaPW}
                        required={true}
                    />
                    <button type="button" className="showPW" onClick={toggleConfirmPasswordVisibility}>
                        {showConfirmPassword ? "Nascondi Password" : "Mostra Password"}
                    </button>
                </div>
                {errorMessages.confermaPW && <p className="error">{errorMessages.confermaPW}</p>}
                <button type="submit" className="formButton">
                    Invio
                </button>
            </form>
        </div>
    )
};
export default ModificaPassword;