package cloud;

import util.QueryService;

import java.util.Scanner;
import java.util.Set;

public class TestCloudQueryService {

    public static void main(String[] args) {

        QueryService service = new CloudQueryService();
        Scanner scanner = new Scanner(System.in);
        String exitWord = "quit";

        String inputKeyword = "";
        while (!inputKeyword.equalsIgnoreCase(exitWord)) {
            // Request a word from the user
            System.out.print("Enter a keyword to be looked up in the resume dictionary " +
                    "(or enter 'quit' to shut the service down): ");
            inputKeyword = scanner.nextLine();

            if (!inputKeyword.equalsIgnoreCase(exitWord)) {
                Set<String> resultSet = service.lookupKeyword(inputKeyword);
                if(resultSet.size() < 1) {
                    System.out.println("-----------------------------------------");
                    System.out.println("No resumes containing input keyword were found.");
                    System.out.println("-----------------------------------------");
                } else {
                    System.out.println("-----------------------------------------");
                    System.out.println("The following resumes containing input keyword were found: "
                            + String.join(", ", resultSet) + ".");
                    System.out.println("-----------------------------------------");
                }
            }
        }

        System.out.println("Resume Full-text search service is shutting down");
        scanner.close();
        service.closeService();

    }

}
