package customer.tasks;

import common.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeAllocationPreferenceTask implements Task
{
    public void execute(Controller controller)
    {
        System.out.println("Task started: Change allocation preference");
        int numFunds = getNumberOfDesiredFundsFromInput(controller);
        HashMap percentages = getPercentageFromInput(controller, numFunds);
        try{
            Date date = controller.getDate();
            //String login = "mike";
            int allocation_no = controller.assignNextAllocationNumber(date);
            for (Object symbol : percentages.keySet()){
                controller.changeAllocationPreference(allocation_no, symbol.toString(),
                        (Double) percentages.get(symbol));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
    }
    private int getNumberOfDesiredFundsFromInput(Controller controller)
    {
        System.out.println("How many funds would you like to add preferences for?");
        String input = controller.getUserInput();
        int numberOfDesiredFunds;
        if (input.isEmpty()){
            return 0;
        }
        try
        {
            numberOfDesiredFunds = Integer.parseInt(input);
            System.out.println("successfully read input");
        }
        catch (Exception exception)
        {
            numberOfDesiredFunds = 0;
        }
        return numberOfDesiredFunds;
    }
    private HashMap getPercentageFromInput(Controller controller, int numFunds)
    {
        HashMap<String, Double> percentages = new HashMap<String, Double>();
        String input1, input2;
        double percent;
        boolean goodInput = false;
        double total = 0;
        while(!goodInput){
            total = 0;
            for(int i = 0; i < numFunds; i++){
                // finishing this now
                System.out.println("Please enter a mutual fund:?");
                input1 = controller.getUserInput();
                System.out.println("Please enter a percentage (0.__):?");
                input2 = controller.getUserInput();
                percent = Double.parseDouble(input2);
                percentages.put(input1,percent);
            }
            for (String key : percentages.keySet()){
                total+= percentages.get(key);
            }
            System.out.println(String.valueOf(total));
            if ( total == Math.floor(total) && !Double.isInfinite(total)){
                goodInput = true;
                System.out.println("percentages read in successfully");
            }
            else{
                System.out.println("percentages total not equal to 1");
            }
        }

        return percentages;
    }
}