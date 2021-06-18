package customer.menu;

import customer.tasks.*;

import java.sql.SQLException;
import java.util.HashMap;

import common.*;

enum CustomerTaskType
{
    None,
    ShowBalanceAndShares,
    ShowMFsByName,
    ShowMFsByPrice,
    SearchMF,
    DepositInvestment,
    BuyShares,
    SellShares,
    ShowROI,
    PredictTransactions,
    ChangeAllocationPreference,
    RankAllocations,
    ShowPortfolio,
    ExitApplication
}

public class CustomerMenu
{
    private HashMap<CustomerTaskType, Task> tasks;

    public CustomerMenu()
    {
        initializeTasks();
    }

    public void run(Controller controller)
    {
        System.out.println("Running as customer");

        getCustomerLoginInfo(controller);

        CustomerTaskType customerTaskType = CustomerTaskType.None;
        while (customerTaskType != CustomerTaskType.ExitApplication)
        {
            boolean validInput = false;

            displayTaskOptions(controller);
    
            while (!validInput)
            {
                String input = controller.getUserInput();
                customerTaskType = determineTaskTypeFromInput(input);
                validInput = customerTaskType != CustomerTaskType.None;
    
                if (!validInput)
                    System.out.print("Invalid Input: \"" + input
                        + "\"\nPlease enter one of the options (1-13): ");
            }

            try{
                if (customerTaskType != CustomerTaskType.ExitApplication)
                    tasks.get(customerTaskType).execute(controller);
            }
            catch(SQLException e){
                System.out.println("SqlException in costumerMenu");
            }

        }
        System.out.println("Exiting the application");
    }

    void getCustomerLoginInfo(Controller controller)
    {
        boolean successfulLogin = false;

        String customerUsername = "";
        String customerPassword = "";
        while (!successfulLogin)
        {
            System.out.println("Enter customer login information:");
            System.out.print("\tLogin: ");
            customerUsername = controller.getUserInput();

            System.out.print("\tPassword: ");
            customerPassword = controller.getUserInput();

            try {
                successfulLogin = controller.tryToLoginCustomer(
                        customerUsername, customerPassword);
            }
            catch(SQLException e1){
                System.out.println(e1);
            }
        }
    }

    private void displayTaskOptions(Controller controller)
    {
        System.out.println("\nWelcome "
                + controller.getCustomerUsername() + "!");
        System.out.print("Select Option:\n"
                + "\t1. Show balance and total number of shares\n"
                + "\t2. Show mutual funds sorted by name\n"
                + "\t3. Show mutual funds sorted by prices on date\n"
                + "\t4. Search for a mutual fund\n"
                + "\t5. Deposit an amount for investment\n"
                + "\t6. Buy shares\n"
                + "\t7. Sell shares\n"
                + "\t8. Show return on investment\n"
                + "\t9. Predict the gain or loss of transactions\n"
                + "\t10. Change allocation preference\n"
                + "\t11. Rank the allocations\n"
                + "\t12. Show portfolio\n"
                + "\t13. Exit the application\n"
                + "\nEnter option number: ");
    }

    private CustomerTaskType determineTaskTypeFromInput(String input)
    {
        int optionNumber = 0;
        try
        {
            optionNumber = Integer.parseInt(input);
        }
        catch (Exception exception)
        {
            return CustomerTaskType.None;
        }

        switch (optionNumber)
        {
            case 1: return CustomerTaskType.ShowBalanceAndShares;
            case 2: return CustomerTaskType.ShowMFsByName;
            case 3: return CustomerTaskType.ShowMFsByPrice;
            case 4: return CustomerTaskType.SearchMF;
            case 5: return CustomerTaskType.DepositInvestment;
            case 6: return CustomerTaskType.BuyShares;
            case 7: return CustomerTaskType.SellShares;
            case 8: return CustomerTaskType.ShowROI;
            case 9: return CustomerTaskType.PredictTransactions;
            case 10: return CustomerTaskType.ChangeAllocationPreference;
            case 11: return CustomerTaskType.RankAllocations;
            case 12: return CustomerTaskType.ShowPortfolio;
            case 13: return CustomerTaskType.ExitApplication;
            default: return CustomerTaskType.None;
        }
    }

    void initializeTasks()
    {
        tasks = new HashMap<CustomerTaskType, Task>();

        tasks.put(CustomerTaskType.ShowBalanceAndShares,
            new ShowBalanceAndSharesTask());
        tasks.put(CustomerTaskType.ShowMFsByName,
            new ShowMFsByNameTask());
        tasks.put(CustomerTaskType.ShowMFsByPrice,
            new ShowMFsByPriceTask());
        tasks.put(CustomerTaskType.SearchMF,
            new SearchMFTask());
        tasks.put(CustomerTaskType.DepositInvestment,
            new DepositInvestmentTask());
        tasks.put(CustomerTaskType.BuyShares,
            new BuySharesTask());
        tasks.put(CustomerTaskType.SellShares,
            new SellSharesTask());
        tasks.put(CustomerTaskType.ShowROI,
            new ShowROITask());
        tasks.put(CustomerTaskType.PredictTransactions,
            new PredictTransactionsTask());
        tasks.put(CustomerTaskType.ChangeAllocationPreference,
            new ChangeAllocationPreferenceTask());
        tasks.put(CustomerTaskType.RankAllocations,
            new RankAllocationsTask());
        tasks.put(CustomerTaskType.ShowPortfolio,
            new ShowPortfolioTask());
    }
}