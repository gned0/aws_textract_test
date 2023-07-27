package org.examples;
// snippet-start:[textract.java2._start_doc_analysis.import]
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

// snippet-end:[textract.java2._start_doc_analysis.import]

/**
 * Before running this Java V2 code example, set up your development environment, including your credentials.
 *
 * For more information, see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */
public class RetrieveAndUse {
    static String bucketName = "gian-bucket-00";
    static String keyName = "textract_output/2330edf31b60eeb7d34c9325ce64f25c3a1609afc41115246516ecbf62667903/1";
    static ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
    static Region region = Region.EU_WEST_3;
    public static void main(String[] args) {


        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
        getObjectBytes(s3Client,bucketName, keyName, "C:\\Users\\gianl\\OneDrive\\Desktop\\Lavoro\\aws_gradle_test\\res");
    }


    // snippet-start:[textract.java2._start_doc_analysis.main]


    // snippet-start:[s3.java2.getobjectdata.main]
    public static void getObjectBytes (S3Client s3, String bucketName, String keyName, String path) {

        try {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();

            // Write the data to a local file.


        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
    // snippet-end:[textract.java2._start_doc_analysis.main]
}
