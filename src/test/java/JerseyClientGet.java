//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.Response;
//
//public class JerseyClientGet {
//
//    public static void main(String[] args) {
//        try {
//            List<Catraca> list = new ArrayList();
//            
//            Client client = ClientBuilder.newClient();
//            WebTarget target = client.target("http://localhost:8080/perifericosws/ws/catraca/load/");
//
//            Res response = target.request().get(Response.class);
//            
//            List<Catraca> list = target.request().post(new GenericType<List<Catraca>>() {}, List);
//            
//            
//            System.out.println(response);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//
//    }
//
//}
