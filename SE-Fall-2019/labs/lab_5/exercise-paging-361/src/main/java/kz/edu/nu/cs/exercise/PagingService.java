package kz.edu.nu.cs.exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/items")
public class PagingService {

    private List<String> list = new CopyOnWriteArrayList<String>();

    public PagingService() {
        for (int i = 0; i < 102; i++) {
            list.add("Entry " + String.valueOf(i));
        }
    }

    @GET
    public Response getMyList(@QueryParam("page") int page) {
        System.out.println("Received " + page);
        int itemToStartFrom = page*10;
        if(itemToStartFrom<0 || itemToStartFrom >= list.size()) {
            return Response.status(400).build();
        }
        Gson gson = new Gson();
        String json;

        PagedHelper p = new PagedHelper();
        if(page == 0) {
            p.setList(list.subList(0, 10));
        } else if(itemToStartFrom >= 0) {
            p.setList(list.subList(itemToStartFrom,
                    itemToStartFrom+10 > list.size() ? list.size() : itemToStartFrom+10));
        }
        else {
            p.setList(new ArrayList<>());
        }
        p.setPrev("" + (page-1>=0 ? page-1: null));
        p.setNext("" + ((page+1)*10<list.size() ? page+1 : null));

        json = gson.toJson(p, PagedHelper.class);

        return Response.ok(json).build();
    }
}
