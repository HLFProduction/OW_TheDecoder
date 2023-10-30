import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.io.File; // Only for reading

public class TM_MainGame {
    private Scanner kb;
    
    private final String[] morseCode = { 
            ".-", // A
            "-...", // B
            "-.-.", // C
            "-..", // D
            ".", // E
            "..-.", // F
            "--.", // G
            "....", // H
            "..", // I
            ".---", // J
            "-.-", // K
            ".-..", // L
            "--", // M
            "-.", // N
            "---", // O
            ".--.", // P
            "--.-", // Q
            ".-.", // R
            "...", // S
            "-", // T
            "..-", // U
            "...-", // V
            ".--", // W
            "-..-", // X
            "-.--", // Y
            "--..", // Z
            
    };

    private HashMap<String, String> databaseID = new HashMap<>(); // This database holds id for keywords
    private HashMap<String, String> databaseInfo = new HashMap<>(); // This database holds information for id
    
    private char blackSquare = '\u25A0';

    private Map<String, Character> morseToChar = new HashMap<String, Character>();

    private String currentMessage;
    
    private TM_Story account;
    
    private boolean playing = false;
    
    private boolean[] installedDatabase = new boolean[5];

    private boolean[] finished = new boolean[3];
    
    // Game variables
    private static int SEARCH_ID_WAITTIME = 300;
    private static int SEARCH_DATA_WAITTIME = 500;
    private static int INSTALL_WAITTIME = 600;
    private static int LETTER_WAITTIME = 20;
    private static int MESSAGE_LINE_WAITTIME = 100;
    
    private static String UNKNOWN_COMMAND_ERROR = "Error: UNKNOWN COMMAND. Refer to manual for a list of commands.";
    private static String INCORRECT_SYNTAX_ERROR = "Error: SYNTAX ERROR. Refer to manual for command syntax.";
    private static String CANNOT_TRANSPOSE_ERROR = "ERROR: TRANSPOSE ERROR. Cannot transpose Morse code.";
    
    public TM_MainGame() throws InterruptedException {
        if (!setup()) {
            printMsgLn("Error setting up the game. Try again!");
        } else {
            playing = true;
        }
        
        while (playing) {
            if (account == null) {
                // Log in and select chapter
                login();
            } else {
                // Play
                play();
            }
            
        }
        kb.close();
    }
    
    // Setup the game
    private boolean setup() throws InterruptedException {
        // Assigning a morse code map
        for (int i = 0; i < morseCode.length; i++) {
            morseToChar.put(morseCode[i], (char) (i + 65));
        }
        
        // Set up input
        kb = new Scanner(System.in);
        return true;
    }
    
    // Login to an account. Acts as chapters
    private void login() throws InterruptedException {
        printMsg("Welcome. Please enter your credentials to continue: ");
        String cred = kb.nextLine();
        printMsg("Logging in");
        printDelayed("...", LETTER_WAITTIME * 15);
        System.out.println("");
        switch (cred.toLowerCase()) {
        case "martinss99286":
            // Martin Swanson
            printMsgLn("Logged in as: Martin Swanson");
            account = new TM_Swanson(this);
            break;
        case "vdo188125":
            // Van Do
            printMsgLn("Logged in as: Van Do");
            account = new TM_VanDo(this);
            break;
        case "quit":
            printMsgLn("Goodbye.");
            playing = false;
            break;
        case "rookie":
            printMsgLn("Logged in as: Rookie");
            account = new TM_Tutorial(this);
        default:
            // Rookie
            if (finished[0]) {
                printMsgLn("Incorrect login credentials. Try again.");
                printMsgLn("If you want to revisit the tutorial again, login as rookie");
            } else {
                printMsgLn("Inccorrect login credentials. Starting as: Rookie");
                account = new TM_Tutorial(this);
            }
            break;
        }
        System.out.println();
    }
    
    // Play the game
    private boolean play() throws InterruptedException {
        // This is a game loop
        printMsg("Incoming Transmission: ");
        printDelayed("...", LETTER_WAITTIME * 15);
        System.out.println();
        String msg = account.nextMessage();
        currentMessage = msg;
        printMultiMsg(msg);
        
        boolean awaitingResponse = true;
        while (awaitingResponse) {
            printMsg("Action? ");
            String response = kb.nextLine();
            awaitingResponse = !checkAction(response);
            System.out.println();
        }
        return true;
    }
    
    // Determine action executed
    private boolean checkAction(String response) throws InterruptedException {
        // Cross check with a list of possible commands
        // Clean responses and eliminate nulls
        response.trim();
        if (response.length() < 4) {
            printMsgLn(UNKNOWN_COMMAND_ERROR);
            return false;
        }
        response = response.toLowerCase();
        switch (response.substring(0, 4)) {
        case "help":
            printMsgLn("List of possible commands:");
            printMsgLn("rep : Reply to handler");
            printMsgLn("sid : Search for ID using a keyword");
            printMsgLn("src : Search for information using a keyword or ID");
            printMsgLn("dcr : Decrypt the message from handler");
            printMsgLn("ins : Install a database");
            printMsgLn("list: list all available entry IDs");
            printMsgLn("quit: Logout of the current account");
            printMsgLn("Refer to the manual for information about each command.");
            return false;
        case "rep ": // Communicate with handler
            return respond(response.substring(4).trim());
        case "sid ": // Search id
            searchID(response.substring(4).trim());
            return false;
        case "src ": // Search data
            searchData(response.substring(4).trim());
            return false;
        /*case "ecr ": // Encrypt
            printMsg("Encryption: " + encrypt(response.substring(4).trim()));
            return false;*/
        case "dcr ": // Decrypt
            printMsg("Decryption: " + decrypt(response.substring(4).trim()));
            return false;
        case "ins ": // Install
            install(response.substring(4).trim());
            return false;
        case "list": // List all databases
            listData();
            return false;
        case "quit":
            account = null;
            return true;
        default:
            printMsgLn(UNKNOWN_COMMAND_ERROR);
            return false;
        }
    }
    
    // Install a database
    private void install(String database) throws InterruptedException {
        switch (database) {
        case "self": //Install account database
            ArrayList<String> datas = account.getDatabase();
            if (datas == null || datas.size() < 0) {
                printMsgLn("Account database was installed. No new entries added.");
            } else {
                for (String data : datas) {
                    String[] dataSegment = data.split("~");
                    // No check, since data entered to database is in prod
                    databaseID.put(dataSegment[0], dataSegment[1]);
                    databaseInfo.put(dataSegment[1], dataSegment[2]);
                }
                printMsgLn("Account database has been installed. " + datas.size() + " new entries added");
            }
            break;
        case "b1528d2": // Base database
            printMsg("Database " + database);
            Thread.sleep(INSTALL_WAITTIME);
            if (installedDatabase[0]) {
                printMsgLn(" was installed. No new entries added");
            }
            else {
                installedDatabase[0] = true;
                loadData("ALBERT", "alb3", "Handler;ID: doejuw;Affiliation: Heisenberg");   
                loadData("FOUNDATION", "fnd", "A bastion city, far remote from the busiest cities of the Golden Age. It has the resources to remain completely self-sustainable, as well as having multiple levels of defenses. The foundation was contracted by the Council to Heisenberg Corporation as a fallback from the potential collapse of the Golden Age.");
                loadData("DARK HARVEST", "dhv", "The mine spans deep below the Foundation. It is filled with numerous resources and minerals, as well as storing giant harvesters to gather dark matter from the Void. The name, Dark Harvest, originated from these harvesters.");
                loadData("Energy World", "ewi", "A world filled with energy.");
                loadData("Industria", "ewi");
                loadData("Void World", "vwi", "A world devoid of energy.");
                loadData("Void", "wvi");
                loadData("Inanis", "vwi");
                loadData("Golden Age", "gea", "The age of technological advancement, where humanity continuously harvests dark matter as a resource to create machines, artifacts, medicines, etc. The Golden Age world was ruled over by the Council.");
                loadData("Heisenberg", "heis", "Robotics corporation, formed early during the Golden Age. Specializing in technology and robotics, Heisenberg was manufacturing multiple different mechanical systems and engines such as the harvester, as well as operating as independent contract military organization.");
                loadData("Fleming", "flmg", "Traditional, family formed organization formed during the Primitive Era. Specializing in pharmaceutical and smithing, Fleming was the leading pharmaceutical company during the Golden Age.");
                loadData("Phoenix", "phnx", "An order of people capable of wielding the Void’s power, populated by magi and templar");
                
                loadData("Orb Walker", "ow", "Orb Walkers are the most skillful at handling their Void power. They are capable of shaping volatile dark matter into their own power sources as well as manipulating the battlefield in their favor.");
                loadData("Orb Walkers", "ow");
                loadData("Illac Mori", "icm", "Once a member of the Council and one of the most powerful Seekers, Iliac Mori was also known as the merciless Reaper.");
                loadData("Reaper", "icm");
                loadData("Seeker", "seek", "Special agents of the Council whose task is to seek out any threats to the Golden Age, especially any threats related to the Void, and neutralize it quietly.");
                loadData("Seekers", "seek");
                loadData("Dark matter", "dmt", "A formless resource gathered from the Void, dark matter is an extremely potent conductive material, capable of absorbing energy from almost all sources including thermal energy from its surroundings. It reacts unpredictably when in contact with organic matter, sometimes granting the person touching it power to create Void tears and wield it as a power.");
                printMsgLn(" installed successfully. 13 new entries added.");
            }
            break;
        default:
            break;
        }
    }
    
    // Add to the database
    private boolean loadData(String name, String id, String info) {
        name = name.toUpperCase();
        boolean installed = databaseInfo.containsKey(id);
        id = id.toLowerCase();
        databaseID.put(name, id);
        databaseInfo.put(id, info);
        return installed;
    }
    
    // Add name matched with id. Used for available id
    private boolean loadData(String name, String id) {
        if (!databaseID.containsKey(id)) {
            return false;
        } else {
            name = name.toUpperCase();
            id = id.toLowerCase();
            databaseID.put(name, id);
            return true;
        }
    }
    
    // Encode info
    private String encrypt(String response) {
        if (response == null) {
            return currentMessage;
        }
        String[] responses = response.split("-");
        if (responses[0] != "" || responses[1] == null || responses[1] == "") {
            return INCORRECT_SYNTAX_ERROR;
        }
        switch (responses[1].charAt(0)) {
        case 'm':
            // decrypt from morse to english
            if (responses[1].charAt(1) != 'c') {
                return "Error, invalid command.";
            }
            currentMessage = toMorse(currentMessage);
            break;
            /*
        case 'c':
            // Change number
            if (currentMessage.trim().charAt(0) == '-' || currentMessage.trim().charAt(0) == blackSquare) {
                return CANNOT_TRANSPOSE_ERROR;
            }
            
            char[] chars = currentMessage.toCharArray();
            currentMessage = "";
            for (int i = 0; i < chars.length; i++) {
                currentMessage += (char) (chars[i] + 3);
            }
            break;*/
        }
        if (responses.length > 2) {
            return decrypt(response.substring(2));
        }
        return currentMessage;
    }
    
    // Decode info
    private String decrypt(String response) {
        // Decrypt the current message using rules in the response
        // Each pass applies 1 decryption
        if (response == null) {
            return currentMessage;
        }
        String[] responses = response.split("-");
        if (responses[0] != "" || responses[1] == null || responses[1] == "") {
            return INCORRECT_SYNTAX_ERROR;
        }
        switch (responses[1].charAt(0)) {
        case 'm':
            // decrypt from morse to english
            if (responses[1].charAt(1) != 'c') {
                return "Error, invalid command.";
            }
            currentMessage = toCharMessage(currentMessage);
            break;
        /*case 'c':
            // Change number
            if (currentMessage.trim().charAt(0) == '-' || currentMessage.trim().charAt(0) == blackSquare) {
                return CANNOT_TRANSPOSE_ERROR;
            }
            
            char[] chars = currentMessage.toCharArray();
            currentMessage = "";
            for (int i = 0; i < chars.length; i++) {
                currentMessage += (char) (chars[i] - 3);
            }
            break; */
        }
        if (responses.length > 2) {
            return decrypt(response.substring(2));
        }
        return currentMessage;
        
    }
    
    // Respond to handler
    private boolean respond(String response) throws InterruptedException {
        printMsg("Processing...");
        printMsgLn("Response sent.");
        return account.checkResponse(response);
    }
    
    // Search for id of a data entry
    private void searchID(String response) throws InterruptedException {
        // Look up the database and get a result
        // Return false if no result
        String result = databaseID.get(response.toUpperCase());
        if (result == null) {
            printMsg("No entry related to " + response);
        } else {
            printMsg(response.toUpperCase() + ": ");
            Thread.sleep(SEARCH_ID_WAITTIME);
            printMsgLn(result);
        }
    }
    
    // Search for information using name or id
    private void searchData(String response) throws InterruptedException {
        // Look up the database and get a result
        String result = databaseInfo.get(response.toLowerCase()); // Start with id database
        if (result == null) {
            // Try searching through 1 more layer
            result = databaseInfo.get(databaseID.get(response.toUpperCase()));
            if (result != null) {
                printMsg(response.toUpperCase() + ": ");
                Thread.sleep(SEARCH_DATA_WAITTIME);
                printMultiMsg(result);
            } else {
                printMsg("No entry related to " + response);
            }
        } else {
            printMsg(response + ": ");
            Thread.sleep(SEARCH_DATA_WAITTIME);
            printMultiMsg(result);
        }
    }
    
    // List all id from the database
    private void listData() throws InterruptedException {
        Set<String> keys = databaseInfo.keySet();
        for (String key : keys) {
            printMsgLn(key);
        }
    }
    
    // Print Morse code
    private void printMorse(String morse) throws InterruptedException {
        String printStr = ""; 
        char[] morseCode = morse.toCharArray();
        for (char morseChar : morseCode) {
            if (morseChar == '.') {
                printStr += blackSquare; // Dot
            } else if (morseChar == '-') {
                printStr += blackSquare + "" + blackSquare + "" + blackSquare; // Dash
            } else if (morseChar == ' ') {
                printStr += " "; // Space between letters
            } else if (morseChar == '_') {
                printStr += "   "; // Space between words
            }
            printStr += " ";
        }
        printMsgLn(printStr);
    }
    
    // Print any message
    private void printMsg(String msg) throws InterruptedException {
        int currentChar = 0;
        while (currentChar < msg.length()) {
            System.out.print(msg.charAt(currentChar));
            currentChar++;
            //wait(10);
            Thread.sleep(LETTER_WAITTIME);
        }
    }
    
    // Print any message then go to next line
    public void printMsgLn(String msg) throws InterruptedException {
        printMsg(msg);
        System.out.println("");
    }
    
    // Print multiple messages on multiple lines
    private void printMultiMsg(String msgs) throws InterruptedException {
        // Break down the string
        String[] messages = msgs.split(";");
        if (messages != null) {
            for (int i = 0; i < messages.length; i++) {
                printMsgLn(messages[i]);
                Thread.sleep(MESSAGE_LINE_WAITTIME);
            }
        } else {
            // Single line.
            printMsgLn(msgs);
        }
    }
    
    // Special method for printing a string with different delay
    private void printDelayed(String msg, int delay) throws InterruptedException {
        int currentChar = 0;
        while (currentChar < msg.length()) {
            System.out.print(msg.charAt(currentChar));
            currentChar++;
            //wait(10);
            Thread.sleep(delay);
        }
    }
    
    // Decrypt and Encrypt methods
    public String toMorse(String msg) {
        String encryptedMsg = "";
        msg = msg.toUpperCase();
        char[] msgLetters = msg.toCharArray();
        for (char c : msgLetters) {
            // Break up string into characters
            if (c == ' ') {
                encryptedMsg += "_";
            } else {
                // decrypt each character into morse.
                c -= 65; // Char can be represented as numbers (ASCII Format), so to find the letter just minus by a number
                encryptedMsg += morseCode[c] + " "; // From the letter, look up morse code and add to msg, with a space
            }
        }
        return encryptedMsg;
    }
    
    private String toChar(String morse) {
        // decrypt morse to character
        // Lookup the database
        try {
            return morseToChar.get(morse).toString();
        } catch (Exception e) {
            return "_";
        }
    }
    
    private String toCharMessage(String msg) {
        // Decrypt a morse msg into English
        // Use toChar to decrypt, this just splits the message up
        String[] segments = msg.split(" ");
        String decrypt = "";
        if (msg.length() < 1) {
            return "";
        }
        for (String segment : segments) {
            if (segment.charAt(0) == '_') {
                decrypt += " ";
                decrypt += toChar(segment.substring(1).trim());
            } else if (!segment.equals(" ")) {
                decrypt += toChar(segment.trim());
            }
        }
        return decrypt;
    }
}
