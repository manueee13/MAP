package com.manueee.systembreach.util.commands;

import com.manueee.systembreach.model.FileSystem;
import com.manueee.systembreach.model.Terminal;
import com.manueee.systembreach.model.GameState;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe che implementa tutti i comandi disponibili nel gioco.
 * Ogni comando è implementato come metodo statico che manipola lo stato del gioco.
 */
public final class Commands {
    
    private Commands() {
        // Utility class
    }

    /**
     * Stamnpa l'errore di comando non valido.
     * @return l'errore di comando non valido
     */
    public static String invalidCommand() {
        return "Non esiste nessun comando con questo nome, digita 'list' per l'elenco dei comandi disponibili";
    }

    /**
     * Stampa la lista comandi disponibili.
     * @return la lista dei comandi disponibili
     */
    public static String listCommand(GameState gameState) {
        return Stream.of(EnumCommands.values())
                .filter(cmd -> gameState.isCommandAvailable(cmd))
                .map(EnumCommands::getCommand)
                .filter(cmd -> !cmd.isEmpty()) // Filtra INVALID
                .collect(Collectors.joining(" "));
    }

    public static String lsCommand(FileSystem fs, String path) {
        return fs.ls(path);
    }

    public static String cdCommand(FileSystem fs, String path) {
        if (path == null || path.trim().isEmpty()) {
            return "cd: missing operand";
        }
        String result = fs.cd(path);
        switch (result) {
            case "NOEXIST":
                return "cd: " + path + ": No such file or directory";
            case "NOTDIR":
                return "cd: " + path + ": Not a directory";
            case "OK":
                return "";
            default:
                return "cd: unknown error";
        }
    }

    public static String catCommand(GameState gameState, String path) {
        FileSystem fs = gameState.getFileSystem();

        if (path == null || path.trim().isEmpty()) {
            return "cat: missing operand";
        }

        if (path.equals("documentation.txt") && gameState.getCurrentQuestId() == 2) {
            gameState.advanceQuest();
        }

        String result = fs.cat(path);
        switch (result) {
            case "NOEXIST":
                return "cat: " + path + ": No such file or directory";
            case "NOTFILE":
                return "cat: " + path + ": Is a directory";
            default:
                return result;
        }

    }

    public static void clearCommand(Terminal terminal) {
        terminal.clearOutputBuffer();
    }

    public static String manualCommand(String cmd) {
        if (cmd == null || cmd.trim().isEmpty()) {
            StringBuilder result = new StringBuilder();
            Stream.of(EnumCommands.values()).filter(command -> !command.equals(EnumCommands.INVALID)).forEach(command -> result.append(command.getCommandInfo()));
        return result.toString();
        }

        EnumCommands command = EnumCommands.fromString(cmd);
        if (cmd.equals(EnumCommands.INVALID.toString())) {
            return "man: " + cmd + ": No manual entry or invalid command";
        }

        return command.getCommandInfo();
    }

    /**
     * Comando per decifrare file zip protetti da password.
     * @param gameState lo stato corrente del gioco
     * @param args gli argomenti del comando
     * @return il risultato dell'operazione
     */
    public static String decryptCommand(GameState gameState, String args) {
        FileSystem fs = gameState.getFileSystem();

        if (args == null || args.trim().isEmpty()) {
            return EnumCommands.DECRYPT.getCommandInfo();
        }
        
        String [] params = args.trim().split("\\s+");
        if (params.length < 1) {
            return "fcrackzip: missing operand";
        }

        boolean isBruteForce = false;
        String dictionaryPath = null;
        String archivePath = null;

        for (int i = 0; i < params.length; i++) {
            switch (params[i]) {
                case "-b":
                    isBruteForce = true;
                    break;
                case "-D":
                    if (i + 1 < params.length) {
                        dictionaryPath = params[++i];
                    } else {
                        return "fcrackzip: missing dictionary path";
                    }
                    break;
                default:
                    if (params[i].endsWith(".zip")) {
                        archivePath = params[i];
                    } else {
                        return "fcrackzip: invalid option or file not recognized";
                    }
            }
        }

        // Check archivio e dizionario esistono
        if (archivePath == null) {
            return "fcrackzip: missing archive path";
        }
        
        String archiveNode = fs.getNode(archivePath);

        if (archiveNode == null && !archivePath.equals("docs.zip")) {
            return "fcrackzip: invalid archive";
        }

        if (isBruteForce) {
            return "> L'attacco bruteforce richiede troppo tempo, devo trovare un'altra alternativa...";
        }

        if (dictionaryPath != null) {
            String dictionaryNode = fs.getNode(dictionaryPath);
            if (dictionaryNode == null && !dictionaryPath.equals("dictionary.txt")) {
                return "fcrackzip: illegal dictionary file";
            }

            int currentQuestId = gameState.getCurrentQuestId();
            if (currentQuestId == 1) {
                gameState.advanceQuest();
                gameState.unlockCommand(EnumCommands.SQLINJECT);
                gameState.unlockCommand(EnumCommands.CURL);
                fs.createDirectory("/mnt/sda1/docs");
                fs.createFile(
                    "/mnt/sda1/docs/documentation.txt",
                    "...\n...\nIN CASO DI ESECUZIONE INVOLONTARIA DEL PROGRAMMA COLLEGARSI AL SITO ONION 'http://phantomorganization7xw3v.onion/program/SB013' E INSERIRE LE CREDENZIALI FORNITE."
                );
                return "fcrackzip: archive successfully decrypted. The content is allocated in the current directory.\n\n> C'è l'ho fatta! Ora devo leggere la documentazione!";
            }
            
        }
        
        return "fcrackzip: unknown error";    
    }

    /**
     * Comando per effettuare richieste HTTP.
     * @param gameState lo stato corrente del gioco
     * @param args gli argomenti del comando
     * @return il risultato dell'operazione
     */
    public static String curlCommand(GameState gameState, String args) {
        if (args == null || args.trim().isEmpty()) {
            return EnumCommands.CURL.getCommandInfo();
        }

        String[] params = args.trim().split("\\s+");
        String regex = "http://[^/]+\\.[^/]+";

        if (params.length >= 2 && params[0].equals("-u")) {
            if (params[1].equals("http://phantomorganization7xw3v.onion/program/SB013")) {
                if (params.length >= 3 && params[2].equals("-o")) {
                    if (params.length >= 4 && !params[3].equals("-d")) {
                        if (params[4].equals("username=us378&password=v93g@1!mv")) {
                            gameState.getFileSystem().createFile("./", "SB013_debloat");
                            gameState.advanceQuest();
                            return "curl: download successful in the current directory.";
                        } else {
                            return "403 FORBIDDEN";
                        }
                    }
                }
                else if (params.length >= 3 && params[2].equals("-d")) {
                    if (params[3].equals("username=us378&password=v93g@1!mv")) {
                        return "> è entrato ma non ha fatto nulla... forse dovrei provare a scaricarlo";
                    } else {
                        return "403 FORBIDDEN";
                    }
                } else {
                    return "curl: illegal operation";
                }
            } else if (params[1].equals("http://phantomorganization7xw3v.onion")) {
                return "> è il sito dell'organizzazione, non posso fare nulla qui...";
            } else if (params[1].matches(regex)) {
                return "> Devo concentrarmi sul sito dell'organizzazione...";
            } else {
                return "curl: illegal URL";
            }
        }
        return "curl: illegal operation";
    }

    /**
     * Esegue il comando di SQL injection
     * @param args argomenti del comando
     * @return risultato dell'esecuzione
     */
    public static String sqlinjectCommand(String args) {
        if (args == null || args.trim().isEmpty()) {
            return EnumCommands.SQLINJECT.getCommandInfo();
        }

        String[] params = args.trim().split("\\s+");

        // Controllo URL
        if (params.length == 2 && params[0].equals("-u")) {
            if (!"http://phantomorganization7xw3v.onion".equals(params[1])) {
                return "> Forse dovrei concentrarmi sul sito dell'organizzazione...";
            } else {
                return "sqlmap: this site is vulnerable to SQL injection.";
            }
        }
    
        // Enumerazione database
        if (params.length >= 3 && params[0].equals("-u")) {
            if (params[2].equals("--dbs")) {
                return "sqlmap:\n\n[0] databases\n[1] information_schema\n[2] SB013\n\n";
            }
    
            // Enumerazione tabelle
            if (params[2].equals("-D") && params.length >= 4) {
                String dbName = params[3];
                if (dbName.equals("SB013")) {
                    if (params.length >= 5 && params[4].equals("--tables")) {
                        return "sqlmap:\n\n[0] CLIENT\n[1] USERS\n[2] PAYMENT\n\n";
                    }

                    // Estrazione dati tabella
                    if (params.length >= 6 && params[4].equals("-T")) {
                        String tableName = params[5];
                        switch (tableName) {
                            case "USERS":
                                return formatTableResult(
                                    "USERS",
                                    new String[]{"username", "password"},
                                    new String[][]{{"us378", "v93g@1!mv"}}
                                );
                            case "CLIENT":
                                return "> Non avevo dubbi che dietro ai clienti ci fosserò organizzazioni governative...comunque non è la tabella con le credenziali.";
                            case "PAYMENT":
                                return "> Guarda che cifre, mi chiedo come faranno a cambiare tutti quei bitcoin...comunque non è questa la tabella giusta.";
                            default:
                                return "sqlmap: unknown table";
                        }
                    }
                } else if (dbName.equals("information_schema") || dbName.equals("databases")) {
                    return "> Questo database non ha le tabelle che mi servono...";
                } else {
                    return "sqlmap: unknown database";
                }
            }
        }
        
        return "sqlmap: illegal operation";
    }

    /**
     * Formatta il risultato della query in una tabella ASCII
     */
    private static String formatTableResult(String tableName, String[] headers, String[][] data) {
        StringBuilder result = new StringBuilder();
        result.append("sqlmap: ").append(tableName).append(":\n");
        result.append("+-----------+-----------+\n");
        result.append("| ").append(String.join(" | ", headers)).append(" |\n");
        result.append("+-----------+-----------+\n");
        
        for (String[] row : data) {
            result.append("| ").append(String.join(" | ", row)).append(" |\n");
        }
        result.append("+-----------+-----------+");
        
        return result.toString();
    }

    public static String reverseCommand() {
        // TODO
        return "objdump";
    }
}
