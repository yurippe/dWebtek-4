package dk.teamrisk.XML;

import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.Easy.EasyXMLResponse;
import dk.teamrisk.Easy.XMLValidator;
import dk.teamrisk.data.ShoppingCartItem;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;


/**
 * Creates a sellItems XML document based on an item and a customer
 */
public class SellItems extends BaseXMLObject {

    private ShoppingCartItem cartItem;
    private int customerID;

    /**
     * Constructor, getting the item to use
     */
    public SellItems(int itemID, int customerID, int amount){
        this.cartItem = new ShoppingCartItem(itemID, amount);
        this.customerID = customerID;
    }

    /**
     * Create the sellItem XML based on the item object
     * @return EasyXMLResponse, containing the XML in the response
     */
    @Override
    public EasyXMLResponse constructXML() {
        EasyXMLResponse feedback = new EasyXMLResponse();

        //Create the sellItems root and add the stuff
        Element root = new Element("sellItems");
        root.setNamespace(EasyXML.XML_NAMESPACE);

        root.addContent(createElement("shopKey", EasyXML.SHOP_KEY));
        root.addContent(createElement("itemID", Integer.toString(this.cartItem.getItemID())));
        root.addContent(createElement("customerID", Integer.toString(this.customerID)));
        root.addContent(createElement("saleAmount", Integer.toString(this.cartItem.getAmount())));

        //Make a document
        Document item = new Document(root);
        XMLOutputter xmlOutputter = new XMLOutputter();
        String outp = xmlOutputter.outputString(item);
        if(!(XMLValidator.validates(outp))){
            return feedback.setErrorMessage("ItemCreate XML Doesn't validate").setResponse(outp);
        }

        //Return how well it went...
        return feedback.setResponse(outp);
    }
}
