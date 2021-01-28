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
            PreparedStatement getScore = Main.db.prepareStatement("SELECT Scores.ScoreValue, Users.UserName FROM Scores INNER JOIN Users ON Scores.UserID = Users.UserID ORDER BY ScoreValue DESC");
            //executes the query
            ResultSet results = getScore.executeQuery();
            //adds the next set of data to the array while there is still data to add
            while(results.next()){
                //sets up the variable 'row' as a JSON object to store the data
                JSONObject row = new JSONObject();
                //adds the current data to the current row
                row.put("ScoreValue", results.getInt(1));
                row.put("UserName", results.getString(2));
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
    @POST
    @Path("add")
    //sets up the function with the parameters needed to create a new user
    public String UsersCreate(@FormDataParam("UserID") Integer UserID, @FormDataParam("ScoreValue") Integer ScoreValue) {
        System.out.println("Invoked scores.AddScore()");
        try {
            //sets up the SQL statement to add a new user to the database
            PreparedStatement newScore = Main.db.prepareStatement("INSERT INTO Scores (UserID, ScoreValue) VALUES (?, ?)");
            //inserts the values passed into the function where the question marks (unknown values) are in the SQL statement
            newScore.setInt(1, UserID);
            newScore.setInt(2, ScoreValue);

            //executes the SQL statement
            newScore.execute();
            return "{\"OK\": \"Added score.\"}";
            //returns that there was an error and what said error entails
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }
}

