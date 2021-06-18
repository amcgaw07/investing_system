# Overview
This program is a simulated 401k investment system built with Java, JDBC, and PostgreSQL
*Instructions for running the program are at the bottom*

**Within the system there is both admin and customer functionality with the following options:**

Admins are able to:
1. Erase the database
2. Add customers
3. Add new mutual funds
4. Update share quotes for a day
5. Show highest volume categories
6. Rank all the investors
7. Update the current date

Customers are able to: 
1. View their balance and number of shares
2. List the mutual funds sorted by name
3. List the mutual funds sorted by price on a certain date
4. Search for a mutual fund
5. Deposit for an investment
6. Buy shares
7. Sell shares
8. Show their ROI (return on investment)
9. Predict the gain or loss of their transactions
10. Change their allocation preferences
11. Rank their allocations
12. Show their portfolio (what they own, what it's worth, and the predicted yield of their transactions

# To Run:
1. Download and Setup [DataGrip](https://www.jetbrains.com/help/datagrip/meet-the-product.html) for a local PostgreSQL server
2. Within common/Controller.java edit the url, user, and password to match your connection details
```
public Controller() throws SQLException, ClassNotFoundException
    {
        scanner = new Scanner(System.in);
        Class.forName("org.postgresql.Driver");
	    String url = "jdbc:postgresql://localhost:5432/";
        Properties props = new Properties();
        props.setProperty("user", "HERE");
        props.setProperty("password", "HERE");
        conn = DriverManager.getConnection(url, props);
        st = conn.createStatement();
    }
   
   ```

3.  Download and incorporate the [JDBC driver](https://jdbc.postgresql.org/) into your project (easiest within an IDE; IntelliJ, Eclipse, etc.)
4. Compile and run the main class, Driver.java

