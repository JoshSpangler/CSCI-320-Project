import entities.*;

import java.sql.*;

/**
 * Using the various Entities classes, populates the database with the CSV data
 */
public class Populator {

  // The connection to the database
  private Connection conn;


  /**
   * Create a database connection with the given params
   * @param location: path of where to place the database
   * @param user: user name for the owner of the database
   * @param password: password of the database owner
   */
  public void createConnection(String location,
                               String user,
                               String password){
    try {

      //This needs to be on the front of your location
      String url = "jdbc:h2:" + location;

      //This tells it to use the h2 driver
      Class.forName("org.h2.Driver");

      //creates the connection
      conn = DriverManager.getConnection(url,
          user,
          password);
    } catch (SQLException | ClassNotFoundException e) {
      //You should handle this better
      e.printStackTrace();
    }
  }

  /**
   * just returns the connection
   * @return: returns class level connection
   */
  public Connection getConnection(){
    return conn;
  }


  /**
   * Starts and populates database
   * @param args: not used
   */
  public static void main(String[] args) {
    Populator p = new Populator();

    // Hard drive location of the database
    String location = "./h2demo/h2demo";
    String user = "me";
    String password = "password";

    //Create the database connections, basically makes the database
    p.createConnection(location, user, password);

    // Create table for each (currently working) Entity class
    try {
      BodyDesignOption.createTable(p.getConnection());
      BodyDesignOption.populateTableFromCSV(p.getConnection(),
          "csvs/Body_Design_option.csv");

      Brand.createTable(p.getConnection());
      Brand.populateTableFromCSV(p.getConnection(),
          "csvs/Brand.csv");

      ColorOption.createTable(p.getConnection());
      ColorOption.populateTableFromCSV(p.getConnection(),
          "csvs/Color_Option.csv");

      Customer.createTable(p.getConnection());
      Customer.populateTableFromCSV(p.getConnection(),
          "csvs/Customer.csv");

      CustomerPhone.createTable(p.getConnection());
      CustomerPhone.populateTableFromCSV(p.getConnection(),
          "csvs/Customer_Phone.csv");

      Inventory.createTable(p.getConnection());
      Inventory.populateTableFromCSV(p.getConnection(),
          "csvs/Inventory.csv");

      InventoryDealer.createTable(p.getConnection());
      InventoryDealer.populateTableFromCSV(p.getConnection(),
          "csvs/Inventory_Dealer.csv");

      Manufacturer.createTable(p.getConnection());
      Manufacturer.populateTableFromCSV(p.getConnection(),
          "csvs/Manufacturer.csv");

      ManufacturerSupplier.createTable(p.getConnection());
      ManufacturerSupplier.populateTableFromCSV(p.getConnection(),
          "csvs/Manufacturer_Supplier.csv");

      Model.createTable(p.getConnection());
      Model.populateTableFromCSV(p.getConnection(),
          "csvs/Model.csv");

      ModelBodyDesignOption.createTable(p.getConnection());
      ModelBodyDesignOption.populateTableFromCSV(p.getConnection(),
          "csvs/Model_Body_Design_Option.csv");

      ModelColorOption.createTable(p.getConnection());
      ModelColorOption.populateTableFromCSV(p.getConnection(),
          "csvs/Model_Color_Option.csv");

      ModelUpholsteryOption.createTable(p.getConnection());
      ModelUpholsteryOption.populateTableFromCSV(p.getConnection(),
          "csvs/Model_Upholstry_Option.csv");

      ModelWheelsOption.createTable(p.getConnection());
      ModelWheelsOption.populateTableFromCSV(p.getConnection(),
          "csvs/Model_Wheels_Option.csv");

      OptionalUpgrades.createTable(p.getConnection());
      OptionalUpgrades.populateTableFromCSV(p.getConnection(),
          "csvs/Optional_Upgrades.csv");

      Sale.createTable(p.getConnection());
      Sale.populateTableFromCSV(p.getConnection(),
          "csvs/Sale.csv");

      Supplier.createTable(p.getConnection());
      Supplier.populateTableFromCSV(p.getConnection(),
          "csvs/Supplier.csv");

      UpholsteryOption.createTable(p.getConnection());
      UpholsteryOption.populateTableFromCSV(p.getConnection(),
          "csvs/Upholstry_Option.csv");

      Vehicle.createTable(p.getConnection());
      Vehicle.populateTableFromCSV(p.getConnection(),
          "csvs/Vehicle.csv");

      WheelsOption.createTable(p.getConnection());
      WheelsOption.populateTableFromCSV(p.getConnection(),
          "csvs/Wheels_Option.csv");


    }
    catch(SQLException e) {
      e.printStackTrace();
    }
  }
}
