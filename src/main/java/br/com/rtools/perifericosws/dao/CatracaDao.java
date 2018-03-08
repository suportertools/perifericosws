package br.com.rtools.perifericosws.dao;

import br.com.rtools.perifericosws.entity.Catraca;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatracaDao extends DB {

    public final List<Catraca> listaCatraca(String client, String mac) {

        try {
            List<Catraca> list = new ArrayList();

            ResultSet rs = query(client,
                    " SELECT c.* \n "
                    + " FROM soc_catraca c \n "
                    + "WHERE c.is_ativo = TRUE \n "
                    + "  AND c.ds_mac = '" + mac + "' \n "
                    + "ORDER BY c.nr_numero"
            );

            while (rs.next()) {
                list.add(
                        new Catraca(
                                rs.getInt("id"),
                                rs.getInt("nr_numero"),
                                rs.getInt("nr_porta"),
                                rs.getInt("nr_quantidade_digitos"),
                                rs.getBoolean("is_bloquear_sem_foto"),
                                rs.getInt("nr_tipo_giro_catraca"),
                                rs.getString("ds_lado_giro_catraca"),
                                rs.getInt("id_departamento"),
                                rs.getString("ds_servidor_foto"),
                                rs.getBoolean("is_servidor_beep"),
                                rs.getBoolean("is_biometrico"),
                                rs.getBoolean("is_leitor_biometrico_externo"),
                                rs.getBoolean("is_grava_frequencia_catraca"),
                                rs.getBoolean("is_verifica_biometria"),
                                rs.getBoolean("is_verifica_liberacao"),
                                "",
                                rs.getString("ds_ip"),
                                rs.getString("ds_mac"),
                                rs.getInt("nr_servidor"),
                                true
                        )
                );
            }
            return list;
        } catch (SQLException e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    public final Catraca pesquisaCatraca(String client, String mac, Integer id_catraca) {

        try {

            ResultSet rs = query(client,
                    " SELECT c.* \n "
                    + " FROM soc_catraca c \n "
                    + "WHERE c.is_ativo = TRUE \n "
                    + "  AND c.ds_mac = '" + mac + "' \n "
                    + "  AND id = " + id_catraca
            );

            rs.next();

            return new Catraca(
                    rs.getInt("id"),
                    rs.getInt("nr_numero"),
                    rs.getInt("nr_porta"),
                    rs.getInt("nr_quantidade_digitos"),
                    rs.getBoolean("is_bloquear_sem_foto"),
                    rs.getInt("nr_tipo_giro_catraca"),
                    rs.getString("ds_lado_giro_catraca"),
                    rs.getInt("id_departamento"),
                    rs.getString("ds_servidor_foto"),
                    rs.getBoolean("is_servidor_beep"),
                    rs.getBoolean("is_biometrico"),
                    rs.getBoolean("is_leitor_biometrico_externo"),
                    rs.getBoolean("is_grava_frequencia_catraca"),
                    rs.getBoolean("is_verifica_biometria"),
                    rs.getBoolean("is_verifica_liberacao"),
                    "",
                    rs.getString("ds_ip"),
                    rs.getString("ds_mac"),
                    rs.getInt("nr_servidor"),
                    true
            );
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }
}
