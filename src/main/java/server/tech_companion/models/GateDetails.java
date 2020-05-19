package server.tech_companion.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GateDetails {
	private String location;
	private String accessCodes;
	private String operator1;
	private String operator2;
	private String gateType1;
	private String gateType2;
	private Boolean isMasterSlave;
}
