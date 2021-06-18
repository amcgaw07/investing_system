package customer.tasks;

import common.*;

import java.sql.SQLException;

public class ShowPortfolioTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Show portfolio");
        //TODO: make it do the thing. get input if necessary and call
        try{
            controller.showPortfolio();
        }
        catch (SQLException e){
            System.out.println(e.toString());
            System.out.println("SQLException occurent in show portfolio task");
        }
    }
}