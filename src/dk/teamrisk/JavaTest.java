package dk.teamrisk;

import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.XML.Item;

import java.util.List;

/**
 * Created by Kristian on 3/3/2016.
 */
public class JavaTest {

    public static void main(String[] args){

        List<Item> items = EasyXML.listItems();
        for(Item i : items){
            try {
                i.setItemURL("img/nopic.jpg");
                EasyXML.modifyItem(i);
            } catch (Exception e){
                System.out.println(i.getItemName());
            }
        }
    }
}
