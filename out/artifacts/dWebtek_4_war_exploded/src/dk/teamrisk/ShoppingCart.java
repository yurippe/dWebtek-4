package dk.teamrisk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Kristian on 3/3/2016.
 */
public class ShoppingCart {
    //              itemID    count
    private HashMap<Integer, Integer> items;

    public ShoppingCart(){
        this.items = new HashMap<>();
    }

    public void addToCart(int ID){addToCart(ID, 1);}
    public void addToCart(int ID, int count){
        if(this.items.containsKey(ID)){
            this.items.put(ID, this.items.get(ID) + count);
        } else {
            this.items.put(ID, count);
        }
    }

    public void emptyCart(){
        this.items = new HashMap<>();
    }

    public String getShoppingCartJSON(){

        JSONArray itemlist = new JSONArray();
        for(Integer i : items.keySet()){
            JSONObject item = new JSONObject();
            item.put("itemid", i);
            item.put("amount", items.get(i));
            itemlist.put(item);
        }
        return itemlist.toString();
    }


}
