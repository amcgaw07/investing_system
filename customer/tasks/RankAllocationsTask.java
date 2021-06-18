package customer.tasks;

import common.*;

import java.sql.SQLException;

public class RankAllocationsTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Rank the allocations");
        //TODO: make it do the thing. get input if necessary and call
        try{
            controller.rankAllocations();
        }
        catch(SQLException e){
            System.out.println(e.toString());
            System.out.println("SQLException in rank allocations task");
        }
    }
}