package dk.teamrisk.data;

import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.XML.Item;
import org.json.JSONObject;

/**
 * Created by Kristian on 3/3/2016.
 */
public class ShoppingCartItem {

    private int itemID;
    private String itemName;
    private String itemURL;
    private int itemPrice;
    private int itemStock;
    private String itemDescription;

    //how many in the shopping cart;
    private int amount;

    public ShoppingCartItem(int itemID){
        this(EasyXML.getItem(itemID));
    }

    private ShoppingCartItem(Item item){
        this.itemID = item.getItemID();
        this.itemName = item.getItemName();
        this.itemURL = item.getItemURL();
        this.itemPrice = item.getItemPrice();
        this.itemDescription = item.getItemDescription();
        this.itemStock = item.getItemStock();
    }

    public void increaseAmount(int count){
        this.amount += count;
    }
    public int getAmount(){return this.amount;}

    public JSONObject getJSON(){
        JSONObject item = new JSONObject();
        item.put("itemID", itemID);
        item.put("itemName", itemName);
        item.put("itemURL", itemURL);
        item.put("itemPrice", itemPrice);
        item.put("itemStock", itemStock);
        item.put("amount", amount);
        return item;
    }

    public int getItemPrice(){
        return this.itemPrice;
    }
}
