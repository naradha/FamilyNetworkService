package arjung.family.network.entity;

import java.util.List;

import lombok.Data;

@Data
public class GetRelationsResult {

	private List<Relationship> relationships;
	private NodeUserInfo userInfo;
}
