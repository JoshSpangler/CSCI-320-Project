package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about reduction-resulting class ModelColorOption
 * @author Josh Spangler
 */
public class ModelColorOption {

  //
  // Attributes
  //
  private String modelName, color;

  /**
   * ModelColorOption constructor
   * @param data contains relevant attributes for ModelColorOption class
   */
  public ModelColorOption(String[] data) {
    this.modelName = data[0];
    this.color = data[1];
  }

  /**
   * Create the ModelColorOption table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS model_color_option("
          + "MODEL_NAME VARCHAR(255),"
          + "COLOR VARCHAR(255),"
          + "PRIMARY KEY (MODEL_NAME, COLOR),"
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
   * Populates a table for the ModelColorOption class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<ModelColorOption> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new ModelColorOption(data));
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
   * Creates SQL statement to do a bulk add of ModelColorOption entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<ModelColorOption> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO model_color_option (MODEL_NAME, COLOR) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      ModelColorOption e = arr.get(i);
      sb.append(String.format("(\'%s\', \'%s\')", e.getModelName(), e.getColor()));

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

  public String getColor() {
    return color;
  }
}
