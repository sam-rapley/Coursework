package controllers;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("users/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class Users{
    //Tells the server to fetch something from the database
    @GET
    //runs the function if users/list is sent
    @Path("list")
    public String UsersList() {
        //to show that the function has started
        System.out.println("Invoked Users.UsersList()");
        //sets up a new JSON array to return the list of users in the database
        JSONArray list = new JSONArray();
        //try and catch used in case of any errors
        try {
            //prepares the SQL statement to send to the database
            PreparedStatement listSQL = Main.db.prepareStatement("SELECT UserID, UserName FROM Users");
            //executes the query on the database
            ResultSet results = listSQL.executeQuery();
            //formats the results into a JSON array
            while (results.next()) {
                //sets up a new row as a JSON object
                JSONObject row = new JSONObject();
                //adds the id and username to the current row
                row.put("UserID", results.getInt(1));
                row.put("UserName", results.getString(2));
                //adds the data in the row to the response
                list.add(row);
                //prints the id from the request within the IDE
                System.out.println(results.getInt(1));
            }
            //prints the array
            System.out.println(list);
            //returns the list to where it was called as a string 
            return list.toString();
            //catches any errors
        } catch (Exception exception) {
            //prints what the error was
            System.out.println("Database error: " + exception.getMessage());
            //tells the user that there has been an error
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }
    //tells the server to add something to the database
    @POST
    @Path("add")
    //sets up the function with the parameters needed to create a new user
    public String UsersCreate(@FormDataParam("UserID") Integer UserID, @FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password, @FormDataParam("Email") String Email, @FormDataParam("Validated") Boolean Validated ) {
        System.out.println("Invoked Users.UsersCreate()");
        try {
            //sets up the SQL statement to add a new user to the database
            PreparedStatement newUser = Main.db.prepareStatement("INSERT INTO Users (UserID, UserName, Password, Email, Validated) VALUES (?, ?, ?, ?, ?)");
            //inserts the values passed into the function where the question marks (unknown values) are in the SQL statement
            newUser.setInt(1, UserID);
            newUser.setString(2, UserName);
            newUser.setString(3, Password);
            newUser.setString(4, Email);
            newUser.setBoolean(5, Validated);
            //executes the SQL statement
            newUser.execute();
            return "{\"OK\": \"Added user.\"}";
            //returns that there was an error and what said error entails
        } catch (Exception exception) {
            exception.printStackTrace();
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }
    @GET
    @Path("checklogin/{UserName}")
    //sets up the function to check whether a login is correct given the user's username
    public String CheckLogin(@PathParam("UserName") String UserName){
        System.out.println("Invoked Users.CheckLogin() with UserName " + UserName);
        try {
            //sets up the SQL statement to get the matching UserId, password and token from the database to compare with those provided
            PreparedStatement check = Main.db.prepareStatement("SELECT UserID, Password FROM Users WHERE UserName = ?");
            //places the username in the sql statement where the ? is
            check.setString(1, UserName);
            //executes the db query
            ResultSet results = check.executeQuery();
            // sets up a JSON object to store the details of the user given
            JSONObject details = new JSONObject();
            //fills the JSON object with the required data
            if (results.next()) {
                details.put("UserName", UserName);
                details.put("UserID", results.getString(1));
                details.put("Password", results.getString(2));
            }
            //returns the details of the user with the given username
            return details.toString();
            //returns an error (most likely would be if the username entered is wrong)
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to check login, this account does not exist.\"}";
        }
    }
    @GET
    @Path("checkadmin/{UserName}")
    //sets up the function to check whether a user with a given username is an admin
    public String CheckAdmin(@PathParam("UserName") String UserName) {
        System.out.println("Invoked Users.CheckAdmin() with UserName " + UserName);
        try{
            //sets up the SQL query to get the admin status of the given user
            PreparedStatement checkAdmin = Main.db.prepareStatement("SELECT Admin FROM Users WHERE UserName = ?");
            //adds the username to the query in place of the ?
            checkAdmin.setString(1, UserName);
            //executes the
            ResultSet results = checkAdmin.executeQuery();
            //sets up a new object to store the result of the query
            JSONObject admin = new JSONObject();
            //adds the username and their admin status to the object
            if (results.next()) {
                admin.put("UserName", UserName);
                admin.put("Admin", results.getString(1));
            }
            return admin.toString();
            //returns that there has been an error, again most likely if the username is incorrect
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to run check, this account does not exist.\"}";
        }
    }
    @POST
    @Path("update")
    //sets up the function to update a user's details given their id
    public String UsersUpdate(@FormDataParam("UserID") Integer UserID, @FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password, @FormDataParam("Email") String Email) {
        System.out.println("Invoked Users.UpdateUser() with UserID " + UserID);
        try {
            //sets up the sql query to update the user's details
            PreparedStatement update = Main.db.prepareStatement("UPDATE Users SET (UserName, Password, Email) = (?, ?, ?) WHERE UserID = ?");
            //gives the query the required data
            update.setString(1, UserName);
            update.setString(2, Password);
            update.setString(3, Email);
            update.setInt(4, UserID);
            //executes the query
            update.execute();
            return "{\"OK\": \"Updated user.\"}";
            //tells the user that there has been an error in updating the user's details
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("delete/{UserID}")
    //sets up the function to delete a user given their id
    public String DeleteUser(@PathParam("UserID") Integer UserID) throws Exception {
        System.out.println("Invoked Users.DeleteUser()");
        //immediately gives an error message if a user id hasn't been entered
        if (UserID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        //attempts to delete the requested user
        try {
            //sets up the sql query to delete the user
            PreparedStatement toDelete = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            //adds the userID into the sql query
            toDelete.setInt(1, UserID);
            //executes the query
            toDelete.execute();
            return "{\"OK\": \"User deleted\"}";
            //tells the user that there has been an error in deleting the required data
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
}

