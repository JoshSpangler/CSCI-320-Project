package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about reduction-resulting class InventoryDealer
 */
public class InventoryDealer {

  //
  // Attributes
  //
  private int dealerID, inventoryID;

  /**
   * InventoryDealer constructor
   * @param data contains relevant attributes for InventoryDealer class
   */
  public InventoryDealer(String[] data) {
    this.dealerID = Integer.parseInt(data[0]);
    this.inventoryID = Integer.parseInt(data[1]);
  }

  /**
   * Create the InventoryDealer table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS inventory_dealer("
          + "DEALER_ID INT,"
          + "INVENTORY_ID INT,"
          + "PRIMARY KEY (DEALER_ID, INVENTORY_ID),"
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
   * Populates a table for the InventoryDealer class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<InventoryDealer> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new InventoryDealer(data));
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
   * Creates SQL statement to do a bulk add of InventoryDealer entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<InventoryDealer> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO inventory_dealer (DEALER_ID, INVENTORY_ID) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      InventoryDealer e = arr.get(i);
      sb.append(String.format("(%d, %d)", e.getDealerID(), e.getInventoryID()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getDealerID() {
    return dealerID;
  }

  public int getInventoryID() {
    return inventoryID;
  }
}
