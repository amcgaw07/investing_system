package customer.tasks;

import common.*;

public class ShowBalanceAndSharesTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Show the balance and the total number of shares");
        controller.showBalanceAndShares();
    }
}
