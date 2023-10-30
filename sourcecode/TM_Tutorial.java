
public class TM_Tutorial extends TM_Story {
    
    public TM_Tutorial(TM_MainGame game) {
        super(game);
    }
    
    public boolean checkResponse(String response) throws InterruptedException {
        switch (currentStep) {
        case 0: // not null and not empty
            if (response != null && !response.equals("")) {
                currentStep++;
                return true;
            } else {
                return false;
            }
        case 1: // ID attached to Albert's entry
            return checkAnswer(response.toLowerCase(), "doejuw", currentStep + 1);
        case 2: // ID of the package
            return checkAnswer(response.toLowerCase(), "alb3", currentStep + 1);
        case 3: // Decrypted message
            return checkAnswer(response.toLowerCase(), "skyburner", currentStep + 2);
        /*case 4: // Encrypted "Foundation" Skip this for now
            return checkAnswer(response.toLowerCase(), "foundation", currentStep + 1);*/
        case 5: // Encrypt database of Foundation
            return checkAnswer(response.toLowerCase(), "fnd-mc", currentStep + 1);
        case 6: // Logging out
            return false;
        default:
            return false;
        }
    }

    @Override
    protected void setDatabase() {
        // Void. This account has no database.
    }

    @Override
    protected void setMessages() {
        messages = new String[8];
        String encrypted = "Skyburner";
        encrypted = game.toMorse(encrypted);
        messages[0] = "Hello there. This is your handler, Albert Holmes.;"
                + "Refer to manual attached to the software for commands and responses, then send me a reply.";
        messages[1] = "Good, you're getting the hang of it. The software you're using, it's a modified version of the software we used in the field.;"
                + "It has full access to your personal database. Well, yours is probably empty right now, so let's get you initialized.;"
                + "Install package b1528d2.;"
                + "Afterwards, search for an entry on Albert and give me the attached id.";
        messages[2] = "Yup, that's correct. Alright, now let's talk about your role here.;"
                + "You are going to assist our detective team. Your job is to gather some information and send it directly to us.;"
                + "Let's try packaging databases. Package all information you have about me and send it to me.;"
                + "Again, refer to your manual on what you need to do";
        messages[3] = "Received. Of course, it's usually not that simple.;"
                + "Right now, we are communicating on a secured line so you can send things around like this.;"
                + "Our agents are in the field on unsecured lines, so you will also need to encrypt this information.;"
                + "But before that, let's go through decryption first. Refer to page 3 on encryption commands and decrypt the following message: ;"
                + " " + encrypted + ";"
                + "Reply what the message is decrypted to.";
        messages[4] = "Operation Skyburner was glorious for sure.;"
                + "Well, no time to chit chat. I hope your wrote down the decryption code, because now you need to respond with the same encryption.;"
                + "Say, how about encrypting the word Foundation and sending it to me?";
        messages[5] = "Good. When you receive database requests, you will also need to follow the same encryption protocol.;"
                + "Lengthy process, yes, but this is to ensure our information doesn't fall on the wrong hands.;"
                + "Follow the last encryption protocol (-mc), and send me the database entry for the Foundation.";
        messages[6] = "Received. Now that we've explored most of the system, one last thing I need to cover.;"
                + "The modified version of this software bypasses usual security limits;"
                + "and allows you to log into different people's accounts for information you don't have by default.;"
                + "Your first case: Martin Swanson.;"
                + "Log out of the software, and log in with Swanson's info available at the end of the manual.;"
                + "I will establish contact once you got in.";
    }
}
