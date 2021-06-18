package customer.tasks;

import common.*;

import java.sql.SQLException;

public class ShowROITask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Show return on investment");
        //TODO: make it do the thing. get input if necessary and call controller.showROI()
        //String symbol = getSymbolFromInput(controller);
        //String login = getLoginFromInput(controller);
        try{
            controller.showROI();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println("SQLError in showROITask");
        }
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
    private String getSymbolFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tSymbol: ",
                "Symbol is required.");
    }
    private String getLoginFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tLogin: ",
                "Login is required.");
    }
}