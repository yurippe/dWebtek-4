package dk.teamrisk;


import dk.teamrisk.XML.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.List;

@Path("shop")
public class ShopService {
    /**
     * Our Servlet session. We will need this for the shopping basket
     */
    HttpSession session;

    public ShopService(@Context HttpServletRequest servletRequest) {
        session = servletRequest.getSession();
    }

    /**
     * Make the price increase per request (for the sake of example)
     */
    private static int priceChange = 0;

    @GET
    @Path("items")
    @Produces("text/json")
    public String getItems() {

        //You should get the items from the cloud server.
        //In the template we just construct some simple data as an array of objects
        List<Item> items = EasyXML.listItems();
        JSONArray jsonItems = new JSONArray();

        for(Item i : items){
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("itemID", i.getItemID());
            jsonItem.put("itemName", i.getItemName());
            jsonItem.put("itemPrice", i.getItemPrice());
            jsonItem.put("itemDescription", DocumentRenderer.renderDocument(i).getResponse());

            jsonItems.put(jsonItem);

        }

        return jsonItems.toString();

    }

    @GET
    @Path("user")
    @Produces("text/json")
    public String getLoggedInUser(){

        User user = (User) session.getAttribute("user");
        if(user == null){

            JSONObject jsonItem = new JSONObject();
            jsonItem.put("status", "error");
            jsonItem.put("message", "not logged in");
            return jsonItem.toString();

        }

        JSONObject jsonItem = new JSONObject();
        jsonItem.put("status", "ok");
        jsonItem.put("message", user.getUsername());
        return jsonItem.toString();
    }

    @POST
    @Path("login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String loginUser(@FormParam("username") String username, @FormParam("password") String password) {

        EasyXMLResponse response = EasyXML.loginCustomer(username, password);
        if(response.wasSuccessful()){
            User user = (User) response.getData();
            session.setAttribute("user", user);
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("status", "ok");
            jsonItem.put("message", response.getResponse());
            return jsonItem.toString();
        } else {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("status", "error");
            jsonItem.put("message", response.getErrorMessage());
            return jsonItem.toString();
        }

    }

    @GET
    @Path("logout")
    @Produces("text/json")
    public String logoutUser(){
        this.session.invalidate();
        JSONObject jsonItem = new JSONObject();
        jsonItem.put("status", "ok");
        jsonItem.put("message", "logged out");
        return jsonItem.toString();
    }

    @POST
    @Path("createuser")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String createUser(@FormParam("username") String username, @FormParam("password") String password) {

        EasyXMLResponse response = EasyXML.createCustomer(username, password);
        if(response.wasSuccessful()){
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("status", "ok");
            jsonItem.put("message", response.getResponse());
            return jsonItem.toString();
        } else {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("status", "error");
            jsonItem.put("message", response.getErrorMessage());
            return jsonItem.toString();
        }

    }

    @POST
    @Path("addtocart")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String addToCart(@FormParam("itemid") int itemID, @FormParam("amount") int amount){

        User user = (User) this.session.getAttribute("user");
        if(user == null){
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("status", "error");
            jsonItem.put("message", "you need to log in before you can add items to the cart");
            return jsonItem.toString();
        }

        //The user is logged in
        user.getShoppingCart().addToCart(itemID, amount);

        JSONObject jsonItem = new JSONObject();
        jsonItem.put("status", "ok");
        jsonItem.put("message", "Added " + amount + " of item " + itemID);
        return jsonItem.toString();
    }

    @GET
    @Path("shoppingcart")
    @Produces("text/json")
    public String getShoppingCart(){
        User user = (User) this.session.getAttribute("user");

        if(user == null) {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("status", "error");
            jsonItem.put("message", "Not logged in");
            return jsonItem.toString();
        }

        return user.getShoppingCart().getShoppingCartJSON();
    }

}
