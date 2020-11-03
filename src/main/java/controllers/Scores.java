package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Scores/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Scores {
    @GET
    @Path("getscore/{ScoreID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckLogin(@PathParam("ScoreID") Integer ScoreID) {
        System.out.println("Invoked Scores.GetScore() with UserID " + UserID);
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT ScoreValue FROM Scores WHERE ScoreID = ?");
            ps.setInt(1, ScoreID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()) {
                response.put("UserID", ScoreID);
                response.put("Score", results.getString(1));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, this account does not yet have a score.\"}";
        }
    }
}
