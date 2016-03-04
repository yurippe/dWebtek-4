package dk.teamrisk.Services;


import dk.teamrisk.Easy.DocumentRenderer;
import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.XML.BaseXMLObject;
import dk.teamrisk.data.User;
import dk.teamrisk.XML.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.List;

@Path("shop")
public class ShopService extends BaseService{


    public ShopService(@Context HttpServletRequest servletRequest) {
        super(servletRequest);
    }


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

        return generateJsonResponse("ok", jsonItems);

    }

    @POST
    @Path("addtocart")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String addToCart(@FormParam("itemID") int itemID, @FormParam("amount") int amount){

        if(itemID <= 0){
            return generateJsonResponse("error", "bad request");
        }

        User user = getUser();
        if(user == null){
            return generateJsonResponse("error", "You need to log in before you can add items to the cart");
        }

        //The user is logged in
        user.getShoppingCart().addToCart(itemID, amount);

        return generateJsonResponse("ok", "Added " + amount + " of item " + itemID);
    }

    @GET
    @Path("getshoppingcart")
    @Produces("text/json")
    public String getShoppingCart(){
        User user = getUser();

        if(user == null) {
            return generateJsonResponse("error", "Not logged in");
        }

        return generateJsonResponse("ok", user.getShoppingCart().getShoppingCartJSON());
    }

}
