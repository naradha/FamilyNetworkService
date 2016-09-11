package arjung.family.network.entity;

import lombok.Data;

@Data
public class ContactInfo {

	private String email;
	private String mobile;
	private String homePhone;
	private String workPhone;
	private Address homeAddress;
	private Address workAddress;
}
