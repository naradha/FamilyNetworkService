package arjung.family.network.entity;

import java.util.List;

import lombok.Data;

@Data
public class Person {
	
	private UserInfo userInfo;
	private List<Relationship> relationships;

}
