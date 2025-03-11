
# Organigramma Aziendale


Di seguito viene fornita una descrizione del prototipo realizzato, che costituisce un’applicazione completa per la creazione e gestione 
di un organigramma aziendale. Le funzionalità descritte sono progettate per rispondere ai requisiti funzionali e non funzionali definiti 
in fase di progettazione.

# Descrizione

L’applicazione consente di caricare un organigramma di esempio tramite l’opzione “Carica Organigramma”, all’avvio viene mostrata una 
prima unità radice chiamata “Azienda”

# Visualizzazione

Nel pannello centrale, le unità aziendali sono rappresentate come rettangoli, collegati graficamente alle relative sotto-unità con segmenti.
Questa modalità mostra solo le unità, senza rappresentare gli impiegati (visualizzabili cliccando su “visualizza info unità”), e si aggiorna 
automaticamente in base alle modifiche apportate (inserimento, rimozione di unità).


Barra degli Strumenti (a sinistra)
   La barra degli strumenti offre i seguenti comandi di gestione dell’organigramma:
   
   - Modifica Nome Unità: Modifica il nome di un'unità esistente.
   - Aggiungi Sotto Unità: Definisce una nuova sotto-unità per un’unità selezionata.
   - Rimuovi Unità: Rimuove un'unità selezionata (a meno che non sia la radice).
   - Aggiungi Dipendente ad Unità: Consente di creare un nuovo impiegato da assegnare alle unità potendo scegliere ruoli ammissibili specifici
   - Crea Ruolo Ammissibile: Definisce un nuovo ruolo ammissibile per l’unità selezionata, che può essere assegnato ai dipendenti.

Pannello Laterale (a destra)
Il pannello laterale include ulteriori opzioni per la visualizzazione e la gestione avanzata dell’organigramma:

-Visualizza info Unità: Si apre un pannello in cui dipendentemente dall’unità selezionata sono disponibili tutti i dipendenti
 per quella specifica unità con i rispettivi ruoli ed anche una lista dei ruoli ammissibili per quella unità. Nota che di fianco ciascun 
 dipendente sono presenti dei bottoni per la visualizzazione delle rispettive informazione, modifica ed eventuale rimozione. Discorso analogo
 per i ruoli ammissibili.
-Gestisci Tutti i Dipendenti: Si apre un pannello che consente la visualizzazione di tutti i dipendenti presenti nell'organigramma in ordine
alfabetico. Sotto ognuno di essi è possibile visualizzare tutte le unità a cui appartiene il dipendente col rispettivo ruolo. È anche
possibile modificare i dati dei dipendenti e licenziare un dipendente. Licenziandolo verrà rimosso da tutte le unità presenti nell'organigramma.
-Salva Organigramma: Permette di salvare l’organigramma corrente su, da poter ricaricare successivamente.
-Carica Organigramma: Consente di ricaricare un organigramma precedentemente salvato.
-Azzera Organigramma: Ripristina l’organigramma allo stato iniziale.
