package admin.tasks;

import common.*;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AddMutualFundTask implements Task
{
    private static String taskName = "Add new mutual fund";

    public void execute(Controller controller)
    {
        System.out.println("Task started: " + taskName);

        System.out.println("Enter information for the mutual fund:");
        String columnName = getColumnNameFromInput(controller);
        String symbol = getSymbolFromInput(controller);
        String name = getNameFromInput(controller);
        String description = getDescriptionFromInput(controller);
        String category = getCategoryFromInput(controller);
        Date createdDate = getDateFromInput(controller);

        System.out.println("Adding mutual fund with the information:"
                + "\n\tColumn Name: " + columnName
                + "\n\tSymbol: " + symbol
                + "\n\tName: " + name
                + "\n\tDescription: " + description
                + "\n\tCategory: " + category
                + "\n\tDate: " + createdDate);

        // TODO: Create the mutual fund
        try{
            //Java dates suck and this returns the wrong date but its close
            /*GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            gc.clear();
            int day = createdDate.getDay() - 1;
            gc.set(createdDate.getYear(), createdDate.getMonth(), day);
            long milliseconds = gc.getTimeInMillis();*/
            //Date date = getDateFromInput(controller);
            controller.insertMutualFund(columnName, symbol, name, description, category, createdDate.toStringDashes());
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

    private String getColumnNameFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tColumn Name: ",
                "Mutual fund's column name is required.");
    }

    private String getSymbolFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tSymbol: ",
                "Mutual fund's symbol is required.");
    }

    private String getNameFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tName: ",
                "Mutual fund's name is required.");
    }

    private String getDescriptionFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tDescription: ",
                "Mutual fund's description is required.");
    }

    private String getCategoryFromInput(Controller controller)
    {
        return getRequiredStringInput(controller, "\tCategory: ",
                "Mutual fund's category is required.");
    }

    private Date getDateFromInput(Controller controller)
    {
        boolean validInput = false;

        while (!validInput)
        {
            String month = getRequiredStringInput(controller,"\tDate month: ",
                    "Mutual fund's date month is required.");
            String day = getRequiredStringInput(controller, "\tDate day: ",
                    "Mutual fund's date day is required.");
            String year = getRequiredStringInput(controller, "\tDate year: ",
                    "Mutual fund's date year is required.");

            try
            {
                validInput = true;
                return new Date(Integer.parseInt(month),
                        Integer.parseInt(day), Integer.parseInt(year));
            }
            catch (Exception exception)
            {
                validInput = false;
                System.out.println("Error processing date.\n"
                        + "Ensure all input only consists of integers.");
            }
        }

        // This line should not be reached
        return new Date(0, 0, 0);
    }
}
