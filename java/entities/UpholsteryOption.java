package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about an UpholsteryOption
 */
public class UpholsteryOption {

  //
  // Attributes
  //
  private String name;
  private int materialCost;

  /**
   * UpholsteryOption constructor
   * @param data contains relevant attributes for UpholsteryOption class
   */
  public UpholsteryOption(String[] data) {
    this.name = data[0];
    this.materialCost = Integer.parseInt(data[1]);
  }

  /**
   * Create the UpholsteryOption table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS upholstery_option("
          + "NAME VARCHAR(255) PRIMARY KEY,"
          + "MATERIAL_COST INT,"
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
   * Populates a table for the UpholsteryOption class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<UpholsteryOption> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new UpholsteryOption(data));
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
   * Creates SQL statement to do a bulk add of UpholsteryOption entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<UpholsteryOption> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO upholstery_option (NAME, MATERIAL_COST) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      UpholsteryOption e = arr.get(i);
      sb.append(String.format("(\'%s\', %d)", e.getName(), e.getMaterialCost()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public String getName() {
    return name;
  }

  public int getMaterialCost() { return materialCost; }
}
