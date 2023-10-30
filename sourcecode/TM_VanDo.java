
public class TM_VanDo extends TM_Story {

    public TM_VanDo(TM_MainGame game) {
        super(game);
    }
    
    @Override
    public boolean checkResponse(String response) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void setDatabase() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void setMessages() {
        messages = new String[1];
        messages[0] = "You have reached the end of this version. Stay tuned for more updates.;"
                + "Meanwhile, look around the database, see what you can find about the world of Orb Walker.";
    }

}
