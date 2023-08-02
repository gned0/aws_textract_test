package cloud;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import util.DynamoService;
import util.GetPropertyValues;

import java.util.*;

public class DynamoServiceImpl implements DynamoService {

    private final String tableName;
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .credentialsProvider(ProfileCredentialsProvider.create())
            .region(GetPropertyValues.getRegion())
            .build();

    public DynamoServiceImpl(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public Set<String> getAllValues(boolean print) {

        // Define the name of the Set attribute you want to retrieve values from
        String attribute = "Resumes";

        // Set to store all the strings from the Set attribute
        Set<String> allStringsSet = new HashSet<>();

        // Prepare the ScanRequest to read all items from the table
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .build();

        // Perform the Scan operation to read all items
        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        // Process each item in the scan result to extract the Set attribute values
        for (Map<String, AttributeValue> item : scanResponse.items()) {
            AttributeValue setAttribute = item.get(attribute);
            if (setAttribute != null) {
                allStringsSet.addAll(setAttribute.ss());
            }
        }

        if(print) allStringsSet.forEach(System.out::println);
        return allStringsSet;
    }

    @Override
    public void putItemInTable(String keyword, String resume) {

        Set<String> itemsToAdd = new HashSet<>(Set.of(resume));

        UpdateItemRequest request = UpdateItemRequest.builder()
                    .tableName(tableName)
                    .key(Collections.singletonMap("Keyword", AttributeValue.builder().s(keyword).build()))
                    .updateExpression("ADD Resumes :values")
                    .expressionAttributeValues(Collections.singletonMap(":values", AttributeValue.builder().ss(itemsToAdd).build()))
                    .build();

        try {
            UpdateItemResponse response = dynamoDbClient.updateItem(request);

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
    public Set<String> keywordQuery(String keyword) {

        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("Keyword", AttributeValue.builder()
                .s(keyword)
                .build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .attributesToGet("Resumes")
                .tableName(tableName)
                .build();

        return new HashSet<>(dynamoDbClient.getItem(request).item().get("Resumes").ss());
    }

    @Override
    public void close() {
        dynamoDbClient.close();
    }

    public void scan() {


        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(this.tableName)
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        for (Map<String, AttributeValue> item : scanResponse.items()) {
            for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
                String key = entry.getKey();
                AttributeValue value = entry.getValue();

                System.out.println("Key: " + key);
                System.out.println("Value: " + value);
            }
        }

    }
}
