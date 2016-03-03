package dk.teamrisk.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Kristian on 3/3/2016.
 */
public class ShoppingCart {
    //              itemID    count
    private HashMap<Integer, ShoppingCartItem> items;

    public ShoppingCart(){
        this.items = new HashMap<>();
    }

    public void addToCart(int ID){addToCart(ID, 1);}
    public void addToCart(int ID, int count){
        if(this.items.containsKey(ID)){
            this.items.get(ID).increaseAmount(count);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem(ID);
            newItem.increaseAmount(count);
            this.items.put(ID, newItem);
        }
    }

    public void emptyCart(){
        this.items = new HashMap<>();
    }

    public JSONArray getShoppingCartJSON(){

        JSONArray itemlist = new JSONArray();
        for(Integer i : items.keySet()){
            itemlist.put(items.get(i).getJSON());
        }
        return itemlist;
    }


}
