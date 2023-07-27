package local;

import interfaces.QueryService;

public class TestLocalQueryService {

    public static void main(String args[]) {

        QueryService service = new LocalQueryService();



        service.closeService();


    }

}
