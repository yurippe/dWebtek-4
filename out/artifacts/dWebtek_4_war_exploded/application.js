/**
 * Created by Kristian on 3/3/2016.
 */


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
        $("#loginerror").css("display", "");
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
            $("#loginerror").css("display", "");
            return;
        }
        $("#login").css("display", "none");
        $("#loggedin").css("display", "");
        $("#loggedinerror").html("Logging in ... please wait").css("display", "");
        $("#logoutbutton").css("display", "none");
        $("#welcomeback").css("display", "none");
        $.post("rest/user/login", {username: $uname, password: $pass}, function(data){

            if(data.status === "error"){
                $("#login").css("display", "");
                $("#loggedin").css("display", "none");
                $("#loginerror").html(data.message);
                $("#loginerror").css("display", "");
            } else {
                $("#login input[name=\"username\"]").val("");
                $("#login input[name=\"password\"]").val("");
                isLoggedIn();
            }

        }, "json")
    });

    $("#logoutbutton").click(function(){
        $.get("rest/user/logout", null, function(data){}, "json");
        $("#loginerror").css("display", "none");
        $("#logedinerror").css("display", "none");
        setTimeout(function(){
            isLoggedIn();
        }, 100);
    });

    isLoggedIn();
    loadItems();

});









function focusProduct(itemid){

}