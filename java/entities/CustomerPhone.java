package entities;

/**
 * Holds data about a CustomerPhone
 */
public class CustomerPhone {

  //
  // Attributes
  //
  private int ID;
  private String phoneNumber;

  /**
   * CustomerPhone constructor
   * @param data contains relevant attributes for CustomerPhone class
   */
  public CustomerPhone(String[] data) {
    this.ID = Integer.parseInt(data[0]);
    this.phoneNumber = data[1];
  }

  public int getID() {
    return ID;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
