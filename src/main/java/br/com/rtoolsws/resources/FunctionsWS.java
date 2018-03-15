package br.com.rtoolsws.resources;

import br.com.rtoolsws.dao.DB;
import br.com.rtoolsws.security.enums.Levels;
import br.com.rtoolsws.utils.Headers;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/functions")
public class FunctionsWS extends DB {

    @Context
    private ServletConfig servletConfig;
    @Context
    private ServletContext servletContext;
    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpServletResponse httpServletResponse;

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/catraca")
    @Produces(MediaType.APPLICATION_JSON)
    public Response catraca(@Context HttpHeaders headers, @FormParam("nr_pessoa") String nr_pessoa, @FormParam("departamento_id") String departamento_id, @FormParam("tipo") String tipo, @FormParam("via") String via) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        String retorno = null;
        String userAgent = headers.getRequestHeader("user-agent").get(0);
        ResultSet rs_funcao = query(Headers.getClient(headers), "SELECT func_catraca(" + nr_pessoa + ", " + departamento_id + ", " + tipo + ", " + via + ") AS retorno");
        try {
            rs_funcao.next();
            retorno = rs_funcao.getInt("retorno") + "";
        } catch (SQLException e) {
            return Response.status(500).entity(retorno).build();
        }

        return Response.status(200).entity(retorno).build();
    }

}
