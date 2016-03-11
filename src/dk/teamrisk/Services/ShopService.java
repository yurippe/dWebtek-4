package dk.teamrisk.Services;


import dk.teamrisk.Easy.DocumentRenderer;
import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.Easy.EasyXMLResponse;
import dk.teamrisk.XML.BaseXMLObject;
import dk.teamrisk.XML.Items;
import dk.teamrisk.data.ShoppingCart;
import dk.teamrisk.data.ShoppingCartItem;
import dk.teamrisk.data.User;
import dk.teamrisk.XML.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Map;

/**
 * Handling everything related to the shoppingcart and the items sold.
 */
@Path("shop")
public class ShopService extends BaseService {


    public ShopService(@Context HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    /**
     * Create a list of all the items in the shop.
     */
    @GET
    @Path("items")
    @Produces("text/json")
    public String getItems() {

        //You should get the items from the cloud server.
        //In the template we just construct some simple data as an array of objects
        List<Item> items = EasyXML.listItems();
        JSONArray jsonItems = new JSONArray();

        for (Item i : items) {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("itemID", i.getItemID());
            jsonItem.put("itemName", i.getItemName());
            jsonItem.put("itemPrice", i.getItemPrice());
            jsonItem.put("itemDescription", DocumentRenderer.renderDocument(i).getResponse());
            jsonItem.put("itemURL", i.getItemURL());
            jsonItem.put("itemStock", i.getItemStock());
            jsonItems.put(jsonItem);

        }

        return generateJsonResponse("ok", jsonItems);

    }

    /**
     * Add an item to the shoppingcart.
     * @param itemID The itemID of the item
     * @param amount The amount to be added.
     * @return The success of the addition, including checking of stock.
     */
    @POST
    @Path("addtocart")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String addToCart(@FormParam("itemID") int itemID, @FormParam("amount") int amount) {

        if (itemID <= 0) {
            return generateJsonResponse("error", "bad request");
        }

        User user = getUser();
        if (user == null) {
            return generateJsonResponse("error", "You need to log in before you can add items to the cart");
        }

        //The user is logged in


        if (user.getShoppingCart().addToCart(itemID, amount)) {
            return generateJsonResponse("ok", "Added " + amount + " of item " + itemID);
        } else {
            return generateJsonResponse("error", "Out of stock");
        }
    }

    /**
     * Checks the user being logged in and returns the current shoppingcart.
     * @return A JsonResponse including the shoppingcart
     */
    @GET
    @Path("getshoppingcart")
    @Produces("text/json")
    public String getShoppingCart() {
        User user = getUser();

        if (user == null) {
            return generateJsonResponse("error", "Not logged in");
        }

        return generateJsonResponse("ok", user.getShoppingCart().getShoppingCartJSON());
    }

    /**
     * A user should also be able to remove an item from the cart,
     * which is done quite similar to the addition of an item.
     * @param itemID The itemID of the item to remove.
     * @param amount The amount to remove
     * @return The success of the operation
     */
    @POST
    @Path("removefromcart")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String removeFromCart(@FormParam("itemID") int itemID, @FormParam("amount") int amount) {

        if (itemID <= 0) {
            return generateJsonResponse("error", "bad request");
        }

        User user = getUser();
        if (user == null) {
            return generateJsonResponse("error", "Good going, trying to remove something you don't have");
        }

        if (user.getShoppingCart().removeFromCart(itemID, amount)) {
            return generateJsonResponse("ok", "Removed " + amount + " of item " + itemID);
        } else {
            return generateJsonResponse("error", "shit happened?");
        }
    }

    /**
     * When posted to this url, all items currently on the serverside shoppingcart are sold, if possible
     * @return The success of the operation
     */
    @POST
    @Path("sellitem")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String sellItemsInCart() {
        User user = getUser();
        ShoppingCart shoppingCart = user.getShoppingCart();

        if (user == null) {
            return generateJsonResponse("error", "Not logged in");
        }

        //Test, that everything is in stock
        Map<Integer, Item> items = EasyXML.mapItems();
        for (ShoppingCartItem i : shoppingCart) {
            int amountInStock = items.get(i.getItemID()).getItemStock();
            if (i.getAmount() > amountInStock) {
                return generateJsonResponse("error", "item" + i.getItemID() + "is out of stock");
            }
        }

        //Sell Items
        for (ShoppingCartItem i : shoppingCart) {
            EasyXMLResponse response = EasyXML.sellItems(i.getItemID(), user.getID(), i.getAmount());
            if (!response.wasSuccessful()) {
                return generateJsonResponse("error", "sell failed: " + response.getResponse());
            }
        }
        user.getShoppingCart().emptyCart();

        return generateJsonResponse("ok", "items were all sold");
    }
}
