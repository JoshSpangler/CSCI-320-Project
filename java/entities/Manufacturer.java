package entities;

/**
 * Holds data about a Manufacturer
 */
public class Manufacturer {

  //
  // Attributes
  //
  private int ID, zip;
  private String name, street, county, state;

  /**
   * Manufacturer constructor
   * @param data contains relevant attributes for Manufacturer class
   */
  public Manufacturer(String[] data) {
    this.ID = Integer.parseInt(data[0]);
    this.name = data[1];
    this.street = data[2];
    this.county = data[3];
    this.state = data[4];
    this.zip = Integer.parseInt(data[5]);
  }

  public int getID() {
    return ID;
  }

  public int getZip() {
    return zip;
  }

  public String getName() {
    return name;
  }

  public String getStreet() {
    return street;
  }

  public String getCounty() {
    return county;
  }

  public String getState() {
    return state;
  }
}
