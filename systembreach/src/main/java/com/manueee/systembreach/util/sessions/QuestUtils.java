package com.manueee.systembreach.util.sessions;

import com.manueee.systembreach.model.Mail;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class per la gestione delle quest di gioco.
 * Parte del Controller nel pattern MVC, gestisce la logica di progressione
 * delle missioni e l'invio delle relative mail.
 */
public final class QuestUtils {
    
    private static final List<Quest> quests = new ArrayList<>();
    
    private QuestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Rappresenta una singola quest del gioco
     */
    public static class Quest {
        private final int id;
        private final Mail mail;

        public Quest(int id, Mail mail) {
            this.id = id;
            this.mail = mail;
        }

        // Getters
        public int getId() { return id; }
        public Mail getMail() { return mail; }
    }

    static {
        // Inizializzazione delle quest
        quests.add(new Quest(1, new Mail("m4t3@d4rkm41l.onion", 
            "Cr4k1ng z1P", 
            "E' un bel casino, cercherò di aiutarti.\nDovresti provare ad estreare l'archivio zip.\nSe è protetto con la password prova con 'fcrackzip', ma usa la modalità dizionario altrimenti ci impiegherà più dell'eta' dell'universo!")));
        
        quests.add(new Quest(2, new Mail("m4t3@d4rkm41l.onion", 
            "B3n F4tT0!", 
            "Non è era cosi difficile in fondo.\nOra vedi cosa c'è nella documentazione, magari riusciamo a trovare un modo per fermarlo.")));
        
        quests.add(new Quest(3, new Mail("m4t3@d4rkm41l.onion", 
            "SqL 1nJ3cT10n", 
            "Ho analizzato il sito e sembra ancora online.\nPer accedere ci servono le credenziali.\nL'unico modo è provare con un attacco di tipo SQL Injection.\nUna volta fatto usa il comando 'curl' per scaricare il programma.")));
        
        quests.add(new Quest(4, new Mail("m4t3@d4rkm41l.onion", 
            "4vV1aL0!", 
            "Ho identificato l'IP della centrale nucleare: 130.110.144.13.\nOra dobbiamo solo fermare il malware.")));
        
        quests.add(new Quest(5, new Mail("m4t3@d4rkm41l.onion", 
            "R3v3R53 3n63N33r1n6", 
            "Non ci voleva... Non sembra funzionare\nDobbiamo disassemblare il file è trovato la funzione che ferma il malware.\nDovresti avere 'objdump' installato.")));
        
        quests.add(new Quest(6, new Mail("m4t3@darkm41l.onion", 
            "PwN tH3 M4lW4r3", 
            "Non è rimasto molto tempo!\nIl malware ha generato un path nel sito: '/backend/fo49vm3nc092/SB013/'\nProva a far partire l'exploit tramite 'curl'.")));
    }

    /**
     * Recupera tutte le quest disponibili fino all'ID specificato
     * @param id ID massimo delle quest da recuperare
     * @return Lista immutabile delle quest disponibili
     */
    public static List<Quest> getQuests(int id) {
        return Collections.unmodifiableList(quests.subList(0, Math.min(id, quests.size())));
    }

    /**
     * Recupera la mail associata a una specifica quest
     * @param id ID della quest
     * @return Mail associata alla quest o null se non trovata
     */
    public static Mail getMail(int id) {
        return getQuestById(id).map(Quest::getMail).orElse(null);
    }

    /**
     * Recupera una quest specifica tramite ID
     * @param id ID della quest da recuperare
     * @return Optional contenente la quest se trovata
     */
    public static Optional<Quest> getQuestById(int id) {
        return quests.stream()
                    .filter(quest -> quest.getId() == id)
                    .findFirst();
    }
}
