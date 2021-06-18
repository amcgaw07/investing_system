package admin.tasks;

import common.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class UpdateShareQuotesTask implements Task
{
    private static String taskName = "Update share quotes for a day";
    private static String lineDelimiter = ",";

    public void execute(Controller controller)
    {
        System.out.println("Task started: " + taskName);

        String filename = getFilenameFromInput(controller);
        System.out.println("Attempting to read file: " + filename);

        HashMap<String, Double> mutualFundPrices
            = new HashMap<String, Double>();

        boolean successfulRead = tryToReadPricesFromFile(
                filename, mutualFundPrices);

        if (successfulRead)
        {
            System.out.println("Successfully read file: " + filename);

            // TO DO: Update mutual fund prices using the hash map
            controller.updateShareQuotes(mutualFundPrices);
        }
        else
        {
            System.out.println("Failed to read file: " + filename);
        }

        System.out.println("Task completed: " + taskName);
    }

    private String getFilenameFromInput(Controller controller)
    {
        String input = "";
        while (input.isEmpty())
        {
            System.out.print("Enter filename containing MF prices: ");
            input = controller.getUserInput();

            if (input.isEmpty())
                System.out.println("Filename is required.");
        }
        return input;
    }

    boolean tryToReadPricesFromFile(String filename,
        HashMap<String, Double> mutualFundPrices)
    {
        try
        {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine())
            {
                String line = fileScanner.nextLine();
                tryToParseLine(line, mutualFundPrices);
            }
            fileScanner.close();

            return true;
        }
        catch (FileNotFoundException exception)
        {
            System.out.println("Failed to find file: " + filename);
            return false;
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    void tryToParseLine(String line, HashMap<String, Double> mutualFundPrices)
    {
        String[] splitResult = line.split(lineDelimiter);

        if (splitResult.length > 2) return;

        try
        {
            String mutualFundName = splitResult[0];
            double mutualFundPrice = Double.parseDouble(splitResult[1]);
            mutualFundPrices.put(mutualFundName, mutualFundPrice);
        }
        catch (Exception exception) {}
    }
}