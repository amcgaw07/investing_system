package application;

import admin.menu.AdminMenu;
import common.Controller;
import customer.menu.CustomerMenu;
import java.sql.*;
import java.util.*;

public class Application
{
    private Controller controller;

    private AdminMenu adminMenu;
    private CustomerMenu customerMenu;

    public Application()
    {
        try{
            controller = initializeController();
            initializeMenus();
        }
        catch(SQLException e ){
            System.out.println("e1.toString() = " + e.toString());
            System.out.println("message = " + e.getMessage());
            System.out.println("SQLState = " + e.getSQLState());
            System.out.println("SQLErrorCode = " + e.getErrorCode());
        }
        catch(ClassNotFoundException e){
            System.out.println("e1.toString() = " + e.toString());
            System.out.println("message = " + e.getMessage());
        }
    }

    public void run() {
        if (promptWhetherToRunAsAdmin()) adminMenu.run(controller);
        else customerMenu.run(controller);
    }

    private boolean promptWhetherToRunAsAdmin()
    {
        AppUserType userType = AppUserType.None;
        boolean validInput = false;

        System.out.print("\nSelect Option:\n"
            + "\t1. Run as administrator\n"
            + "\t2. Run as customer\n"
            + "\nEnter option number: ");

        while (!validInput)
        {
            String input = controller.getUserInput();
            userType = determineAppUserTypeFromInput(input);
            validInput = userType != AppUserType.None;

            if (!validInput)
                System.out.print("Invalid Input: \"" + input
                    + "\"\nPlease enter one of the options (1-2): ");
        }
        return userType == AppUserType.Admin;
    }

    private enum AppUserType { None, Admin, Customer }

    private AppUserType determineAppUserTypeFromInput(String input)
    {
        int optionNumber = 0;
        try
        {
            optionNumber = Integer.parseInt(input);
        }
        catch (Exception exception)
        {
            return AppUserType.None;
        }

        switch (optionNumber)
        {
            case 1: return AppUserType.Admin;
            case 2: return AppUserType.Customer;
            default: return AppUserType.None;
        }
    }

    public Controller initializeController() throws SQLException, ClassNotFoundException
    {
        controller = new Controller();
        return controller;
    }

    public void initializeMenus()
    {
        adminMenu = new AdminMenu();
        customerMenu = new CustomerMenu();
    }
}
