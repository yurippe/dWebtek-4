/**
 * Created by Kristian on 3/3/2016.
 */

var $loginform;
$loginform  = "<p id=\"loginerror\"></p><label>Username <input type=\"text\" name=\"username\" /></label>\n";
$loginform += "<label>Password <input type=\"password\" name=\"password\"/></label>\n";
$loginform += "<button type=\"button\" id=\"loginbutton\" >Log in</button>\n";
$loginform += "<button type=\"button\" id=\"signupbutton\" >Sign up</button>\n";



$(function(){

    //Set up REST calls for signup button
    $("#signupbutton").click(function () {
        $uname = $("#login input[name=\"username\"]").val();
        $pass = $("#login input[name=\"password\"]").val();

        if($uname.length < 3 ||$pass.length < 3){
            $("#loginerror").html("Username and password must be at least 3 characters long");
            return;
        }

        $("#loginerror").html("Creating user ... please wait");
        $.post("rest/user/createuser", {username: $uname, password: $pass}, function(data){

            if(data.status === "error"){
                $("#loginerror").html(data.message);
            } else {
                //hack for auto login
                $("#loginbutton").click();
            }

        }, "json")
    });

    //Set up REST calls for login button
    $("#loginbutton").click(function () {
        $uname = $("#login input[name=\"username\"]").val();
        $pass = $("#login input[name=\"password\"]").val();
        if($uname.length < 3 ||$pass.length < 3){
            $("#loginerror").html("Username and password must be at least 3 characters long");
            return;
        }
        $("#login").css("display", "none");
        $("#loggedin").css("display", "");
        $("#loggedinerror").html("Logging in ... please wait").css("display", "");
        $("#welcomeback").css("display", "none");
        $.post("rest/user/login", {username: $uname, password: $pass}, function(data){

            if(data.status === "error"){
                $("#loginerror").html(data.message);
            } else {
                isLoggedIn();
            }

        }, "json")
    });

    $("#logoutbutton").click(function(){
        $.get("rest/user/logout", null, function(data){}, "json");
        setTimeout(function(){
            isLoggedIn();
        }, 100);
    });

    isLoggedIn();
    loadItems();
});

function loadItems(){


    $.get("rest/shop/items", null, function(data, textStatus) {

        if(data.status === "error"){
            $("#products").html("<h1>an error occured</h1>");
            return;
        }

        //$("#products").html("<table></table>");
        $("#products").html("");
        $.each(data.data, function(index, element){

            /*$tablerow = "<tr><td>" + element.itemName + "</td><td>" + element.itemDescription + "</td>";
            $tablerow += "<td>" + element.itemPrice + "</td><td>";
            $tablerow += "<button type=\"button\" class=\"addToCart\" data-itemid=\"" + element.itemID + "\">";
            $tablerow += "add to cart</button>";
            $tablerow += "</td></tr>";*/
            $tablerow = createProductHTML(element);
            $("#products").append($tablerow);
        })

        $("button.addToCart").click(function(){
            $itemid = $(this).attr("data-itemid");
            $count = $("#products input[data-itemid=\"" + $itemid + "\"]").val();
            if($count < 1){alert("you need to add at least 1 item"); return;}
            $.post("rest/shop/addtocart", {itemID : $itemid, amount: $count}, function(data, textStatus){

                if(data.status === "ok") {
                    updateCart();
                } else {
                    alert(data.message);
                }

            }, "json");
        })

        $("a.imgLink").click(function () {
           focusProduct($(this).attr("data-itemid"));
        });

    }, "json");


}

function updateCart(){

    $.get("rest/shop/getshoppingcart", null, function(data, textStatus) {

        $("#shoppingcartitems").html("<table></table>");
        if(data.status === "error"){
            alert(data.message);
        }
        else {
            $.each(data.data.items, function(index, element){

                $("#shoppingcart table").append("<tr><td>" + element.itemName + "(" + element.itemID + ")</td><td>" +
                    element.itemPrice + ",- each</td><td>amount: " + element.amount + "</td></tr>")

            });

            $("#shoppingcart span.totalPrice").html(data.data.data.sum);

        }

    }, "json");
}

function isLoggedIn() {

    $.get("rest/user/user", null, function(data, textStatus){
        //User is not logged in, present the login form:
        if(data.status === "error"){
            $("#login").css("display", "");
            $("#loggedin").css("display", "none");
            $("#welcomeback").css("display", "none");
        }

        else {
            $("#loggedinerror").html("");
            $("#login").css("display", "none");
            $("#loggedin").css("display", "");
            $("#welcomeback").css("display", "");
            $("#loggedinerror").css("display", "none");
            $("#loggedin span").html(data.data.username);
        }

    }, "json");

}

function createProductHTML(item){

    $prodhtml  = "<div class=\"product\" data-itemid=\"" + item.itemID + "\"><div><a class=\"imgLink\" data-itemid=\"" + item.itemID + "\"><img src=\"";
    $prodhtml += item.itemURL + "\" alt=\"\" class=\"product_image\"></a>";
    $prodhtml += "<p><a class=\"imgLink\" data-itemid=\"" + item.itemID + "\">";
    $prodhtml += item.itemName + "</a><br>" + item.itemPrice + ",-<br /><input type=\"number\" value=\"1\" ";
    $prodhtml += "class=\"noOfItemsForm\" data-itemid=\"" + item.itemID + "\" > ";
    //$prodhtml += "<a href=\"\"> <img src=\"img/cart.svg\" alt=\"\" class=\"cart_icon\"> </a>";
    $prodhtml += "<button type=\"button\" class=\"addToCart\" data-itemid=\"" + item.itemID + "\">add to cart</button>"
    $prodhtml += "</p></div>" + item.itemDescription + "</div>";

    return $prodhtml;
}

function focusProduct(itemid){

}