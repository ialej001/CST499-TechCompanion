package server.tech_companion.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue {
	@GraphQLQuery(name = "location")
    private String location;
	@GraphQLQuery(name = "problem")
    private String problem;
	@GraphQLQuery(name = "resolution")
    private String resolution;
}