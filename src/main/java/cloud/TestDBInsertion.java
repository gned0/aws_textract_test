package cloud;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import util.DynamoService;

public class TestDBInsertion {

    public static void main(String[] args) {

        DynamoService ddbService = new DynamoServiceImpl("ResumeTable");
        ddbService.putItemInTable("Engineer", "augusto_cv.pdf");

    }
}
