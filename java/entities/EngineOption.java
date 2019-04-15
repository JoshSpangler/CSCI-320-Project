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
 * @author Josh Spangler
 */
public class EngineOption {

  //
  // Attributes
  //
  private int ID, numCylinders;
  private float numLitres;
  private String engineName;

  /**
   * EngineOption constructor
   * @param data contains relevant attributes for EngineOption class
   */
  public EngineOption(String[] data) {
    this.ID = Integer.parseInt(data[0]);
    this.engineName = data[1];
    this.numLitres = Float.parseFloat(data[2]);
    this.numCylinders = Integer.parseInt(data[3]);
  }

  /**
   * Create the EngineOption table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS engine_option("
          + "ID INT PRIMARY KEY,"
          + "ENGINE_NAME VARCHAR(255),"
          + "NUM_LITRES NUMERIC(2, 1),"
          + "NUM_CYLINDERS INT,"
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
    sb.append("INSERT INTO engine_option (ID, ENGINE_NAME, NUM_LITRES, NUM_CYLINDERS) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      EngineOption e = arr.get(i);
      sb.append(String.format("(%d, \'%s\', %f, %d)",
          e.getID(), e.getEngineName(), e.getNumLitres(), e.getNumCylinders()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getID() { return ID; }

  public float getNumLitres() {
    return numLitres;
  }

  public int getNumCylinders() {
    return numCylinders;
  }

  public String getEngineName() {
    return engineName;
  }
}
