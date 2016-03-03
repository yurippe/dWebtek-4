/**
 * Created by Kristian on 3/3/2016.
 */

$(function(){




    loadItems();
});

function loadItems(){


    $.get("rest/shop/items", null, function(data, textStatus) {

        $("#products").html("<table></table>");
        $.each(data, function(index, element){

            $tablerow = "<tr><td>" + element.itemName + "</td><td>" + element.itemDescription + "</td>";
            $tablerow += "<td>" + element.itemPrice + "</td><td>";
            $tablerow += "<button type=\"button\" class=\"addToCart\" data-itemid=\"" + element.itemID + "\">";
            $tablerow += "add to cart</button>";
            $tablerow += "</td></tr>";
            $("#products table").append($tablerow);
        })

        $("button.addToCart").click(function(){
            $itemid = $(this).attr("data-itemid");
            $.post("rest/shop/addtocart", {itemid : $itemid, amount: 1}, function(data, textStatus){
                
                updateCart();

            }, "json");
        })

    }, "json");


}

function updateCart(){

    $.get("rest/shop/shoppingcart", null, function(data, textStatus) {

        $("#shoppingcart").html("<table></table>");

        if(data.status === "error"){
            alert(data.message);
        }
        else {
            $.each(data, function(index, element){

                $("#shoppingcart table").append("<tr><td>" + element.itemid + "</td><td>" + element.amount + "</td></tr>")

            });

        }

    }, "json");
}