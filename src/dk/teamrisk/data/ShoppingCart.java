package dk.teamrisk.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * A shoppingcart containing all the items currently considered purchasing by buyhappy customers.
 */
public class ShoppingCart implements Iterable<ShoppingCartItem> {
    //              itemID    count
    private HashMap<Integer, ShoppingCartItem> items;

    public ShoppingCart(){
        this.items = new HashMap<>();
    }

    public boolean addToCart(int ID){return addToCart(ID, 1);}

    /**
     * Adds an item (or modifies it) to the shoppingcart
     * @param ID The item to add
     * @param count The amount of items to add
     * @return If the addition was successful
     */
    public boolean addToCart(int ID, int count){
        //Check if there's already a shoppingcartitem
        if(this.items.containsKey(ID)){
            ShoppingCartItem scItem = this.items.get(ID);
            if(scItem.getItemStock() < scItem.getAmount() + count){
                return false;
            }else {
                scItem.changeAmount(count);
                return true;
            }
        }
        //Create a new shoppingcartitem
        else {
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

    /**
     * Removes a shoppingcartitem from the cart. If only parts of it needs to be removed, it is only modified
     * @param ID The itemID
     * @param count The amount to remove
     * @return If the removal was successful
     */
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

    /**
     * Reset the cart
     */
    public void emptyCart(){
        this.items = new HashMap<>();
    }

    /**
     * Convert the shoppingcart from Java to JSON
     * @return A JSONObject containing all information on the data
     */
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
