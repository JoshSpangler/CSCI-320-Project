package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a Customer
 * @author Josh Spangler
 */
public class Customer {

  //
  // Attributes
  //
  private int ID;
  private String name, street, county, state, zip;

  /**
   * Customer constructor
   * @param data contains relevant attributes for Customer class
   */
  public Customer(String[] data) {
    this.ID = Integer.parseInt(data[0]);
    this.name = data[1];
    this.street = data[2];
    this.county = data[3];
    this.state = data[4];
    this.zip = data[5];
  }

  /**
   * Create the Customer table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS customer("
          + "ID INT PRIMARY KEY,"
          + "NAME VARCHAR(255),"
          + "STREET VARCHAR(255),"
          + "COUNTY VARCHAR(255),"
          + "STATE VARCHAR(255),"
          + "ZIP VARCHAR(255),"
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
   * Populates a table for the Customer class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<Customer> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new Customer(data));
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
   * Creates SQL statement to do a bulk add of Customer entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<Customer> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO customer (ID, NAME, STREET, COUNTY, STATE, ZIP) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      Customer e = arr.get(i);
      sb.append(String.format("(%d, \'%s\', \'%s\', \'%s\', \'%s\', \'%s\')",
          e.getID(), e.getName(), e.getStreet(), e.getCounty(), e.getState(), e.getZip()));

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

  public String getZip() {
    return zip;
  }

  public String getName() {
    return name;
  }

  public String getStreet() {
    return street;
  }

  public String getCounty() {
    return county;
  }

  public String getState() {
    return state;
  }
}
