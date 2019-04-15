import entities.*;

import java.sql.*;

/**
 * Using the various Entities classes, populates the database with the CSV data
 * @author Josh Spangler
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
    String location = "./database/database";
    String user = "me";
    String password = "password";

    //Create the database connections, basically makes the database
    p.createConnection(location, user, password);

    // Create table for each (currently working) Entity class
    try {
      BodyDesignOption.createTable(p.getConnection());
      BodyDesignOption.populateTableFromCSV(p.getConnection(),
          "data/output/body_design_opt.csv");

      ColorOption.createTable(p.getConnection());
      ColorOption.populateTableFromCSV(p.getConnection(),
          "data/output/color_opt.csv");

      Company.createTable(p.getConnection());
      Company.populateTableFromCSV(p.getConnection(),
          "data/output/company.csv");

      Customer.createTable(p.getConnection());
      Customer.populateTableFromCSV(p.getConnection(),
          "data/output/customer.csv");

      CustomerPhone.createTable(p.getConnection());
      CustomerPhone.populateTableFromCSV(p.getConnection(),
          "data/output/cust_phone.csv");

      Dealer.createTable(p.getConnection());
      Dealer.populateTableFromCSV(p.getConnection(),
          "data/output/dealer.csv");

      EngineOption.createTable(p.getConnection());
      EngineOption.populateTableFromCSV(p.getConnection(),
          "data/output/engine_opt.csv");

      IndividualBuyer.createTable(p.getConnection());
      IndividualBuyer.populateTableFromCSV(p.getConnection(),
          "data/output/person.csv");

      Manufacturer.createTable(p.getConnection());
      Manufacturer.populateTableFromCSV(p.getConnection(),
          "data/output/manufacturer.csv");

      ManufacturerSupplier.createTable(p.getConnection());
      ManufacturerSupplier.populateTableFromCSV(p.getConnection(),
          "data/output/supplies.csv");

      Model.createTable(p.getConnection());
      Model.populateTableFromCSV(p.getConnection(),
          "data/output/model.csv");

      ModelBodyDesignOption.createTable(p.getConnection());
      ModelBodyDesignOption.populateTableFromCSV(p.getConnection(),
          "data/output/model_design.csv");

      ModelColorOption.createTable(p.getConnection());
      ModelColorOption.populateTableFromCSV(p.getConnection(),
          "data/output/model_color_opt.csv");

      ModelUpholsteryOption.createTable(p.getConnection());
      ModelUpholsteryOption.populateTableFromCSV(p.getConnection(),
          "data/output/model_upholstery_opt.csv");

      ModelWheelsOption.createTable(p.getConnection());
      ModelWheelsOption.populateTableFromCSV(p.getConnection(),
          "data/output/model_wheel_opt.csv");

      OptionalUpgrades.createTable(p.getConnection());
      OptionalUpgrades.populateTableFromCSV(p.getConnection(),
          "data/output/opt_upgrade.csv");

      Sale.createTable(p.getConnection());
      Sale.populateTableFromCSV(p.getConnection(),
          "data/output/sale.csv");

      Supplier.createTable(p.getConnection());
      Supplier.populateTableFromCSV(p.getConnection(),
          "data/output/supplier.csv");

      UpholsteryOption.createTable(p.getConnection());
      UpholsteryOption.populateTableFromCSV(p.getConnection(),
          "data/output/upholstery_opt.csv");

      Vehicle.createTable(p.getConnection());
      Vehicle.populateTableFromCSV(p.getConnection(),
          "data/output/vehicle.csv");

      VehicleOptionalUpgrade.createTable(p.getConnection());
      VehicleOptionalUpgrade.populateTableFromCSV(p.getConnection(),
          "data/output/vehicle_upgrade.csv");

      WheelsOption.createTable(p.getConnection());
      WheelsOption.populateTableFromCSV(p.getConnection(),
          "data/output/wheel_opt.csv");


    }
    catch(SQLException e) {
      e.printStackTrace();
    }
  }
}
