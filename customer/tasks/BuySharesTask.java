package customer.tasks;

import common.*;

import java.sql.SQLException;

public class BuySharesTask implements Task
{
    public void execute(Controller controller) throws SQLException {
        System.out.println("Task started: Buy shares");
        String symbol = getSymbolFromInput(controller);;
        int numShares = getnumSharesFromInput(controller);;

        controller.buyShares( symbol, numShares);
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

    private int getnumSharesFromInput(Controller controller)
    {
        //return getRequiredStringInput(controller, "\tNumber of shares: ",
         //       "Number of shares is required.");

        System.out.print("\tNumber of shares:");
        String input = controller.getUserInput();
        try{
            return Integer.parseInt(input);
        }
        catch(NumberFormatException e){
            System.out.println("numberFOrmatException");
            return 0;
        }
    }
}
