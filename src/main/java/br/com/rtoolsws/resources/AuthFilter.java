package br.com.rtoolsws.resources;

import br.com.rtoolsws.resources.AuthWS;
import static br.com.rtoolsws.resources.AuthWS.TOKEN;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.security.Principal;
import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Security
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        //Verifica se o header AUTHORIZATION exite ou não se exite extrai o token 
        //se não abaorta a requsição retornando uma NotAuthorizedException
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (TOKEN == null || TOKEN.isEmpty()) {
            throw new NotAuthorizedException("Authorization header precisa ser provido");
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("rtools ")) {
            throw new NotAuthorizedException("Authorization header precisa ser provido");
        }

        //extrai o token do header
        String token = authorizationHeader.substring("rtools".length()).trim();
        //verificamos se o metodo é valido ou não
        //se não for valido  a requisição é abortada e retorna uma resposta com status 401 UNAUTHORIZED
        //se for valida modificamos o o SecurityContext da request 
        //para que quando usarmos o  getUserPrincipal retorne o login do usuario 
        try {
            // metodo que verifica  se o token é valido ou não 
            Claims claims = AuthWS.validToken(token);
//            Caso não for valido vai retornar um objeto nulo e executar um exception
            if (claims == null) {
                //Metodo que modifica o SecurityContext pra disponibilizar o logi
                throw new Exception("Token inválido");
            }
            //Metodo que modifica o SecurityContext pra disponibilizar o login do usuario
            // alterRequestContext(requestContext, claims.getId());

            String client = requestContext.getHeaderString("client");

            if (!TOKEN.equals(token)) {
                if (!AuthWS.ok(client, token, null)) {
                    throw new NotAuthorizedException("Authorization header precisa ser provido");
                }
            }

            // claims.getExpiration()
        } catch (Exception e) {
            e.printStackTrace();
            //Caso o token for invalido a requisição é abortada e retorna uma resposta com status 401 UNAUTHORIZED
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
    //Metodo que modfica o SecurityContext

    private void alterRequestContext(ContainerRequestContext requestContext, String login) {
        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                return new Principal() {

                    @Override
                    public String getName() {
                        return login;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "rtools";
            }
        });
    }

}
