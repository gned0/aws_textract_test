package cloud;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import util.DynamoService;

public class TestDB {

    public static void main(String[] args) {


        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.EU_WEST_3;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();

        DynamoService ddbService = new DynamoServiceImpl("ResumeTable");
        ddbService.putItemInTable("Engineer", "augusto_cv.pdf");
        ddb.close();
    }
}
