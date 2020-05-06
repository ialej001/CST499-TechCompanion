package server.tech_companion.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import server.tech_companion.services.CustomerService;
import server.tech_companion.services.WorkOrderService;

@RestController
public class MainController {
	private final GraphQL graphQL;

    @Autowired
    public MainController(CustomerService customerService, WorkOrderService workOrderService) {

        GraphQLSchema schema = new GraphQLSchemaGenerator().withBasePackages("server.tech_companion")
        .withOperationsFromSingletons(customerService, workOrderService)
        .generate();

        graphQL = GraphQL.newGraphQL(schema).build();
    }

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> index(@RequestBody Map<String, String> request, HttpServletRequest raw) {
        ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput()
        .query(request.get("query"))
        .operationName(request.get("operationName"))
        .context(raw)
        .build());

        return executionResult.toSpecification();
    }
    
}
