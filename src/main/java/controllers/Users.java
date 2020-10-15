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


public class Users {
    @GET
    @Path("list")
    public String UsersList() {
        System.out.println("Invoked Users.UsersList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName FROM Users");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject row = new JSONObject();
                row.put("UserID", results.getInt(1));
                row.put("UserName", results.getString(2));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @POST
    @Path("create")
    public String UsersCreate(@FormDataParam("UserID") Integer UserID, @FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password, @FormDataParam("Email") String Email, @FormDataParam("Validated") Boolean Validated ) {
        System.out.println("Invoked Users.UsersCreate()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserID, UserName, Password, Email, Validated) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, UserID);
            ps.setString(2, UserName);
            ps.setString(3, Password);
            ps.setString(4, Email);
            ps.setBoolean(5, Validated);
            ps.execute();
            return "{\"OK\": \"Added user.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }
    @GET
    @Path("checklogin/{UserName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckLogin(@PathParam("UserName") String UserName) {
        System.out.println("Invoked Users.CheckExists() with UserName " + UserName);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Password, Token FROM Users WHERE UserName = ?");
            ps.setString(1, UserName);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()) {
                response.put("UserName", UserName);
                response.put("UserID", results.getString(1));
                response.put("Password", results.getString(2));
                response.put("Token", results.getInt(3));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, this account does not exist.\"}";
        }
    }
    @GET
    @Path("checkadmin/{UserName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckAdmin(@PathParam("UserName") String UserName) {
        System.out.println("Invoked Users.CheckAdmin() with UserName " + UserName);
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT Admin FROM Users WHERE UserName = ?");
            ps.setString(1, UserName);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()) {
                response.put("UserName", UserName);
                response.put("Admin", results.getString(1));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, this account does not exist.\"}";
        }
    }
    @POST
    @Path("update")
    public String UsersUpdate(@FormDataParam("UserID") Integer UserID, @FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password, @FormDataParam("Email") String Email) {
        System.out.println("Invoked Users.UsersUpdate() with UserID " + UserID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users  SET (UserName, Password, Email) = (?, ?, ?) WHERE UserID = ?");
            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.setString(3, Email);
            ps.setInt(4, UserID);
            ps.execute();
            return "{\"OK\": \"Updated user.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("delete/{UserID}")
    public String DeleteUser(@PathParam("UserID") Integer UserID) throws Exception {
        System.out.println("Invoked Users.DeleteUser()");
        if (UserID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.execute();
            return "{\"OK\": \"User deleted\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

}
