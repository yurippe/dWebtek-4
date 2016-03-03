package dk.teamrisk.data;

/**
 * Created by Kristian on 3/3/2016.
 */
public class User {

    private String username;
    private int ID;

    private ShoppingCart shoppingCart;

    public User(String username, int ID){
        this.username = username;
        this.ID = ID;
        this.shoppingCart = new ShoppingCart();
    }

    public String getUsername(){return this.username;}
    //public void setUsername(String username){this.username = username;}

    public int getID(){return this.ID;}

    public ShoppingCart getShoppingCart(){return this.shoppingCart;}
}
