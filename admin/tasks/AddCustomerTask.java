package admin.tasks;

import common.*;

import java.sql.SQLException;
import java.sql.Driver;
import java.sql.DriverManager;

public class AddCustomerTask implements Task
{
    private static String taskName = "Add a customer";

    public void execute(Controller controller)
    {
        System.out.println("Task started: " + taskName);

        System.out.println("Enter information for the customer:");
        String login = getLoginFromInput(controller);
        String name = getNameFromInput(controller);
        String email = getEmailFromInput(controller);
        String address = getAddressFromInput(controller);
        String password = getPasswordFromInput(controller);
        int initialBalance = getInitialBalanceFromInput(controller);

        System.out.println("Adding customer with the information:"
            + "\n\tLogin: " + login
            + "\n\tName: " + name
            + "\n\tEmail: " + email
            + "\n\tAddress: " + address
            + "\n\tPassword: " + password
            + "\n\tInitial Balance: " + initialBalance);

        // TODO: Insert the information into the appropriate tables
        try{
            controller.insertCostumer(login, name, email, address, password, initialBalance);
        }
        catch (SQLException exception){
            System.out.println("SQLException occurred in AddCustomerTask");
        }
        System.out.println("Task completed: " + taskName);
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

    private String getLoginFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tLogin: ",
            "Customer's login is required.");
    }

    private String getNameFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tName: ",
            "Customer's name is required.");
    }

    private String getEmailFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tEmail: ",
            "Customer's email is required.");
    }

    private String getAddressFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tAddress: ",
                "Customer's address is required.");
    }

    private String getPasswordFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tPassword: ",
            "Customer's password is required.");
    }

    private int getInitialBalanceFromInput(Controller controller)
    {
        System.out.print("\tInitial Balance: ");

        String input = controller.getUserInput();
        int initialBalance = 0;

        // If the balance is not given, it should be initialized with 0
        if (input.isEmpty())
            return 0;
        try
        {
            initialBalance = Integer.parseInt(input);
        }
        catch (Exception exception)
        {
            initialBalance = 0;
        }
        return initialBalance;
    }
}
