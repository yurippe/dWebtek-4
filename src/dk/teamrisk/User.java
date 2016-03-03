package dk.teamrisk;

/**
 * Created by Kristian on 3/3/2016.
 */
public class User {

    private String username;
    private int ID;

    public User(String username, int ID){
        this.username = username;
        this.ID = ID;
    }

    public String getUsername(){return this.username;}
    //public void setUsername(String username){this.username = username;}

    public int getID(){return this.ID;}
}
