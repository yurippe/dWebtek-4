package dk.teamrisk;

/**
 * Created by Kristian on 3/3/2016.
 */
public class JavaTest {

    public static void main(String[] args){
        System.out.println(EasyXML.createCustomer("kristiankgs", "ksg").getResponse());
        System.out.println("--");
        System.out.println(EasyXML.loginCustomer("kristiang", "ksg").getResponse());
        System.out.println("--");
        System.out.println(EasyXML.loginCustomer("kristiang", "ksssg").getResponse());
        System.out.println("--");
    }
}
