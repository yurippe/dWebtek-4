package dk.teamrisk.XML;

import dk.teamrisk.EasyXML;
import dk.teamrisk.EasyXMLResponse;
import org.jdom2.Element;

/**
 * A basic XML object, used for item and items
 */
public abstract class BaseXMLObject {
    /**
     * A constructor...
     */
    public abstract EasyXMLResponse constructXML();

    /**
     * Creates a JDOM Element with the cloud namespace and some stuff in it
     * @param elementName The name of the element
     * @param elementText The text in the element
     * @return The element
     */
    protected Element createElement(String elementName, String elementText) {
        return new Element(elementName).setNamespace(EasyXML.XML_NAMESPACE).setText(elementText);
    }
}
