package util;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.util.HashSet;
import java.util.Set;

public class S3ServiceImpl implements S3Service {

    private final String bucketName = GetPropertyValues.getBucketName();
    private final ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
    private final S3Client s3Client = S3Client.builder()
            .region(GetPropertyValues.getRegion())
            .credentialsProvider(credentialsProvider)
            .build();
    private final TextractClient textractClient = TextractClient.builder()
            .region(GetPropertyValues.getRegion())
            .credentialsProvider(credentialsProvider)
            .build();

    @Override
    public Set<String> listResumes() {
        Set<String> resumes = new HashSet<>();
        ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucketName).build();
        ListObjectsV2Iterable response = this.s3Client.listObjectsV2Paginator(request);
        for (ListObjectsV2Response page : response) {
            page.contents().forEach((S3Object object) -> {
                resumes.add(object.key());
            });
        }
        return resumes;
    }

    @Override
    public String startTextDetection(String docName) {
        try {

            software.amazon.awssdk.services.textract.model.S3Object s3Object = software.amazon.awssdk.services.textract.model.S3Object.builder()
                    .bucket(bucketName)
                    .name(docName)
                    .build();

            DocumentLocation location = DocumentLocation.builder()
                    .s3Object(s3Object)
                    .build();

            StartDocumentTextDetectionRequest textDetectionRequest = StartDocumentTextDetectionRequest.builder()
                    .documentLocation(location)
                    .build();

            StartDocumentTextDetectionResponse response = this.textractClient.startDocumentTextDetection(textDetectionRequest);
            System.out.println("Sent request for document: " + docName + " jobId is" + response.jobId());
            // Get the job ID
            return response.jobId();

        } catch (TextractException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "" ;
    }

    @Override
    public GetDocumentTextDetectionResponse getJobResults(String jobId) {

        GetDocumentTextDetectionResponse response = null;
        boolean finished = false;
        String status = "" ;

        while (!finished) {
            GetDocumentTextDetectionRequest request = GetDocumentTextDetectionRequest.builder()
                    .jobId(jobId)
                    .maxResults(1000)
                    .build();

            response = textractClient.getDocumentTextDetection(request);
            status = response.jobStatus().toString();

            if (status.compareTo("SUCCEEDED") == 0) {
                finished = true;
                System.out.println("Job " + jobId + " done");
            }
        }

        return response;
    }

    @Override
    public void close() {
        this.s3Client.close();
        this.textractClient.close();
    }

}
