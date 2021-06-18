package customer.tasks;

import common.*;

public class ShowMFsByPriceTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Show mutual funds sorted by price on a date");
        String createdDate = getDateStringFromInput(controller);
        controller.showMFbyDate(createdDate);
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
    private String getDateStringFromInput(Controller controller)
    {
        boolean validInput = false;
        String month = "01";
        String day = "01";
        String year = "01";
        while (!validInput)
        {
            month = getRequiredStringInput(controller,"\tDate month: ",
                    "Mutual fund's date month is required.");
            day = getRequiredStringInput(controller, "\tDate day: ",
                    "Mutual fund's date day is required.");
            year = getRequiredStringInput(controller, "\tDate year: ",
                    "Mutual fund's date year is required.");
            try
            {
                Date test = new Date(Integer.parseInt(month),
                        Integer.parseInt(day), Integer.parseInt(year));
                validInput = true;
            }
            catch (Exception exception)
            {
                validInput = false;
                System.out.println("Error processing date.\n"
                        + "Ensure all input only consists of integers.");
            }
        }
        return day +"-"+ month +"-"+ year;
    }

}