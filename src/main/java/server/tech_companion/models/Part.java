package server.tech_companion.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "parts")
public class Part {
    @Id
    private ObjectId _id;
    @GraphQLQuery(name = "partNumber")
    private String partNumber;
    @GraphQLQuery(name = "description")
    private String description;
    private String name;
    @GraphQLQuery(name = "price")
    private Double price;
    private Integer quantity;

    @Override
    public String toString() {
        return "Part [_id=" + _id + ", description=" + description + ", name=" + name + ", partNumber=" + partNumber
                + ", price=" + price + ", quantity=" + quantity + "]";
    }
}