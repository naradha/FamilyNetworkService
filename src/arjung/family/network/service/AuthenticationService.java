package arjung.family.network.service;

import java.io.File;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import arjung.family.network.entity.Credential;
import arjung.family.network.entity.CredentialEntitiy;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;

/*
 * http://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
 */
@Path("/authentication")
public class AuthenticationService {
	private ObjectMapper objectMapper;
	private Map<String, CredentialEntitiy> credentials;
	private Gson gson;
	
	private static final String BASE_DATA_PATH = "/Users/arjung/Documents/workspace/data/";
	
	public AuthenticationService() throws Exception{
		this.objectMapper = new ObjectMapper();
		this.gson = new Gson();
		credentials = objectMapper.readValue(new File(BASE_DATA_PATH + "/Credentials.txt"), new TypeReference<Map<String, CredentialEntitiy>>() {});
	}

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("login")
	@SneakyThrows
	public Response login(final String credentialString) {
		final Credential credential = gson.fromJson(credentialString, Credential.class);
		if (!authenticate(credential.getUsername(), credential.getPassword())) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		final String token = generateToken(credentials.get(credential.getUsername()).getId());
		
		Map<String, String> returnValue = ImmutableMap.of("id", 
				credentials.get(credential.getUsername()).getId(), "token", token);
		
		return Response.ok(gson.toJson(returnValue)).build();
	}

	private String generateToken(String username) throws Exception {
		return Jwts.builder().setSubject(username).signWith(SignatureAlgorithm.HS512, "arjun").compact();
	}

	@SneakyThrows
	private boolean authenticate(String username, String password) {
		Map<String, CredentialEntitiy> credentials = objectMapper.readValue(new File(BASE_DATA_PATH + "/Credentials.txt"), new TypeReference<Map<String, CredentialEntitiy>>() {});

		return credentials.containsKey(username) && credentials.get(username).getPassword().equals(password);
	}
}
