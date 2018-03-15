package br.com.rtoolsws.resources;

import br.com.rtoolsws.dao.DB;
import br.com.rtoolsws.security.enums.Levels;
import br.com.rtoolsws.utils.Headers;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/catraca_frequecia")
public class CatracaFrequenciaWS extends DB {

    @Context
    HttpHeaders headers;

    
    @Security(Levels.LEVEL_1)
    @POST
    @Path("/sispessoa")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public synchronized Response sispessoa(@FormParam("departamento_id") Integer departamento_id, @FormParam("nr_pessoa") Integer nr_pessoa, @FormParam("es") String es) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        query_execute(Headers.getClient(headers), "INSERT INTO soc_catraca_frequencia (dt_acesso, ds_hora_acesso, id_departamento, id_sis_pessoa, ds_es) VALUES (CURRENT_DATE, to_char(LOCALTIME(0), 'HH24:MI'), " + departamento_id + ", " + nr_pessoa + ", '" + es + "')");
        query_execute(Headers.getClient(headers), "UPDATE conv_movimento SET is_ativo = false, dt_entrada = CURRENT_DATE WHERE id = " + nr_pessoa + " AND is_ativo = true;");
        return Response.status(200).entity("1").build();
    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/pessoa")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public synchronized Response pessoa(
            @FormParam("departamento_id") Integer departamento_id,
            @FormParam("nr_pessoa") Integer nr_pessoa,
            @FormParam("tipo") String tipo
    ) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
        query_execute(Headers.getClient(headers), "INSERT INTO soc_catraca_frequencia (dt_acesso, ds_hora_acesso, id_departamento, id_pessoa, ds_es) VALUES (CURRENT_DATE, to_char(LOCALTIME(0), 'HH24:MI'), " + departamento_id + ", " + nr_pessoa + ", '" + tipo + "')");
        return Response.status(200).entity("1").build();
    }

}
