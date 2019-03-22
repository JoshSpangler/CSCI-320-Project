package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a WheelsOption
 */
public class WheelsOption {

  //
  // Attributes
  //
  private int ID, diameter;
  private String woName, style, runFlat;

  /**
   * WheelsOption constructor
   * @param data contains relevant attributes for WheelsOption class
   */
  public WheelsOption(String[] data) {
    this.ID = Integer.parseInt(data[0]);
    this.diameter = Integer.parseInt(data[1]);
    this.woName = data[2];
    this.style = data[3];
    this.runFlat = data[4];
  }

  /**
   * Create the WheelsOption table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS wheels_option("
          + "ID INT PRIMARY KEY,"
          + "DIAMETER INT,"
          + "NAME VARCHAR(255),"
          + "STYLE VARCHAR(255),"
          + "RUNFLAT VARCHAR(255),"
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
   * Populates a table for the WheelsOption class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<WheelsOption> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new WheelsOption(data));
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
   * Creates SQL statement to do a bulk add of WheelsOption entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<WheelsOption> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO wheels_option (ID, DIAMETER, NAME, STYLE, RUNFLAT) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      WheelsOption e = arr.get(i);
      sb.append(String.format("(%d, %d, \'%s\', \'%s\', \'%s\')",
          e.getID(), e.getDiameter(), e.getWoName(), e.getStyle(), e.getRunFlat()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getID() {
    return ID;
  }

  public int getDiameter() {
    return diameter;
  }

  public String getWoName() {
    return woName;
  }

  public String getStyle() {
    return style;
  }

  public String getRunFlat() {
    return runFlat;
  }
}
