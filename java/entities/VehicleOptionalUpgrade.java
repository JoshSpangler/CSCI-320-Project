package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about reduction-resulting class VehicleOptionalUpgrade
 * @author Josh Spangler
 */
public class VehicleOptionalUpgrade {

  //
  // Attributes
  //
  private String name;
  private int VIN;

  /**
   * VehicleOptionalUpgrade constructor
   * @param data contains relevant attributes for VehicleOptionalUpgrade class
   */
  public VehicleOptionalUpgrade(String[] data) {
    this.VIN = Integer.parseInt(data[0]);
    this.name = data[1];
  }

  /**
   * Create the VehicleOptionalUpgrade table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS vehicle_optional_upgrade("
          + "VIN INT,"
          + "NAME VARCHAR(255),"
          + "PRIMARY KEY (VIN, NAME)"
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
   * Populates a table for the VehicleOptionalUpgrade class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<VehicleOptionalUpgrade> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new VehicleOptionalUpgrade(data));
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
   * Creates SQL statement to do a bulk add of VehicleOptionalUpgrade entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<VehicleOptionalUpgrade> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO vehicle_optional_upgrade (VIN, NAME) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      VehicleOptionalUpgrade e = arr.get(i);
      sb.append(String.format("(%d, \'%s\')", e.getVIN(), e.getName()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getVIN() {
    return VIN;
  }

  public String getName() {
    return name;
  }
}
