package dk.teamrisk.data;

import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.XML.Item;
import org.json.JSONObject;

/**
 * Created by Kristian on 3/3/2016.
 */
public class ShoppingCartItem extends Item {

    private int amount;

    public ShoppingCartItem(int itemID){
        this(itemID, 1);
    }

    public ShoppingCartItem(int itemID, int amount){
        super(EasyXML.getItem(itemID));
        this.amount = amount;
    }

    public void changeAmount(int count){
        this.amount += count;
    }

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
