# Bridge | CONTRIBUTING RULES

## Commits

Abbiamo regole molto precise su come possono essere formattati i messaggi di commit di git. Questo porta a messaggi più leggibili e facili da seguire quando si consulta la cronologia del progetto.
Seguiamo le specifiche descritte da [Conventional Commits](https://www.conventionalcommits.org/).

Quanto segue è tratto e modificato da https://github.com/angular/angular/blob/22b96b9/CONTRIBUTING.md#-commit-message-guidelines

### Formato del messaggio di commit

Ogni messaggio di commit consiste in un `header` e un `body`. L'intestazione ha un formato speciale che include un `type`, uno `scope` e un `sommario`:
Ogni riga del messaggio di commit non può essere più lunga di 100 caratteri! Questo permette al messaggio di essere più facile da leggere su GitHub e nei vari strumenti di git.

```
<type>(<scope>): <sommario>
<LINEA VUOTA>
<body>
```

### Intestazione

L'**intestazione** è obbligatoria e il **campo** dell'intestazione deve essere omesso se la modifica è comune tra i pacchetti, altrimenti è obbligatorio. Usare il campo sommario per fornire una descrizione sintetica della modifica:

```
<type>(<scope>): <sommario>
  │ │ │
  │ │ └─⫸ Sintesi al presente. Non in maiuscolo. Senza punto alla fine.
  │ │
  │ └─⫸ Commit scope: backend | frontend
  │
  └─⫸ Tipo di commit: build|ci|docs|feat|fix|perf|refactor|test
```

#### Riassunto

- usare l'imperativo, il tempo presente: "cambia", non "cambiato"
- non scrivere in maiuscolo la prima lettera
- non mettere il punto (.) alla fine

#### Type

Deve essere uno dei seguenti:

- build: Modifiche che riguardano il sistema di compilazione o le dipendenze esterne.
- ci: modifiche ai file di configurazione e agli script di CI
- docs: Modifiche alla sola documentazione
- feat: Una nuova funzionalità
- fix: Una correzione di bug
- perf: Una modifica al codice che migliora le prestazioni
- refactor: Una modifica del codice che non corregge un bug né aggiunge una funzionalità
- style: Modifiche che non influiscono sul significato del codice (spazi bianchi, formattazione, punti mancanti, ecc.).
- test: Aggiunta di test mancanti o correzione di test esistenti

### Scope

Lo scope (ambito) deve essere il `package` all'interno del quale avviene la modifica.

Ambiti supportati:

- `backend`
- `frontend`
- `front&back`

### Body

Il **body** NON è obbligatorio per tutti i commit. Quando il corpo è presente, deve essere lungo almeno 20 caratteri e deve essere conforme al formato Commit Message Body.

\*\*Come nel sommario, usare l'imperativo.

Spiegare la motivazione della modifica nel corpo del messaggio di commit. Questo messaggio di commit deve spiegare perché si sta apportando la modifica. È possibile includere un confronto tra il comportamento precedente e quello nuovo per illustrare l'impatto della modifica.

## Branches

I nuovi branches devono seguire questo formato per i loro nomi: `<feat>/<descrizione>`.

- `feat` si riferisce alla funzionalità a cui si sta lavorando.
- `descrizione` la descrizione indica su quale ambito si sta lavorando (frontend | backend | front&back) e contenere informazioni che si reputano necessarie.

esempio: registrazione/front&back
