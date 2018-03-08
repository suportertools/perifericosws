package br.com.rtools.perifericosws.ws;

import br.com.rtools.perifericosws.dao.DB;
import br.com.rtools.perifericosws.entity.Catraca;
import br.com.rtools.perifericosws.entity.RetornoJson;
import br.com.rtools.perifericosws.utils.Headers;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pessoa")
public class PessoaWS extends DB {

    @POST
    @Path("/select")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response atualizar(@Context HttpHeaders headers, @FormParam("pessoa_id") Integer pessoa_id) {
        if (!AuthWS.ok(headers)) {
            return Response.status(401).entity("").build();
        }
        GenericEntity generic = new GenericEntity<RetornoJson>(new RetornoJson()) {
        };

        RetornoJson json = null;
        ResultSet rs_pessoa = query(Headers.getClient(headers),
                "SELECT p.ds_nome AS nome, \n "
                + "     f.ds_foto AS foto \n "
                + "FROM pes_pessoa p \n "
                + "INNER JOIN pes_fisica f ON f.id_pessoa = p.id \n "
                + "WHERE p.id = " + pessoa_id);
        try {
            rs_pessoa.next();
            //  VERIFICA SE O CÓDIGO ENVIADO É VÁLIDO
            rs_pessoa.getString("nome");
        } catch (SQLException e) {
            json = new RetornoJson(
                    pessoa_id,
                    "",
                    "",
                    "",
                    -1,
                    "Código da Pessoa não Encontrado",
                    null,
                    false
            );
            generic = new GenericEntity<RetornoJson>(json) {};
            return Response.status(200).entity(generic).build();
        }

        // SE O RETORNO DA FUNÇÃO FOR LIBERADA
        if (pessoa_id > 0) {
            try {
                json = new RetornoJson(
                        pessoa_id,
                        rs_pessoa.getString("nome"),
                        rs_pessoa.getString("foto"),
                        "",
                        null,
                        "Catraca Liberada",
                        null,
                        true
                );
            } catch (SQLException ex) {
                Logger.getLogger(PessoaWS.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(401).entity(generic).build();
            }
        } else {
            try {
                // SE O RETORNO DA FUNÇÃO NÃO FOR LIBERADA
                ResultSet rs_erro = query(Headers.getClient(headers),
                        "  SELECT ce.id AS id, \n "
                        + "       ce.ds_descricao AS descricao_erro \n "
                        + "  FROM soc_catraca_erro ce \n "
                        + " WHERE ce.nr_codigo = " + pessoa_id
                );

                rs_erro.next();

                json = new RetornoJson(
                        pessoa_id,
                        rs_pessoa.getString("nome"),
                        rs_pessoa.getString("foto"),
                        "",
                        rs_erro.getInt("id"),
                        rs_erro.getString("descricao_erro"),
                        null,
                        false
                );
            } catch (SQLException ex) {
                Logger.getLogger(PessoaWS.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(401).entity(generic).build();
            }
        }
        if (pessoa_id < 0) {
            try {
                ResultSet rs_erro = query(Headers.getClient(headers),
                        "  SELECT ce.id AS id, \n "
                        + "       ce.ds_descricao AS descricao_erro \n "
                        + "  FROM soc_catraca_erro ce \n "
                        + " WHERE ce.nr_codigo = " + pessoa_id
                );

                rs_erro.next();

                json = new RetornoJson(
                        pessoa_id,
                        "",
                        "",
                        "",
                        rs_erro.getInt("id"),
                        rs_erro.getString("descricao_erro"),
                        null,
                        false
                );
            } catch (SQLException ex) {
                Logger.getLogger(PessoaWS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        generic = new GenericEntity<RetornoJson>(json) {};
        return Response.status(200).entity(generic).build();
    }

}
