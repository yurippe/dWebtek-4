package dk.teamrisk.Services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * Created by Kristian on 3/5/2016.
 */

@Path("cloud")
public class FakeCloudService {

    private HttpSession session;
    public FakeCloudService(@Context HttpServletRequest servletRequest) {


        session = servletRequest.getSession();

    }

    @GET
    @Path("listitems")
    @Produces("text/xml")
    public String getListItems(){

        String r = "<items xmlns=\"http://www.cs.au.dk/dWebTek/2014\">";
        r += "<item><itemID>1337</itemID><itemName>Test item 1</itemName><itemURL>img/noimg.jpg</itemURL><itemPrice>20</itemPrice><itemStock>10</itemStock><itemDescription><document>Example <w:bold xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">descrip</w:bold><w:italics xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">tion</w:italics></document></itemDescription></item>\n";
        r += "<item><itemID>1338</itemID><itemName>Test item 2</itemName><itemURL>img/noimg.jpg</itemURL><itemPrice>10</itemPrice><itemStock>0</itemStock><itemDescription><document>Example <w:bold xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">descrip</w:bold><w:italics xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">tion</w:italics></document></itemDescription></item>\n";
        r += "<item><itemID>1339</itemID><itemName>Test item 3</itemName><itemURL>img/noimg.jpg</itemURL><itemPrice>50</itemPrice><itemStock>2</itemStock><itemDescription><document>Example <w:bold xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">descrip</w:bold><w:italics xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">tion</w:italics></document></itemDescription></item>\n";
        r += "</items>";
        return r;
    }

}
