package controllers;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("options/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class Options {
    @GET
    @Path("getType")
    //sets up the function to get the type of the option required
    public String getType(@FormDataParam("OptionID") int OptionID){
        try {
            //sets up the sql query to get the option's type from the database
            PreparedStatement getType = Main.db.prepareStatement("SELECT OptionType FROM Options WHERE OptionID = ?");
            //inserts the optionID into the query
            getType.setInt(1, OptionID);
            //executes the query
            ResultSet results = getType.executeQuery();
            //sets up a new JSON object to store the result of the query
            JSONObject type = new JSONObject();
            if (results.next()) {
                //adds the data to the JSON object
                type.put("OptionID", OptionID);
                type.put("OptionType", results.getString(1));
            }
            //returns the option's ID and type
            return type.toString();
            //tells the user that there has been
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get option type, see console for more info.\"}";
        }
    }

}
