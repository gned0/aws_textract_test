package cloud;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import util.DynamoService;

import java.util.*;

public class DynamoServiceImpl implements DynamoService {

    private String tableName;

    public DynamoServiceImpl(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public List<String> getAllValues() {
        return null;
    }

    @Override
    public void putItemInTable(String keyword, String resume) {

        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.EU_WEST_3;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();


        Set<String> itemsToAdd = new HashSet<>(Set.of(resume));

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("Keyword", AttributeValue.builder().s(keyword).build()))
                .updateExpression("ADD Resumes :values")
                .expressionAttributeValues(Collections.singletonMap(":values", AttributeValue.builder().ss(itemsToAdd).build()))
                .build();



        try {
            UpdateItemResponse response = ddb.updateItem(request);
            System.out.println(tableName +" was successfully updated. The request id is "+response.responseMetadata().requestId());

        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The Amazon DynamoDB table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public List<String> keywordQuery(String keyword) {
        return null;
    }
}
