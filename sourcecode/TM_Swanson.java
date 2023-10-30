import java.util.ArrayList;

public class TM_Swanson extends TM_Story {
    
    public TM_Swanson(TM_MainGame game) {
        super(game);
    }
    
    @Override
    public boolean checkResponse(String response) throws InterruptedException {
        switch (currentStep) {
        case 0: // Send entry about swanson
            return checkAnswer(response.toLowerCase(), "msw88", currentStep + 1);
        case 1: // Send mission briefing
            return checkAnswer(response.toLowerCase(), "c12e105", currentStep + 1);
        case 2: // Found graph with locked access
            return checkAnswer(response.toLowerCase(), "gh8821529", currentStep + 1);
        case 3: // Reply Van Do
            if (checkAnswer(response.toLowerCase(), "van do", currentStep + 1) || checkAnswer(response.toLowerCase(), "vdo", currentStep + 1) || checkAnswer(response.toLowerCase(), "vdo885", currentStep + 1)) {
                return true;
            }
            return false;
        case 4: // Log out
            return false;
        default:
            return false;
        }
    }

    @Override
    protected void setDatabase() {
        database = new ArrayList<String>();
        setEntry("Sewers Incursion", "c12e105", "Location: Sewers entrance 105, Dark Harvest;"
                + "Mission: Investigate the increased demon appearance within the Sewers area.;"
                + "Assigned: 6 days ago;"
                + "Data received: gh8821529");
        setEntry("Sewers", "sww", "Leftover of the waste processing system connected to Dark Harvest from outside.;"
                + "Entrances to the system are either sealed off completely or closed off with guard posts");
        setEntry("GH8821529 Report", "gh8821529", "ACCESS DENIED.;"
                + "Require Clearance Level 2;"
                + "Author: Van Do");
        setEntry("Van Do", "vdo885", "Researcher;"
                + "ID: vdo;"
                + "Affiliation: Fleming Pharmaceutical;"
                + "Specializes in tracking Void Incursions;"
                + "Current contract: Heisenberg;"
                + "Current location: HARPER Facility");
        setEntry("Martin Swanson", "msw88", "Field Agent;"
                + "ID: msw;"
                + "Affiliation: Heisenberg;"
                + "Current mission: c12e105;"
                + "Current location: UNKNOWN");
        setEntry("Swanson", "msw88", "Field Agent;"
                + "ID: msw;"
                + "Affiliation: Heisenberg;"
                + "Current mission: c12e105;"
                + "Current location: UNKNOWN");
    }

    @Override
    protected void setMessages() {
        messages = new String[5];
        messages[0] = "Welcome back, Rookie.;"
                + "You now have access to agent Swanson's account and database.;"
                + "Download their database to your system, and we shall begin our investigation.;"
                + "While you're there, send me information about agent Swanson from their database.";
        messages[1] = "Received. Agent Swanson’s mission briefing is c12e105.;"
                + "That might still be in the database, try pulling it up.;"
                + "Send me the latest search result.";
        messages[2] = "Interesting.;"
                + "Looks like whatever mission Swanson was on should have been a quick one.;"
                + "They have been MIA for over 3 days now.;"
                + "Well, try to trace that database and see if you can get anywhere with it.;"
                + "Maybe we will find what they are after.";
        messages[3] = "High security clearance required?;"
                + "Maybe Swanson had external clearance access from someone else.;"
                + "Look around, any names stick out to you?";
        messages[4] = "Van Do. Fleming Researcher, specialized in tracking Void Incursions.;"
                + "Give me a moment…;"
                + "Alright, she gave us her login information.;"
                + "Try logging in with her account.;"
                + "Account credential: vdo188125"; 
    }

}
