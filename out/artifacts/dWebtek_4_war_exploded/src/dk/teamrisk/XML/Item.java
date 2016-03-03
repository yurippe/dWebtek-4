package dk.teamrisk.XML;

import dk.teamrisk.Easy.DocumentRenderer;
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
 * An item, being the link between Java and the XML representation
 */
public class Item extends BaseXMLObject {

    //All the children of an item, saved in their own fields
    private int itemID;
    private String itemName;
    private String itemURL;
    private int itemPrice;
    private int itemStock;
    private String itemDescription;

    /**
     * Constructor
     * @param itemID
     * @param itemName
     * @param itemURL
     * @param itemPrice
     * @param itemStock
     * @param itemDescription
     */
    public Item(int itemID, String itemName, String itemURL, int itemPrice, int itemStock, String itemDescription) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.itemDescription = itemDescription;
    }

    /**
     * Creates an EasyXMLResponse object, which represents the welformedness of this item.
     * @return EasyXMLResponse and with it hopefully happiness.
     */
    @Override
    public EasyXMLResponse constructXML() {
        EasyXMLResponse feedback = new EasyXMLResponse();

        //Sets up the root of the item XML version
        Element root = new Element("item");
        root.setNamespace(EasyXML.XML_NAMESPACE);

        //Puts in all the fields
        root.addContent(createElement("itemID", Integer.toString(this.itemID)));
        root.addContent(createElement("itemName", this.itemName));
        root.addContent(createElement("itemURL", this.itemURL));
        root.addContent(createElement("itemPrice", Integer.toString(this.itemPrice)));
        root.addContent(createElement("itemStock", Integer.toString(this.itemStock)));

        //Item description is pretty complex, and is handled elsewhere.
        Element itemDescriptionElement = new Element("itemDescription");
        itemDescriptionElement.setNamespace(EasyXML.XML_NAMESPACE);

        //First compile the document element (itemDescription should be a valid xml of a document)
        SAXBuilder documentBuilder = new SAXBuilder();
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(this.itemDescription));
        Document itemDescription = null;

        //Put the itemDescription together and cross your fingers.
        try {
            itemDescription = documentBuilder.build(inputSource);
        } catch (JDOMException e) {
            return feedback.setErrorMessage(e.getMessage()).setFatalException(e);
        } catch (IOException e) {
            return feedback.setErrorMessage("IOException").setFatalException(e);
        }

        //Add the itemDescription to the item XML root.
        Element docItemDesc = itemDescription.getRootElement().setNamespace(EasyXML.XML_NAMESPACE);
        itemDescriptionElement.addContent(docItemDesc.clone());
        root.addContent(itemDescriptionElement);

        //Create a document object from the item
        Document item = new Document(root);

        //Test the document object - problems are safed in the feedback object.
        XMLOutputter xmlOutputter = new XMLOutputter();
        String outp = xmlOutputter.outputString(item);
        if (!(XMLValidator.validates(outp))) {
            return feedback.setErrorMessage("Item XML Doesn't validate").setResponse(outp);
        }

        //Return the feedback
        return feedback.setResponse(outp);
    }


    public String getItemURL() {
        return itemURL;
    }
    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public int getItemID() {
        return itemID;
    }
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemStock() {
        return itemStock;
    }
    public void setItemStock(int itemStock) {
        this.itemStock = itemStock;
    }

    public String getItemDescription() {
        return itemDescription;
    }
    public String getItemDescriptionAsXHTML() {
        EasyXMLResponse resp = DocumentRenderer.renderDocument(this);

        if (resp.wasSuccessful()) return resp.getResponse();
        else return resp.getErrorMessage();
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

}