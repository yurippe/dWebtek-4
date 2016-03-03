package dk.teamrisk.XML;

import dk.teamrisk.Easy.EasyXML;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * An Items object, containing all items from the store.
 */
public class Items {

    //A list of all the items.
    //NOTE: We can not call constuct XML on these items, because itemDescription contains too much
    private List<Item> items;

    /**
     * Constructor, creating a list of objects.
     * @param xml The XML object to be pased
     * @throws JDOMException
     * @throws IOException
     */
    public Items(String xml) throws JDOMException, IOException{

        //Create a Document object from the XML input
        SAXBuilder builder = new SAXBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document xmlDoc = builder.build(is);

        //Create a list of all "item" elements in the document
        this.items = new ArrayList<>();
        for(Element child : xmlDoc.getRootElement().getChildren()){
            //Safe the data into their respective fields
            int iID = Integer.parseInt(child.getChild("itemID", EasyXML.XML_NAMESPACE).getText());
            String iName = child.getChild("itemName", EasyXML.XML_NAMESPACE).getText();
            String iURL = child.getChild("itemURL", EasyXML.XML_NAMESPACE).getText();
            int iPrice = Integer.parseInt(child.getChild("itemPrice", EasyXML.XML_NAMESPACE).getText());
            int iStock = Integer.parseInt(child.getChild("itemStock", EasyXML.XML_NAMESPACE).getText());

            //Convert the itemDescription into a String
            Element item = child.getChild("itemDescription", EasyXML.XML_NAMESPACE)
                    .getChild("document", EasyXML.XML_NAMESPACE).clone();
            XMLOutputter xmlOutputter = new XMLOutputter();
            String iDescription = xmlOutputter.outputString(item);

            //Add the item to the list
            this.items.add(new Item(iID, iName, iURL, iPrice, iStock, iDescription));
        }
    }

    public List<Item> getList(){ return this.items; }
}
