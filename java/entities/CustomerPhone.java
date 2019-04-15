package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a CustomerPhone
 * @author Josh Spangler
 */
public class CustomerPhone {

  //
  // Attributes
  //
  private int ID;
  private String phoneNumber;

  /**
   * CustomerPhone constructor
   * @param data contains relevant attributes for CustomerPhone class
   */
  public CustomerPhone(String[] data) {
    this.ID = Integer.parseInt(data[0]);
    this.phoneNumber = data[1];
  }

  /**
   * Create the CustomerPhone table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS customer_phone("
          + "ID INT,"
          + "PHONE_NUMBER VARCHAR(10),"
          + "PRIMARY KEY (ID, PHONE_NUMBER),"
          + ");";

      /**
       * Create a query and execute
       */
      Statement stmt = conn.createStatement();
      stmt.execute(query);
    }
    catch(SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Populates a table for the CustomerPhone class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<CustomerPhone> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new CustomerPhone(data));
      }
      br.close();
    }
    catch(IOException e) {
      e.printStackTrace();
    }

    /**
     * Creates SQL statement to do bulk add, then executes SQL statement
     */
    String sql = createInsertEntitiesSQL(arr);
    Statement stmt = conn.createStatement();
    stmt.execute(sql);
  }

  /**
   * Creates SQL statement to do a bulk add of CustomerPhone entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<CustomerPhone> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO customer_phone (ID, PHONE_NUMBER) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      CustomerPhone e = arr.get(i);
      sb.append(String.format("(%d, \'%s\')", e.getID(), e.getPhoneNumber()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getID() { return ID; }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
