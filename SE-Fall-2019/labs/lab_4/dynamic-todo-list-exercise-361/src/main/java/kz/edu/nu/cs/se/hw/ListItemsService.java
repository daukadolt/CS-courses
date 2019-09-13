package kz.edu.nu.cs.se.hw;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/items")
public class ListItemsService {
    
    private List<String> list = new CopyOnWriteArrayList<String>();
    public ListItemsService() {
        for (int i = 0; i < 20; i++) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            list.add("Entry " + String.valueOf(i) + ", created at: " + timestamp);
        }
        
        Collections.reverse(list);
    }
    
    @GET
    public Response getList() {
        Gson gson = new Gson();

        return Response.ok(gson.toJson(list)).build();
    }
    
    @GET
    @Path("{id: [0-9]+}")
    public Response getListItem(@PathParam("id") String id) {
        int i = Integer.parseInt(id);
        
        return Response.ok(list.get(i)).build();
    }
    
    @POST
    public Response postListItem(@FormParam("newEntry") String entry) {
        if(entry.matches("[ \t]*")) {
            return Response.status(406).build();
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        list.add(0, entry + ", created at: " + timestamp);
        return Response.ok().build();
    }

    @DELETE
    public Response clearList() {
        list.clear();
        return Response.ok().build();
    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteSelectedItem(@PathParam("id") String id) {
        int theId = Integer.parseInt(id);
        if(theId < 0 || theId>list.size()) {
            Response.status(406).build();
        } else {
            list.remove(theId);
        }
        return Response.ok().build();
    }
}
