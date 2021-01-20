"use strict";
//sets up the function to call the listUsers API
function getUsersList(){
    console.log("invoked users.getUsersList()");
    //sets the url to be called
    const url = "/users/list";
    //calls the API
    fetch(url, {
        method:"GET",
        //returns the JSON received
        }).then(response =>{
            return response.json();
            //goes on to check whether the API has returned an error message
        }).then(response=>{
            //if there is an error, displays it as an alert on the webpage
            if (response.hasOwnProperty("Error")){
                alert(JSON.stringify(response));
                // if there is no error, calls the function to format the response
            }else{
                formatUsersList(response);
            }
        });
}
//sets up the function to format the data received from the usersList API
function formatUsersList(JSONArray){
    //creates a new string for the data to be processed into html
    let dataHTML = "";
    //adds the values to dataHTML and formats them as a table
    for(let item of JSONArray){
        dataHTML += "<tr><td>" + item.UserID + "<tr><td>" + item.UserName + "<tr><td>"
    }
    //adds the html to the UsersTable element
    document.getElementById("UsersTable").innerHTML = dataHTML;
}
//sets up the function to call the 'getUser' API
function getUser(){
    console.log("invoked users.getUser()");
    //sets UserID to have the value in a textfield on the webpage
    const UserID = document.getElementById("UserID").value;
    //sets the url to be sent
    const url = "/users/getUser";
    //gets the response of the API
    fetch(url + UserID,{
        method:"GET",
        //returns the response as JSON
    }).then(response =>{
        return response.json();
        //Alerts the user of any errors that have occurred
    }).then(response=>{
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response))
            //sets a textbox to have the values passed in from the API
        }else{
            document.getElementById("DisplayOneUser").innerHTML = response;
        }
    });
}
function checkLogin(){
    console.log("invoked users.checkLogin()");
    //sets username to have the value in a text box on the webpage
    const username = document.getElementById("username").value;

    //sets the url to be sent
    const url = "/users/checklogin/";
    //gets the response of the API
    fetch(url + username,{
        method:"GET",
        //returns the response as JSON
    }).then(response =>{
        return response.json();
        //Alerts the user of any errors that have occurred
    }).then(response=>{
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response));
            //sets a textbox to have the values passed in from the API
        }else{
            if(response.Password === document.getElementById("password").value){
                window.open("/client/menu.html", "_self");
            }else{
                alert("Incorrect password, please try again");
            }
        }
    });
}
//sets up the function to call the addUser API
function addUser(){
    console.log("invoked users.addUser()");
    //sets the value of the data to be sent to the API as the values in InputUserDetails
    const formData = new FormData(document.getElementById('InputUserDetails'));
    //sets the url
    let url = "/users/add";
    //sends the data to the API
    fetch(url, {
        method: "POST",
        body: formData,
        //returns the response from the API
    }).then(response => {
        return response.json()
        //alerts the user if there is an error
    }).then(response =>{
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response))
        //}else{
           // window.open("/client/menu.html", "_self");
        }
    });
}

//sets up the function to call the API checkAdmin
function checkAdmin(){
    console.log("invoked users.checkAdmin");
    //sets the value of userID to the data in a textfield
    const UserID = document.getElementById("UserID").value;
    //sets the url
    const url = "/users/checkAdmin";
    //gets the response of the API
    fetch(url + UserID,{
        method:"GET",
        //returns the response from the API as JSON
    }).then(response =>{
        return response.json();
    }).then(response=>{
        //alerts the user if there is an error
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response))
            //otherwise, opens the admin page
        }else{
            window.open("/client/admin.html", "_self")
        }
    });
}

function addOption(){
    console.log("invoked users.addOption");
    const formData = new FormData(document.getElementById('InputOptionChoices'));
    let url = "/users/addOption";
    fetch(url, {
        method: "POST",
        body: formData,
        //returns the response from the API
    }).then(response => {
        return response.json()
        //alerts the user if there is an error
    }).then(response =>{
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response))
        }
    });
}

//loads the options page in the current window
function loadOption(){
    window.open("/Client/option.html", "_self");
}
//loads the game page in the current window
function loadGame(){
    window.open("/Client/game.html", "_self");
}
//loads the signup page in the current window
function loadSignUp(){
    window.open("/Client/signup.html", "_self");
}
//loads the login page in the current window
function loadLogin(){
    window.open("/Client/login.html", "_self");
}

