package org.examples;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.textract.model.*;
import software.amazon.awssdk.services.textract.TextractClient;

public class AsyncDetectAndUse {
    static String bucketName = "gian-bucket-00";
    static String docName = "rachel_cv.pdf";
    public static void main(String[] args) {

        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();

        Region region = Region.EU_WEST_3;
        TextractClient textractClient = TextractClient.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();


        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        String jobId = startDocAnalysisS3(textractClient, bucketName, docName);
        System.out.println("Getting results for job " + jobId);
        GetDocumentTextDetectionResponse response = getJobResults(textractClient, jobId);
        useResponse(response);
        textractClient.close();
        s3Client.close();
    }

    private static void useResponse(GetDocumentTextDetectionResponse response) {

        response.blocks().forEach(block -> {
            System.out.println("Block type is: " + block.blockType()
                    + ", block text is " + block.text());
        });

    }

    public static String startDocAnalysisS3 (TextractClient textractClient, String bucketName, String docName) {

        try {

            OutputConfig config = OutputConfig.builder()
                    .s3Bucket(bucketName)
                    .build();

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

    private static GetDocumentTextDetectionResponse getJobResults(TextractClient textractClient, String jobId) {

        GetDocumentTextDetectionResponse response = null;
        boolean finished = false;
        int index = 0 ;
        String status = "" ;

        try {
            while (!finished) {
                GetDocumentTextDetectionRequest request = GetDocumentTextDetectionRequest.builder()
                        .jobId(jobId)
                        .maxResults(1000)
                        .build();

                response = textractClient.getDocumentTextDetection(request);
                status = response.jobStatus().toString();

                if (status.compareTo("SUCCEEDED") == 0) {
                    finished = true;
                    System.out.println(index + " status is: " + status);
                    //putS3Object(s3Client, bucketName, docName.substring(0, docName.length() - 4), response);
                    }
                else {
                    System.out.println(index + " status is: " + status);
                    Thread.sleep(1000);
                }
                index++ ;
            }


        } catch( InterruptedException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Prova");
        return response;
    }

}