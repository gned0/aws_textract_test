package local;

import util.AWSService;
import util.AWSServiceImpl;
import util.QueryService;

import java.util.Scanner;
import java.util.Set;

public class TestLocalQueryService {

    public static void main(String[] args) {

        QueryService service = new LocalQueryService();
        AWSService aws = new AWSServiceImpl();
        aws.listResumes().forEach(e -> System.out.println(e));
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a keyword to be looked up in the resume dictionary: ");
        String inputKeyword = scanner.nextLine();
        scanner.close();

        Set<String> resultSet = service.lookupKeyword(inputKeyword);
        if(resultSet.size() < 1) {
            System.out.println("No resume containing input keyword were found.");
        } else {
            System.out.println("The following resumes containing input keyword were found: "
                    + String.join(", ", resultSet) + ".");
        }

        service.closeService();

    }

}
