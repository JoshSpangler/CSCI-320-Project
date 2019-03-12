package entities;

/**
 * Holds data about an Inventory
 */
public class Inventory {

  //
  // Attributes
  //
  private int ID;

  /**
   * Inventory constructor
   * @param data contains relevant attributes for Inventory class
   */
  public Inventory(String[] data) {
    this.ID = Integer.parseInt(data[0]);
  }

  public int getID() {
    return ID;
  }
}
