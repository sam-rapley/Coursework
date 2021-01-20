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
    @Path("add")
    //sets up the function to add a new option choice or change an existing one
    public String addChoice(@FormDataParam("OptionID") Integer OptionID, @FormDataParam("UserID") Integer UserID, @FormDataParam("Choice") String Choice){
        System.out.println("Invoked optionChoices.add()");
        try{
            //sets up the sql statement to add the user's option choice to the database
            PreparedStatement addChoice = Main.db.prepareStatement("INSERT INTO OptionChoices (OptionID, UserID, Choice) VALUES (?, ?, ?)");
            //adds the required data into the sql statement
            addChoice.setInt(1, OptionID);
            addChoice.setInt(2, UserID);
            addChoice.setString(3, Choice);
            //executes the query
            addChoice.execute();
            return "{\"OK\": \"Added choice.\"}";
            //returns that there has been an error in adding the choice
        } catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to add new item, please see server console for more info.\"}";
        }
    }
}
