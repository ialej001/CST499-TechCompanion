package server.tech_companion.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue {
    private String location;
    private String problem;
    private String resolution;
}