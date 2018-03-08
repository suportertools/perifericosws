package br.com.rtools.perifericosws.dao;

import br.com.rtools.perifericosws.conf.DataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    private static Connection conn = null;
    private Statement statement = null;
    private Boolean conectado;
    //private Conf_Cliente conf_cliente;

    public Connection getConnection(String client) {
        try {
            if (conn == null) {
                DataBase dataBase = new DataBase();
                dataBase.loadJson(client);
                String conexao = "jdbc:postgresql://" + dataBase.getHost() + ":" + dataBase.getPort() + "/" + dataBase.getDatabase() + "?ApplicationName=" + "PerifericosWS";
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
                conn = DriverManager.getConnection(conexao, dataBase.getUser(), dataBase.getPassword());
                return conn;
            }
        } catch (SQLException e) {
            // new Logs().save("database", "Problemas com a base de dados!!!! SQLException: " + e.getMessage());
            try {
                Thread.sleep(1000 * 60 * 5);
            } catch (Exception e5) {

            }
        }
        return conn;
    }

    public ResultSet query(String client, String qry) {
        return query(client, qry, true);
    }

    public ResultSet query(String client, String qry, Boolean closeResultSet) {
        PreparedStatement ps = null;
        Connection oConnect = null;
        ResultSet rs = null;

        try {
            if (getConnection(client) != null) {
                oConnect = this.getConnection(client);
                ps = oConnect.prepareStatement(qry);
                rs = ps.executeQuery();
            }
        } catch (SQLException e) {
            e.getMessage();
        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//                if (oConnect != null) {
//                    oConnect.close();
//                }
//                if (closeResultSet) {
//                    if (rs != null) {
//                        rs.close();
//                    }
//                }
//            } catch (Exception e) {
//                e.getMessage();
//            }
        }
        return rs;
    }

    public void query_execute(String client, String qry) {
        PreparedStatement ps = null;
        Connection oConnect = null;
        try {
            if (getConnection(client) != null) {
                oConnect = this.getConnection(client);
                ps = oConnect.prepareStatement(qry);
                Integer x = ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.getMessage();
        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//                if (oConnect != null) {
//                    oConnect.close();
//                }
//            } catch (Exception e) {
//                e.getMessage();
//            }
        }
    }

    public Statement getStatement() {
//        try {
//            if (statement == null) {
//                conf_cliente = new Conf_Cliente();
//                conf_cliente.loadJson();
//                String conexao = "jdbc:postgresql://" + conf_cliente.getPostgres_ip() + ":" + conf_cliente.getPostgres_porta() + "/" + conf_cliente.getPostgres_banco();
//                Class.forName("org.postgresql.Driver");
//                Connection con = DriverManager.getConnection(conexao, conf_cliente.getPostgres_usuario(), conf_cliente.getPostgres_senha());
//
//                statement = con.createStatement();
//                conectado = true;
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            e.getMessage();
//            conectado = false;
//        }
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Boolean getConectado() {
        return conectado;
    }

    public void setConectado(Boolean conectado) {
        this.conectado = conectado;
    }

    public boolean isActive(String client) {
        if (getConnection(client) != null) {
            try {
                Connection oConnect = this.getConnection(client);
                PreparedStatement ps = oConnect.prepareStatement("SELECT version()");
                ps.executeQuery();
                return true;
            } catch (SQLException e) {
                System.err.println("Erro de conexão: " + e.getMessage());
                if (e.getMessage().toLowerCase().contains("connection has been closed")) {
                    try {
                        Thread.sleep(1000 * 60 * 1);
                        conn = null;
                        getConnection(client);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //new Logs().save("Conexão", e.getMessage());
                return false;
            }
        }
        return false;
    }
}
