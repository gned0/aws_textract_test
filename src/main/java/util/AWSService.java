package util;

import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;

import java.util.Set;

public interface AWSService {

    Set<String> listResumes();
    String startTextDetection(String docName);
    GetDocumentTextDetectionResponse getJobResults(String jobId);

}
