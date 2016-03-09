package dk.teamrisk.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by Kristian on 3/3/2016.
 */
public class ShoppingCart implements Iterable<ShoppingCartItem> {
    //              itemID    count
    private HashMap<Integer, ShoppingCartItem> items;

    public ShoppingCart(){
        this.items = new HashMap<>();
    }

    public boolean addToCart(int ID){return addToCart(ID, 1);}
    public boolean addToCart(int ID, int count){
        if(this.items.containsKey(ID)){
            ShoppingCartItem scItem = this.items.get(ID);
            if(scItem.getItemStock() < scItem.getAmount() + count){
                return false;
            }else {
                scItem.changeAmount(count);
                return true;
            }
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem(ID);
            if(newItem.getItemStock() < count){
                return false;
            } else {
                newItem.changeAmount(count);
                this.items.put(ID, newItem);
                return true;
            }
        }
    }

    public boolean removeFromCart(int ID, int count){
        if(this.items.containsKey(ID) && count > 0){
            ShoppingCartItem scItem = this.items.get(ID);
            if(scItem.getAmount() - count <= 0) {
                this.items.remove(ID);
            } else {
                scItem.changeAmount(-count);
            }
            return true;
        } else {
            return false;
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
        shoppingCartData.put("items", itemlist);
        shoppingCartData.put("sum", totalprice);
        return shoppingCartData;
    }

    @Override
    public Iterator<ShoppingCartItem> iterator() {
        return items.values().iterator();
    }

    public ShoppingCartItem getItem(int itemID){
        return items.get(itemID);
    }
}
