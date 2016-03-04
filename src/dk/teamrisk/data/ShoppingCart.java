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

    public JSONObject getShoppingCartJSON(){

        JSONArray itemlist = new JSONArray();
        int totalprice = 0;
        for(Integer i : items.keySet()){
            ShoppingCartItem cartItem = items.get(i);
            itemlist.put(cartItem.getJSON());
            totalprice += cartItem.getAmount() * cartItem.getItemPrice();
        }
        JSONObject shoppingCartData = new JSONObject();
        JSONObject data = new JSONObject();
        shoppingCartData.put("items", itemlist);
        data.put("sum", totalprice);
        shoppingCartData.put("data", data);
        return shoppingCartData;
    }


}
