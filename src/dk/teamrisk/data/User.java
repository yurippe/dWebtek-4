package dk.teamrisk.data;

/**
 * Information on the user.
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

    public int getID(){return this.ID;}

    public ShoppingCart getShoppingCart(){return this.shoppingCart;}
}
