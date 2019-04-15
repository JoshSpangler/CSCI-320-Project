package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about reduction-resulting class ModelWheelsOption
 * @author Josh Spangler
 */
public class ModelWheelsOption {

  //
  // Attributes
  //
  private String modelName;
  private int wheelsID;

  /**
   * ModelWheelsOption constructor
   * @param data contains relevant attributes for ModelWheelsOption class
   */
  public ModelWheelsOption(String[] data) {
    this.modelName = data[0];
    this.wheelsID = Integer.parseInt(data[1]);
  }

  /**
   * Create the ModelWheelsOption table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS model_wheels_option("
          + "MODEL_NAME VARCHAR(255),"
          + "WHEELS_ID INT,"
          + "PRIMARY KEY (MODEL_NAME, WHEELS_ID),"
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
   * Populates a table for the ModelWheelsOption class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<ModelWheelsOption> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new ModelWheelsOption(data));
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
   * Creates SQL statement to do a bulk add of ModelWheelsOption entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<ModelWheelsOption> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO model_wheels_option (MODEL_NAME, WHEELS_ID) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      ModelWheelsOption e = arr.get(i);
      sb.append(String.format("(\'%s\', %d)", e.getModelName(), e.getWheelsID()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public String getModelName() {
    return modelName;
  }

  public int getWheelsID() {
    return wheelsID;
  }
}
