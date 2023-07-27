package util;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;

import java.util.HashSet;
import java.util.Set;

public class AWSServiceImpl implements AWSService{

    private final String bucketName = "gian-bucket-00";
    private final Region region = Region.EU_WEST_3;
    private final ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
    private TextractClient textractClient;
    private S3Client s3Client;


    public AWSServiceImpl() {

        TextractClient textractClient = TextractClient.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();


        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

    }

    @Override
    public Set<String> listResumes() {
        Set<String> resumes = new HashSet<>();
        S3Client client = S3Client.builder().region(Region.US_EAST_1).build();
        ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucketName).build();
        ListObjectsV2Iterable response = s3Client.listObjectsV2Paginator(request);
        for (ListObjectsV2Response page : response) {
            page.contents().forEach((S3Object object) -> {
                resumes.add(object.key());
            });
        }
        return resumes;
    }

    @Override
    public String startTextDetection(TextractClient textractClient, String bucketName, String docName) {
        return null;
    }

    @Override
    public GetDocumentTextDetectionResponse getJobResults(TextractClient textractClient, String jobId) {
        return null;
    }
}
