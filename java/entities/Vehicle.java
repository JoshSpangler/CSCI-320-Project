package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a Vehicle
 */
public class Vehicle {

  //
  // Attributes
  //
  private String color, model, design, style, saleID;
  private int VIN, wheelsID, dealerID, manufacturerID, day, month, year, price;

  /**
   * Vehicle constructor
   * @param data contains relevant attributes for Vehicle class
   */
  public Vehicle(String[] data) {
    this.VIN = Integer.parseInt(data[0]);
    this.model = data[1];
    this.wheelsID = Integer.parseInt(data[2]);
    this.color = data[3];
    this.design = data[4];
    this.style = data[5];
    this.day = Integer.parseInt(data[6]);
    this.month = Integer.parseInt(data[7]);
    this.year = Integer.parseInt(data[8]);
    this.price = Integer.parseInt(data[9]);
    this.dealerID = Integer.parseInt(data[10]);
    this.saleID = data[11];
    this.manufacturerID = Integer.parseInt(data[12]);
  }

  /**
   * Create the Vehicle table with the given attributes
   * @param conn the database connection to work with
   */
  public static void createTable(Connection conn) {
    try {
      String query = "CREATE TABLE IF NOT EXISTS vehicle("
          + "VIN INT PRIMARY KEY,"
          + "MODEL VARCHAR(255),"
          + "WHEELS_ID INT,"
          + "COLOR VARCHAR(255),"
          + "DESIGN VARCHAR(255),"
          + "STYLE VARCHAR(255),"
          + "DAY INT,"
          + "MONTH INT,"
          + "YEAR INT,"
          + "PRICE INT,"
          + "DEALER_ID INT,"
          + "SALE_ID VARCHAR(255),"
          + "MANUFACTURER_ID INT,"
          + "FOREIGN KEY (MODEL, WHEELS_ID) REFERENCES model_wheels_option(MODEL_NAME, WHEELS_ID),"
          + "FOREIGN KEY (MODEL, COLOR) REFERENCES model_color_option(MODEL_NAME, COLOR),"
          + "FOREIGN KEY (MODEL, DESIGN) REFERENCES model_body_design_option(MODEL_NAME, DESIGN_NAME),"
          + "FOREIGN KEY (MODEL, STYLE) REFERENCES model_upholstery_option(MODEL_NAME, UPHOLSTERY)"
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
   * Populates a table for the Vehicle class
   * @param conn database connection to work with
   * @param fileName name of CSV file
   * @throws SQLException
   */
  public static void populateTableFromCSV(Connection conn, String fileName) throws SQLException {
    ArrayList<Vehicle> arr = new ArrayList<>();

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      String[] data;
      while((line = br.readLine()) != null) {
        data = line.split(",");
        arr.add(new Vehicle(data));
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
   * Creates SQL statement to do a bulk add of Vehicle entities
   * @param arr list of entities
   */
  private static String createInsertEntitiesSQL(ArrayList<Vehicle> arr) {
    StringBuilder sb = new StringBuilder();

    /**
     * The start of the statement,
     * tells it the table to add it to
     * the order of the data in reference
     * to the columns to ad dit to
     */
    sb.append("INSERT INTO vehicle (VIN, MODEL, WHEELS_ID, COLOR, DESIGN, STYLE, DAY, MONTH, YEAR, " +
        "PRICE, DEALER_ID, SALE_ID, MANUFACTURER_ID) VALUES");

    for(int i = 0; i < arr.size(); i++) {
      Vehicle e = arr.get(i);
      sb.append(String.format("(%d, \'%s\', %d, \'%s\', \'%s\', \'%s\', %d, %d, %d, %d, %d, \'%s\', %d)",
          e.getVIN(), e.getModel(), e.getWheelsID(), e.getColor(), e.getDesign(), e.getStyle(), e.getDay(),
          e.getMonth(), e.getYear(), e.getPrice(), e.getDealerID(), e.getSaleID(), e.getManufacturerID()));

      if(i != arr.size()-1) {
        sb.append(",");
      }
      else {
        sb.append(";");
      }
    }

    return sb.toString();
  }

  public int getVIN() {
    return VIN;
  }

  public String getColor() {
    return color;
  }


  public String getModel() {
    return model;
  }

  public int getWheelsID() {
    return wheelsID;
  }

  public int getManufacturerID() {
    return manufacturerID;
  }

  public String getDesign() {
    return design;
  }

  public String getStyle() {
    return style;
  }

  public int getDealerID() {
    return dealerID;
  }

  public int getDay() {
    return day;
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }

  public int getPrice() {
    return price;
  }

  public String getSaleID() {
    return saleID;
  }
}
