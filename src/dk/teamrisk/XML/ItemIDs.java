package dk.teamrisk.XML;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rasmus on 09/03/2016.
 */
public class ItemIDs {

    //A list of all the items.
    //NOTE: We can not call constuct XML on these items, because itemDescription contains too much
    private List<Integer> itemIDs;

    /**
     * Constructor, creating a list of objects.
     * @param xml The XML object to be pased
     * @throws JDOMException
     * @throws IOException
     */
    public ItemIDs(String xml) throws JDOMException, IOException{

        //Create a Document object from the XML input
        SAXBuilder builder = new SAXBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document xmlDoc = builder.build(is);

        //Create a list of all "item" elements in the document
        this.itemIDs = new ArrayList<>();
        for(Element child : xmlDoc.getRootElement().getChildren()){
            //Safe the data into their respective fields
            int iID = Integer.parseInt(child.getValue());

            //Add the item to the list
            this.itemIDs.add(iID);
        }
    }

    public List<Integer> getList(){ return this.itemIDs; }
}
