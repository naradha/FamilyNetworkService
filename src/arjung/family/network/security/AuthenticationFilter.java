package arjung.family.network.security;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	
        // Get the HTTP Authorization header from the request
        String authorizationHeader = 
            requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly 
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

       
	    // Validate the token
	    if (!validToken(token)) {
	    	requestContext.abortWith(
	                Response.status(Response.Status.UNAUTHORIZED).build());
	    }
	    
	    setContext(requestContext, token);
	    
    }

    private void setContext(ContainerRequestContext requestContext, String token) {
requestContext.setSecurityContext(new SecurityContext() {
			
			@Override
			public boolean isUserInRole(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isSecure() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Principal getUserPrincipal() {
				return new Principal() {
					
					@Override
					public String getName() {
						return getIdFromToken(token);
					}
				};
			}
			
			@Override
			public String getAuthenticationScheme() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
	}

    private String getIdFromToken(String token) {
		return Jwts.parser().setSigningKey("arjun").parseClaimsJws(token).getBody().getSubject();
		
	}
    
	private boolean validToken(String token) {
    	try {
           Jwts.parser().setSigningKey("arjun").parseClaimsJws(token);
           return true;
    	} catch (SignatureException e) {
    		return false;
    	}
    }
}
