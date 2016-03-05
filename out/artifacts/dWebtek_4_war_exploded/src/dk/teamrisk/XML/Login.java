package dk.teamrisk.XML;

import dk.teamrisk.Easy.EasyResponse;
import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.Easy.EasyXMLResponse;
import dk.teamrisk.Easy.XMLValidator;
import dk.teamrisk.data.User;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Kristian on 3/3/2016.
 */
public class Login extends BaseXMLObject{

    private String username;
    private String password;

    public Login(String username, String password){
        this.username = username;
        this.password = password;
    }


    @Override
    public EasyXMLResponse constructXML() {
        EasyXMLResponse response = new EasyXMLResponse();

        //Sets up the root of the item XML version
        Element root = new Element("login");
        root.setNamespace(EasyXML.XML_NAMESPACE);

        //Puts in all the fields
        root.addContent(createElement("customerName", this.username));
        root.addContent(createElement("customerPass", this.password));

        //Create a document object from the item
        Document createCustomerDoc = new Document(root);

        //Test the document object - problems are safed in the feedback object.
        XMLOutputter xmlOutputter = new XMLOutputter();
        String outp = xmlOutputter.outputString(createCustomerDoc);
        if (!(XMLValidator.validates(outp))) {
            return response.setErrorMessage("Login XML Doesn't validate").setResponse(outp);
        }

        //Return the feedback
        return response.setResponse(outp);
    }


    public EasyXMLResponse wasSuccessful(EasyResponse httpResponse){
        EasyXMLResponse response = new EasyXMLResponse();
        if(httpResponse.wasSuccessful() && !(httpResponse.getResponse().equals(""))){

            //Create a SAXBuilder and more for the creation of a new document
            SAXBuilder documentBuilder = new SAXBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(httpResponse.getResponse()));

            //Attempt to create the object within the cloud namespace
            Document document = null;
            try {
                document = documentBuilder.build(inputSource);
            } catch (JDOMException e) {
                return response.setErrorMessage("JDOMException").setFatalException(e);
            } catch (IOException e) {
                return response.setErrorMessage("IOException").setFatalException(e);
            }

            if(!(document.getRootElement().getChild("customerID", EasyXML.XML_NAMESPACE) == null)){
                String customerID = document.getRootElement().getChild("customerID", EasyXML.XML_NAMESPACE).getText();
                String customerUN = document.getRootElement().getChild("customerName", EasyXML.XML_NAMESPACE).getText();
                return response.setResponse("Successfully logged in").setData(new User(customerUN, Integer.parseInt(customerID)));
            } else {
                return response.setResponse("Could not log in").setErrorMessage("Could not log in");
            }

        }
        return response.setResponse("Could not log in").setErrorMessage(httpResponse.getErrorMessage());
    }
}
