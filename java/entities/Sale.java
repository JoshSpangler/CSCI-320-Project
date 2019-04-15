package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a Sale
 * @author Josh Spangler
 */
public class Sale {

  //
  // Attributes
  //
  private int saleID, customerID,
      day, month, year;

  /**
   * Sale constructor
   * @param data contains relevant attributes for Sale class
   */
  public Sale(String[] data) {
    this.saleID = Integer.parseInt(data[0]);
    this.customerID = Integer.parseInt(data[1]);
    this.day = Integer.parseInt(data[2]);
    this.month = Integer.parseInt(data[3]);
    this.year = Integer.parseInt(data[4]);
  }

  /**
   * Create the Sale table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS sale("
          + "SALE_ID INT PRIMARY KEY,"
          + "CUSTOMER_ID INT,"
          + "DAY INT,"
          + "MONTH INT,"
          + "YEAR INT,"
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
   * Populates a table for the Sale class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<Sale> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new Sale(data));
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
   * Creates SQL statement to do a bulk add of Sale entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<Sale> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO sale (SALE_ID, CUSTOMER_ID, DAY, MONTH, YEAR) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      Sale e = arr.get(i);
      sb.append(String.format("(%d, %d, %d, %d, %d)",
          e.getSaleID(), e.getCustomerID(), e.getDay(), e.getMonth(), e.getYear()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getSaleID() {
    return saleID;
  }


  public int getCustomerID() {
    return customerID;
  }

  public int getDay() {
    return day;
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }
}
