package common;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.sql.Date;

public class Controller
{
    private Scanner scanner;
    private Connection conn;
    private Statement st;

    // Generally, store these as hashed values
    // but storing as regular strings for this assignment
    private String adminUsername;
    private String adminPassword;
    private String customerUsername;
    private String customerPassword;

    public Controller() throws SQLException, ClassNotFoundException
    {
        scanner = new Scanner(System.in);
        Class.forName("org.postgresql.Driver");
        //String url = "jdbc:postgresql://localhost:5432/";
        String url = "jdbc:postgresql://class3.cs.pitt.edu:5432/";
        Properties props = new Properties();
        props.setProperty("user", "ajm279");
        props.setProperty("password", "");
        conn = DriverManager.getConnection(url, props);
        st = conn.createStatement();
    }

    public String getUserInput()
    {
        return scanner.nextLine();
    }

    public String getCustomerUsername()
    {
        return customerUsername;
    }

    public String getAdminUsername()
    {
        return adminUsername;
    }

    public boolean tryToLoginCustomer(String username, String password) throws SQLException
    {
        customerUsername = username;
        customerPassword = password;

        try {
            //String query1 = "SELECT * FROM CUSTOMER WHERE login = '"+username+"' and password='"+password+"'";
            //ResultSet res1 = st.executeQuery(query1);
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM CUSTOMER WHERE login = ? and password=?");
            pstmt.setString(1,username);
            pstmt.setString(2,password);

            ResultSet res1 = pstmt.executeQuery();
            if (res1.next() == false) {
                System.out.println("Incorrect Login Information Please Enter a Correct Username and Password");
                return false;
            } else
                System.out.println("Successfully logged in as " + username);
                return true;
        }
        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
        return false;
    }

    public boolean tryToLoginAdmin(String username, String password)
    {
        adminUsername = username;
        adminPassword = password;

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ADMINISTRATOR WHERE login = ? and password=?");
            pstmt.setString(1,username);
            pstmt.setString(2,password);

            ResultSet res1 = pstmt.executeQuery();

            if (res1.next() == false) {
                System.out.println("Incorrect Login Information Please Enter a Correct Username and Password");
                return false;
            } else
                System.out.println("Successfully logged in as " + username);
            return true;
        }
        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
        return false;
    }

    //-------------------------- Admin --------------------------------

    //Task 1. works
    public void eraseDatabase() throws SQLException
    {
        try{
            conn.setAutoCommit(false);
            st.executeUpdate("TRUNCATE MUTUAL_DATE CASCADE");
            st.executeUpdate("TRUNCATE CUSTOMER CASCADE;");
            st.executeUpdate("TRUNCATE ADMINISTRATOR CASCADE;");
            st.executeUpdate("TRUNCATE MUTUAL_FUND CASCADE;");
            st.executeUpdate("TRUNCATE OWNS CASCADE;");
            st.executeUpdate("TRUNCATE TRXLOG CASCADE;");
            st.executeUpdate("TRUNCATE ALLOCATION CASCADE;");
            st.executeUpdate("TRUNCATE PREFERS CASCADE;");
            st.executeUpdate("TRUNCATE CLOSING_PRICE CASCADE;");
            conn.commit();
            System.out.println("Database Erased (truncated) Successfully");
        }

        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
    }

    //Task 2. works
    public void insertCostumer(String login, String name, String email, String address, String password, int initialBalance)
            throws SQLException
    {
        try{
           conn.setAutoCommit(false);
           st.executeUpdate("INSERT INTO CUSTOMER (login, name, email, address, password, balance) " +
                   "VALUES ('"+login+"', '"+name+"', '"+email+"','"+address+"','"+password+"',"+initialBalance+");");


           conn.commit();
           System.out.println("Customer inserted successfully");
        }
        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
    }

    //Task 3. works
    public void insertMutualFund(String columnName, String symbol, String name, String description, String category, String strDate) throws SQLException
    {
        try{
            //conn.setAutoCommit(false);
            //st.executeUpdate("INSERT INTO MUTUAL_FUND VALUES ('" +
            // symbol + "', '" + name + "','" + description + "','" + category + "'," + createdDate + ");");
            /*PreparedStatement pstmt = conn.prepareStatement("INSERT INTO MUTUAL_FUND VALUES (?,?,?,?,?);");
            pstmt.setString(1, symbol);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setString(4, category);
            pstmt.setString(5, "TO_DATE('"+strDate+"', 'DD-MM-YYYY')");
            */
            String query1 = "INSERT INTO MUTUAL_FUND VALUES('"+symbol+"', '"+name+"', '"+description+"', '"+category+ "', "+ "TO_DATE('"+strDate+"', 'DD-MM-YYYY'))";
            st.executeUpdate(query1);
            //conn.commit();
            System.out.println("Mutual Fund inserted successfully");
        }
        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
    }

    //Task 4. works but must be fed absolute path or relative path from root = 1555_team9-main/common/mf_input.txt
    public void updateShareQuotes(HashMap mutualFundPrices){
        try{
            String symbol = "defaultStopYellingAtMe";
            double price = 0;
            Date p_date;
            long millis = System.currentTimeMillis();
            p_date = new Date(millis);
            conn.setAutoCommit(false);
            Iterator itr = mutualFundPrices.keySet().iterator();
            while(itr.hasNext()){
                symbol = (String) itr.next();
                price = (double) mutualFundPrices.get(symbol);

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CLOSING_PRICE VALUES (?,?,?);");
            pstmt.setString(1, symbol);
            pstmt.setDouble(2, price);
            pstmt.setDate(3, p_date);
            pstmt.execute();
            }
            conn.commit();
            System.out.println("Customer inserted successfully");
        }
        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
    }

    //Task 5. i think works but need to create dummy data to test better
    public void showTopK(int k){
        System.out.println("Showing top K highest volume categories");
        try {
            String query1 = "DROP TABLE IF EXISTS temp1 CASCADE;\n" +
                    "CREATE TABLE temp1 as (SELECT o.symbol, mf.category, o.login, o.shares FROM mutual_fund as mf LEFT JOIN owns as o ON mf.symbol = o.symbol\n" +
                    "    WHERE login = 'mike');";
            String query2 = "DROP TABLE IF EXISTS categories CASCADE;\n" +
                    "CREATE TABLE categories\n" +
                    "(\n" +
                    "    category VARCHAR(20),\n" +
                    "    amount INT\n" +
                    ");";
            String query3 = "INSERT INTO categories VALUES\n" +
                    "    ('fixed', (SELECT SUM(shares) as fixed_sum FROM temp1 where category = 'fixed'));\n" +
                    "INSERT INTO categories VALUES\n" +
                    "    ('bonds', (SELECT SUM(shares) as bonds_sum FROM temp1 where category = 'bonds'));\n" +
                    "INSERT INTO categories VALUES\n" +
                    "    ('mixed', (SELECT SUM(shares) as mixed_sum FROM temp1 where category = 'mixed'));\n" +
                    "INSERT INTO categories VALUES\n" +
                    "    ('stocks', (SELECT SUM(shares) as stocks_sum FROM temp1 where category = 'stocks'));\n";
            String query4 = "SELECT * FROM categories order by amount;";
            st.executeUpdate(query1);
            st.executeUpdate(query2);
            st.executeUpdate(query3);
            ResultSet res1 = st.executeQuery(query4);
            String category;
            int amount;
            int i = 1;
            while (res1.next() && k>0) {
                System.out.print("\t" + i + ". ");
                category = res1.getString("category");
                amount = res1.getInt("amount");
                System.out.println(category + ": " + amount);
                k--;
                i++;
            }
        }
        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
    }

    //Task 6. works but i hate how its implemented and prob should test more
    public void rankInvestors(){
        System.out.println("Ranking all investors");
        try {
            String query1 = "DROP TABLE IF EXISTS temp3 CASCADE;\n" +
                    "CREATE TABLE temp3 as (SELECT o.symbol, cp.price, o.login, o.shares, cp.p_date FROM\n" +
                    "    (SELECT * FROM closing_price where p_date = (SELECT p_date FROM closing_price ORDER BY p_date DESC FETCH FIRST ROW ONLY))\n" +
                    "        as cp JOIN owns as o ON cp.symbol = o.symbol);";
            String query2 = "SELECT * FROM temp3;";
            st.executeUpdate(query1);
            ResultSet res1 = st.executeQuery(query2);
            String symbol, login;
            int shares;
            double price;
            HashMap<String, Double> investorValue = new HashMap<String, Double>();
            while (res1.next()) {
                symbol = res1.getString("symbol");
                login = res1.getString("login");
                shares = res1.getInt("shares");
                price = res1.getDouble("price");
                if(!investorValue.containsKey(login)){ // if investor not already in map
                    investorValue.put(login, (double)shares*price);
                }
                else{ // if investor is in map
                    double oldValue = investorValue.get(login);
                    investorValue.replace(login, oldValue + (double)shares*price);
                }
            }
            String query3 = "DROP TABLE IF EXISTS investors CASCADE;\n" +
                    "CREATE TABLE investors\n" +
                    "(\n" +
                    "    investor VARCHAR(20),\n" +
                    "    totalValue INT\n" +
                    ");";
            st.executeUpdate(query3);

            conn.setAutoCommit(false);
            investorValue.forEach((k, v) -> {
                String query4 = "INSERT INTO investors (investor, totalValue) VALUES ('"+k+"', "+v+")";
                try {
                    st.executeUpdate(query4);
                } catch (SQLException whyDoINeedEmbeddedTries) {
                    whyDoINeedEmbeddedTries.printStackTrace();
                }
            });
            conn.commit();
            conn.setAutoCommit(true);
            String query5 = "SELECT * FROM investors ORDER BY totalValue desc";
            ResultSet res2 = st.executeQuery(query5);

            String investor;
            double totalValue;
            while (res2.next()) {
                investor = res2.getString("investor");
                totalValue = res2.getDouble("totalValue");
                System.out.println("Investor: " + investor + ". Value = "+String.valueOf(totalValue));
            }
        }
        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
    }
    //Task 7. works. i thought this worked locally but it didn't work on class3
    public void updateDate(String currentDate){
        try{
            conn.setAutoCommit(false);
            st.executeUpdate("TRUNCATE MUTUAL_DATE CASCADE");

            //Date date = new Date(milliseconds);
            String query1 = "INSERT INTO MUTUAL_DATE (p_date)\n" +
                    "VALUES (TO_DATE('"+currentDate+"', 'DD-MM-YYYY'));";
            st.executeUpdate(query1);
            conn.commit();
            System.out.println("Date updated successfully");
        }
        catch(SQLException e1){
            try{
                System.out.println("rollback rollback rollback");
                System.out.println("e1.toString() = " + e1.toString());
                System.out.println("message = " + e1.getMessage());
                conn.rollback();
            }
            catch(SQLException e2){
                System.out.println("tried to rollback but failed");
                System.out.println("e2.toString() = " + e2.toString());
                System.out.println("message = " + e2.getMessage());
            }
        }
    }

    //-------------------------- Costumer --------------------------------
    //task 1 works with login
    public void showBalanceAndShares(){
        //String name = "mike";
        try{
            PreparedStatement pstmt = conn.prepareStatement("SELECT balance " +
                    "FROM customer WHERE login = ?");
            pstmt.setString(1, getCustomerUsername());
            ResultSet res1 = pstmt.executeQuery();

            pstmt = conn.prepareStatement("SELECT SUM(shares) FROM owns WHERE login = ?");
            pstmt.setString(1, getCustomerUsername());
            ResultSet res2 = pstmt.executeQuery();
            double balance;
            int numShares;
            res1.next();
            res2.next();
            balance = res1.getDouble("balance");
            numShares = res2.getInt("sum");
            System.out.println("User = " + getCustomerUsername() + ". Balance = " + String.valueOf(balance)
                    + ". Total shares = " + String.valueOf(numShares));

            System.out.println("Balance and shares retrieved successfully");
        }
        catch(SQLException e1){
            System.out.println("e1.toString() = " + e1.toString());
            System.out.println("message = " + e1.getMessage());
        }
    }



    //task 2. works
    public void showMFsByName()
        throws SQLException, ClassNotFoundException{
        String query1 =
                "SELECT * FROM mutual_fund ORDER BY name;";
        ResultSet res1 = st.executeQuery(query1);
        String symbol, name, description, category;
        Date date;
        while (res1.next()) {
            symbol = res1.getString("symbol");
            name = res1.getString("name");
            description = res1.getString("description");
            category = res1.getString("category");
            //date = res1.getDate("c_date");
            System.out.println(symbol + " " + name + " " + description + " " + category);
        }
    }

    //task 3. works but the output is ugly with login
    public void showMFbyDate(String date) {
        try{
            String login = getCustomerUsername();
            /*PreparedStatement pstmt = conn.prepareStatement( "DROP TABLE IF EXISTS temp4 CASCADE;\n" +
                    "CREATE TABLE temp4 as SELECT t.symbol, t.price, t.name, t.description, t.category, o.login\n" +
                    "    FROM (SELECT cp.symbol, cp.price, mf.name, mf.description, mf.category\n" +
                    "    FROM closing_price as cp LEFT JOIN mutual_fund as mf on cp.symbol = mf.symbol\n" +
                    "    WHERE cp.p_date = TO_DATE(?, 'DD-MM-YYYY') ) as t\n" +
                    "    LEFT JOIN (SELECT login, symbol FROM owns WHERE login = ?) as o\n" +
                    "    ON t.symbol = o.symbol ORDER BY price DESC;\n" +
                    "SELECT * FROM temp4;");
            pstmt.setString(1, date);
            pstmt.setString(2, login);
            ResultSet res1 = pstmt.executeQuery();*/
            String query1 = "DROP TABLE IF EXISTS temp4 CASCADE;\n" +
                    "CREATE TABLE temp4 as SELECT t.symbol, t.price, t.name, t.description, t.category, o.login\n" +
                    "    FROM (SELECT cp.symbol, cp.price, mf.name, mf.description, mf.category\n" +
                    "    FROM closing_price as cp LEFT JOIN mutual_fund as mf on cp.symbol = mf.symbol\n" +
                    "    WHERE cp.p_date = TO_DATE('"+date+"', 'DD-MM-YY') ) as t\n" +
                    "    LEFT JOIN (SELECT login, symbol FROM owns WHERE login = '"+login+"') as o\n" +
                    "    ON t.symbol = o.symbol ORDER BY price DESC;\n";
            String query2="SELECT * FROM temp4;";
            st.executeUpdate(query1);
            ResultSet res1 = st.executeQuery(query2);
            String symbol, name, description, category;
            double price;
            while(res1.next()){
                symbol = res1.getString("symbol");
                name = res1.getString("name");
                description = res1.getString("description");
                category = res1.getString("category");
                login = res1.getString("login");
                price = res1.getDouble("price");
                if(login == null){
                    System.out.println("Price = "+String.valueOf(price)+". Name = "+name+". Description = "+ description+". Category =  "+category+". Owned by costumer = false");
                }
                else{
                    System.out.println("Price = "+String.valueOf(price)+". Name = "+name+". Description = "+ description+". Category =  "+category+". Owned by costumer = true");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //task 4. works
    public void searchMutualFunds(String keyword1, String keyword2)
            throws SQLException, ClassNotFoundException{
        String query1 =
                "SELECT search_mutual_funds('"+keyword1+ "', '"+keyword2+"');";
        ResultSet res1 = st.executeQuery(query1);
        String ans;
        while (res1.next()) {
            ans = res1.getString(1);
            System.out.println(ans);
        }
    }

    //task 5. works. i think...
    public void deposit(double depositAmount)
            throws SQLException, ClassNotFoundException{
        try{
            String name = "mike";
            /*CallableStatement properCase = conn.prepareCall( "CALL deposit_for_investment(?, ?);");
            properCase.setString(1, name);
            properCase.setDouble(2, depositAmount);
            properCase.execute();
            properCase.close();*/
            String query1 = "CALL deposit_for_investment('"+getCustomerUsername()+"', "+String.valueOf(depositAmount)+");";
            st.execute(query1);
        }
        catch(SQLException e1){
            System.out.println("e1.toString() = " + e1.toString());
            System.out.println("message = " + e1.getMessage());
        }
    }

    //task 6. works but idk about trigger and balance
    public void buyShares(String symbol, int numShares)
            throws SQLException {
        String query1 =
                "SELECT buy_shares('" + getCustomerUsername() + "', '" + symbol + "', " + numShares + ");";
        ResultSet res1 = st.executeQuery(query1);
        res1.next();
            if(res1.getBoolean(1) == false)
                System.out.println("Not enough money in account to buy " + numShares + " shares of " + symbol + " ");
            else
                System.out.println("Successfully bought  " + numShares + " shares of " + symbol + " ");

    }
    //task 7. Works
    //TODO: add function sellShares() that is called in customer/tasks/sellShareTask
    public void sellShares(String symbol, int numShares) throws SQLException {
        String login = getCustomerUsername();
        String query1 = "SELECT shares FROM owns WHERE login = '"+login+"' and symbol = '"+symbol+"';";
        ResultSet res1 = st.executeQuery(query1);
        int startingShares = 0;
        while (res1.next()) {
            startingShares = res1.getInt("shares");
        }
        System.out.println(String.valueOf(startingShares));
        String query2 = "UPDATE owns SET shares = " + String.valueOf(startingShares - numShares) + " WHERE login = '"+login+"' and symbol = '"+symbol+"';";
        st.executeUpdate(query2);

        //trxlog
        String query3 = "SELECT price\n" +
                "    FROM CLOSING_PRICE\n" +
                "    WHERE symbol = '"+symbol+"'\n" +
                "    ORDER BY p_date DESC\n" +
                "    LIMIT 1;";
        ResultSet res2 = st.executeQuery(query3);
        double price = 0;
        while(res2.next()){
            price = res2.getDouble("price");
        }
        String query4 = "SELECT p_date\n" +
                "    FROM MUTUAL_DATE\n" +
                "    ORDER BY p_date DESC\n" +
                "    LIMIT 1;";
        ResultSet res3 = st.executeQuery(query4);
        Date date;
        res3.next();
        date = res3.getDate("p_date");
        String currentDate = date.toString();
        String query5 = "INSERT INTO trxlog(login, symbol, action, num_shares, price, amount, t_date) " +
                "VALUES ('"+login+"', '"+symbol+"', 'sell', "+String.valueOf(numShares)+", "+String.valueOf(price)+", "
                +String.valueOf(numShares*price)+", (SELECT p_date\n" +
                "    FROM MUTUAL_DATE\n" +
                "    ORDER BY p_date DESC\n" +
                "    LIMIT 1));";
        st.executeUpdate(query5);
    }

    //task 8. //works
    public void showROI() throws SQLException {
        String login = getCustomerUsername();
        String query1 = "SELECT symbol FROM owns WHERE login = '"+login+"';";
        ResultSet res1 = st.executeQuery(query1);
        String symbol;
        ResultSet res2, res3;
        System.out.println("Displaying ROI for each owned by, "+getCustomerUsername());
        while(res1.next()){ //FOR EACH SYMBOL USER OWNSF
            symbol = res1.getString("symbol");

            String query3 = "SELECT name\n" +
                    "FROM mutual_fund\n" +
                    "WHERE symbol = '"+symbol+"';";
            res3 = st.executeQuery(query3);
            res3.next();
            String query2 = "SELECT return_ROI('" + symbol + "', '" + login + "');";
            res2 = st.executeQuery(query2);
            while(res2.next()){
                double ROI = res2.getDouble(1);
                System.out.println("Symbol: " + symbol + " Name: " +res3.getString(1)+" ROI = " + String.valueOf(ROI));
            }
        }

    }


    //task 9.
    //TODO: add function predictTransactions() that is called in customer/tasks/predictTransactionsTask
    public void predictTransactions() throws SQLException{
        String user = getCustomerUsername();
        String query1 = "SELECT * FROM trxlog WHERE login = '"+user+"' and action != 'deposit'";
        ResultSet res1 = st.executeQuery(query1);
        String login, symbol, action;
        int numShares, amount;
        while(res1.next()){
            login = res1.getString("login");
            symbol = res1.getString("symbol");
            action = res1.getString("action");
            numShares = res1.getInt("num_shares");
            amount = res1.getInt("amount");
            String query2 = "SELECT predict_gain_loss('"+login+"', '"+symbol+"', '"+action
                    +"',"+String.valueOf(numShares)+", "+String.valueOf(amount)+")\n";
            ResultSet res2 = st.executeQuery(query2);
            res2.next();
            String prediction = res2.getString(1);
            System.out.println("prediction for (login,action,symbol,amount) = ("+login+","+action+","+symbol+","+numShares+"): "+prediction);
        }
    }

    //task 10. //works
    //TODO: add function changeAllocationPreference() that is called in customer/tasks/changeAllocationPreferenceTask
    public Date getDate() throws SQLException {
        String query2 = "    SELECT p_date\n" +
                "    FROM MUTUAL_DATE\n" +
                "    ORDER BY p_date DESC\n" +
                "    LIMIT 1;";
        ResultSet res2 = st.executeQuery(query2);
        res2.next();
        return res2.getDate("p_date");
    }

    public int assignNextAllocationNumber(Date date) throws SQLException {
        String query1 = "SELECT allocation_no FROM allocation ORDER BY allocation_no " +
                "DESC FETCH FIRST 1 ROW ONLY;\n";
        ResultSet res1 = st.executeQuery(query1);
        res1.next();
        int allocation_no = res1.getInt("allocation_no") + 1;
        String dateString = date.toString();
        //System.out.println(dateString);
        String query2 = "INSERT INTO allocation(allocation_no, login, p_date) VALUES " +
                "('"+allocation_no+"', '"+getCustomerUsername()+"', TO_DATE('"+dateString+"', 'YYYY-MM-DD'))";
        st.executeUpdate(query2);
        return allocation_no;
    }

    public void changeAllocationPreference(int allocation_no, String symbol, double percentage)
            throws SQLException {
        String query1 = "INSERT INTO prefers(allocation_no, symbol, percentage) " +
                "VALUES ('"+String.valueOf(allocation_no)+"', '"+symbol+"', " +
                String.valueOf(percentage)+");\n";
        st.executeUpdate(query1);
        System.out.println("successful");
    }

    //task 11.
    //TODO: add function rankAllocations() that is called in customer/tasks/rankAllocationsTask
    public void rankAllocations() throws SQLException {
        String login = getCustomerUsername();
        String query1 = "SELECT allocation_no FROM allocation WHERE login = '"+login+"' ORDER BY allocation_no DESC FETCH FIRST ROW ONLY;";
        ResultSet res1 = st.executeQuery(query1);

        int allocation_no;
        HashMap<String, Double> rankedAllocations = new HashMap<String, Double>();
        while(res1.next()){
            allocation_no = res1.getInt("allocation_no");
            //System.out.println(String.valueOf(allocation_no));
            String query2 = "SELECT symbol FROM prefers WHERE allocation_no = '"+String.valueOf(allocation_no)+"';";
            ResultSet res2 = st.executeQuery(query2);
            String symbol;
            while(res2.next()){
                symbol = res2.getString("symbol");
                String query3 = "SELECT return_ROI('" + symbol + "', '" + login + "');";
                ResultSet res3 = st.executeQuery(query3);
                double ROI;
                res3.next();
                ROI = res3.getDouble(1);
                rankedAllocations.put(symbol, ROI);
                System.out.println(symbol + String.valueOf(ROI));
            }
        }
        System.out.println(rankedAllocations.keySet());
    }

    //task 12.
    //TODO: add function showPortfolio() that is called in customer/tasks/showPortfolioTask
    public void showPortfolio() throws SQLException {
        String query1 = "SELECT login, a.symbol as symbol, price, shares \n" +
                "FROM (SELECT symbol, price FROM closing_price\n" +
                "WHERE symbol in (SELECT symbol FROM owns WHERE login = '"+getCustomerUsername()+"')\n" +
                "and p_date in (SELECT p_date FROM closing_price \n" +
                "ORDER BY p_date DESC LIMIT 1))\n" +
                "AS a LEFT JOIN (SELECT * FROM owns WHERE login = '"+getCustomerUsername()+"')\n" +
                "AS b ON a.symbol = b.symbol;";
        ResultSet res1 = st.executeQuery(query1);
        String login, symbol;
        double closing_price;
        int shares;

        double currentPortfolioValue = 0.0;

        while(res1.next()){
            login = res1.getString("login");
            symbol = res1.getString("symbol");
            closing_price = res1.getDouble("price");
            shares = res1.getInt("shares");

            String query2 = "SELECT * FROM trxlog WHERE login = '"+login+"' and symbol = '"+symbol+"';";

            double buyTotalPrice = 0.0;
            double sellTotalPrice = 0.0;
            int buyTotal=0;
            int sellTotal=0;

            String action;
            int num_shares;
            double trx_price, amount;
            ResultSet res2 = st.executeQuery(query2);
            while(res2.next()){
                action = res2.getString("action");
                num_shares = res2.getInt("num_shares");
                trx_price = res2.getDouble("price");
                amount = res2.getDouble("amount");
                if (action.equals("buy")){
                    buyTotalPrice+=amount;
                    buyTotal+=num_shares;
                }
                if (action.equals("sell")){
                    sellTotalPrice+=amount;
                    sellTotal+=num_shares;
                }
            }
            currentPortfolioValue += closing_price*shares;
            System.out.println("Symbol: "+symbol+". Number of shares: "
                    +String.valueOf(shares)+". Current Value: "
                    +String.valueOf(closing_price*shares) +". Cost: "
                    +String.valueOf(buyTotalPrice)+ ". Adjusted_cost: "
                    +String.valueOf((buyTotalPrice-sellTotalPrice)) +" . Yield: "
                    +String.valueOf((buyTotal-sellTotal)*closing_price- (buyTotalPrice)));
        }
        System.out.println("Total Portfolio Value = " + currentPortfolioValue);
    }
}