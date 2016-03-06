/**
 * Created by Kristian on 3/6/2016.
 */

var $cart = {items: {}, sum: 0};

function createCartHTML(element){

    return "<tr><td>" + element.itemName + " (" + element.itemID + ")</td><td>" +
    element.itemPrice + ",- each</td><td>amount: " + "<span data-itemid=\"" + element.itemID + "\">" +
    element.amount + "</span></td></tr>";

}

function updateCart(){

    $.get("rest/shop/getshoppingcart", null, function(data, textStatus) {

        $("#shoppingcartitems").html("<table></table>");
        if(data.status === "error"){
            alert(data.message);
        }
        else {
            $.each(data.data.items, function(index, element){

                $("#shoppingcart table").append(createCartHTML(element));

                $cart.items[String(element.itemID)] = {amount: Number(element.amount), price: Number(element.itemPrice)};

            });

            $("#shoppingcart span.totalPrice").html(data.data.data.sum);
            $cart.sum = Number(data.data.data.sum);

        }

    }, "json");
}


function addToCart(buttonClicked){
    $itemid = $(buttonClicked).attr("data-itemid");
    $count = Number($("#products input[data-itemid=\"" + $itemid + "\"]").val()); //the amount they want to add to the cart


    $product = $cart.items[$itemid];

    if($count < 1){alert("you need to add at least 1 item"); return;}

    if(!($product === undefined)){
        //we already have some of this product in the shopping cart
        if($count + $product.amount <= $itemdata[$itemid].stock){
            //all good
        } else {
            alert("We do not have that many of that item in stock");
            return;
        }

    } else {
        //Just need to make sure that $count is <= stock
        if($count <= $itemdata[$itemid].stock){
            //all good
        } else {
            alert("We do not have that many of that item in stock");
            return;
        }
    }

    $.post("rest/shop/addtocart", {itemID : $itemid, amount: $count}, function(data, textStatus){

        if(data.status === "ok") {
            updateCart();
        } else {
            alert(data.message);
        }

    }, "json");

}