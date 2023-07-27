package util;

import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;

import java.util.Set;

public interface AWSService {

    Set<String> listResumes();
    String startTextDetection(TextractClient textractClient, String bucketName, String docName);
    GetDocumentTextDetectionResponse getJobResults(TextractClient textractClient, String jobId);

}
