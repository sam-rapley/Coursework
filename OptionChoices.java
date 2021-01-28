package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("optionChoices/{UserID}")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class OptionChoices {
    @GET
    @Path("getChoices")
    //sets up the function to get the option choices of the required user from the database
    public String getChoices(@PathParam("UserID") Integer UserID){
        System.out.println("Invoked optionChoices.GetChoices()");
        JSONArray choices = new JSONArray();
        try {
            //sets up the sql statement to get the optionIDs and their associated choice values from the database
            PreparedStatement getChoices = Main.db.prepareStatement("SELECT OptionID, Choice FROM optionChoices WHERE UserID = ?");
            //executes the query
            ResultSet results = getChoices.executeQuery();
            //adds the next set of data to the array while there is still data to add
            while (results.next()) {
                //sets up a new row to store the next set of data
                JSONObject row = new JSONObject();
                //adds the data to the row
                row.put("OptionID", results.getInt(1));
                row.put("Choice", results.getString(2));
                //adds the data to the array
                choices.add(row);
            }
            System.out.println(choices);
            //returns the option IDs and choices
            return choices.toString();
            //returns that there has been an error
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @POST
    @Path("addOption")
    public String OptionAdd(@FormDataParam("UserID") Integer UserID, @FormDataParam("Music") Integer Music, @FormDataParam("Sfx") Integer Sfx, @FormDataParam("Difficulty") String Difficulty, @FormDataParam("Graphics") String Graphics)throws Exception {
        System.out.println("Invoked Users.DeleteUser()");
        if (UserID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try{
            PreparedStatement toAdd = Main.db.prepareStatement("INSERT INTO OptionChoices VALUES (?,?,?,?,?) WHERE UserID = ?");
            toAdd.setInt(1, Music);
            toAdd.setInt(2, Sfx);
            toAdd.setString(3, Difficulty);
            toAdd.setString(4, Graphics);
            toAdd.setInt(5, UserID);
            toAdd.execute();
            return "{\"OK\": \"Option choices changed\"}";
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to change options, please see server console for more info.\"}";
        }
    }
}
