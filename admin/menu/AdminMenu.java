package admin.menu;

import admin.tasks.*;
import common.*;

import java.sql.SQLException;
import java.util.HashMap;

enum AdminTaskType
{
    None,
    EraseDatabase,
    AddCustomer,
    AddMutualFund,
    UpdateShareQuotes,
    ShowTopCategories,
    RankInvestors,
    UpdateCurrentDate,
    ExitApplication
}

public class AdminMenu
{
    private HashMap<AdminTaskType, Task> tasks;

    public AdminMenu()
    {
        initializeTasks();
    }

    public void run(Controller controller)
    {
        System.out.println("Running as administrator");

        getAdminLoginInfo(controller);

        AdminTaskType adminTaskType = AdminTaskType.None;
        while (adminTaskType != AdminTaskType.ExitApplication)
        {
            boolean validInput = false;

            displayTaskOptions(controller);
    
            while (!validInput)
            {
                String input = controller.getUserInput();
                adminTaskType = determineTaskTypeFromInput(input);
                validInput = adminTaskType != AdminTaskType.None;
    
                if (!validInput)
                    System.out.print("Invalid Input: \"" + input
                        + "\"\nPlease enter one of the options (1-8): ");
            }

            try{
                if (adminTaskType != AdminTaskType.ExitApplication)
                    tasks.get(adminTaskType).execute(controller);
            }
            catch(SQLException e){
                System.out.println("sqlException in adminMenu");
            }
        }
        System.out.println("Exiting the application");
    }

    private void getAdminLoginInfo(Controller controller)
    {
        boolean successfulLogin = false;

        String adminUsername = "";
        String adminPassword = "";
        while (!successfulLogin)
        {
            System.out.println("Enter admin login information:");
            System.out.print("\tLogin: ");
            adminUsername = controller.getUserInput();

            System.out.print("\tPassword: ");
            adminPassword = controller.getUserInput();

            successfulLogin = controller.tryToLoginAdmin(
                    adminUsername, adminPassword);
        }
    }

    private void displayTaskOptions(Controller controller)
    {
        System.out.println("\nWelcome "
                + controller.getAdminUsername() + "!");
        System.out.print("Select Option:\n"
                + "\t1. Erase the database\n"
                + "\t2. Add a customer\n"
                + "\t3. Add new mutual fund\n"
                + "\t4. Update share quotes for a day\n"
                + "\t5. Show top-k highest volume categories\n"
                + "\t6. Rank all the investors\n"
                + "\t7. Update the current date\n"
                + "\t8. Exit the application\n"
                + "\nEnter option number: ");
    }

    private AdminTaskType determineTaskTypeFromInput(String input)
    {
        int optionNumber = 0;
        try
        {
            optionNumber = Integer.parseInt(input);
        }
        catch (Exception exception)
        {
            return AdminTaskType.None;
        }

        switch (optionNumber)
        {
            case 1: return AdminTaskType.EraseDatabase;
            case 2: return AdminTaskType.AddCustomer;
            case 3: return AdminTaskType.AddMutualFund;
            case 4: return AdminTaskType.UpdateShareQuotes;
            case 5: return AdminTaskType.ShowTopCategories;
            case 6: return AdminTaskType.RankInvestors;
            case 7: return AdminTaskType.UpdateCurrentDate;
            case 8: return AdminTaskType.ExitApplication;
            default: return AdminTaskType.None;
        }
    }

    private void initializeTasks()
    {
        tasks = new HashMap<AdminTaskType, Task>();

        tasks.put(AdminTaskType.EraseDatabase,
            new EraseDatabaseTask());
        tasks.put(AdminTaskType.AddCustomer,
            new AddCustomerTask());
        tasks.put(AdminTaskType.AddMutualFund,
            new AddMutualFundTask());
        tasks.put(AdminTaskType.UpdateShareQuotes,
            new UpdateShareQuotesTask());
        tasks.put(AdminTaskType.ShowTopCategories,
            new ShowTopCategoriesTask());
        tasks.put(AdminTaskType.RankInvestors,
            new RankInvestorsTask());
        tasks.put(AdminTaskType.UpdateCurrentDate,
            new UpdateCurrentDateTask());
    }
}