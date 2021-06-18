package customer.tasks;

import common.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShowMFsByNameTask implements Task
{
    public void execute(Controller controller)
    {

        try{
            controller.showMFsByName();
        }
        catch(SQLException e1){
            System.out.println("e1.toString() = " + e1.toString());
            System.out.println("message = " + e1.getMessage());
        }
        catch(ClassNotFoundException e2){
            System.out.println("e2.toString() = " + e2.toString());
            System.out.println("message = " + e2.getMessage());
        }
    }
}