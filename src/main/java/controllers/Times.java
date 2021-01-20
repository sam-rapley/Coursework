package controllers;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("times/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class Times {
    @GET
    @Path("list")
    //sets up the function to get a list of times - to be used for the 'times' leaderboard
    public String TimesList(){
        System.out.println("Invoked Times.TimesList");
        //sets up a new array to score the times
        JSONArray times = new JSONArray();
        try{
            //sets up the sql query to get the times and other required data for the leaderboard from the database
            PreparedStatement getTime = Main.db.prepareStatement("SELECT TimeID, UserID, TimeValue FROM Times ORDER BY TimeValue DESC ");
            //executes the query
            ResultSet results = getTime.executeQuery();
            //adds the next set of data to the array while there is still data to add
            while(results.next()){
                //sets up the variable 'row' as a JSON object to store the data
                JSONObject row = new JSONObject();
                //adds the current data to the current row
                row.put("TimeID", results.getInt(1));
                row.put("UserID", results.getInt(2));
                row.put("TimeValue", results.getInt(3));
                //adds the data in the row to the response
                times.add(row);
            }
            //returns the times etc.
            return times.toString();
            //returns that there has been an error in getting the time data
        } catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items. Error code xx.\"}";
        }

    }
}
