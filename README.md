# System Breach

## Introduzione
**System Breach** è un'avventura grafica/testuale sviluppata in Java, pensata per immergere il giocatore in un contesto di alta tensione e intrighi tecnologici. Il giocatore veste i panni di un hacker che deve sventare un attacco informatico mortale, esplorando file crittografati, risolvendo enigmi e sfidando il tempo.

---

## Storia del Gioco

### 1. L'hard disk misterioso
L'hacker entra in possesso di alcuni vecchi hard disk provenienti da organizzazioni segrete. Questi dispositivi contengono informazioni che potrebbero rivelarsi preziose. Una volta connesso l'hard disk, l'hacker scopre che un malware si attiva automaticamente, progettato per attaccare una centrale nucleare.

### 2. La scoperta del malware
Il malware ha un timer di 30 minuti, al termine del quale disabiliterà i sistemi di sicurezza della centrale, provocando un disastro. L'obiettivo è fermare il malware esplorando l'hard disk, decifrando i file e scoprendo informazioni cruciali per neutralizzarlo.

### 3. Obiettivo principale
L'hacker deve fermare l'attacco, risolvendo enigmi e superando ostacoli tecnici. Durante l'esplorazione, emergono indizi sull'organizzazione responsabile del malware, che potrebbero avere legami con entità politiche o militari.

---

## Funzionalità del Gioco

1. **Minigiochi e enigmi**: Decodifica password, analisi di codice e bypass di sistemi di sicurezza.
2. **Sistema di progressione**: Nuove aree dell'hard disk si sbloccano man mano che vengono scoperti file chiave.
3. **Countdown e tensione**: Un timer costante aumenta la pressione per completare gli obiettivi.
4. **Finale**:
   - Successo: Il malware viene fermato, la centrale salvata, e viene suggerito un possibile sequel.
   - Fallimento: La centrale subisce un danno irreversibile, con conseguenze drammatiche.

---

## Struttura del Progetto

**System Breach** è stato progettato seguendo l'architettura MVC (Model-View-Controller) per garantire modularità e scalabilità.

### Diagramma delle Classi
Il diagramma rappresenta una parte significativa del codice, evidenziando le principali entità coinvolte:
- **Model**: Gestione dei dati, inclusi file e informazioni sull'hard disk.
- **View**: Interfaccia grafica realizzata con Swing.
- **Controller**: Logica di gioco e coordinamento tra Model e View.

![Diagramma delle Classi](./docs/class_diagram.png)

### Specifica Algebrica

WIP

### Tecnologie Utilizzate

- **File**: Salvataggio della partita in corso
- **JDBC**: Utilizzo del database H2 come parte di una sfida di SQL Injection
- **Socket**: ?
- **Lambda Expressions**: Snellimento del codice e gestione delle funzioni.
- **Thread**: Implementazione del timer e di operazioni asincrone.
- **Swing**: Creazione dell'interfaccia grafica.

---

## Requisiti

- **Java**: Versione 8 o superiore.
- **Maven**: Per la gestione delle dipendenze.
- **H2 Database**: Inclusione automatica tramite Maven.

---

## Installazione

1. Clonare la repository:
   ```bash
   git clone https://github.com/username/system-breach.git
   ```
2. Eseguire il comando Maven:
   ```bash
   mvn clean install
   ```
3. Avviare il gioco:
   ```bash
   mvn exec:java
   ```

---

## Documentazione

- La documentazione Javadoc è inclusa nella cartella `docs`.
- Per ulteriori dettagli, consultare il file `DOCUMENTATION.md` nella repository.

---

## Crediti

Progetto realizzato da:


---

## Licenza

Questo progetto è distribuito sotto la licenza MIT. Consultare il file `LICENSE` per maggiori dettagli.
