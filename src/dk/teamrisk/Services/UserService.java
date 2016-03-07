package dk.teamrisk.Services;


import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.Easy.EasyXMLResponse;
import dk.teamrisk.data.User;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@Path("user")
public class UserService extends BaseService{

    public UserService(@Context HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @GET
    @Path("user")
    @Produces("text/json")
    public String getLoggedInUser(){

        User user = getUser();
        if(user == null){
            return generateJsonResponse("error", "Not logged in");
        }

        JSONObject userdata = new JSONObject();
        userdata.put("username", user.getUsername());
        userdata.put("userId", user.getID());
        return generateJsonResponse("ok", "", userdata);
    }

    @POST
    @Path("login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String loginUser(@FormParam("username") String username, @FormParam("password") String password) {
        if(username == null || username.length() < 3 ||
                password == null || password.length() < 3){
            return generateJsonResponse("error", "bad request");
        }

        EasyXMLResponse response = EasyXML.loginCustomer(username, password);
        if (response.wasSuccessful()){
            User user = (User) response.getData();
            setUser(user);
            return generateJsonResponse("ok", response.getResponse());
        } else {
            return generateJsonResponse("error", response.getErrorMessage());
        }
    }

    @GET
    @Path("logout")
    @Produces("text/json")
    public String logoutUser(){
        invalidateSession();
        return generateJsonResponse("ok", "Logged out");
    }

    @POST
    @Path("createuser")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/json")
    public String createUser(@FormParam("username") String username, @FormParam("password") String password) {

        if(username == null || username.length() < 3 || password == null || password.length() < 3){
            return generateJsonResponse("error", "bad request");
        }

        EasyXMLResponse response = EasyXML.createCustomer(username, password);
        if(response.wasSuccessful()){
            return generateJsonResponse("ok", response.getResponse());
        } else {
            return generateJsonResponse("error", response.getErrorMessage());
        }
    }
}
