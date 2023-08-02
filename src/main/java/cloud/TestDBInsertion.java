package cloud;

import util.DynamoService;

public class TestDBInsertion {

    public static void main(String[] args) {

        DynamoService ddbService = new DynamoServiceImpl("ResumeTable");
        ddbService.putItemInTable("engineer", "augusto_cv.pdf");

    }
}
