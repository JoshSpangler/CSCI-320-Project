package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about an OptionalUpgrades entity
 */
public class OptionalUpgrades {

  //
  // Attributes
  //
  private String optionalUpgrade;
  private int cost;


  /**
   * OptionalUpgrades constructor
   * @param data contains relevant attributes for OptionalUpgrades class
   */
  public OptionalUpgrades(String[] data) {
    this.optionalUpgrade = data[0];
    this.cost = Integer.parseInt(data[1]);
  }

  /**
   * Create the OptionalUpgrades table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS optional_upgrades("
          + "OPTIONAL_UPGRADE VARCHAR(255) PRIMARY KEY,"
          + "COST INT,"
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
   * Populates a table for the OptionalUpgrades class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<OptionalUpgrades> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new OptionalUpgrades(data));
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
   * Creates SQL statement to do a bulk add of OptionalUpgrades entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<OptionalUpgrades> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO optional_upgrades (OPTIONAL_UPGRADE, COST) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      OptionalUpgrades e = arr.get(i);
      sb.append(String.format("(\'%s\', %d)",
          e.getOptionalUpgrade(), e.getCost()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public String getOptionalUpgrade() {
    return optionalUpgrade;
  }

  public int getCost() {
    return cost;
  }
}
