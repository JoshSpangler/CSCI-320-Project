package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a Model
 * @author Josh Spangler
 */
public class Model {

  //
  // Attributes
  //
  private String modelName, series, driveTrain, brandName, transmission;
  private int year, baseCost, engineID;

  /**
   * Model constructor
   * @param data contains relevant attributes for Model class
   */
  public Model(String[] data) {
    this.modelName = data[0];
    this.brandName = data[1];
    this.series = data[2];
    this.year = Integer.parseInt(data[3]);
    this.driveTrain = data[4];
    this.transmission = data[5];
    this.baseCost = Integer.parseInt(data[6]);
    this.engineID = Integer.parseInt(data[7]);
  }

  /**
   * Create the Model table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS model("
          + "MODEL_NAME VARCHAR(255) PRIMARY KEY,"
          + "BRAND_NAME VARCHAR(255),"
          + "SERIES VARCHAR(255),"
          + "YEAR INT,"
          + "DRIVETRAIN VARCHAR(255),"
          + "TRANSMISSION VARCHAR(255),"
          + "BASE_COST INT,"
          + "ENGINE_ID INT,"
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
   * Populates a table for the Model class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<Model> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new Model(data));
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
   * Creates SQL statement to do a bulk add of Model entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<Model> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO model (MODEL_NAME, BRAND_NAME, SERIES, YEAR, DRIVETRAIN, TRANSMISSION, BASE_COST, ENGINE_ID) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      Model e = arr.get(i);
      sb.append(String.format("(\'%s\', \'%s\', \'%s\', %d, \'%s\', \'%s\', %d, %d)",
          e.getModelName(), e.getBrandName(), e.getSeries(), e.getYear(), e.getDriveTrain(), e.getTransmission(),
          e.getBaseCost(), e.getEngineID()));

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

  public String getSeries() {
    return series;
  }

  public String getDriveTrain() {
    return driveTrain;
  }

  public String getBrandName() {
    return brandName;
  }

  public String getTransmission() {
    return transmission;
  }

  public int getYear() {
    return year;
  }

  public int getBaseCost() {
    return baseCost;
  }

  public int getEngineID() {
    return engineID;
  }
}
