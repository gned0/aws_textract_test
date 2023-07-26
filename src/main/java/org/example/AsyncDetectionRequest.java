package org.example;
// snippet-start:[textract.java2._start_doc_analysis.import]
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.model.*;
import software.amazon.awssdk.services.textract.TextractClient;

// snippet-end:[textract.java2._start_doc_analysis.import]

/**
 * Before running this Java V2 code example, set up your development environment, including your credentials.
 *
 * For more information, see the following documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */
public class AsyncDetectionRequest {

    public static void main(String[] args) {

        String bucketName = "gian-bucket-00";
        String docName = "rachel_cv.pdf";
        Region region = Region.EU_WEST_3;
        TextractClient textractClient = TextractClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        String jobId = startDocAnalysisS3(textractClient, bucketName, docName);
        System.out.println("Getting results for job " + jobId);
        String status = getJobResults(textractClient, jobId);
        System.out.println("The job status is "+ status);
        textractClient.close();
    }

    // snippet-start:[textract.java2._start_doc_analysis.main]
    public static String startDocAnalysisS3 (TextractClient textractClient, String bucketName, String docName) {

        try {

            S3Object s3Object = S3Object.builder()
                    .bucket(bucketName)
                    .name(docName)
                    .build();

            DocumentLocation location = DocumentLocation.builder()
                    .s3Object(s3Object)
                    .build();

            StartDocumentTextDetectionRequest documentAnalysisRequest = StartDocumentTextDetectionRequest.builder()
                    .documentLocation(location)
                    .build();

            StartDocumentTextDetectionResponse response = textractClient.startDocumentTextDetection(documentAnalysisRequest);

            // Get the job ID
            String jobId = response.jobId();
            return jobId;

        } catch (TextractException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "" ;
    }

    private static String getJobResults(TextractClient textractClient, String jobId) {

        boolean finished = false;
        int index = 0 ;
        String status = "" ;

        try {
            while (!finished) {
                GetDocumentTextDetectionRequest request = GetDocumentTextDetectionRequest.builder()
                        .jobId(jobId)
                        .maxResults(1000)
                        .build();

                GetDocumentTextDetectionResponse response = textractClient.getDocumentTextDetection(request);
                status = response.jobStatus().toString();

                if (status.compareTo("SUCCEEDED") == 0) {
                    finished = true;
                    response.blocks().forEach(block -> System.out.println(block.text()));
                    }
                else {
                    System.out.println(index + " status is: " + status);
                    Thread.sleep(1000);
                }
                index++ ;
            }

            return status;

        } catch( InterruptedException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
    // snippet-end:[textract.java2._start_doc_analysis.main]
}