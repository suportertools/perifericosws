package br.com.rtools.perifericosws.ws;

import br.com.rtools.perifericosws.dao.DB;
import br.com.rtools.perifericosws.utils.Headers;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/catraca_monitora")
public class CatracaMonitoraWS extends DB {

    @POST
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response start(@Context HttpHeaders headers) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
        String deviceMac = headers.getRequestHeader("Device-Mac").get(0);
        if (deviceMac == null || deviceMac.isEmpty()) {
            return Response.status(404).entity("0").build();
        }
        query_execute(Headers.getClient(headers), "DELETE FROM soc_catraca_monitora WHERE id_catraca IN (SELECT id FROM soc_catraca WHERE ds_mac = '" + headers.getRequestHeader("Device-Mac").get(0) + "')");
        return Response.status(200).entity("1").build();
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context HttpHeaders headers, @FormParam("catraca_id") String catraca_id) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
        query_execute(Headers.getClient(headers), "DELETE FROM soc_catraca_monitora WHERE id_catraca = " + catraca_id);
        return Response.status(200).entity(1).build();
    }

    @POST
    @Path("/store")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response store(@Context HttpHeaders headers, @FormParam("catraca_id") String catraca_id) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
        query_execute(Headers.getClient(headers), "INSERT INTO soc_catraca_monitora (id_catraca, nr_ping, is_ativo) VALUES (" + catraca_id + ", 0, false);");
        return Response.status(200).entity(1).build();
    }

    @POST
    @Path("/clear1")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clear1(@Context HttpHeaders headers, @FormParam("catraca_id") String catraca_id) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
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
        return Response.status(200).entity(1).build();
    }

    @POST
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response status(@Context HttpHeaders headers, @FormParam("random_id") Integer random_id, @FormParam("ativo") Boolean ativo, @FormParam("status") String status, @FormParam("catraca_id") Integer catraca_id) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
        query_execute(Headers.getClient(headers), "UPDATE soc_catraca_monitora SET nr_ping = " + random_id + ", is_ativo = " + ativo + ", ds_status = '" + status + "' WHERE id_catraca = " + catraca_id);
        return Response.status(200).entity(1).build();
    }

    @POST
    @Path("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response status(@Context HttpHeaders headers, @FormParam("catraca_id") Integer random_id, @FormParam("catraca_id") Integer catraca_id) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
        query_execute(Headers.getClient(headers), "UPDATE soc_catraca_monitora SET nr_ping = " + random_id + ", is_ativo = true WHERE id_catraca = " + catraca_id);
        return Response.status(200).entity(1).build();
    }

    @GET
    @Path("/clear2")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clear2(
            @Context HttpHeaders headers,
            @FormParam("nr_pessoa") String nr_pessoa,
            @FormParam("nome") Integer nome,
            @FormParam("foto") Integer foto,
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
        return Response.status(200).entity(1).build();
    }

    @POST
    @Path("/atualizar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response atualizar(@Context HttpHeaders headers, @FormParam("catraca_id") Integer catraca_id) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
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
