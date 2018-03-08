package br.com.rtools.perifericosws.ws;

import br.com.rtools.perifericosws.dao.DB;
import br.com.rtools.perifericosws.utils.Headers;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;

@Path("/auth")
public class AuthWS extends DB {

    @Context
    private ServletConfig servletConfig;
    @Context
    private ServletContext servletContext;
    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpServletResponse httpServletResponse;

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders headers) {
        try {
            UUID uuidX = UUID.randomUUID();
            MessageDigest access_token = MessageDigest.getInstance("MD5");
            access_token.update(uuidX.toString().replace("-", "_").toLowerCase().getBytes(), 0, uuidX.toString().replace("-", "_").toLowerCase().length());

            String token_md5 = new BigInteger(1, access_token.digest()).toString(16);
            DigestUtils.md5Hex(token_md5);
            String mac_token = headers.getRequestHeader("Authorization").get(0);
            String tipo_dispositivo = headers.getRequestHeader("Device-Type").get(0);
            query_execute(Headers.getClient(headers), "UPDATE sis_dispositivo SET dt_conectado = current_date, ds_token = '" + token_md5 + "' WHERE ds_mac = '" + mac_token + "' AND is_ativo = true AND id_tipo_dispositivo = " + tipo_dispositivo);
            return Response.status(200).entity(token_md5).build();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(404).entity("0").build();
    }

    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpHeaders headers) {
        String mac_token = headers.getRequestHeader("Authorization").get(0);
        String tipo_dispositivo = headers.getRequestHeader("Device-Type").get(0);
        query_execute(Headers.getClient(headers), "UPDATE sis_dispositivo SET ds_token = '', dt_conectado = null WHERE ds_mac = '" + mac_token + "' AND id_tipo_dispositivo = " + tipo_dispositivo);
        return Response.status(200).entity("0").build();
    }

    public static boolean ok(HttpHeaders headers) {
        return new AuthWS().okNoNStatic(headers);
    }

    public boolean okNoNStatic(HttpHeaders headers) {
        String token = "";
        try {
            token = headers.getRequestHeader("Authorization").get(0);
        } catch (Exception e) {

        }
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            ResultSet rs = query(Headers.getClient(headers), "SELECT id FROM sis_dispositivo WHERE ds_token = '" + token + "'", false);
            if (rs == null) {
                return false;
            }
            while (rs.next()) {
                return true;
            }
            try {
                String expires = headers.getRequestHeader("Expires").get(0);
            } catch (Exception e) {

            }
        } catch (SQLException ex) {
            return false;
        }
        return false;
    }

}
