package com.manueee.systembreach.util.sessions;

import com.manueee.systembreach.model.Mail;
import com.manueee.systembreach.model.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class QuestUtils {
    
    public static class Quest {
        private int id;
        private Mail mail;
        private Predicate<GameState> condition;

        public Quest(int id, Mail mail, Predicate<GameState> condition) {
            this.id = id;
            this.mail = mail;
            this.condition = condition;
        }

        public int getId() {
            return id;
        }

        public Mail getMail() {
            return mail;
        }

        public boolean isCompleted(GameState state) {
            return condition.test(state);
        }
    }

    private static List<Quest> quests = new ArrayList<>();
    //TODO: Settare correttamente le condizioni.
    static {
         // Quest 1-1: Cr4k1ng z1P
         quests.add(new Quest(
            1,
            new Mail("m4t3@d4rkm41l.onion", "Cr4k1ng z1P", "E' un bel casino, cercherò di aiutarti.\nDovresti provare ad estreare l'archivio zip.\nSe è protetto con la password prova con 'fcrackzip', ma usa la modalità dizionario altrimenti ci impiegherà più dell'eta' dell'universo!"),
            null//state -> state.hasExecutedCommand("frackzip -D -p /home/dictionary.txt docs.zip")
        ));
        
        // Quest 1-2: Leggi la docs
        quests.add(new Quest(
            2,
            new Mail("m4t3@d4rkm41l.onion", "B3n F4tT0!", "Non è stato cosi difficile in fondo.\nOra vedi cosa c'è nella documentazione, magari riusciamo a trovare un modo per fermarlo."),
            null//state -> state.hasExecutedCommand("cat /mnt/docs/documentation.txt")
        ));

        // Quest 2-1: SQL Injection
        quests.add(new Quest(
            3,
            new Mail("m4t3@d4rkm41l.onion", "SqL 1nJ3cT10n", "Ho analizzato il sito e sembra ancora online.\nPer accedere ci servono le credenziali.\nL'unico modo è provare con un attacco di tipo SQL Injection.\nUna volta fatto usa il comando 'curl' per scaricare il programma."),
            null//state -> state.hasExecutedCommand("curl -o " + path + "http://www.d33pst4t3org.onion/program")
        ));

        // Quest 3-1: Reverse Engineering
        quests.add(new Quest(
            4,
            new Mail("m4t3@d4rkm41l.onion", "R3v3R53 3n63N33r1n6", "Non ci voleva...\nDobbiamo fare reverse engineering e risalire la chiave di attivazione. "),
            null//state -> state.hasExecutedCommand("gdb -q /mnt/program") 
        ));
    }

    public QuestUtils() {
    }

    public static List<Quest> getQuests(int id) {
        return quests;
    }

    public static Mail getMail(int id) {
        return quests.stream()
        .filter(quest -> quest.getId() == id)
        .findFirst()
        .orElse(null)
        .getMail();
    }

    public static Quest getQuestById(int id) {
        return quests.stream()
        .filter(quest -> quest.getId() == id)
        .findFirst()
        .orElse(null);
    }

    public static boolean isQuestCompleted(Quest quest, GameState state) {
        return quest.isCompleted(state);
    }
}
