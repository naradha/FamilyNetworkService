package arjung.family.network.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import arjung.family.network.entity.CreatePersonRequest;
import arjung.family.network.entity.Credential;
import arjung.family.network.entity.CredentialEntitiy;
import arjung.family.network.entity.FamilyData;
import arjung.family.network.entity.GetRelationsResult;
import arjung.family.network.entity.NodeUserInfo;
import arjung.family.network.entity.Person;
import arjung.family.network.entity.Relationship;
import arjung.family.network.entity.UserDetailsResult;
import arjung.family.network.entity.UserInfo;
import arjung.family.network.security.Secured;
import lombok.SneakyThrows;

@Path("/relationshipGraph")
public class RelationshipGraphService {
	private ObjectMapper objectMapper;
	private Map<String, Person> familyData;
	private Gson gson;
	
	private static final String BASE_DATA_PATH = "/Users/arjung/Documents/workspace/data/";
	
	public RelationshipGraphService() throws Exception {
		this.objectMapper = new ObjectMapper();
		this.gson = new Gson();
		FamilyData fd = objectMapper.readValue(new File(BASE_DATA_PATH + "/FamilyData.txt"), FamilyData.class);
		familyData = fd.getFamilyData();
	}
	
	@POST
	@Produces("application/text")
	@Consumes("application/json")
	@Path("create/person")
	public String createPerson(final String createPerson) throws Exception {
		final CreatePersonRequest createPersonRequest = gson.fromJson(createPerson, CreatePersonRequest.class);
		final String personId = UUID.randomUUID().toString();
        
        
        updateUserInfoDataBase(personId, createPersonRequest.getUserInfo());
        updateCredentialDataBase(personId, createPersonRequest.getCredential());
        return personId;
	}
	
	@SneakyThrows
	private void updateCredentialDataBase(String personId, Credential credential) {
		Map<String, CredentialEntitiy> credentials = objectMapper.readValue(new File(BASE_DATA_PATH + "/Credentials.txt"), new TypeReference<Map<String, CredentialEntitiy>>() {});
		CredentialEntitiy credentialEntitiy = new CredentialEntitiy();
		credentialEntitiy.setId(personId);
		credentialEntitiy.setPassword(credential.getPassword());
		
		credentials.put(credential.getUsername(), credentialEntitiy);
		objectMapper.writeValue(new File(BASE_DATA_PATH + "/Credentials.txt"), credentials);
		
	}

	private void updateUserInfoDataBase(String personId, UserInfo userInfo) {
		final Person person = new Person();
        person.setUserInfo(userInfo);
        
        familyData.put(personId, person);
		updateDatabase();
	}

	@PUT
	@Path("create/relationship/from/{personId}/to/{relatedPersonId}/relationshipType/{relationshipType}")
	@Secured
	public void createRelationship(@PathParam("personId") final String personId, 
			@PathParam("relatedPersonId") final String relatedPersonId,
			@PathParam("relationshipType") final String relationshipType) {
		establishOneWayRelation(personId, relatedPersonId, relationshipType);
		establishOneWayRelation(relatedPersonId, personId, oppositeRelation(relationshipType));
		
		updateDatabase();
	}
	
	/*@POST
	@Path("/upload/profilePicture/{personId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@SneakyThrows
	public void uploadProfilePicture(
			@PathParam("personId") final String personId, 
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail){
		OutputStream os = new FileOutputStream(new File(BASE_DATA_PATH + "profilePic/" + personId + ".jpeg"));
		byte[] buffer = new byte[256];
		int bytes = 0;
		while ((bytes = uploadedInputStream.read(buffer)) != -1) {
		     os.write(buffer, 0, bytes);
		}
	}*/
	
	@GET
	@Path("/get/profile/picture/{personId}")
	@Produces("images/jpeg")
	public Response getProfilePicture(@PathParam("personId") final String personId) {
		final File profilePicture = new File(BASE_DATA_PATH + "profilePic/" + personId + ".jpeg");
		if (!profilePicture.exists()) {
			return Response.noContent().build();
		}
		ResponseBuilder response = Response.ok((Object) profilePicture);  
        response.header("Content-Disposition","attachment; filename=\""+ personId +".jpeg\"");  
        return response.build();  
	}
	
	private String oppositeRelation(String relationshipType) {
		if (relationshipType.equals("PARENT")) {
			return "CHILD";
		} 
		
		if (relationshipType.equals("CHILD")) {
			return "PARENT";
		} 
		
		return relationshipType;
	}

	private void establishOneWayRelation(String personId, String relatedPersonId, String relationshipType) {
		final Person person = familyData.get(personId);
		final Relationship relationship = new Relationship();
		relationship.setRelatedPersonId(relatedPersonId);
		relationship.setRelationshipType(relationshipType);
		
		if (person.getRelationships() == null) {
			person.setRelationships(new ArrayList<>());
		}
		person.getRelationships().add(relationship);
		
	}

	@GET
	@Produces("application/json")
	@Path("search/{searchString}")
	public String search(@PathParam("searchString") final String searchInput) {
		final String searchString = searchInput.toLowerCase();
		final Map<String, Person> searchResult = new HashMap<>();
		for (final Entry<String, Person> person : familyData.entrySet()) {
			if (person.getValue().getUserInfo().getFirstName().toLowerCase().contains(searchString) ||
					person.getValue().getUserInfo().getLastName().toLowerCase().contains(searchString)) {
				searchResult.put(person.getKey(), person.getValue());	
			}
		}
		
		return gson.toJson(searchResult);
    }

	@GET
	@Produces("application/json")
	@Path("getRelations/{personId}/depth/{depth}")
	@Secured
	public String getRelations(@PathParam("personId") final String personId, @PathParam("depth") final int depth) {
		final Map<String, GetRelationsResult> relations = new HashMap<>();
		
		getRelations(personId, depth, relations, 0);
		
		return gson.toJson(relations);
    }
	
	@GET
	@Produces("application/json")
	@Path("get/user/details/{personId}")
	@Secured
	public String getUserDetails(@PathParam("personId") final String personId) {
		Map<String, GetRelationsResult> relations = new HashMap<>();
		getRelations(personId, 1, relations, 0);
		UserDetailsResult userDetailsResult = new UserDetailsResult();
		userDetailsResult.setRelationships(relations.get(personId).getRelationships());
		userDetailsResult.setUserInfo(familyData.get(personId).getUserInfo());
		userDetailsResult.setRelationInfo(new HashMap<>());
		
		relations.entrySet().stream().filter(e -> e.getKey() != personId)
			.forEach(e -> {
				userDetailsResult.getRelationInfo().put(e.getKey(), e.getValue().getUserInfo());
			});
		
		return gson.toJson(userDetailsResult);
	}
	
	@SneakyThrows
	private void updateDatabase() {
    	FamilyData familyDataEntity = new FamilyData();
    	familyDataEntity.setFamilyData(familyData);
		objectMapper.writeValue(new File(BASE_DATA_PATH + "/FamilyData.txt"), familyDataEntity);
		
	}

	private void getRelations(final String personId, 
			final int depth,
			final Map<String, GetRelationsResult> relations,
			final int level) {
		if (relations.containsKey(personId)) {
			return;
		}
		final GetRelationsResult getRelationsResult = new GetRelationsResult();
		relations.put(personId, getRelationsResult);
		
		final Person person = familyData.get(personId);
		final NodeUserInfo nodeUserInfo = getNodeUserInfo(person, level);
		getRelationsResult.setUserInfo(nodeUserInfo);
		
		if (depth <= 0) {
			return;
		}
		
		if (null != person.getRelationships()) {
		    getRelationsResult.setRelationships(person.getRelationships());
		    for (final Relationship relationship : person.getRelationships()) {
				getRelations(relationship.getRelatedPersonId(), depth - 1, relations,
						getLevel(relationship.getRelationshipType(), level));		
			}
		}
		
	}

	private int getLevel(String relationshipType, int level) {
		switch (relationshipType) {
			case "PARENT":
				return level -1;
			case "CHILD":
				return level + 1;
			default:
				return level;
		}
	}

	private NodeUserInfo getNodeUserInfo(final Person person, 
			final int level) {
		final NodeUserInfo nodeUserInfo = new NodeUserInfo();
		nodeUserInfo.setName(person.getUserInfo().getFirstName() + " " + person.getUserInfo().getLastName());
		nodeUserInfo.setLevel(level);
		nodeUserInfo.setGender(person.getUserInfo().getGender());
		
		return nodeUserInfo;
		
	}
}
