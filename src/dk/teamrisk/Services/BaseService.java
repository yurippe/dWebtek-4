package dk.teamrisk.Services;

import dk.teamrisk.data.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Kristian on 3/3/2016.
 */
public abstract class BaseService {

    protected HttpSession session;

    protected BaseService(HttpServletRequest servletRequest){
        session = servletRequest.getSession();
    }

    protected User getUser(){
        return (User) session.getAttribute("user");
    }

    protected void setUser(User user){
        this.session.setAttribute("user", user);
    }

    protected void invalidateSession(){
        this.session.invalidate();
    }

    protected String generateJsonResponse(String status, JSONObject data){
        return generateJsonResponse(status, "", data);
    }

    protected String generateJsonResponse(String status, JSONArray data){
        return generateJsonResponse(status, "", data);
    }

    protected String generateJsonResponse(String status){
        return generateJsonResponse(status, "");
    }

    protected String generateJsonResponse(String status, String message){
        return generateJsonResponse(status, message, new JSONObject());
    }

    protected String generateJsonResponse(String status, String message, JSONObject data){
        JSONObject jsonItem = new JSONObject();
        jsonItem.put("status", status);
        jsonItem.put("message", message);
        jsonItem.put("data", data);

        return jsonItem.toString();
    }

    protected String generateJsonResponse(String status, String message, JSONArray data){
        JSONObject jsonItem = new JSONObject();
        jsonItem.put("status", status);
        jsonItem.put("message", message);
        jsonItem.put("data", data);

        return jsonItem.toString();
    }
}
