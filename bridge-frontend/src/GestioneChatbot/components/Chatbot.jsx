import { useState } from 'react';
import OpenAI from "openai";

const Chatbot = () => {
    const [messages, setMessages] = useState([]);
    const [userInput, setUserInput] = useState('');
    const [loading, setLoading] = useState(false);


    const openai = new OpenAI({
        apiKey: import.meta.env.VITE_OPENAI_API_KEY , // Usa una variabile ambiente
        dangerouslyAllowBrowser: true
    });

    const handleSendMessage = async () => {
        if (!userInput.trim()) return; // Ignora input vuoti
        setLoading(true);

        // Aggiorna i messaggi con l'input dell'utente
        const newMessages = [
            ...messages,
            { role: "user", content: userInput },
        ];
        setMessages(newMessages);

        try {
            const response = await openai.chat.completions.create({
                model: "gpt-3.5-turbo-16k",
                messages: [
                    {
                        role: "system",
                        content: `
              Tu sei “Bridge”, un chatbot progettato per assistere i rifugiati nel loro reintegro sociale ed economico.
              Il tuo obiettivo è offrire supporto immediato e informazioni utili sui servizi disponibili all’interno della piattaforma.
            `,
                    },
                    ...newMessages,
                ],
                temperature: 0,
                max_tokens: 2800,
                top_p: 1,
                frequency_penalty: 0,
                presence_penalty: 0,
            });

            const botReply = response.choices[0]?.message?.content || "Nessuna risposta ricevuta.";
            setMessages([...newMessages, { role: "assistant", content: botReply }]);
        } catch (error) {
            console.error("Errore durante la comunicazione con OpenAI:", error);
            setMessages([...newMessages, { role: "assistant", content: "Errore nel recuperare la risposta." }]);
        } finally {
            setLoading(false);
            setUserInput('');
        }
    };

    return (
        <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto" }}>
            <div style={{ border: "1px solid #ccc", borderRadius: "10px", padding: "10px", height: "400px", overflowY: "auto" }}>
                {messages.map((msg, index) => (
                    <div key={index} style={{ marginBottom: "10px" }}>
                        <strong>{msg.role === "user" ? "Tu:" : "Bridge:"}</strong>
                        <p>{msg.content}</p>
                    </div>
                ))}
                {loading && <p>Scrivendo...</p>}
            </div>
            <div style={{ marginTop: "10px" }}>
                <input
                    type="text"
                    value={userInput}
                    onChange={(e) => setUserInput(e.target.value)}
                    placeholder="Scrivi un messaggio..."
                    style={{ width: "80%", padding: "10px", marginRight: "10px" }}
                />
                <button onClick={handleSendMessage} style={{ padding: "10px" }} disabled={loading}>
                    Invia
                </button>
            </div>
        </div>
    );
};

export default Chatbot;