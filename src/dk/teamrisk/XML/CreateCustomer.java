package dk.teamrisk.XML;

import dk.teamrisk.Easy.EasyResponse;
import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.Easy.EasyXMLResponse;
import dk.teamrisk.Easy.XMLValidator;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

/**
 * Handling the creation of a new customer request.
 */
public class CreateCustomer extends BaseXMLObject{

    private String username;
    private String password;

    public CreateCustomer(String username, String password){
        this.username = username;
        this.password = password;
    }

    @Override
    public EasyXMLResponse constructXML() {
        EasyXMLResponse response = new EasyXMLResponse();

        //Sets up the root of the item XML version
        Element root = new Element("createCustomer");
        root.setNamespace(EasyXML.XML_NAMESPACE);

        //Puts in all the fields
        root.addContent(createElement("shopKey", EasyXML.SHOP_KEY));
        root.addContent(createElement("customerName", this.username));
        root.addContent(createElement("customerPass", this.password));

        //Create a document object from the item
        Document createCustomerDoc = new Document(root);

        //Test the document object - problems are safed in the feedback object.
        XMLOutputter xmlOutputter = new XMLOutputter();
        String outp = xmlOutputter.outputString(createCustomerDoc);
        if (!(XMLValidator.validates(outp))) {
            return response.setErrorMessage("CreateCustomer XML Doesn't validate").setResponse(outp);
        }

        //Return the feedback
        return response.setResponse(outp);
    }


    public EasyXMLResponse wasSuccessful(EasyResponse httpResponse){
        EasyXMLResponse response = new EasyXMLResponse();
        if(httpResponse.wasSuccessful()){

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
                String id = document.getRootElement().getChild("customerID", EasyXML.XML_NAMESPACE).getText();

                return response.setResponse("Successfully made user").setData(Integer.getInteger(id));
            } else {
                return response.setResponse("Username was already taken").setErrorMessage("Username was already taken");
            }

        }
        //Not sure if a 403 means the username is taken or what
        return response.setResponse("Username was already taken").setErrorMessage("Username was already taken");
    }
}
