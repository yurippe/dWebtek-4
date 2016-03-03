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
    public String getLoggedInUser(){

        User user = (User) session.getAttribute("user");
        if(user == null){ return "Not logged in";}

        return user.getUsername();
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
    public String logoutUser(){
        this.session.invalidate();
        return "Session deleted";
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
}
