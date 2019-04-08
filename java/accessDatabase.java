import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class accessDatabase {

    /**
     * Connects to the database
     *
     * @param loc  the location of the database file
     * @param usr  the username
     * @param pswd the password
     * @return a connection to the database
     */
    public static Connection connect(String loc,
                                     String usr,
                                     String pswd) {
        try {
            String url = "jdbc:h2:" + loc;
            Class.forName("org.h2.Driver");
            return (DriverManager.getConnection(url, usr, pswd));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Closes the connection to the database
     *
     * @param c the connection
     */
    public static void closeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            System.err.println("There was a problem closing the database. ");
        }
    }

    /**
     * Prints the result of an sql query
     *
     * @param result the result
     * @throws SQLException
     */
    public static String[][] getResults(ResultSet result, int columnNum) throws SQLException {
        ArrayList<String[]> r = new ArrayList<String[]>();
        while (result.next()) {
            String[] s = new String[columnNum];
            for (int i = 0; i < columnNum; i++) {
                s[i] = result.getString(i + 1);
            }
            r.add(s);
        }
        return r.toArray(String[][]::new);
    }

    public static int[] getDate(){
        Date date=new Date();
        int[] d=new int[3];
        String monthString=date.toString().split(" ")[1];
        if(monthString.equals("January")){
            d[0]=1;
        }
        else if(monthString.equals("February")){
            d[0]=2;
        }
        else if(monthString.equals("March")){
            d[0]=3;
        }
        else if(monthString.equals("April")){
            d[0]=4;
        }
        else if(monthString.equals("May")){
            d[0]=5;
        }
        else if(monthString.equals("June")){
            d[0]=6;
        }
        else if(monthString.equals("July")){
            d[0]=7;
        }
        else if(monthString.equals("August")){
            d[0]=8;
        }
        else if(monthString.equals("September")){
            d[0]=9;
        }
        else if(monthString.equals("October")){
            d[0]=10;
        }
        else if(monthString.equals("November")){
            d[0]=11;
        }
        else{
            d[0]=12;
        }
        d[1]=Integer.parseInt(date.toString().split(" ")[2]);
        d[2]=Integer.parseInt(date.toString().split(" ")[5]);
        return d;
    }

    /**
     * Combines the strings on like VINs
     *
     * @param carData the original string[][] to combine
     * @return a String[][] with the carData
     */
    public static String[][] combineLikeVINs(String[][] carData, int optUpgrade, int addCost) {
        ArrayList<String[]> carList = new ArrayList<String[]>();
        //loops through the carData looking for non-null values
        for (int i = 0; i < carData.length; i++) {
            if (carData[i][0] == null) {
                continue;
            }
            //loops through the non-null values again looking for two VINs that are the same where i!=j
            for (int j = 0; j < carData.length; j++) {
                if (carData[j][0] == null || i == j) {
                    continue;
                }
                //appends the two optional upgrades and adds the additional costs
                if (carData[i][0].equals(carData[j][0])) {
                    if (optUpgrade != -1) {
                        carData[i][optUpgrade] += (", " + carData[j][optUpgrade]);
                    }
                    if (addCost != -1) {
                        carData[i][addCost] = (Integer.parseInt(carData[i][addCost]) + Integer.parseInt(carData[j][addCost])) + "";
                    }
                    carData[j][0] = null;
                }
            }
            //formats that into a list
            if (optUpgrade != -1) {
                carData[i][optUpgrade] = "{" + carData[i][optUpgrade] + "}";
            }
            carList.add(carData[i]);
        }
        return carList.toArray(String[][]::new);
    }

    /**
     * Gets the cars for the customer to choose from based on the data
     *
     * @param c             the connection to the database
     * @param model         the model
     * @param color         the color
     * @param wheelDiameter the wheel diameter
     * @param wheelName     the wheel name
     * @param wheelStyle    the wheel style
     * @param wheelRF       the wheel run flat
     * @param upholstry     the upholstry
     */
    public static String[][] getUnsoldCars(Connection c, String series, String model, String color,
                                           String wheelDiameter, String wheelName, String wheelStyle, String wheelRF,
                                           String upholstry, String dealerID) {
        try {
            String query =
                    "SELECT MODEL.SERIES," +
                            "MODEL, " +
                            "COLOR, " +
                            "DESIGN, " +
                            "VEHICLE.STYLE, " +
                            "PRICE, " +
                            "WHEELS_DIAMETER, " +
                            "WHEELS_NAME, " +
                            "WHEELS_STYLE, " +
                            "WHEELS_RUNFLAT " +
                            "FROM ((VEHICLE JOIN WHEELS_OPTION AS " +
                            "WHEELS_OPTION(WHEELS_ID, WHEELS_DIAMETER, WHEELS_NAME, WHEELS_STYLE, WHEELS_RUNFLAT) " +
                            "ON VEHICLE.WHEELS_ID=WHEELS_OPTION.WHEELS_ID) JOIN MODEL ON VEHICLE.MODEL=MODEL.MODEL_NAME) WHERE SALE_ID='null'";
            if(!series.equals("*")){
                query+=(" AND SERIES='"+series+"'");
            }
            if (!model.equals("*")) {
                query += (" AND MODEL='" + model + "'");
            }
            if (!color.equals("*")) {
                query += ("AND COLOR='" + color + "'");
            }
            if (!wheelName.equals("*")) {
                query += (" AND WHEELS_DIAMETER=" + wheelDiameter +
                        " AND WHEELS_NAME='" + wheelName + "'" +
                        " AND WHEELS_STYLE='" + wheelStyle + "'" +
                        " AND WHEELS_RUNFLAT='" + wheelRF + "'");
            }
            if (!upholstry.equals("*")) {
                query += (" AND STYLE='" + upholstry + "'");
            }
            if (!dealerID.equals("*")) {
                query += (" AND DEALER_ID='" + dealerID + "'");
            }
            query += ";";
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return getResults(result, 10);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the sale history for the dealer based on user input
     *
     * @param c          the connection to the database
     * @param dealerID   the dealer's ID
     * @param isSelected the booleans representing what preferences the dealer wishes to sort by
     * @return a matrix of strings representing the cars
     */
    public static String[][] getCarHistoryByDealerID(Connection c, String dealerID, boolean[] isSelected) {
        try {
            int count = 0;
            int optUpgrade = -1;
            int addCost = -1;
            String[] cQuery = new String[]{"VEHICLE.VIN", "MODEL.SERIES", "VEHICLE.MODEL", "VEHICLE.COLOR", "VEHICLE.DESIGN",
                    "VEHICLE.STYLE", "VEHICLE.DAY", "VEHICLE.MONTH", "VEHICLE.YEAR", "MODEL.BASE_COST",
                    "OPTIONAL_UPGRADES.OPTIONAL_UPGRADE", "OPTIONAL_UPGRADES.COST", "VEHICLE.PRICE",
                    "WHEELS_OPTION.DIAMETER", "WHEELS_OPTION.NAME", "WHEELS_OPTION.STYLE", "WHEELS_OPTION.RUNFLAT",
                    "MODEL.DRIVETRAIN", "MODEL.TRANSMISSION", "SALE.DAY", "SALE.MONTH", "SALE.YEAR", "CUSTOMER.NAME",
                    "CUSTOMER.STREET", "CUSTOMER.COUNTY", "CUSTOMER.STATE", "CUSTOMER.ZIP"};
            String query = "SELECT";
            for (int i = 0; i < isSelected.length; i++) {
                if (isSelected[i]) {
                    if (count != 0) {
                        query += ",";
                    }
                    query += (" " + cQuery[i]);
                    if (cQuery[i].equals("OPTIONAL_UPGRADES.OPTIONAL_UPGRADE")) {
                        optUpgrade = count;
                    } else if (cQuery[i].equals("OPTIONAL_UPGRADES.COST")) {
                        addCost = count;
                    }
                    count++;
                }
            }
            if (count == 0) {
                for(int i=0; i<cQuery.length; i++){
                    if(i!=0){
                        query+=",";
                    }
                    query+=" "+cQuery[i];
                    if (cQuery[i].equals("OPTIONAL_UPGRADES.OPTIONAL_UPGRADE")) {
                        optUpgrade = count;
                    } else if (cQuery[i].equals("OPTIONAL_UPGRADES.COST")) {
                        addCost = count;
                    }
                    count++;
                }
            }
            query += " FROM ((((((VEHICLE JOIN VEHICLE_OPTIONAL_UPGRADE ON VEHICLE.VIN=VEHICLE_OPTIONAL_UPGRADE.VIN) " +
                    "JOIN OPTIONAL_UPGRADES ON VEHICLE_OPTIONAL_UPGRADE.NAME=OPTIONAL_UPGRADES.OPTIONAL_UPGRADE) " +
                    "JOIN WHEELS_OPTION ON VEHICLE.WHEELS_ID=WHEELS_OPTION.ID) " +
                    "JOIN MODEL ON VEHICLE.MODEL=MODEL.MODEL_NAME) " +
                    "JOIN SALE ON VEHICLE.SALE_ID=SALE.SALE_ID) " +
                    "JOIN CUSTOMER ON SALE.CUSTOMER_ID=CUSTOMER.ID) " +
                    "WHERE VEHICLE.DEALER_ID=";
            query += (dealerID + " AND VEHICLE.SALE_ID!='null';");
            System.out.println(query);
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return combineLikeVINs(getResults(result, count), optUpgrade, addCost);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * alters vehicle, sale,
     */
    public static void buyCar(Connection c, String dealerID, String series, String model, String colorChoice, String wheelChoice,
                              String upholstery, String design, ArrayList<String> upgrades){

        try {
            String query = "SELECT MAX(VIN) FROM VEHICLE;";
            Statement stmt = c.createStatement();
            ResultSet result=stmt.executeQuery(query);
            result.next();
            String vin=result.getString(1);
            int price=0;
            int[] date=getDate();
            String saleID="null";
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * alters vehicle sale and customer.
     */
    public static void sellCar(Connection c, String firstname, String lastName, String gender, String annualIncome, String street, String county,
                               String state, String zip, String VIN){
        try {
            String[][] results;
            String query = "SELECT * FROM CUSTOMER WHERE NAME='" + firstname + " " + lastName + "'  AND STREET='" +
                    street + "' AND COUNTY='" + county + "' AND STATE='" + state + "' AND ZIP='" + zip + "';";
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            results=getResults(result, 6);
            int customerID;
            if(results.length==0){
                query="SELECT MAX(ID) FROM CUSTOMER;";
                result=stmt.executeQuery(query);
                result.next();
                customerID=Integer.parseInt(result.getString(1))+1;
                query="INSERT INTO CUSTOMER VALUES("+customerID+", '"+firstname+" "+lastName+"', '"+
                        street+"', '"+county+"', '"+state+"', '"+zip+"');";
                System.out.println(query);
                stmt.execute(query);
                query="INSERT INTO PERSON VALUES("+customerID+", '"+gender+"', "+Integer.parseInt(annualIncome)+");";
                System.out.println(query);
                stmt.execute(query);
            }
            else{
                customerID=Integer.parseInt(results[0][0]);
            }
            query="SELECT MAX(SALE_ID) FROM SALE;";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            int sale_id=Integer.parseInt(result.getString(1))+1;
            int[] date=getDate();
            query="INSERT INTO SALE VALUES("+sale_id+", "+customerID+", "+date[0]+", "+date[1]+", "+date[2]+");";
            System.out.println(query);
            stmt.execute(query);
            query="UPDATE VEHICLE SET SALE_ID="+sale_id+" WHERE VIN="+Integer.parseInt(VIN)+";";
            System.out.println(query);
            stmt.executeUpdate(query);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static String getVIN(Connection c,String car){
        try {
            String[] carData = car.split(", ");
            String query = "SELECT VEHICLE.VIN FROM VEHICLE JOIN WHEELS_OPTION ON VEHICLE.WHEELS_ID=WHEELS_OPTION.ID WHERE " +
                    "VEHICLE.MODEL='" + carData[1] +
                    "' AND VEHICLE.COLOR='" + carData[2] +
                    "' AND VEHICLE.DESIGN='" + carData[3] +
                    "' AND VEHICLE.STYLE='" + carData[4] +
                    "' AND VEHICLE.PRICE=" + carData[5] +
                    " AND WHEELS_OPTION.DIAMETER=" + carData[6] +
                    " AND WHEELS_OPTION.NAME='" + carData[7] +
                    "' AND WHEELS_OPTION.STYLE='" + carData[8] +
                    "' AND WHEELS_OPTION.RUNFLAT='" + carData[9] +
                    "';";
            System.out.println(query);
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            result.next();
            return result.getString(1);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}