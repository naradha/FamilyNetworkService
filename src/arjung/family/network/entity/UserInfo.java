package arjung.family.network.entity;

import lombok.Data;

@Data
public class UserInfo {

	private String firstName;
	private String lastName;
	private String gender;
	private ContactInfo contactInfo;
}
