package br.com.rtoolsws.resources;

import br.com.rtoolsws.dao.DB;
import br.com.rtoolsws.security.enums.Levels;
import br.com.rtoolsws.utils.Headers;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/catraca_liberada")
public class CatracaLiberadaWS extends DB {

    @Context
    HttpHeaders headers;

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/liberar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response liberar(@FormParam("inner") Integer inner, @FormParam("catraca_id") Integer catraca_id) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }

        String queryString
                = "SELECT COUNT(*) AS qnt \n "
                + " FROM soc_catraca_liberada cl \n"
                + "INNER JOIN soc_catraca c ON c.id = cl.id_catraca\n"
                + "WHERE c.nr_numero = " + inner;

        ResultSet rs = query(Headers.getClient(headers), queryString);
        if (rs != null) {
            try {
                rs.next();
                if (rs.getInt("qnt") > 1) {
                    query_execute(Headers.getClient(headers), "DELETE FROM soc_catraca_liberada WHERE id_catraca = " + catraca_id);
                    return Response.status(200).entity("1").build();
                } else {
                    return Response.status(200).entity("0").build();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CatracaMonitoraWS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.status(200).entity("0").build();
    }

}
