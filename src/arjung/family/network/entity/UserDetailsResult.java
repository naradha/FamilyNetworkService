package arjung.family.network.entity;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class UserDetailsResult {

	private List<Relationship> relationships;
	private UserInfo userInfo;
	private Map<String, NodeUserInfo> relationInfo;
}
