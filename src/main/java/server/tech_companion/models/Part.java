package server.tech_companion.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "parts")
public class Part {
    @Id
    private ObjectId _id;
    private String string_id;
    private String partNumber;
    private String description;
    private String name;
    private Double price;

    @Override
    public String toString() {
        return "Part [_id=" + _id + ", description=" + description + ", name=" + name + ", partNumber=" + partNumber
                + ", price=" + price + "]";
    }
}