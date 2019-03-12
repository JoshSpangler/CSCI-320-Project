package entities;

/**
 * Holds data about a Vehicle
 */
public class Vehicle {

  //
  // Attributes
  //
  private String VIN, color, upholstery, model;
  private int wheelID, manufacturerID, inventoryID, saleID;

  /**
   * Vehicle constructor
   * @param data contains relevant attributes for Vehicle class
   */
  public Vehicle(String[] data) {
    this.VIN = data[0];
    this.color = data[1];
    this.wheelID = Integer.parseInt(data[2]);
    this.upholstery = data[3];
    this.model = data[4];
    this.manufacturerID = Integer.parseInt(data[5]);
    this.inventoryID = Integer.parseInt(data[6]);
    this.saleID = Integer.parseInt(data[7]);
  }

  public String getVIN() {
    return VIN;
  }

  public String getColor() {
    return color;
  }

  public String getUpholstery() {
    return upholstery;
  }

  public String getModel() {
    return model;
  }

  public int getWheelID() {
    return wheelID;
  }

  public int getManufacturerID() {
    return manufacturerID;
  }

  public int getInventoryID() {
    return inventoryID;
  }

  public int getSaleID() {
    return saleID;
  }
}
