package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about reduction-resulting class ManufacturerSupplier
 */
public class ManufacturerSupplier {

  //
  // Attributes
  //
  private int manufacturerID, supplierID;

  /**
   * ManufacturerSupplier constructor
   * @param data contains relevant attributes for ManufacturerSupplier class
   */
  public ManufacturerSupplier(String[] data) {
    this.manufacturerID = Integer.parseInt(data[0]);
    this.supplierID = Integer.parseInt(data[1]);
  }

  /**
   * Create the ManufacturerSupplier table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS manufacturer_supplier("
          + "MANUFACTURER_ID INT,"
          + "SUPPLIER_ID INT,"
          + "PRIMARY KEY (MANUFACTURER_ID, SUPPLIER_ID),"
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
   * Populates a table for the ManufacturerSupplier class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<ManufacturerSupplier> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new ManufacturerSupplier(data));
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
   * Creates SQL statement to do a bulk add of ManufacturerSupplier entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<ManufacturerSupplier> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO manufacturer_supplier (MANUFACTURER_ID, SUPPLIER_ID) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      ManufacturerSupplier e = arr.get(i);
      sb.append(String.format("(%d, %d)", e.getManufacturerID(), e.getSupplierID()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getManufacturerID() {
    return manufacturerID;
  }

  public int getSupplierID() {
    return supplierID;
  }
}
