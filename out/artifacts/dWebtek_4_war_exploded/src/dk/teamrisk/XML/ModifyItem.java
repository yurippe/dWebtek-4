package dk.teamrisk.XML;

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
 * Taking care of all the tedious work with adjusting an item.
 */
public class ModifyItem extends BaseXMLObject {

    private Item base;

    /**
     * Constructor, getting the item to use
     * @param buildFrom The item...
     */
    public ModifyItem(Item buildFrom){
        this.base = buildFrom;
    }

    /**
     * Create the modifyItem XML based on the item object
     * @return EasyXMLResponse, containing the XML in the response
     */
    @Override
    public EasyXMLResponse constructXML() {
        EasyXMLResponse feedback = new EasyXMLResponse();

        //Create the modifyItem object
        Element root = new Element("modifyItem");
        root.setNamespace(EasyXML.XML_NAMESPACE);

        root.addContent(createElement("shopKey", EasyXML.SHOP_KEY));
        root.addContent(createElement("itemID", Integer.toString(this.base.getItemID())));
        root.addContent(createElement("itemName", this.base.getItemName()));
        root.addContent(createElement("itemPrice", Integer.toString(this.base.getItemPrice())));
        root.addContent(createElement("itemURL", this.base.getItemURL()));

        //Item description is pretty complex
        Element itemDescriptionElement = new Element("itemDescription");
        itemDescriptionElement.setNamespace(EasyXML.XML_NAMESPACE);

        //First compile the document element (itemDescription should be a valid xml of a document)
        SAXBuilder documentBuilder = new SAXBuilder();
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(this.base.getItemDescription()));

        //Create the itemDescription as a document
        Document itemDescription = null;
        try {
            itemDescription = documentBuilder.build(inputSource);
        } catch (JDOMException e) {
            return feedback.setErrorMessage(e.getMessage()).setFatalException(e);
        } catch (IOException e) {
            return feedback.setErrorMessage("IOException").setFatalException(e);
        }

        //Create an Element object containing the itemDescription. This is put into the modifyItem
        Element docItemDesc = itemDescription.getRootElement().setNamespace(EasyXML.XML_NAMESPACE);
        itemDescriptionElement.addContent(docItemDesc.clone());
        root.addContent(itemDescriptionElement);

        //Finally, create the modifyItem object.
        Document item = new Document(root);
        XMLOutputter xmlOutputter = new XMLOutputter();
        String outp = xmlOutputter.outputString(item);
        if(!(XMLValidator.validates(outp))){
            return feedback.setErrorMessage("ItemCreate XML Doesn't validate").setResponse(outp);
        }

        //Return how well it went.
        return feedback.setResponse(outp);
    }
}
