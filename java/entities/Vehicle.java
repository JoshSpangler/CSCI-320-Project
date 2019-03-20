package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a Vehicle
 */
public class Vehicle {

  //
  // Attributes
  //
  private String VIN, color, upholstery, model;
  private int wheelsID, manufacturerID, inventoryID, saleID;

  /**
   * Vehicle constructor
   * @param data contains relevant attributes for Vehicle class
   */
  public Vehicle(String[] data) {
    this.VIN = data[0];
    this.color = data[1];
    this.wheelsID = Integer.parseInt(data[2]);
    this.upholstery = data[3];
    this.model = data[4];
    this.manufacturerID = Integer.parseInt(data[5]);
    this.inventoryID = Integer.parseInt(data[6]);
    this.saleID = Integer.parseInt(data[7]);
  }

  /**
   * Create the Vehicle table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS vehicle("
          + "VIN VARCHAR(255) PRIMARY KEY,"
          + "COLOR VARCHAR(255),"
          + "WHEELS_ID INT,"
          + "UPHOLSTERY VARCHAR(255),"
          + "MODEL VARCHAR(255),"
          + "MANUFACTURER_ID INT,"
          + "INVENTORY_ID INT,"
          + "SALE_ID INT,"
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
   * Populates a table for the Vehicle class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<Vehicle> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new Vehicle(data));
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
   * Creates SQL statement to do a bulk add of Vehicle entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<Vehicle> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO vehicle (VIN, COLOR, WHEELS_ID, UPHOLSTERY, " +
        "MODEL, MANUFACTURER_ID, INVENTORY_ID, SALE_ID) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      Vehicle e = arr.get(i);
      sb.append(String.format("(\'%s\', \'%s\', %d, \'%s\', \'%s\', %d, %d, %d)",
          e.getVIN(), e.getColor(), e.getWheelsID(), e.getUpholstery(), e.getModel(),
          e.getManufacturerID(), e.getInventoryID(), e.getSaleID()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public String getVIN() {
    return VIN;
  }

  public String getColor() {
    return color;
  }

  public String getUpholstery() {
    return upholstery;
  }

  public String getModel() {
    return model;
  }

  public int getWheelsID() {
    return wheelsID;
  }

  public int getManufacturerID() {
    return manufacturerID;
  }

  public int getInventoryID() {
    return inventoryID;
  }

  public int getSaleID() {
    return saleID;
  }
}