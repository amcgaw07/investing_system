package admin.tasks;

import common.*;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class UpdateCurrentDateTask implements Task
{
    private static String taskName = "Update share quotes for a day";

    public void execute(Controller controller)
    {
        System.out.println("Task started: " + taskName);

        Date currentDate = getDateFromInput(controller);

        // TO DO: Set current date as the new date
        /*GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gc.clear();
        int day = currentDate.getDay() - 1;
        gc.set(currentDate.getYear(), currentDate.getMonth(), day);
        long milliseconds = gc.getTimeInMillis();*/

        controller.updateDate(currentDate.toStringDashes());

        System.out.println("The new current date is " + currentDate + ".");

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

    private Date getDateFromInput(Controller controller)
    {
        boolean validInput = false;

        while (!validInput)
        {
            String month = getRequiredStringInput(controller,"\tDate month (MM): ",
                    "Date month is required.");
            String day = getRequiredStringInput(controller, "\tDate day (DD): ",
                    "Date day is required.");
            String year = getRequiredStringInput(controller, "\tDate year(YYYY): ",
                    "Date year is required.");

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