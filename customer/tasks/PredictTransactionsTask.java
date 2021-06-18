package customer.tasks;

import common.*;

import java.sql.SQLException;

public class PredictTransactionsTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Predict the gain or loss of the transactions");
        //TODO: make it do the thing. get input if necessary and call controller.predictTransactions()
        try{
            controller.predictTransactions();
        }
        catch(SQLException e){
            System.out.println("SQLException in Predict Transactions");
            System.out.println(e.toString());
        }
    }
}