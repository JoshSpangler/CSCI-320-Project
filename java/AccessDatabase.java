import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class AccessDatabase {

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


    /**
     * Gets an integer version of the date
     * @return an integer version of the date
     */
    public static int[] getDate(){
        Date date=new Date();
        int[] d=new int[3];
        String monthString=date.toString().split(" ")[1];
        //Sets the month int
        if(monthString.equals("Jan")){
            d[0]=1;
        }
        else if(monthString.equals("Feb")){
            d[0]=2;
        }
        else if(monthString.equals("Mar")){
            d[0]=3;
        }
        else if(monthString.equals("Apr")){
            d[0]=4;
        }
        else if(monthString.equals("May")){
            d[0]=5;
        }
        else if(monthString.equals("Jun")){
            d[0]=6;
        }
        else if(monthString.equals("Jul")){
            d[0]=7;
        }
        else if(monthString.equals("Aug")){
            d[0]=8;
        }
        else if(monthString.equals("Sep")){
            d[0]=9;
        }
        else if(monthString.equals("Oct")){
            d[0]=10;
        }
        else if(monthString.equals("Nov")){
            d[0]=11;
        }
        else{
            d[0]=12;
        }
        //Sets the day integer
        d[1]=Integer.parseInt(date.toString().split(" ")[2]);
        //Sets the year integer
        d[2]=Integer.parseInt(date.toString().split(" ")[5]);
        return d;
    }

    /**
     * Combines the strings on like VINs
     *
     * @param carData the original string[][] to combine
     * @return a String[][] with the carData
     */
    public static String[][] combineLikeVINs(String[][] carData, int optUpgrade, int addCost, int customerPhone,
                                             boolean includeVin, boolean includeOptUpgrade, boolean includeCustPhone) {
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
                        if(!carData[i][optUpgrade].contains(carData[j][optUpgrade])){
                            carData[i][optUpgrade] += (", " + carData[j][optUpgrade]);
                        }
                        if(addCost!=-1){
                            carData[i][addCost]=(Integer.parseInt(carData[i][addCost])+Integer.parseInt(carData[j][addCost]))+"";
                        }
                    }
                    if(customerPhone!=-1){
                        if(!carData[i][customerPhone].contains(carData[j][customerPhone])) {
                            carData[i][customerPhone] += (", " + carData[j][customerPhone]);
                        }
                    }
                    carData[j][0] = null;
                }
            }
            //formats that into a list
            if (optUpgrade != -1 && includeOptUpgrade) {

                carData[i][optUpgrade] = "{" + carData[i][optUpgrade] + "}";
            }
            if(customerPhone!=-1 && includeCustPhone){
                carData[i][customerPhone]="{"+carData[i][customerPhone]+"}";
            }
            carList.add(carData[i]);
        }
        //Goes through and divides by
        if(addCost!=-1 && customerPhone!=-1){
            for(int i=0; i<carList.size(); i++){
                carList.get(i)[addCost] = (Integer.parseInt(carList.get(i)[addCost]) / carData[i][customerPhone].split(",").length) + "";
            }
        }
        if(!includeVin) {
            for (int i = 0; i < carList.size(); i++) {
                String[] correctedCarData = new String[carList.get(i).length - 1];
                for (int j = 1; j < carList.get(i).length; j++) {
                    correctedCarData[j - 1] = carList.get(i)[j];
                }
                carList.set(i, correctedCarData);
            }
            optUpgrade--;
            customerPhone--;
        }
        if(!includeOptUpgrade){
            for(int i=0; i<carList.size(); i++){
                String[] correctedCarData = new String[carList.get(i).length - 1];
                int current=0;
                for (int j = 0; j < carList.get(i).length; j++) {
                    if(j==optUpgrade){
                        continue;
                    }
                    correctedCarData[current] = carList.get(i)[j];
                    current++;
                }
                carList.set(i, correctedCarData);
            }
            customerPhone--;
        }
        if(!includeCustPhone){
            for(int i=0; i<carList.size(); i++){
                String[] correctedCarData = new String[carList.get(i).length - 1];
                int current=0;
                for (int j = 0; j < carList.get(i).length; j++) {
                    if(j==customerPhone){
                        continue;
                    }
                    correctedCarData[current] = carList.get(i)[j];
                    current++;
                }
                carList.set(i, correctedCarData);
            }
        }
        return carList.toArray(String[][]::new);
    }

    public static String[][] getModels(Connection c, String series) {
        try{
            String query =
                    "SELECT MODEL " +
                            "FROM (VEHICLE JOIN MODEL ON VEHICLE.MODEL=MODEL.MODEL_NAME)";
            if(!series.equals("*")){
                query+=(" WHERE SERIES='"+series+"'");
            }
            query += ";";
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return getResults(result, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[][] getColors(Connection c, String model) {
        try{
            String query =
                    "SELECT COLOR " +
                            "FROM MODEL_COLOR_OPTION";
            if(!model.equals("*")){
                query+=(" WHERE MODEL_NAME='"+model+"'");
            }
            query += ";";
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return getResults(result, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[][] getWheels(Connection c, String model) {
        try{
            String query =
                    "SELECT DIAMETER, " +
                            "NAME, " +
                            "STYLE, " +
                            "RUNFLAT " +
                            "FROM (MODEL_WHEELS_OPTION JOIN WHEELS_OPTION " +
                            "ON MODEL_WHEELS_OPTION.WHEELS_ID=WHEELS_OPTION.ID)";
            if(!model.equals("*")){
                query+=(" WHERE MODEL_NAME='"+model+"'");
            }
            query += ";";
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return getResults(result, 4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[][] getUpholstry(Connection c, String model) {
        try{
            String query =
                    "SELECT UPHOLSTERY " +
                            "FROM MODEL_UPHOLSTERY_OPTION";
            if(!model.equals("*")){
                query+=(" WHERE MODEL_NAME='"+model+"'");
            }
            query += ";";
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return getResults(result, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public static String[][] getUnsoldCarsAllDealer(Connection c, String series, String model, String color,
                                           String wheelDiameter, String wheelName, String wheelStyle, String wheelRF,
                                           String upholstry) {
        try {
            String query =
                    "SELECT DEALER_ID, " +
                            "NAME, " +
                            "STREET, " +
                            "COUNTY, " +
                            "STATE, " +
                            "ZIP, " +
                            "MODEL.SERIES, " +
                            "MODEL, " +
                            "COLOR, " +
                            "DESIGN, " +
                            "VEHICLE.STYLE, " +
                            "PRICE, " +
                            "WHEELS_DIAMETER, " +
                            "WHEELS_NAME, " +
                            "WHEELS_STYLE, " +
                            "WHEELS_RUNFLAT " +
                            "FROM (((VEHICLE JOIN DEALER ON VEHICLE.DEALER_ID=DEALER.ID) " +
                            "JOIN WHEELS_OPTION AS " +
                            "WHEELS_OPTION(WHEELS_ID, WHEELS_DIAMETER, WHEELS_NAME, WHEELS_STYLE, WHEELS_RUNFLAT) " +
                            "ON VEHICLE.WHEELS_ID=WHEELS_OPTION.WHEELS_ID) " +
                            "JOIN MODEL ON VEHICLE.MODEL=MODEL.MODEL_NAME) " +
                            "WHERE SALE_ID='null'";
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
            query += " ORDER BY DEALER_ID;";
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return getResults(result, 16);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean dealerInData(Connection c,String dealerID){
        try {
            String query = "SELECT COUNT(ID) FROM DEALER WHERE ID=" + dealerID + ";";
            System.out.println(query);
            Statement stmt = c.createStatement();
            ResultSet result=stmt.executeQuery(query);
            result.next();
            if(!result.getString(1).equals("0")){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
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
            /*
            SELECT DISTINCT VEHICLE.VIN, MODEL.SERIES, VEHICLE.MODEL, VEHICLE.COLOR, VEHICLE.DESIGN, VEHICLE.STYLE, VEHICLE.DAY AS MANUFACTURE_DAY, VEHICLE.MONTH AS MANUFACTURE_MONTH, VEHICLE.YEAR AS MANUFACTURE_YEAR, MODEL.BASE_COST, OPTIONAL_UPGRADES.OPTIONAL_UPGRADE, OPTIONAL_UPGRADES.COST AS OPTIONAL_UPGRADE_COST, VEHICLE.PRICE AS TOTAL_PRICE, WHEELS_OPTION.DIAMETER AS WHEEL_DIAMETER, WHEELS_OPTION.NAME AS WHEEL_NAME, WHEELS_OPTION.STYLE AS WHEEL_STYLE, WHEELS_OPTION.RUNFLAT AS WHEEL_RUNFLAT, MODEL.DRIVETRAIN, MODEL.TRANSMISSION, ENGINE_OPTION.ENGINE_NAME, ENGINE_OPTION.NUM_LITRES, ENGINE_OPTION.NUM_CYLINDERS, MANUFACTURER.NAME AS MANUFACTURER_NAME, MANUFACTURER.STREET AS MANUFACTURERE_STREET, MANUFACTURER.COUNTY AS MANUFACTURER_COUNTY, MANUFACTURER.STATE AS MANUFACTURER_STATE, MANUFACTURER.ZIP AS MANUFACTURER_ZIP, SALE.DAY AS SALE_DAY, SALE.MONTH AS SALE_MONTH, SALE.YEAR AS SALE_YEAR, CUSTOMER.NAME AS CUSTOMER_NAME, CUSTOMER.STREET AS CUSTOMER_STREET, CUSTOMER.COUNTY AS CUSTOMER_COUNTY, CUSTOMER.STATE AS CUSTOMER_STATE, CUSTOMER.ZIP AS CUSTOMER_ZIP, CUSTOMER_PHONE.PHONE_NUMBER AS CUSTOMER_PHONE_NUMBER FROM ((((((((((VEHICLE JOIN VEHICLE_OPTIONAL_UPGRADE ON VEHICLE.VIN=VEHICLE_OPTIONAL_UPGRADE.VIN) JOIN OPTIONAL_UPGRADES ON VEHICLE_OPTIONAL_UPGRADE.NAME=OPTIONAL_UPGRADES.OPTIONAL_UPGRADE) JOIN WHEELS_OPTION ON VEHICLE.WHEELS_ID=WHEELS_OPTION.ID) JOIN MODEL ON VEHICLE.MODEL=MODEL.MODEL_NAME) JOIN SALE ON VEHICLE.SALE_ID=SALE.SALE_ID) JOIN CUSTOMER ON SALE.CUSTOMER_ID=CUSTOMER.ID) JOIN ENGINE_OPTION ON MODEL.ENGINE_ID=ENGINE_OPTION.ID) JOIN CUSTOMER_PHONE ON CUSTOMER.ID=CUSTOMER_PHONE.ID) JOIN MANUFACTURER ON MANUFACTURER.ID=VEHICLE.MANUFACTURER_ID) )WHERE VEHICLE.DEALER_ID=2 AND VEHICLE.SALE_ID!='null';
             */
            int count = 0;
            int optUpgrade = -1;
            int addCost = -1;
            int customerPhone=-1;
            boolean showAll=false;
            String[] cQuery = new String[]{"VEHICLE.VIN", "MODEL.SERIES", "VEHICLE.MODEL", "VEHICLE.COLOR",
                    "VEHICLE.DESIGN", "VEHICLE.STYLE", "VEHICLE.DAY", "VEHICLE.MONTH", "VEHICLE.YEAR",
                    "MODEL.BASE_COST", "OPTIONAL_UPGRADES.OPTIONAL_UPGRADE", "OPTIONAL_UPGRADES.COST", "VEHICLE.PRICE",
                    "WHEELS_OPTION.DIAMETER", "WHEELS_OPTION.NAME", "WHEELS_OPTION.STYLE", "WHEELS_OPTION.RUNFLAT",
                    "MODEL.DRIVETRAIN", "MODEL.TRANSMISSION", "ENGINE_OPTION.ENGINE_NAME", "ENGINE_OPTION.NUM_LITRES",
                    "ENGINE_OPTION.NUM_CYLINDERS", "SALE.DAY", "SALE.MONTH", "SALE.YEAR", "CUSTOMER.NAME",
                    "CUSTOMER.STREET", "CUSTOMER.COUNTY", "CUSTOMER.STATE", "CUSTOMER.ZIP",
                    "CUSTOMER_PHONE.PHONE_NUMBER"};
            String query = "SELECT";
            //Loops through adding all selected attributes to the query
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
                    } else if (cQuery[i].equals("CUSTOMER_PHONE.PHONE_NUMBER")) {
                        customerPhone = count;
                    }
                    count++;
                }
            }

            //If none are chosen default to all of them
            //Adds all options to the query
            if (count == 0) {
                showAll=true;
                for(int i=0; i<cQuery.length; i++){
                    if (cQuery[i].equals("OPTIONAL_UPGRADES.OPTIONAL_UPGRADE")) {
                        optUpgrade = count;
                    } else if (cQuery[i].equals("OPTIONAL_UPGRADES.COST")) {
                        addCost = count;
                    } else if (cQuery[i].equals("CUSTOMER_PHONE.PHONE_NUMBER")) {
                        customerPhone = count;
                    }
                    if(i!=0){
                        query+=",";
                    }
                    query+=" "+cQuery[i];
                    count++;
                }
            }
            //If there are some selected but VIN isn't selected
            else {
                if(!isSelected[0]){
                    for(int i=0; i<cQuery.length; i++){
                        //Increases the needed count
                        if (cQuery[i].equals("OPTIONAL_UPGRADES.OPTIONAL_UPGRADE")) {
                            optUpgrade += 1;
                        } else if (cQuery[i].equals("OPTIONAL_UPGRADES.COST") && addCost!=-1) {
                            addCost += 1;
                        }
                        if (cQuery[i].equals("CUSTOMER_PHONE.PHONE_NUMBER") && customerPhone!=-1) {
                            customerPhone += 1;
                        }
                    }
                    //Adds VIN temporarily
                    query=query.substring(0,6)+" VEHICLE.VIN,"+query.substring(6);
                    count++;
                }
                if(!isSelected[10]){
                    for(int i=0; i<cQuery.length; i++){
                        //Increases the needed count
                        if (cQuery[i].equals("OPTIONAL_UPGRADES.OPTIONAL_UPGRADE")) {
                            optUpgrade = 1;
                        } else if (cQuery[i].equals("OPTIONAL_UPGRADES.COST") && addCost!=-1) {
                            addCost += 1;
                        }
                    if (cQuery[i].equals("CUSTOMER_PHONE.PHONE_NUMBER") && customerPhone!=-1) {
                            customerPhone += 1;
                        }
                    }
                    if(count>1) {
                        query = query.substring(0, query.indexOf("VEHICLE.VIN,") + "VEHICLE.VIN,".length()) + " OPTIONAL_UPGRADES.OPTIONAL_UPGRADE," + query.substring(query.indexOf("VEHICLE.VIN,") + "VEHICLE.VIN,".length());

                    }
                    else{
                        query = query.substring(0, query.indexOf("VEHICLE.VIN") + "VEHICLE.VIN".length()) + ", OPTIONAL_UPGRADES.OPTIONAL_UPGRADE" + query.substring(query.indexOf("VEHICLE.VIN") + "VEHICLE.VIN".length());

                    }
                    count++;
                }
                if(!isSelected[30]){
                    for(int i=0; i<cQuery.length; i++){
                        //Increases the needed count
                        if (cQuery[i].equals("OPTIONAL_UPGRADES.COST") && addCost!=-1) {
                            addCost += 1;
                        }
                    }
                    customerPhone = 2;
                    if(count>2) {
                        query = query.substring(0, query.indexOf(" OPTIONAL_UPGRADES.OPTIONAL_UPGRADE,") + " OPTIONAL_UPGRADES.OPTIONAL_UPGRADE,".length()) + " CUSTOMER_PHONE.PHONE_NUMBER," + query.substring(query.indexOf(" OPTIONAL_UPGRADES.OPTIONAL_UPGRADE,") + " OPTIONAL_UPGRADES.OPTIONAL_UPGRADE,".length());

                    }
                    else{
                        query = query.substring(0, query.indexOf("OPTIONAL_UPGRADES.OPTIONAL_UPGRADE") + "OPTIONAL_UPGRADES.OPTIONAL_UPGRADE".length()) + ", CUSTOMER_PHONE.PHONE_NUMBER" + query.substring(query.indexOf("OPTIONAL_UPGRADES.OPTIONAL_UPGRADE") + "OPTIONAL_UPGRADES.OPTIONAL_UPGRADE".length());
                    }
                    count++;
                }
            }
            String[] parsedquery=query.split(",");
            for(String s:parsedquery){
                System.out.println(s);
            }
            for(int i=0; i<parsedquery.length; i++){
                if(parsedquery[i].contains("OPTIONAL_UPGRADES.OPTIONAL_UPGRADE")){
                    optUpgrade=i;
                }
                else if(parsedquery[i].contains("CUSTOMER_PHONE.PHONE_NUMBER")){
                    customerPhone=i;
                }
                else if(parsedquery[i].contains("OPTIONAL_UPGRADES.COST")){
                    addCost=i;
                }
            }
            query += " FROM ((((((((VEHICLE JOIN VEHICLE_OPTIONAL_UPGRADE ON VEHICLE.VIN=VEHICLE_OPTIONAL_UPGRADE.VIN) " +
                    "JOIN OPTIONAL_UPGRADES ON VEHICLE_OPTIONAL_UPGRADE.NAME=OPTIONAL_UPGRADES.OPTIONAL_UPGRADE) " +
                    "JOIN WHEELS_OPTION ON VEHICLE.WHEELS_ID=WHEELS_OPTION.ID) " +
                    "JOIN MODEL ON VEHICLE.MODEL=MODEL.MODEL_NAME) " +
                    "JOIN ENGINE_OPTION ON ENGINE_OPTION.ID=MODEL.ENGINE_ID)"+
                    "JOIN SALE ON VEHICLE.SALE_ID=SALE.SALE_ID) " +
                    "JOIN CUSTOMER ON SALE.CUSTOMER_ID=CUSTOMER.ID) " +
                    "JOIN CUSTOMER_PHONE ON CUSTOMER.ID=CUSTOMER_PHONE.ID)"+
                    "WHERE VEHICLE.DEALER_ID=";
            query += (dealerID + " AND VEHICLE.SALE_ID!='null';");
            System.out.println(query);
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return(combineLikeVINs(getResults(result, count), optUpgrade, addCost, customerPhone, isSelected[0]||showAll, isSelected[10]||showAll, isSelected[30]||showAll));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new vehicle into the dealer's database
     *
     * Alters:
     * BODY_DESIGN_OPTION
     * COLOR_OPTION
     * MODEL
     * WHEELS_OPTION
     * VEHICLE
     * MODEL_BODY_DESIGN_OPTION
     * MODEL_COLOR_OPTION
     * MODEL_UPHOLSTERY_OPTION
     * MODEL_WHEELS_OPTION
     * OPTIONAL_UPGRADES
     * UPHOLSTERY_OPTION
     * VEHICLE_OPTIONAL_UPGRADE
     * WHEELS_OPTION
     */
    public static void buyCar(Connection c, String dealerID, String brand, String series, String model, String colorChoice, String wheelChoice,
                              String upholstery, String design, int baseprice,ArrayList<String> upgrades){

        try {
            //Gets the date
            int[] date=getDate();
            //Add the brand if it is not already added
            String query="SELECT COUNT(BDNAME) FROM BODY_DESIGN_OPTION WHERE BDNAME='"+design+"';";
            Statement stmt=c.createStatement();
            System.out.println(query);
            ResultSet result=stmt.executeQuery(query);
            result.next();
            if(result.getString(1).equals("0")){
                query="INSERT INTO BODY_DESIGN_OPTION VALUES('"+design+"');";
                stmt.execute(query);
                System.out.println(query);
            }
            query="SELECT COUNT(DESIGN_NAME) FROM MODEL_BODY_DESIGN_OPTION WHERE MODEL_NAME='"+model+
                    "' AND DESIGN_NAME='"+design+"';";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            if(result.getString(1).equals("0")){
                query="INSERT INTO MODEL_BODY_DESIGN_OPTION VALUES('"+model+"', '"+design+"');";
                System.out.println(query);
                stmt.execute(query);
            }
            //Add the color if it is not already added
            query="SELECT COUNT(COLOR) FROM COLOR_OPTION WHERE COLOR='"+colorChoice+"';";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            if(result.getString(1).equals("0")){
                query="INSERT INTO COLOR OPTION VALUES('"+colorChoice+"');";
                System.out.println(query);
                stmt.execute(query);
            }
            query="SELECT COUNT(MODEL_NAME) FROM MODEL_COLOR_OPTION WHERE MODEL_NAME='"+model+"' AND COLOR='"+colorChoice+"';";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            if(result.getString(1).equals("0")){
                query="INSERT INTO MODEL_COLOR_OPTION('"+model+"', '"+colorChoice+"');";
                System.out.println(query);
                stmt.execute(query);
            }
            query="SELECT COUNT(NAME) FROM UPHOLSTERY_OPTION WHERE NAME='"+upholstery+"';";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            if(result.getString(1).equals("0")){
                query="INSERT INTO UPHOLSTERY_OPTION WHERE MODEL_NAME='"+model+"' AND UPHOLSTERY='"+upholstery+"';";
                System.out.println(query);
                stmt.execute(query);
            }
            query="SELECT COUNT(MODEL_NAME) FROM MODEL_UPHOLSTERY_OPTION WHERE MODEL_NAME='"+
                    model+"' AND UPHOLSTERY='"+upholstery+"';";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            if(result.getString(1).equals("0")){
                query="INSERT INTO MODEL_UPHOLSTERY OPTION VALUES('"+model+"', '"+upholstery+"');";
                System.out.println(query);
                stmt.execute(query);
            }
            //Add the engine if it is not already added
            query="SELECT COUNT(ID) FROM ENGINE_OPTION;";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            String engineID=(int)(Math.random()*Integer.parseInt(result.getString(1)))+"";
            //Gets the manufacturer number
            query="SELECT COUNT(ID) FROM MANUFACTURER;";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            String manufacturerNumber=(int)(Math.random()*Integer.parseInt(result.getString(1)))+"";
            //Sets the drivetrain randomly
            String drivetrain;
            int rand=(int)(Math.random()*3);
            if(rand==0){
                drivetrain="AWD";
            }
            else if(rand==1){
                drivetrain="FWD";
            }
            else{
                drivetrain="RWD";
            }
            query="SELECT COUNT(MODEL_NAME) FROM MODEL WHERE MODEL_NAME='"+model+"' AND BRAND_NAME='"+brand+
                    "' AND SERIES='"+series+"';";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            //Inserts into model if not already there
            if(result.getString(1).equals("0")) {
                query = "INSERT INTO MODEL VALUES('" + model + "', '" + brand + "', '" + series + "', " + date[2] + ", '" + drivetrain +
                        "', 'Automatic', " + baseprice + ", " + engineID + ");";
                System.out.println(query);
                stmt.execute(query);
            }
            //Gets the next vin
            query = "SELECT MAX(VIN) FROM VEHICLE;";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            String vin=(Integer.parseInt(result.getString(1))+1)+"";
            //Adds the Optional_Upgrades
            for(int i=0; i<upgrades.size(); i++){
                String[] parsed=upgrades.get(i).split(":");
                query="SELECT COUNT(OPTIONAL_UPGRADE) FROM OPTIONAL_UPGRADES WHERE OPTIONAL_UPGRADE='"+
                        parsed[0]+"' AND COST="+parsed[1]+";";
                System.out.println(query);
                result=stmt.executeQuery(query);
                result.next();
                if(result.getString(1).equals("0")){
                    query="INSERT INTO OPTIONAL_UPGRADES VALUES('"+parsed[0]+"', "+parsed[1]+");";
                    System.out.println(query);
                    stmt.execute(query);
                }
                query="INSERT INTO VEHICLE_OPTIONAL_UPGRADE VALUES("+vin+", '"+parsed[0]+"');";
                System.out.println(query);
                stmt.execute(query);
            }
            //Gets the total price
            int price=baseprice;
            for(int i=0; i<upgrades.size(); i++){
                String[] parsedStr=upgrades.get(i).split(":");
                price+=Integer.parseInt(parsedStr[1]);
            }
            query="SELECT COUNT(ID) FROM MANUFACTURER;";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            int manufacturerID=(int)(Math.random()*Integer.parseInt(result.getString(1)));
            String wheelID;
            String[] wheels=new String[4];
            String[] parsedWheels=wheelChoice.split("\" ");
            wheels[0]=parsedWheels[0];
            parsedWheels=parsedWheels[1].split(" - ");
            wheels[1]=parsedWheels[0];
            parsedWheels=parsedWheels[1].split(" with ");
            wheels[2]=parsedWheels[0];
            wheels[3]=parsedWheels[1];
            query = "SELECT COUNT(ID) FROM WHEELS_OPTION WHERE DIAMETER=" + wheels[0] + " AND NAME='" + wheels[1] + "' AND STYLE='" +
                    wheels[2] + "' AND RUNFLAT='" + wheels[3] + "';";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            if(!result.getString(1).equals("0")) {
                query = "SELECT ID FROM WHEELS_OPTION WHERE DIAMETER=" + wheels[0] + " AND NAME='" + wheels[1] + "' AND STYLE='" +
                        wheels[2] + "' AND RUNFLAT='" + wheels[3] + "';";
                System.out.println(query);
                result = stmt.executeQuery(query);
                result.next();
                wheelID = result.getString(1);
            }
            else {
                query = "SELECT MAX(ID) FROM WHEELS_OPTION;";
                System.out.println(query);
                result = stmt.executeQuery(query);
                result.next();
                wheelID = (Integer.parseInt(result.getString(1)) + 1) + "";
                //Inserts into wheels
                query="INSERT INTO WHEELS_OPTION VALUES("+wheelID+", "+wheels[0]+", '"+wheels[1]+"', '"+wheels[2]+"', '"+wheels[3]+"');";
                System.out.println(query);
                stmt.execute(query);
            }
            query="SELECT COUNT(WHEELS_ID) FROM MODEL_WHEELS_OPTION WHERE MODEL_NAME='"+model+"' AND WHEELS_ID='"+
                    wheelID+"';";
            System.out.println(query);
            result=stmt.executeQuery(query);
            result.next();
            if(result.getString(1).equals("0")){
                query="INSERT INTO MODEL_WHEELS_OPTION VALUES('"+model+"', '"+wheelID+"');";
                System.out.println(query);
                stmt.execute(query);
            }
            query="INSERT INTO VEHICLE VALUES("+vin+", '"+model+"', "+wheelID+
                    ", '"+colorChoice+"', '"+design+"', '"+upholstery+"', "+date[0]+", "+date[1]+", "+date[2]+
                    ", "+price+", "+dealerID+", 'null', "+manufacturerID+");";
            System.out.println(query);
            stmt.execute(query);
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