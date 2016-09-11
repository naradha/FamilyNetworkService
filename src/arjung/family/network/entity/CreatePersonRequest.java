package arjung.family.network.entity;

import lombok.Data;

@Data
public class CreatePersonRequest {

	private UserInfo userInfo;
	private Credential credential;
}
