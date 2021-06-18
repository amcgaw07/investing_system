package customer.tasks;

import common.*;

import java.sql.SQLException;

public class SellSharesTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Sell shares");

        System.out.println("Enter information for selling shares:");
        String symbol = getSymbolFromInput(controller);
        int numShares = getNumSharesFromInput(controller);

        System.out.println("Adding customer with the information:"
                + "\n\tSymbol: " + symbol
                + "\n\tNumber of Shares: " + String.valueOf(numShares));

        // TODO: Insert the information into the appropriate tables
        try{
            controller.sellShares(symbol, numShares);
            System.out.println("Task completed: Sell Shares");
        }
        catch (SQLException exception){
            System.out.println("SQLException occurred in SellSharesTask");
        }

    }
    private String getSymbolFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tSymbol: ",
                "Symbol is required.");
    }

    private int getNumSharesFromInput(Controller controller)
    {
        //return getRequiredStringInput(controller, "\tNumber of shares: ",
        //       "Number of shares is required.");

        System.out.print("\tNumber of shares:");
        String input = controller.getUserInput();
        try{
            return Integer.parseInt(input);
        }
        catch(NumberFormatException e){
            System.out.println("numberFormatException");
            return 0;
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
}
