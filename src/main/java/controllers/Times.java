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
public class Times {
    @GET
    @Path("list")
    public String ScoresList(){
        System.out.println("Invoked Times.TimesList");
        JSONArray response = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT TimeID, UserID, TimeValue FROM Times WHERE TimeID = ? ORDER BY TimeValue DESC ");
            ResultSet results = ps.executeQuery();
            while(results.next()){
                JSONObject row = new JSONObject();
                row.put("TimeID", results.getInt(1));
                row.put("UserID", results.getInt(2));
                row.put("TimeValue", results.getInt(3));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items. Error code xx.\"}";
        }

    }
}
