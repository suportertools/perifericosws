package br.com.rtoolsws.resources;

import br.com.rtoolsws.dao.CatracaDao;
import br.com.rtoolsws.dao.DB;
import br.com.rtoolsws.entity.Catraca;
import br.com.rtoolsws.security.enums.Levels;
import br.com.rtoolsws.utils.Headers;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/catraca")
public class CatracaWS extends DB {

    @Context
    HttpHeaders headers;

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    public Response load() {
        GenericEntity generic = new GenericEntity<List<Catraca>>(new ArrayList()) {
        };
        List<Catraca> list = new CatracaDao().listaCatraca(Headers.getClient(headers), Headers.get(headers, "Device-Mac"));
        try {
            if (list == null || list.isEmpty()) {
                return Response.status(200).entity(generic).build();
            }
            generic = new GenericEntity<List<Catraca>>(list) {
            };
            return Response.status(200).entity(generic).build();

        } catch (Exception e) {
            e.getMessage();
        }
        return Response.status(501).entity(generic).build();
    }

}
