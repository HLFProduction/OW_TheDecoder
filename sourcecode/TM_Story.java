import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class TM_Story {
    // Template for a story segment
    // Each story segment is separated by the log-in person
    protected TM_MainGame game; // Contains method for encoding;
    protected int currentStep;
    protected String[] messages;
    
    protected ArrayList<String> database;
    
    boolean databaseInstalled = false;
    
    public TM_Story(TM_MainGame game) {
        currentStep = 0;
        this.game = game;
        setMessages();
        setDatabase();
    }
    
    // Return next message from handler to be displayed
    public String nextMessage() {
        if (currentStep < messages.length) {
            return messages[currentStep];
        } else {
            return "";
        }
    }
    
    // Method for checking responses and going to the assigned step
    // Specific to each story, use switch to process responses with each step
    public abstract boolean checkResponse(String response) throws InterruptedException; 

    protected boolean checkAnswer(String response, String answer, int next) throws InterruptedException {
        if (response.equals(answer)) {
            currentStep = next;
            return true;
        } else {
            game.printMsgLn("Invalid response. Try again.");
            return false;
        }
    }
    
    protected abstract void setMessages();
    
    // Setup account-based database
    protected abstract void setDatabase();
    
    // Set an entry with all info.
    protected void setEntry(String name, String id, String info) {
        database.add(name.toUpperCase() + "~" + id.toLowerCase() + "~" + info);
    }
    
    public ArrayList<String> getDatabase() {
        if (databaseInstalled) {
            return null;
        }
        return database;
    }
    
}
