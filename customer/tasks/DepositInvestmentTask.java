package customer.tasks;

import common.*;

import java.sql.SQLException;

public class DepositInvestmentTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Deposit an amount for investment");
        Double depositAmount = getDepositAmountFromInput(controller);
        try{
            controller.deposit(depositAmount);
        }
        catch (SQLException exception){
            System.out.println("SQLException occurred in depositInvestmentTask");
        }
        catch (ClassNotFoundException exception){
            System.out.println("ClassNotFoundException occurred in depositInvestmentTask");
        }

    }
    private Double getDepositAmountFromInput(Controller controller)
    {
        Double input = -1.0;
        boolean parsed = false;
        while (!parsed)
        {
            System.out.print("Enter an amount to deposit for investment: ");
            try{
                input = Double.parseDouble(controller.getUserInput());
                parsed = true;
            }
            catch (NumberFormatException e){
                System.out.println("Failed to parse value as a double. Try again");
            }
        }
        return input;
    }

}
