package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Holds data about an IndividualBuyer
 */
public class IndividualBuyer {

  //
  // Attributes
  //
  private int ID, annualIncome;
  private String gender;

  /**
   * IndividualBuyer constructor
   * @param data contains relevant attributes for IndividualBuyer class
   */
  public IndividualBuyer(String[] data) {
    this.ID = Integer.parseInt(data[0]);
    this.gender = data[1];
    this.annualIncome = Integer.parseInt(data[2]);
  }

  /**
   * Create the IndividualBuyer table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS person("
          + "ID INT PRIMARY KEY,"
          + "GENDER VARCHAR(255),"
          + "ANNUAL_INCOME INT,"
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
   * Populates a table for the IndividualBuyer class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<IndividualBuyer> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new IndividualBuyer(data));
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
   * Creates SQL statement to do a bulk add of IndividualBuyer entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<IndividualBuyer> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO person (ID, GENDER, ANNUAL_INCOME) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      IndividualBuyer e = arr.get(i);
      sb.append(String.format("(%d, \'%s\', %d)",
          e.getID(), e.getGender(), e.getAnnualIncome()));

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

  public int getAnnualIncome() {
    return annualIncome;
  }

  public String getGender() {
    return gender;
  }
}
