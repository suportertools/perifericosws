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
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("/catraca_monitora")
public class CatracaMonitoraWS extends DB {

    @Context
    HttpHeaders headers;

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/start")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response start() {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        String deviceMac = headers.getRequestHeader("Device-Mac").get(0);
        if (deviceMac == null || deviceMac.isEmpty()) {
            return Response.status(404).entity("0").build();
        }
        query_execute(Headers.getClient(headers), "DELETE FROM soc_catraca_monitora WHERE id_catraca IN (SELECT id FROM soc_catraca WHERE ds_mac = '" + headers.getRequestHeader("Device-Mac").get(0) + "')");
        return Response.status(200).entity("1").build();
    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response delete(@FormParam("catraca_id") String catraca_id) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        query_execute(Headers.getClient(headers), "DELETE FROM soc_catraca_monitora WHERE id_catraca = " + catraca_id);
        return Response.status(200).entity("1").build();
    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/store")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response store(@FormParam("catraca_id") String catraca_id) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        query_execute(Headers.getClient(headers), "INSERT INTO soc_catraca_monitora (id_catraca, nr_ping, is_ativo) VALUES (" + catraca_id + ", 0, false);");
        return Response.status(200).entity("1").build();
    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/clear1")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clear1(@FormParam("catraca_id") String catraca_id) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        query_execute(Headers.getClient(headers),
                "UPDATE soc_catraca_monitora \n "
                + " SET nr_pessoa = null, \n"
                + "     ds_mensagem = null, \n"
                + "     ds_nome = null, \n"
                + "     ds_foto = null, \n"
                + "     ds_observacao = null \n"
                //+ "     ds_status = 'Ativa' \n"
                + "WHERE id_catraca = " + catraca_id
        );
        return Response.status(200).entity("1").build();
    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response status(@FormParam("random_id") Integer random_id, @FormParam("ativo") Boolean ativo, @FormParam("status") String status, @FormParam("catraca_id") Integer catraca_id) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        query_execute(Headers.getClient(headers), "UPDATE soc_catraca_monitora SET nr_ping = " + random_id + ", is_ativo = " + ativo + ", ds_status = '" + status + "' WHERE id_catraca = " + catraca_id);
        return Response.status(200).entity("1").build();
    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response ping(@FormParam("random_id") Integer random_id, @FormParam("catraca_id") Integer catraca_id) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        query_execute(Headers.getClient(headers), "UPDATE soc_catraca_monitora SET nr_ping = " + random_id + ", is_ativo = true WHERE id_catraca = " + catraca_id);
        return Response.status(200).entity("1").build();
    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/clear2")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clear2(
            @FormParam("nr_pessoa") Integer nr_pessoa,
            @FormParam("nome") String nome,
            @FormParam("foto") String foto,
            @FormParam("observacao") String observacao,
            @FormParam("mensagem") String mensagem,
            @FormParam("liberado") Boolean liberado,
            @FormParam("catraca_id") Integer catraca_id
    ) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }

        query_execute(Headers.getClient(headers),
                "UPDATE soc_catraca_monitora \n "
                + " SET nr_pessoa = " + nr_pessoa + ", \n"
                + "     ds_nome = '" + nome + "', \n"
                + "     ds_foto = '" + foto + "', \n"
                + "     ds_observacao = '" + observacao + "', \n"
                + "     ds_mensagem = '" + mensagem + "', \n"
                + "     is_liberado = " + liberado + " \n"
                + "WHERE id_catraca = " + catraca_id
        );
        return Response.status(200).entity("1").build();
    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/atualizar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response atualizar(@FormParam("catraca_id") Integer catraca_id) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        ResultSet rs = query(Headers.getClient(headers), "SELECT is_atualizar FROM soc_catraca_monitora WHERE id_catraca = " + catraca_id);
        if (rs != null) {
            try {
                rs.next();
                if (rs.getRow() > 0) {
                    if (rs.getBoolean("is_atualizar")) {
                        return Response.status(200).entity("1").build();
                    }
                    return Response.status(200).entity("3").build();
                } else {
                    return Response.status(200).entity("2").build();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CatracaMonitoraWS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.status(200).entity(null).build();
    }

}
