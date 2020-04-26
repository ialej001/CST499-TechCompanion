package server.tech_companion.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@Document(collection = "parts")
public class Part {
    @Id
    private ObjectId _id;
    private String partNumber;
    private String description;
    private String name;
    private Double price;
    private Integer quantity;

    @Override
    public String toString() {
        return "Part [_id=" + _id + ", description=" + description + ", name=" + name + ", partNumber=" + partNumber
                + ", price=" + price + ", quantity=" + quantity + "]";
    }
}