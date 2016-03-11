package dk.teamrisk.Services;

import dk.teamrisk.Easy.XMLValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Kristian on 3/5/2016 and used for testing, when the AU servers were down.
 */

@Path("cloud")
public class FakeCloudService {

    private HttpSession session;
    public FakeCloudService(@Context HttpServletRequest servletRequest) {


        session = servletRequest.getSession();

    }

    @GET
    @Path("listItems")
    @Produces("text/xml")
    public String getListItems(){

        String r = "<items xmlns=\"http://www.cs.au.dk/dWebTek/2014\">";
        r += "<item><itemID>1337</itemID><itemName>Test item 1</itemName><itemURL>img/nopic.jpg</itemURL><itemPrice>20</itemPrice><itemStock>10</itemStock><itemDescription><document>Example <w:bold xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">descrip</w:bold><w:italics xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">tion</w:italics></document></itemDescription></item>\n";
        r += "<item><itemID>1338</itemID><itemName>Test item 2</itemName><itemURL>img/nopic.jpg</itemURL><itemPrice>10</itemPrice><itemStock>0</itemStock><itemDescription><document>Example <w:bold xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">descrip</w:bold><w:italics xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">tion</w:italics></document></itemDescription></item>\n";
        r += "<item><itemID>1339</itemID><itemName>Test item 3</itemName><itemURL>img/nopic.jpg</itemURL><itemPrice>50</itemPrice><itemStock>2</itemStock><itemDescription><document>Example <w:bold xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">descrip</w:bold><w:italics xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">tion</w:italics></document></itemDescription></item>\n";
        r += "</items>";
        return r;
    }

    @POST
    @Path("login")
    @Produces("text/xml")
    public String postLogin(@Context HttpServletRequest request, InputStream requestBody, @Context HttpServletResponse response){

        //Validate that the body is valid, if not return a
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder out = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        }catch (Exception e){
            try {
                response.sendError(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if(!(XMLValidator.validates(out.toString()))){
            try {
                response.sendError(400);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int customerID = 1337; //Make this random if you want, depends on your needs
        String customerName = "John"; //Again do what you want here
        String r = "<loginResponse xmlns=\"http://www.cs.au.dk/dWebTek/2014\">";
        r += "<customerID>" + Integer.toString(customerID) + "</customerID>";
        r += "<customerName>" + customerName + "</customerName></loginResponse>";


     return r;
    }

}
