package br.com.rtoolsws.resources;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Security
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizedFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Pega a classe que contem URL requisitada 
        // E extrai os nivel de permissão dela
        Class<?> classe = resourceInfo.getResourceClass();
//		List<Levels> levelsClass = extrairNivelPermissao(classe);
//
//		// Pega o metodo que contem URL requisitada 
//		// E extrai os nivel de permissão dele
//		Method metodo = resourceInfo.getResourceMethod();
//		List<Levels> levelsMethods = extrairNivelPermissao(metodo);

//		try {
//			//Como modificamos o securityContext na hora de validar o token, para podemos pegar
//			//O login do usuario, para fazer a verificação se ele tem o nivel de permissao necessario
//			//para esse endpoint
//			String login = requestContext.getSecurityContext().getUserPrincipal().getName();
//			// Verifica se o usuario tem permissão pra executar esse metodo
//			// Os niveis de acesso do metodo sobrepoe o da classe
//			if (levelsMethods.isEmpty()) {
//				chech(levelsClass,login);
//			} else {
//				chech(levelsMethods,login);
//			}
//
//		} catch (Exception e) {
//			//Se caso o usuario não possui permissao é dado um execption, 
//			//e retorna um resposta com o status 403 FORBIDDEN 
//			requestContext.abortWith(
//					Response.status(Response.Status.FORBIDDEN).build());
//		}
    }
//	//Metodo que extrai os niveis de permissao que foram definidos no @Seguro
//	private List<Levels> extrairNivelPermissao(AnnotatedElement annotatedElement) {
//		if (annotatedElement == null) {
//			return new ArrayList<Levels>();
//		} else {
//			Security secured = annotatedElement.getAnnotation(Security.class);
//			if (secured == null) {
//				return new ArrayList<Levels>();
//			} else {
//				Levels[] allowedRoles = secured.value();
//				return Arrays.asList(allowedRoles);
//			}
//		}
//	}
//	//Verifica se o usuario tem permissao pra executar o metodo, se não for definido nenhum nivel de acesso no @Seguro,
//	//Entao todos vao poder executar desde que possuam um token valido
//	private void chech(List<Levels> levelsAuthorization,String login) throws Exception {
//		try {
//			if(levelsAuthorization.isEmpty())
//				return;
//			
//			boolean temPermissao = false;
//			//Busca quais os niveis de acesso o usuario tem.
//			                 Levels nivelPermissaoUsuario = new LoginService().buscarNivelPermissao(login);
//			
//			for (Levels nivelPermissao : levelsAuthorization) {
//				if(nivelPermissao.equals(nivelPermissaoUsuario))
//				{
//					temPermissao = true;
//					break;
//				}
//			}
//			
//			if(!temPermissao)
//				throw new Exception("Cliente não possui o nível de permissão para esse método");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}

}
