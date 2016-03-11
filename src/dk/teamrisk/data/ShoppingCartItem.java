package dk.teamrisk.data;

import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.XML.Item;
import org.json.JSONObject;

/**
 * An item in the shoppingcart. Most importantly, it also holds the amount to buy.
 */
public class ShoppingCartItem extends Item {

    private int amount;

    /**
     * Constructor for no amount argument
     * @param itemID The itemID of the item
     */
    public ShoppingCartItem(int itemID){
        this(itemID, 0);
    }

    /**
     * Constructor with the amount as an argument.
     * @param itemID The itemID of the item.
     * @param amount The amount considered for purchase
     */
    public ShoppingCartItem(int itemID, int amount){
        super(EasyXML.getItem(itemID));
        this.amount = amount;
    }

    /**
     * Changes the amount currently considered for purchase.
     * @param count The amount to change
     */
    public void changeAmount(int count){
        this.amount += count;
        System.out.println("Now there is " + amount + " items");
    }

    /**
     * Create a JSONObject of the shoppingcartitem.
     * @return A JSONObject of the shoppingcartitem.
     */
    public JSONObject getJSON(){
        JSONObject item = new JSONObject();
        item.put("itemID", this.getItemID());
        item.put("itemName", this.getItemName());
        item.put("itemURL", this.getItemURL());
        item.put("itemPrice", this.getItemPrice());
        item.put("itemStock", this.getItemStock());
        item.put("amount", amount);
        return item;
    }

    public int getAmount(){
        return this.amount;
    }
}
