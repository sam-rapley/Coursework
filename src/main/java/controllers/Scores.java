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
@Path("scores/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class Scores{
    @GET
    @Path("list")
    //sets up the function to get the scores and related data from the database to be used for the 'scores' leaderboard
    public String ScoresList(){
        System.out.println("Invoked Scores.ScoresList");
        JSONArray scores = new JSONArray();
        try{
            //sets up the sql query to get the score and other required data
            PreparedStatement getScore = Main.db.prepareStatement("SELECT ScoreID, UserID, ScoreValue FROM Scores ORDER BY ScoreValue DESC ");
            //executes the query
            ResultSet results = getScore.executeQuery();
            //adds the next set of data to the array while there is still data to add
            while(results.next()){
                //sets up the variable 'row' as a JSON object to store the data
                JSONObject row = new JSONObject();
                //adds the current data to the current row
                row.put("ScoreID", results.getInt(1));
                row.put("UserID", results.getInt(2));
                row.put("ScoreValue", results.getInt(3));
                //adds the data in the row to the response
                scores.add(row);
            }
            //returns the scores etc.
            return scores.toString();
            //returns that there has been an error in getting the score data
        } catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items. Error code xx.\"}";
        }

    }
}

