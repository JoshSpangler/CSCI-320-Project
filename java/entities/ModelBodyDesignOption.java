package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about reduction-resulting class ModelBodyDesignOption
 * @author Josh Spangler
 */
public class ModelBodyDesignOption {

  //
  // Attributes
  //
  private String modelName, designName;

  /**
   * ModelBodyDesignOption constructor
   * @param data contains relevant attributes for ModelBodyDesignOption class
   */
  public ModelBodyDesignOption(String[] data) {
    this.modelName = data[0];
    this.designName = data[1];
  }

  /**
   * Create the ModelBodyDesignOption table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS model_body_design_option("
          + "MODEL_NAME VARCHAR(255),"
          + "DESIGN_NAME VARCHAR(255),"
          + "PRIMARY KEY (MODEL_NAME, DESIGN_NAME),"
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
   * Populates a table for the ModelBodyDesignOption class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<ModelBodyDesignOption> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new ModelBodyDesignOption(data));
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
   * Creates SQL statement to do a bulk add of ModelBodyDesignOption entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<ModelBodyDesignOption> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO model_body_design_option (MODEL_NAME, DESIGN_NAME) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      ModelBodyDesignOption e = arr.get(i);
      sb.append(String.format("(\'%s\', \'%s\')", e.getModelName(), e.getDesignName()));

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

  public String getDesignName() {
    return designName;
  }
}
