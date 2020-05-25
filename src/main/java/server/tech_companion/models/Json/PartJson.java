package server.tech_companion.models.Json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PartJson {
    private String string_id;
    private String partNumber;
    private String description;
//    private String name;
    private Double price;

    @Override
    public String toString() {
        return "Part [description=" + description + ", partNumber=" + partNumber
                + ", price=" + price + "]";
    }
}