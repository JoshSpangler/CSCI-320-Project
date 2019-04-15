package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;


/**
 * Holds data about a BodyDesignOption
 * @author Josh Spangler
 */
public class BodyDesignOption {

  //
  // Attributes
  //
  private String bdName;

  /**
   * BodyDesignOption constructor
   * @param data contains relevant attributes for BodyDesignOption class
   */
  public BodyDesignOption(String[] data) {
    this.bdName = data[0];
  }

  /**
   * Create the BodyDesignOption table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS body_design_option("
          + "BDNAME VARCHAR(255) PRIMARY KEY,"
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
   * Populates a table for the BodyDesignOption class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<BodyDesignOption> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new BodyDesignOption(data));
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
   * Creates SQL statement to do a bulk add of BodyDesignOption entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<BodyDesignOption> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO body_design_option (BDNAME) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      BodyDesignOption e = arr.get(i);
      sb.append(String.format("(\'%s\')", e.getBdName()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public String getBdName() {
    return bdName;
  }
}
