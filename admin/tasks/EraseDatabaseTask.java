package admin.tasks;

import common.*;

import java.sql.SQLException;

public class EraseDatabaseTask implements Task
{
    private static String taskName = "Erase the database";

    public void execute(Controller controller)
    {
        System.out.println("Task started: " + taskName);

        System.out.print("\nConfirm deletion of all data:\n"
            + "\t1. Yes\n\t2. No\nEnter option number: ");

        boolean confirmation = false;
        boolean validInput = false;
        while (!validInput)
        {
            String input = controller.getUserInput();

            confirmation = input.equals("1");
            validInput = input.equals("1") || input.equals("2");

            if (!validInput)
                System.out.print("Invalid Input: \"" + input
                    + "\"\nPlease enter one of the options (1-2): ");
        }

        if (!confirmation)
        {
            System.out.println("Task cancelled: " + taskName);
            return;
        }

        // TODO: Delete all the tuples of all the tables in the database
        try{
            controller.eraseDatabase();
        }
        catch(SQLException e1){
            System.out.println("rollback rollback rollback");
            System.out.println("e1.toString() = " + e1.toString());
            System.out.println("message = " + e1.getMessage());
        }

        System.out.println("Task completed: " + taskName);
    }
}