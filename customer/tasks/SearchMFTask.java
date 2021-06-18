package customer.tasks;

import common.*;

import java.sql.SQLException;

public class SearchMFTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Search for a mutual fund");
        String keyword1 = getFirstKeywordFromInput(controller);
        String keyword2 = getSecondKeywordFromInput(controller);
        try{
            controller.searchMutualFunds(keyword1, keyword2);
        }
        catch (SQLException exception){
            System.out.println("SQLException occurred in SearchMFTask");
        }
        catch(ClassNotFoundException exception){
            System.out.println("ClassNotFoundException occurred in SearchMFTask");
        }
    }
    private String getFirstKeywordFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tKeyword1: ",
                "Keyword1 is required.");
    }
    private String getSecondKeywordFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tKeyword2: ",
                "Keyword2 is required.");
    }

    private String getRequiredStringInput(Controller controller,
                                          String inputPrompt, String errorMessage)
    {
        String input = "";
        while (input.isEmpty())
        {
            System.out.print(inputPrompt);
            input = controller.getUserInput();

            if (input.isEmpty())
                System.out.println(errorMessage);
        }
        return input;
    }
}
