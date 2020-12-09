"use strict";
function getUsersList(){
    console.log("invoked users.getUsersList()");
    const url = "/users/list";
    fetch(url, {
        method:"GET",
        }).then(response =>{
            return response.json();
        }).then(response=>{
            if (response.hasOwnProperty("Error")){
                alert(JSON.stringify(response));
            }else{
                formatUsersList(response);
            }
        });
}

function formatUsersList(JSONArray){
    let dataHTML = "";
    for(let item of JSONArray){
        dataHTML += "<tr><td>" + item.UserID + "<tr><td>" + item.UserName + "<tr><td>"
    }
    document.getElementById("UsersTable").innerHTML = dataHTML;
}

function getUser(){
    console.log("invoked users.getUser()");
    const UserID = document.getElementById("UserID").value;
    const url = "/users/getUser"
    fetch(url + UserID,{
        method:"GET",}).then(response =>{
            return response.json();
    }).then(response=>{
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response))
        }else{
            document.getElementById("DisplayOneUser").innerHTML = response;
        }
    });
}

function addUser(){
    console.log("invoked users.addUser()");
    const formData = new FormData(document.getElementById('InputUserDetails'));
    let url = "/users/add";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response =>{
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response))
        }else{
            window.open("/client/menu.html", "_self");
        }
    });
}

function loadOption(){
    window.open("/client/option.html", "_self");
}

function loadGame(){
    window.open("/client/game.html", "_self");
}

function checkAdmin(){
    console.log("invoked users.checkAdmin");
    const UserID = document.getElementById("UserID").value;
    const url = "/users/checkAdmin"
    fetch(url + UserID,{
        method:"GET",}).then(response =>{
        return response.json();
    }).then(response=>{
        if(response.hasOwnProperty("Error")){
            alert(JSON.stringify(response))
        }else{
            window.open("/client/admin.html", "_self")
        }
    });
}
