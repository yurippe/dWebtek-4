package dk.teamrisk.XML;

import dk.teamrisk.EasyXML;
import dk.teamrisk.EasyXMLResponse;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

/**
 * Taking care of all the tedious work with adjusting an item.
 */
public class AdjustItemStock extends BaseXMLObject{

    private int itemID;

    private int adjustment;

    /**
     * Constructor based on the necessary values.
     * @param itemID
     * @param adjustment
     */
    public AdjustItemStock(int itemID, int adjustment){
        this.itemID = itemID;
        this.adjustment = adjustment;
    }

    /**
     * Creates an XML file based on both values.
     * @return EasyXMLResponse object containing the document in its response
     */
    @Override
    public EasyXMLResponse constructXML() {
        EasyXMLResponse feedback = new EasyXMLResponse();

        //Create the root element with the namespace
        Element root = new Element("adjustItemStock");
        root.setNamespace(EasyXML.XML_NAMESPACE);

        //Add all the children
        root.addContent(createElement("shopKey", EasyXML.SHOP_KEY));
        root.addContent(createElement("itemID", Integer.toString(this.itemID)));
        root.addContent(createElement("adjustment", Integer.toString(this.adjustment)));

        //Create the XML file as a Document object
        Document item = new Document(root);
        XMLOutputter xmlOutputter = new XMLOutputter();

        //Parse the XML document through the XMLOutputter, checking for validity.
        return feedback.setResponse(xmlOutputter.outputString(item));
    }
}
