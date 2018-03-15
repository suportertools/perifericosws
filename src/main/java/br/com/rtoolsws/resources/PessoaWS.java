package br.com.rtoolsws.resources;

import br.com.rtoolsws.dao.DB;
import br.com.rtoolsws.entity.RetornoJson;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pessoa")
public class PessoaWS extends DB {

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/select_funcao_catraca")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response select_funcao_catraca(@Context HttpHeaders headers, @FormParam("departamento_id") Integer departamento_id, @FormParam("cartao") String cartao) {
        RetornoJson json = new RetornoJson();
        GenericEntity generic = new GenericEntity<RetornoJson>(new RetornoJson()) {
        };
        try {
            ResultSet rs_cartao = query(Headers.getClient(headers),
                    "SELECT nr_cartao_posicao_via AS via, \n "
                    + "     nr_cartao_posicao_codigo AS codigo \n "
                    + " FROM conf_social"
            );
            rs_cartao.next();

            // 1 OU 2 PARA VIA = 99
            int _via = rs_cartao.getInt("via"), _codigo = rs_cartao.getInt("codigo");

            String via_string = cartao.substring(_via, _via + 2);
            Integer numero_via = Integer.valueOf(via_string);

            String codigo_string = cartao.substring(_codigo, _codigo + 8);
            Integer nr_cartao = Integer.parseInt(codigo_string);

            ResultSet rs_funcao;
            if (numero_via != 99) {
                rs_funcao = query(Headers.getClient(headers), "SELECT func_catraca(" + nr_cartao + "," + departamento_id + ", 1, " + numero_via + ") AS retorno");
            } else {
                rs_funcao = query(Headers.getClient(headers), "SELECT func_catraca(" + nr_cartao + "," + departamento_id + ", 3, null) AS retorno");
            }
            rs_funcao.next();

            Integer nr_retorno = rs_funcao.getInt("retorno");

            if (nr_retorno >= 0) {
                if (nr_retorno == 0) {
                    //  LIBERA A CATRACA PARA VISITANTE
                    json = new RetornoJson(
                            nr_retorno,
                            "VISITANTE",
                            "",
                            "",
                            null,
                            "Catraca Liberada",
                            numero_via,
                            true
                    );
                } else {
                    ResultSet rs_pessoa = null;

                    if (numero_via != 99) {
                        rs_pessoa = query(Headers.getClient(headers),
                                "SELECT s.ds_nome AS nome, \n "
                                + "     s.ds_foto_perfil AS foto \n "
                                + "FROM sis_pessoa s \n "
                                + "WHERE s.id = " + nr_retorno);

                    } else {
                        rs_pessoa = query(Headers.getClient(headers),
                                "SELECT p.ds_nome AS nome, \n "
                                + "     f.ds_foto AS foto \n "
                                + "FROM pes_pessoa p \n "
                                + "INNER JOIN pes_fisica f ON f.id_pessoa = p.id \n "
                                + "WHERE p.id = " + nr_retorno);
                    }

                    rs_pessoa.next();

                    try {
                        //  VERIFICA SE O CÓDIGO ENVIADO É VÁLIDO
                        rs_pessoa.getString("nome");
                    } catch (SQLException e) {
                        json = new RetornoJson(
                                nr_retorno,
                                "",
                                "",
                                "",
                                -1,
                                "Código da Pessoa não Encontrado",
                                numero_via,
                                false
                        );
                        generic = new GenericEntity<RetornoJson>(json) {
                        };
                        return Response.status(200).entity(generic).build();
                    }

                    json = new RetornoJson(
                            nr_retorno,
                            rs_pessoa.getString("nome"),
                            rs_pessoa.getString("foto"),
                            "",
                            null,
                            "Catraca Liberada",
                            numero_via,
                            true
                    );
                    generic = new GenericEntity<RetornoJson>(json) {
                    };
                    return Response.status(200).entity(generic).build();
                }
            }

            if (nr_retorno < 0) {
                ResultSet rs_carteirinha = query(Headers.getClient(headers),
                        "SELECT id_pessoa FROM soc_carteirinha WHERE nr_cartao = " + nr_cartao
                );
                rs_carteirinha.next();

                Integer nr_pessoa = rs_carteirinha.getInt("id_pessoa");

                ResultSet rs_pessoa = query(Headers.getClient(headers),
                        "SELECT p.ds_nome AS nome, \n "
                        + "     f.ds_foto AS foto \n "
                        + "FROM pes_pessoa p \n "
                        + "INNER JOIN pes_fisica f ON f.id_pessoa = p.id \n "
                        + "WHERE p.id = " + nr_pessoa);
                rs_pessoa.next();

                ResultSet rs_erro = query(Headers.getClient(headers),
                        "  SELECT ce.id AS id, \n "
                        + "       ce.ds_descricao AS descricao_erro \n "
                        + "  FROM soc_catraca_erro ce \n "
                        + " WHERE ce.nr_codigo = " + nr_retorno
                );

                rs_erro.next();

                json = new RetornoJson(
                        nr_pessoa,
                        rs_pessoa.getString("nome"),
                        rs_pessoa.getString("foto"),
                        "",
                        rs_erro.getInt("id"),
                        rs_erro.getString("descricao_erro"),
                        numero_via,
                        false
                );
            }
        } catch (SQLException | NumberFormatException e) {
            e.getMessage();
        }
        generic = new GenericEntity<RetornoJson>(json) {
        };
        return Response.status(200).entity(generic).build();

    }

    @Security(Levels.LEVEL_1)
    @POST
    @Path("/select")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response select(@Context HttpHeaders headers, @FormParam("pessoa_id") Integer pessoa_id) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("").build();
//        }
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
            //  VERIFICA SE O C�DIGO ENVIADO � V�LIDO
            rs_pessoa.getString("nome");
        } catch (SQLException e) {
            json = new RetornoJson(
                    pessoa_id,
                    "",
                    "",
                    "",
                    -1,
                    "Codigo da Pessoa nao Encontrado",
                    null,
                    false
            );
            generic = new GenericEntity<RetornoJson>(json) {
            };
            return Response.status(200).entity(generic).build();
        }

        // SE O RETORNO DA FUN��O FOR LIBERADA
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
                // SE O RETORNO DA FUN��O N�O FOR LIBERADA
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
        generic = new GenericEntity<RetornoJson>(json) {
        };
        return Response.status(200).entity(generic).build();
    }

}
