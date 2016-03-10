package dk.teamrisk.Easy;

import dk.teamrisk.XML.*;
import org.jdom2.JDOMException;
import org.xml.sax.InputSource;
import org.jdom2.Document;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * One object to take care of everything XML!
 */
public class EasyXML {
    //The namespace used
    public static Namespace XML_NAMESPACE = Namespace.getNamespace("http://www.cs.au.dk/dWebTek/2014");

    //Our top secret shop key! Don't tell anyone!
    public static String SHOP_KEY = "E14ACE44967AC3B0046F0328";
    public static String BASE_URL = "http://webtek.cs.au.dk/cloud"; //"http://localhost:8080/rest/cloud";
    //An empty itemDescription. Is more useful, than you may think.
    public static String emptyDescriptionDocument = "<w:document xmlns:w=\"" + XML_NAMESPACE.getURI() + "\">Description</w:document>";

    /**
     * Sends a createItem request followed up with a ModifyItem request.
     * @param itemToCreate The item to be created
     * @return EasyXMLResponse, that is the response sent back to us.
     */
    public static EasyXMLResponse createItem(Item itemToCreate){
        EasyXMLResponse feedback = new EasyXMLResponse();

        CreateItem newItem = new CreateItem(itemToCreate);

        //send newItem as xml to the correct REST endpoint
        EasyHTTP createItemEndpoint = new EasyHTTP(BASE_URL + "/createItem");

        //Check the item to create for validity
        EasyXMLResponse xmlResponse = newItem.constructXML();
        if(!xmlResponse.wasSuccessful()){
            return xmlResponse;
        }

        //Send the createItem request
        EasyResponse response = createItemEndpoint.postHTTP(xmlResponse.getResponse());

        //If the item to create is malformed or other bad things happened, then return the feedback object.
        if(!(response.wasSuccessful())){return feedback.fromEasyResponse(response);}

        //Put together the XML response as a JDOM object.
        SAXBuilder documentBuilder = new SAXBuilder();
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(response.getResponse()));

        Document itemID;
        try {
            itemID = documentBuilder.build(inputSource);
        } catch (JDOMException e) {
            return feedback.setErrorMessage("XML from server not valid").setFatalException(e);
        } catch (IOException e) {
            return feedback.setErrorMessage("IOException").setFatalException(e);
        }

        //Set the itemID of the item to create to the assigned ID
        itemToCreate.setItemID(Integer.parseInt(itemID.getRootElement().getValue()));

        //Send the modifyItem request with said item.
        return modifyItem(itemToCreate);
    }

    /**
     * Downloads all items from the cloud and makes an ArrayList of said items
     * @return A list of all items in our shop
     */
    public static List<Item> listItems() {
        //Set up the connection
        EasyHTTP listItemsEndpoint = new EasyHTTP(BASE_URL + "/listItems?shopID=96");
        EasyResponse response = listItemsEndpoint.getHTTP();

        //Check if the getRequest was successful
        if (!(response.wasSuccessful())) { return null; }

        //Save the retrieved list into an items object
        Items allItems = null;
        try {
            allItems = new Items(response.getResponse());
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

        //Get the deleted items
        EasyHTTP deletedItemsEndpoint = new EasyHTTP(BASE_URL + "/listDeletedItemIDs?shopID=96");
        response = deletedItemsEndpoint.getHTTP();

        //Check if the getRequest was successful
        if (!(response.wasSuccessful())) { return null; }

        //Save the retrieved list into an itemIDs object
        ItemIDs deletedItems = null;
        try {
            deletedItems = new ItemIDs(response.getResponse());
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

        // Create a hash map and fill it with the items and their IDs as keys
        Map<Integer, Item> itemMap = new HashMap<>();
        for (Item item : allItems.getList()) itemMap.put(item.getItemID(), item);

        // Remove items that occur in the deletedList from the hash map
        for (int deletedItemID : deletedItems.getList()) itemMap.remove(deletedItemID);

        // Return the values as a list
        return new ArrayList<>(itemMap.values());
    }

    /**
     * Downloads all items from the cloud and makes an ArrayList of said items
     * @return A list of all items in our shop
     */
    public static Map<Integer, Item> mapItems(){
        List<Item> list = listItems();

        HashMap<Integer, Item> map = new HashMap<>();

        for(Item i : list){
            map.put(i.getItemID(), i);
        }

        return map;
    }


    /**
     * Gets an item from the cloud based on its ID
     * @param itemID The ID of the item to be downloaded
     * @return The item Downloaded
     */
    public static Item getItem(int itemID){
        //Downloads all items for this shop
        List<Item> items = listItems();

        //Check, if something went wrong
        if(items == null){return null;}

        //Find the item with the same ID as requested
        for(Item item : items){
            if(item.getItemID() == itemID){
                //Praise the sun!
                return item;
            }
        }
        //We didn't find it
        return null;
    }

    /**
     * Sends a modifyItem request.
     * @param itemToMod The modified item
     * @return An EasyXMLResponse object, making you safely look at the outcome
     */
    public static EasyXMLResponse modifyItem(Item itemToMod){
        EasyXMLResponse feedback = new EasyXMLResponse();

        //A lot of the work has been delegated to a ModifyItem object
        ModifyItem modifyItem = new ModifyItem(itemToMod);

        //Check the item for validity
        EasyXMLResponse xmlResponse = modifyItem.constructXML();
        if(!(xmlResponse.wasSuccessful())){
            return xmlResponse;
        }

        //Set up the connection
        EasyHTTP modifyItemEndpoint = new EasyHTTP(BASE_URL + "/modifyItem");

        //Send the modifyItem request
        EasyResponse response = modifyItemEndpoint.postHTTP(xmlResponse.getResponse());

        //Return how well it went.
        return feedback.fromEasyResponse(response);
    }

    /**
     * Sends an adjustItemStock request based on an ID and an adjustment
     * @param itemID The ID of the item to be adjusted
     * @param adjustment The adjustment to be made
     * @return EasyXMLResponse object
     */
    public static EasyXMLResponse adjustItemStock(int itemID, int adjustment){
        EasyXMLResponse feedback = new EasyXMLResponse();

        AdjustItemStock adjustItemStock = new AdjustItemStock(itemID, adjustment);

        //Attempt to create an adjust XML
        EasyXMLResponse xmlResponse = adjustItemStock.constructXML();
        if(!(xmlResponse.wasSuccessful())){
            return xmlResponse;
        }

        //Setting up the HTTP connection to the server
        EasyHTTP modifyItemEndpoint = new EasyHTTP(BASE_URL + "/adjustItemStock");

        //Sending the adjustment
        EasyResponse response = modifyItemEndpoint.postHTTP(xmlResponse.getResponse());

        //Return how well it went
        return feedback.fromEasyResponse(response);
    }

    public static EasyXMLResponse sellItems(int itemID, int customerID, int amount){

        EasyXMLResponse feedback = new EasyXMLResponse();

        SellItems sellItems = new SellItems(itemID, customerID, amount);


        //Attempt to create a sellItem XML
        EasyXMLResponse xmlResponse = sellItems.constructXML();
        if(!xmlResponse.wasSuccessful()){
            return xmlResponse;
        }

        //Setting up the HTTP connection to the server
        EasyHTTP sellItemEndpoint = new EasyHTTP(BASE_URL + "/sellItems");

        //Upload selling
        EasyResponse response = sellItemEndpoint.postHTTP(xmlResponse.getResponse());

        //Return our possible happiness...
        return feedback.fromEasyResponse(response);
    }

    /**
     * Create customer
     */
    public static EasyXMLResponse createCustomer(String username, String password){

        EasyXMLResponse feedback = new EasyXMLResponse();

        CreateCustomer createCustomer = new CreateCustomer(username, password);

        //Setting up the HTTP connection to the server
        EasyHTTP createCustomerEndpoint = new EasyHTTP(BASE_URL + "/createCustomer");

        //Attempt to create an createCustomer XML
        EasyXMLResponse xmlResponse = createCustomer.constructXML();
        if(!(xmlResponse.wasSuccessful())){
            return xmlResponse;
        }

        //Sending createCustomer to the cloud server
        EasyResponse response = createCustomerEndpoint.postHTTP(xmlResponse.getResponse());

        EasyXMLResponse createResponse = createCustomer.wasSuccessful(response);
        if(createResponse.wasSuccessful()){
            return createResponse;
        } else {
            return createResponse;
        }
    }

    public static EasyXMLResponse loginCustomer(String username, String password){

        EasyXMLResponse feedback = new EasyXMLResponse();

        Login login = new Login(username, password);

        //Setting up the HTTP connection to the server
        EasyHTTP loginCustomerEndpoint = new EasyHTTP(BASE_URL + "/login");

        //Attempt to create an createCustomer XML
        EasyXMLResponse xmlResponse = login.constructXML();
        if(!(xmlResponse.wasSuccessful())){
            return xmlResponse;
        }

        //Sending createCustomer to the cloud server
        EasyResponse response = loginCustomerEndpoint.postHTTP(xmlResponse.getResponse());


        EasyXMLResponse loginResponse = login.wasSuccessful(response);
        if(loginResponse.wasSuccessful()){
            return loginResponse;
        } else {
            feedback.setResponse(loginResponse.getErrorMessage()).setErrorMessage(loginResponse.getErrorMessage());
        }

        return feedback;
    }
}