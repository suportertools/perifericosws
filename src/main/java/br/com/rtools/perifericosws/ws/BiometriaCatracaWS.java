/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.perifericosws.ws;

import br.com.rtools.perifericosws.dao.DB;
import br.com.rtools.perifericosws.utils.Headers;
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

@Path("/biometria_catraca")
public class BiometriaCatracaWS extends DB {

    @POST
    @Path("/exists")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exists(@Context HttpHeaders headers, @FormParam("ip") String ip) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
        ResultSet rs = query(Headers.getClient(headers), "SELECT id_pessoa AS pessoa FROM pes_biometria_catraca WHERE ds_ip = '" + ip + "'");
        query_execute(Headers.getClient(headers), "DELETE FROM pes_biometria_catraca WHERE ds_ip = '" + ip + "'");
        if (rs != null) {
            try {
                rs.next();
                if (rs.getRow() > 0) {
                    return Response.status(200).entity(rs.getInt("pessoa")).build();
                } else {
                    return Response.status(200).entity("-1").build();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BiometriaCatracaWS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.status(200).entity("-1").build();
    }

}
