import { useState, useEffect, useRef } from 'react';
import OpenAI from "openai";
import "../css/Chatbot.css";

const Chatbot = () => {
    const [messages, setMessages] = useState([]);
    const [userInput, setUserInput] = useState('');
    const [loading, setLoading] = useState(false);
    const messagesEndRef = useRef(null);

    const openai = new OpenAI({
        apiKey: import.meta.env.VITE_OPENAI_API_KEY,
        dangerouslyAllowBrowser: true
    });

    const handleSendMessage = async () => {
        if (!userInput.trim()) return;
        setLoading(true);

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

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    return (
        <div className="chatbot-container">
            <div className="chatbot-header">
                <h2>Bridge Chatbot</h2>
            </div>
            <div className="chatbot-messages">
                {messages.map((msg, index) => (
                    <div key={index} className={`chatbot-message ${msg.role}`}>
                        <strong>{msg.role === "user" ? "You:" : "Bridge:"}</strong>
                        <p>{msg.content}</p>
                    </div>
                ))}
                {loading && <p className="chatbot-writing">Scrivendo...</p>}
                <div ref={messagesEndRef} />
            </div>
            <div className="chatbot-input">
                <input
                    type="text"
                    value={userInput}
                    onChange={(e) => setUserInput(e.target.value)}
                    onKeyDown={(e) => {
                        if (e.key === "Enter") {
                            handleSendMessage();
                        }
                    }}
                    placeholder="Scrivi un messaggio"
                />

                <button onClick={handleSendMessage} disabled={loading}>
                    Invia
                </button>
            </div>
        </div>
    );
};

export default Chatbot;