package admin.tasks;

import common.*;

public class RankInvestorsTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Rank all the investors");
        controller.rankInvestors();
    }
}