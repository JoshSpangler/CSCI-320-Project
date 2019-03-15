package entities;
import org.h2.engine.Engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about an EngineOption
 */
public class EngineOption {

  //
  // Attributes
  //
  private int numLitres, numCylinders, prodCost;
  private String engineName;

  /**
   * EngineOption constructor
   * @param data contains relevant attributes for EngineOption class
   */
  public EngineOption(String[] data) {
    this.engineName = data[1];
    this.numLitres = Integer.parseInt(data[2]);
    this.numCylinders = Integer.parseInt(data[3]);
    this.prodCost = Integer.parseInt(data[5]);
  }

  /**
   * Create the EngineOption table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS engine_option("
          + "ID INT AUTO_INCREMENT PRIMARY KEY,"
          + "ENGINE_NAME VARCHAR(255),"
          + "NUM_LITRES INT,"
          + "NUM_CYLINDERS INT,"
          + "PRODUCTION_COST INT,"
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
   * Populates a table for the EngineOption class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<EngineOption> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new EngineOption(data));
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
   * Creates SQL statement to do a bulk add of EngineOption entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<EngineOption> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO engine_option (ENGINE_NAME, NUM_LITRES, NUM_CYLINDERS, " +
        "PRODUCTION_COST) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      EngineOption e = arr.get(i);
      sb.append(String.format("(\'%s\', %d, %d, %d)",
          e.getEngineName(), e.getNumLitres(), e.getNumCylinders(), e.getProdCost()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getNumLitres() {
    return numLitres;
  }

  public int getNumCylinders() {
    return numCylinders;
  }

  public int getProdCost() {
    return prodCost;
  }

  public String getEngineName() {
    return engineName;
  }
}
