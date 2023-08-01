package cloud;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import util.DynamoService;

public class TestDBQueries {

    public static void main(String[] args) {

        DynamoService ddbService = new DynamoServiceImpl("ResumeTable");
        var response = ddbService.keywordQuery("Engineer");
        response.forEach(System.out::println);

    }

}
