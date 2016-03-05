package dk.teamrisk;

import dk.teamrisk.Easy.EasyHTTP;
import dk.teamrisk.Easy.EasyXML;
import dk.teamrisk.XML.Item;

import java.util.List;

/**
 * Created by Kristian on 3/3/2016.
 */
public class JavaTest {

    public static void main(String[] args){

        String d = "<w:document xmlns:w=\"http://www.cs.au.dk/dWebTek/2014\">Example <w:bold>descrip</w:bold><w:italics>tion</w:italics></w:document>";
        Item i = new Item(1339, "Test item 3", "img/noimg.jpg", 50, 2, d);
        System.out.println(i.constructXML().getResponse());


        System.out.println(EasyXML.listItems());

        System.out.println(EasyXML.loginCustomer("test", "test").getErrorMessage());


    }
}
