package br.com.rtools.perifericosws.conf;

import br.com.rtools.perifericosws.utils.Logs;
import br.com.rtools.perifericosws.utils.Paths;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class DataBase {

    private String user;
    private String host;
    private String database;
    private String password;
    private Integer port;

    public DataBase() {
        this.user = "";
        this.host = "";
        this.database = "";
        this.password = "";
        this.port = 5432;
    }

    public DataBase(String user, String host, Integer port, String database, String password) {
        this.user = host;
        this.host = host;
        this.port = port;
        this.database = database;
        this.password = password;
    }

    public void loadJson(String client) {
        String path = "";
        Logs logs = new Logs();
        try {        
            String filepath = new Paths().get() + File.separator + "resources" + File.separator + "conf" + File.separator + "client" + File.separator + client.toLowerCase() + File.separator + "database.json";
            File file = new File(filepath);
            if (!file.exists()) {
                return;
            }
            String json = null;
            try {
                json = FileUtils.readFileToString(file);
            } catch (IOException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
            JSONObject jSONObject = new JSONObject(json);
            try {
                user = jSONObject.getString("user");
            } catch (Exception e) {
                if (user == null) {
                    logs.save("DataBase Erro", "(String) host: Erro.  (conf)." + e.getMessage());
                }
            }
            try {
                host = jSONObject.getString("host");
            } catch (Exception e) {
                if (host == null) {
                    logs.save("DataBase Erro", "(String) host: Erro.  (conf)." + e.getMessage());
                }
            }
            try {
                port = jSONObject.getInt("port");
            } catch (Exception e) {
                if (port == null) {
                    logs.save("DataBase Erro", "(String) host: Erro.  (conf)." + e.getMessage());
                }
            }
            try {
                database = jSONObject.getString("database");
            } catch (Exception e) {
                if (database == null) {
                    logs.save("DataBase Erro", "(String) host: Erro.  (conf)." + e.getMessage());
                }
            }
            try {
                password = jSONObject.getString("password");
            } catch (Exception e) {
                if (password == null) {
                    logs.save("DataBase Erro", "(String) host: Erro.  (conf)." + e.getMessage());
                }
            }
        } catch (Exception ex) {
            logs.save("Conf JSONException", ex.getMessage());
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
