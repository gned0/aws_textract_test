package util;

import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;

import java.util.Set;

public interface S3Service {

    Set<String> listResumes();
    String startTextDetection(String docName);
    GetDocumentTextDetectionResponse getJobResults(String jobId);
    void close();

}
