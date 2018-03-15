package br.com.rtoolsws.resources;

//package br.com.rtools.perifericosws.ws;
//
//import br.com.rtools.perifericosws.dao.DB; 
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//@Path("/status")
//public class StatusWS extends DB {
//
//    @GET
//    @Path("/verified")
//    @Produces({MediaType.TEXT_HTML})
//    public synchronized Response active(@Context HttpHeaders headers) {
//        if (!AuthWS.ok(headers)) {
//            return Response.status(401).entity("0").build();
//        }
//        return Response.status(200).entity("1").build();
//    }
//    
//}
