export const getJobRecommendations = async (email) => {
    try {
        const response = await fetch('http://127.0.0.1:5000/send_data', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('authToken')
            },
            body: JSON.stringify({ email: email }),
        });

        const data = await response.json();
        console.log("📩 Risposta dal backend (raw):", data);

        return data;
    } catch (error) {
        console.error("❌ Errore nella richiesta API:", error);
        throw error;
    }
};




