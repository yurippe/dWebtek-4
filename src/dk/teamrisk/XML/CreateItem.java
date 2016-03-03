package dk.teamrisk.XML;

import dk.teamrisk.EasyXML;
import dk.teamrisk.EasyXMLResponse;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

/**
 * Creates the createItem request
 */
public class CreateItem extends BaseXMLObject{

    private Item base;

    /**
     * Constructor taking in the item to create
     * @param buildFrom The item to create from
     */
    public CreateItem(Item buildFrom){
        this.base = buildFrom;
    }

    /**
     * Creates the createItem XML based on the name in the object and other stuff
     * @return EasyXMLResponse, containing the item in response.
     */
    @Override
    public EasyXMLResponse constructXML() {
        EasyXMLResponse feedback = new EasyXMLResponse();

        //Create the root element and its namespace
        Element root = new Element("createItem");
        root.setNamespace(EasyXML.XML_NAMESPACE);

        //Add all the stuff to the root object
        root.addContent(createElement("shopKey", EasyXML.SHOP_KEY));
        root.addContent(createElement("itemName", this.base.getItemName()));

        //Create a document object and parse it into the EasyXMLResponse object.
        Document item = new Document(root);
        XMLOutputter xmlOutputter = new XMLOutputter();
        return feedback.setResponse(xmlOutputter.outputString(item));
    }


}
