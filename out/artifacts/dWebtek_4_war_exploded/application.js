/**
 * Created by Kristian on 3/3/2016.
 */

var $loginform;
$loginform  = "<p id=\"loginerror\"></p><label>Username <input type=\"text\" name=\"username\" /></label>\n";
$loginform += "<label>Password <input type=\"password\" name=\"password\"/></label>\n";
$loginform += "<button type=\"button\" id=\"loginbutton\" >Log in</button>\n";
$loginform += "<button type=\"button\" id=\"signupbutton\" >Sign up</button>\n";



$(function(){

    isLoggedIn();
    loadItems();
});

function loadItems(){


    $.get("rest/shop/items", null, function(data, textStatus) {

        if(data.status === "error"){
            $("#products").html("<h1>an error occured</h1>");
            return;
        }

        $("#products").html("<table></table>");
        $.each(data.data, function(index, element){

            $tablerow = "<tr><td>" + element.itemName + "</td><td>" + element.itemDescription + "</td>";
            $tablerow += "<td>" + element.itemPrice + "</td><td>";
            $tablerow += "<button type=\"button\" class=\"addToCart\" data-itemid=\"" + element.itemID + "\">";
            $tablerow += "add to cart</button>";
            $tablerow += "</td></tr>";
            $("#products table").append($tablerow);
        })

        $("button.addToCart").click(function(){
            $itemid = $(this).attr("data-itemid");
            $.post("rest/shop/addtocart", {itemID : $itemid, amount: 1}, function(data, textStatus){

                if(data.status === "ok") {
                    updateCart();
                } else {
                    alert(data.message);
                }

            }, "json");
        })

    }, "json");


}

function updateCart(){

    $.get("rest/shop/getshoppingcart", null, function(data, textStatus) {

        $("#shoppingcart").html("<table></table>");

        if(data.status === "error"){
            alert(data.message);
        }
        else {
            $.each(data.data, function(index, element){

                $("#shoppingcart table").append("<tr><td>" + element.itemID + "</td><td>" + element.itemName + "</td><td>" + element.amount + "</td></tr>")

            });

        }

    }, "json");
}

function isLoggedIn() {

    $.get("rest/user/user", null, function(data, textStatus){
        //User is not logged in, present the login form:
        if(data.status === "error"){
            $("#login").html($loginform);
            //Also attach click listeners for the buttons:

            $("#signupbutton").click(function () {
                $uname = $("#login input[name=\"username\"]").val();
                $pass = $("#login input[name=\"password\"]").val();
                $.post("rest/user/createuser", {username: $uname, password: $pass}, function(data){

                    if(data.status === "error"){
                        $("#loginerror").html(data.message);
                    } else {
                        $("#loginbutton").click();
                    }

                }, "json")
            });

            $("#loginbutton").click(function () {
                $uname = $("#login input[name=\"username\"]").val();
                $pass = $("#login input[name=\"password\"]").val();
                $.post("rest/user/login", {username: $uname, password: $pass}, function(data){

                    if(data.status === "error"){
                        $("#loginerror").html(data.message);
                    } else {
                        isLoggedIn();
                    }

                }, "json")
            });


            return;
        }

        else {
            $("#login").html("<p>Welcome back: " + data.data.username + "</p><button type=\"button\" id=\"logoutbutton\">Log out</button>");
            $("#logoutbutton").click(function(){
                $.get("rest/user/logout", null, function(data){}, "json");
                setTimeout(function(){
                    isLoggedIn();
                }, 500);
            });
        }

    }, "json");


}