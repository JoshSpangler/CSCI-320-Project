package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a Company
 * @author Josh Spangler
 */
public class Company {

  //
  // Attributes
  private int ID;
  private String companySize;

  /**
   * Company constructor
   * @param data contains relevant attributes for Company class
   */
  public Company(String[] data) {
    this.ID = Integer.parseInt(data[0]);
    this.companySize = data[1];
  }

  /**
   * Create the Company table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS company("
          + "ID INT PRIMARY KEY,"
          + "COMPANY_SIZE VARCHAR(255),"
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
   * Populates a table for the Company class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<Company> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new Company(data));
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
  private static String createInsertEntitiesSQL(ArrayList<Company> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO company (ID, COMPANY_SIZE) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      Company e = arr.get(i);
      sb.append(String.format("(%d, \'%s\')", e.getID(), e.getCompanySize()));

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

  public String getCompanySize() {
    return companySize;
  }
}
