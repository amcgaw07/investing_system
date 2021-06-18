package admin.tasks;

import common.*;

import java.sql.ResultSet;
import java.util.ArrayList;

public class ShowTopCategoriesTask implements Task
{
    private static String taskName = "Show top-k highest volume categories";

    public void execute(Controller controller)
    {
        System.out.println("Task started: " + taskName);

        int kValue = getKValueFromInput(controller);

        ArrayList<String> kHighestVolumeCategories
                = new ArrayList<>(kValue);

        // TODO: populate the array list with k highest volume categories
        // TODO: this is finished but it doesn't use arraylist hope thats okay
        System.out.println(kValue + " highest volume categories:");
        controller.showTopK(kValue);

        int currentKValue = 1;
        for (String category : kHighestVolumeCategories)
        {
            System.out.println("\t" + currentKValue + ". " + category);
            currentKValue++;
        }

        System.out.println("Task completed: " + taskName);
    }

    int getKValueFromInput(Controller controller)
    {
        int kValue = 0;
        boolean validInput = false;
        while (!validInput)
        {
            System.out.print("Enter the k-value: ");

            try
            {
                kValue = Integer.parseInt(controller.getUserInput());
                if (kValue > 0) validInput = true;
            }
            catch (Exception exception)
            {
                kValue = 0;
                validInput = false;

                System.out.println("The k-value must be a positive integer.");
            }
        }
        return kValue;
    }
}