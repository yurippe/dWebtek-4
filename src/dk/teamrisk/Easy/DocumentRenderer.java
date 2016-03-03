package dk.teamrisk.Easy;

import dk.teamrisk.XML.Item;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

/**
 * Converter of the cloud namespace to the XHTML
 */
public class DocumentRenderer {

    /**
     * Create an XHTML convertion of cloud based on a string
     * @param DocumentXML The document as a String
     * @return The XHTMLification
     */
    public static EasyXMLResponse renderDocument(String DocumentXML){
        //Set up an XML response object to put out the XHTML document or useful error information
        EasyXMLResponse response = new EasyXMLResponse();

        //Create a SAXBuilder and more for the creation of a new document
        SAXBuilder documentBuilder = new SAXBuilder();
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(DocumentXML));

        //Attempt to create the object within the cloud namespace
        Document document = null;
        try {
            document = documentBuilder.build(inputSource);
        } catch (JDOMException e) {
            return response.setErrorMessage(e.getMessage()).setFatalException(e);
        } catch (IOException e) {
            return response.setErrorMessage("IOException").setFatalException(e);
        }

        //Get the root element as a basis for manipulation
        Element documentElement = document.getRootElement();

        //Remove the namespace and replace it with a hollow one
        documentElement.removeNamespaceDeclaration(EasyXML.XML_NAMESPACE);
        documentElement.setName("div").setAttribute("class", "itemdescription");
        documentElement.setNamespace(Namespace.NO_NAMESPACE);

        //Rename all children from cloud to XHTML
        renameChildren(documentElement);

        //Should the element be empty, then add a little lonely space
        if(documentElement.getChildren().size() == 0 && documentElement.getText().equals("")){
            documentElement.setText(" ");
        }

        //Output the XHTML document
        XMLOutputter xmlOutputter = new XMLOutputter();
        String outp = xmlOutputter.outputString(documentElement);

        return response.setResponse(outp);
    }

    /**
     * Create a XHTML version of a document based on an item object
     * @param item The item to convert
     * @return The item XHTMLified
     */
    public static EasyXMLResponse renderDocument (Item item){
        return renderDocument(item.getItemDescription());
    }

    /**
     * Renaming of the cloud tags to their XHTML equivalent
     * @param e The element to rename
     */
    private static void renameChildren(Element e){
        //Rename every child object of an element and the recursively rename their children.
        for(Element child : e.getChildren()){
            String name = child.getName();

            //The conversion...
            switch (name) {
                case "bold" : child.setName("b"); break;
                case "italics" : child.setName("i"); break;
                case "list" : child.setName("ul"); break;
                case "item" : child.setName("li"); break;
            }

            child.removeNamespaceDeclaration(EasyXML.XML_NAMESPACE);
            child.setNamespace(Namespace.NO_NAMESPACE);
            renameChildren(child);
        }
    }
}
