import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class accessDatabase {

    /**
     * Connects to the database
     * @param loc the location of the database file
     * @param usr the username
     * @param pswd the password
     * @return a connection to the database
     */
    public static Connection connect(String loc,
                                     String usr,
                                     String pswd){
        try {
            String url = "jdbc:h2:" + loc;
            Class.forName("org.h2.Driver");
            return(DriverManager.getConnection(url, usr, pswd));
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Closes the connection to the database
     * @param c the connection
     */
    public static void closeConnection(Connection c){
        try {
            c.close();
        } catch (SQLException e){
            System.err.println("There was a problem closing the database. ");
        }
    }

    /**
     * Prints the result of an sql query
     * @param result the result
     * @throws SQLException
     */
    public static String[][] getResults(ResultSet result) throws SQLException{
        ArrayList<String[]> r=new ArrayList<String[]>();
        while(result.next()){
            r.add(new String[]{result.getString(1),result.getString(2),result.getString(3),
            result.getString(4),""+result.getInt(5), ""+result.getInt(6),
            result.getString(7), result.getString(8), result.getString(9)});
            /*for(String s:r.get(r.size() - 1)) {
                System.out.print(s+" ");
            }
            System.out.println();*/
        }
        return r.toArray(String[][]::new);
    }

    /**
     * Gets the cars for the customer to choose from based on the data
     *
     * @param c the connection to the database
     * @param model the model
     * @param color the color
     * @param wheelDiameter the wheel diameter
     * @param wheelName the wheel name
     * @param wheelStyle the wheel style
     * @param wheelRF the wheel run flat
     * @param upholstry the upholstry
     */
    public static String[][] getUnsoldCars(Connection c,String model, String color, String wheelDiameter, String wheelName,
                                       String wheelStyle, String wheelRF, String upholstry, String dealerID){
        try {
            String query =
                    "SELECT MODEL, " +
                    "COLOR, " +
                    "DESIGN, " +
                    "VEHICLE.STYLE, " +
                    "PRICE, " +
                    "WHEELS_DIAMETER, " +
                    "WHEELS_NAME, " +
                    "WHEELS_STYLE, " +
                    "WHEELS_RUNFLAT " +
                    "FROM (VEHICLE JOIN WHEELS_OPTION AS " +
                    "WHEELS_OPTION(WHEELS_ID, WHEELS_DIAMETER, WHEELS_NAME, WHEELS_STYLE, WHEELS_RUNFLAT) " +
                    "ON VEHICLE.WHEELS_ID=WHEELS_OPTION.WHEELS_ID) WHERE SALE_ID='null'";
            if(!model.equals("*")){
                query+=(" AND MODEL='"+model+"'");
            }
            if(!color.equals("*")){
                query+=("AND COLOR='"+color+"'");
            }
            if(!wheelName.equals("*")){
                query+=(" AND WHEELS_DIAMETER="+wheelDiameter+
                        " AND WHEELS_NAME='"+wheelName+"'"+
                        " AND WHEELS_STYLE='"+wheelStyle+"'"+
                        " AND WHEELS_RUNFLAT='"+wheelRF+"'");
            }
            if(!upholstry.equals("*")){
                query+=(" AND STYLE='"+upholstry+"'");
            }
            if(!dealerID.equals("*")){
                query+=(" AND DEALER_ID='"+dealerID+"'");
            }
            query+=";";
            Statement stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(query);
            System.out.println(query);
            return getResults(result);

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    //public static String[][] getCarsByDealerID(){

    //}
}
