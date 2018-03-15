package br.com.rtoolsws.resources;

import br.com.rtoolsws.dao.DB;
import br.com.rtoolsws.security.enums.Levels;
import br.com.rtoolsws.utils.Headers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

@Path("/auth")
public class AuthWS extends DB {

    @Context
    HttpHeaders headers;

    //Frase segredo do token, Não passar pra niguem!
    private final static String KEY_SECRET = "rtools";
    public static String TOKEN = null;

    @GET
    @Path("/login")
    public Response login() {
        try {
            // UUID uuidX = UUID.randomUUID();
            // MessageDigest access_token = MessageDigest.getInstance("MD5");
            // access_token.update(uuidX.toString().replace("-", "_").toLowerCase().getBytes(), 0, uuidX.toString().replace("-", "_").toLowerCase().length());
            // String token_md5 = new BigInteger(1, access_token.digest()).toString(16);
            // DigestUtils.md5Hex(token_md5);
            String mac_token = headers.getRequestHeader("Authorization").get(0);
            String tipo_dispositivo = headers.getRequestHeader("Device-Type").get(0);
            crendentialsValidation(mac_token);
            String token_md5 = tokenGenerator(mac_token, 365);
            query_execute(Headers.getClient(headers), "UPDATE sis_dispositivo SET dt_conectado = current_date, ds_token = '" + token_md5 + "' WHERE ds_mac = '" + mac_token + "' AND is_ativo = true AND id_tipo_dispositivo = " + tipo_dispositivo);
            TOKEN = token_md5;
            token_md5 = KEY_SECRET + " " + token_md5;
            return Response.ok(token_md5, MediaType.TEXT_PLAIN).build();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthWS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AuthWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("0", MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Path("/logout")
    public Response logout() {
        try {
            TOKEN = null;
            String mac_token = headers.getRequestHeader("Authorization").get(0);
            mac_token = mac_token.replace(KEY_SECRET + " ", "");
            String tipo_dispositivo = headers.getRequestHeader("Device-Type").get(0);
            query_execute(Headers.getClient(headers), "UPDATE sis_dispositivo SET ds_token = '', dt_conectado = null WHERE ds_mac = '" + mac_token + "' AND id_tipo_dispositivo = " + tipo_dispositivo);            
        } catch (Exception e) {
            
        }
        return Response.ok("0", MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("/end")
    @Produces(MediaType.APPLICATION_JSON)
    public Response end() {
        // new DB().close(Headers.getClient(headers));
        return Response.status(200).entity("1").build();
    }

    public static boolean ok(HttpHeaders headers) {
        return new AuthWS().okNoNStatic(headers, null, null, null);
    }

    public static boolean ok(String client_, String token_, String expires_) {
        return new AuthWS().okNoNStatic(null, client_, token_, expires_);
    }

    public boolean okNoNStatic(HttpHeaders headers, String client_, String token_, String expires_) {
        String token = null;
        String client = null;
        String expires = null;
        if (headers != null) {
            try {
                token = headers.getRequestHeader("Authorization").get(0);
                token = token.replace("rtools ", "");
                client = Headers.getClient(headers);
                // expires = headers.getRequestHeader("Expires").get(0);
            } catch (Exception e) {

            }
        } else {
            token = token_;
            client = client_;
        }
        if (token == null || token.isEmpty() || client == null || client.isEmpty()) {
            return false;
        }
        try {
            token = token.replace(KEY_SECRET + " " + token, "");
            ResultSet rs = query(client, "SELECT id FROM sis_dispositivo WHERE ds_token = '" + token + "'", false);
            if (rs == null) {
                return false;
            }
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }
        return false;
    }

    @Security({Levels.LEVEL_1})
    @GET
    @Path("/status")
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response status() {
        return Response.status(200).entity("1").build();
    }

    @POST
    @Path("/device")
    @Produces({MediaType.TEXT_HTML})
    public synchronized Response device() {
        String deviceMac = headers.getRequestHeader("Device-Mac").get(0);
        ResultSet rs = query(Headers.getClient(headers), "SELECT id FROM sis_dispositivo WHERE ds_mac = '" + deviceMac + "' AND is_ativo = true AND id_tipo_dispositivo = 3", false);
        if (rs == null) {
            return Response.status(401).entity("0").build();
        }
        try {
            while (rs.next()) {
                return Response.status(200).entity("1").build();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuthWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(401).entity("0").build();

    }

    @GET
    @Path("/online")
    public synchronized Response online() {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(0);
        cc.setPrivate(false);
        cc.setNoStore(false);
        return Response.ok("Online", MediaType.TEXT_PLAIN).cacheControl(cc).build();

    }

    //Metodo POST que valaida as crendencias enviadas na request 
    //e se for validas retorna o token para o cliente	
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response fazerLogin(String crendenciaisJson) {
//        try {
//            //Instancia o objeto Gson que vai ser responsavel de transformar o corpo da request que está na variavel crendenciaisJson
//            //em um objeto java Crendencial
//            Gson gson = new Gson();
//            //aqui o objeto gson transforma a crendenciaisJson pra a variavel crendencial do tipo Crencendial
//            Credencial crendencial = gson.fromJson(crendenciaisJson, Credencial.class);
//            //Verifica se a crendencial é valida, se não for vai dar exception 
//            validarCrendenciais(crendencial);
//            //Se a crendencial gera o token e passa a quanidade de dias que o token vai ser valido no caso 1 dia
//            String token = gerarToken(crendencial.getLogin(), 1);
//            //Retorna um reponse com o status 200 OK com o token gerado
//            return Response.ok(token).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //Caso ocorra algum erro retorna uma resposta com o status 401 UNAUTHORIZED
//            return Response.status(Status.UNAUTHORIZED).build();
//        }
//    }
    private void crendentialsValidation(String mac) throws Exception {
        try {
            if (mac.isEmpty()) {
                throw new Exception("Crendencias não válidas!");
            }

        } catch (Exception e) {
            throw e;
        }
    }

    private static String tokenGenerator(String login, Integer days_expires) {
        //Defini qual vai ser o algotirmo da assinatura no caso vai ser o HMAC SHA512
        SignatureAlgorithm algoritimoAssinatura = SignatureAlgorithm.HS256;
        //Data atual que data que o token foi gerado
        Date now = new Date();
        //Define até que data o token é pelo quantidade de dias que foi passo pelo parametro expiraEmDias
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.DAY_OF_MONTH, days_expires);
        //Encoda a frase sergredo pra base64 pra ser usada na geração do token 
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY_SECRET);
        SecretKeySpec key = new SecretKeySpec(apiKeySecretBytes, algoritimoAssinatura.getJcaName());
        //E finalmente utiliza o JWT builder pra gerar o token
        JwtBuilder construtor = Jwts.builder()
                .setIssuedAt(now)//Data que o token foi gerado
                .setIssuer(login)//Coloca o login do usuario mais podia qualquer outra informação
                .signWith(algoritimoAssinatura, key)//coloca o algoritimo de assinatura e frase segredo ja encodada
                .setExpiration(expires.getTime());// coloca até que data que o token é valido

        return construtor.compact();//Constroi o token retorando a string dele
    }

    public static Claims validToken(String token) {
        try {
            //JJWT vai validar o token caso o token não seja valido ele vai executar uma exeption
            //o JJWT usa a frase segredo pra descodificar o token e ficando assim possivel
            //recuperar as informações que colocamos no payload
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(KEY_SECRET))
                    .parseClaimsJws(token).getBody();
            //Aqui é um exemplo que se o token for valido e descodificado 
            //vai imprimir o login que foi colocamos no token
            // System.out.println(claims.getIssuer());
            return claims;
        } catch (Exception ex) {
            throw null;
        }
    }
    //Metodo simples como não usamos banco de dados e foco é o parte autenticação
    //o metodo retorna somente um nivel de acesso, mas em uma aplicação normal
    //aqui seria feitor a verficação de que niveis de permissao o usuario tem e retornar eles

    public Levels findLevels(String login) {

        return Levels.LEVEL_1;

    }

    public static String getKeySecret() {
        return KEY_SECRET;
    }

}
