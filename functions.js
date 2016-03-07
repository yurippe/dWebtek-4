/**
 * Created by Kristian on 3/6/2016.
 */

var $itemdata = {};

/** Create the product html based on an item from rest/shop/items
 * @param item
 * @returns {string|*}
 */
function createProductHTML(item){

    $prodhtml  = "<div class=\"product\"><div><a class=\"imgLink\" data-itemid=\"" + item.itemID + "\"><img src=\"";
    $prodhtml += item.itemURL + "\" alt=\"\" class=\"product_image\"></a>";
    $prodhtml += "<p><a class=\"imgLink\" data-itemid=\"" + item.itemID + "\">";
    $prodhtml += item.itemName + "</a><br>" + item.itemPrice + ",-<br /><input type=\"number\" value=\"1\" ";
    $prodhtml += "class=\"noOfItemsForm\" data-itemid=\"" + item.itemID + "\" > ";
    $prodhtml += "<button type=\"button\" class=\"addToCart\" data-itemid=\"" + item.itemID + "\"";
    $prodhtml += ((Number(item.itemStock) <= 0) ? "disabled" : "") + ">";
    $prodhtml += ((Number(item.itemStock) <= 0) ? "out of stock" : "add to cart") + "</button>";
    $prodhtml += "<br />(" + item.itemStock + " in stock)";
    $prodhtml += "</p></div>" + item.itemDescription + "</div>";

    return $prodhtml;
}

function loadItems(){


    $.get("rest/shop/items", null, function(data, textStatus) {

        if(data.status === "error"){
            $("#products").html("<h1>an error occured</h1>");
            return;
        }

        //$("#products").html("<table></table>");
        $("#products").html("");
        $.each(data.data, function(index, element){

            $tablerow = createProductHTML(element);
            $itemdata[String(element.itemID)] = {name: element.itemName,    price: Number(element.itemPrice),
                                                stock: Number(element.itemStock),   pictureurl: element.itemURL};
            $("#products").append($tablerow);
        })

        $("button.addToCart").click(function(){
            addToCart(this);
        });

        $("a.imgLink").click(function () {
            focusProduct($(this).attr("data-itemid"));
        });

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
            $("#logoutbutton").css("display", "");

            updateCart();
        }

    }, "json");

}