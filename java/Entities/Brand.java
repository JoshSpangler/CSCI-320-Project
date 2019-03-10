package Entities;

/**
 * Holds data about a Brand
 */
public class Brand {

  //
  // Attributes
  //
  private String brandName;

  /**
   * Brand constructor
   * @param data contains relevant attributes for Brand class
   */
  public Brand(String[] data) {
    this.brandName = data[0];
  }

  public String getBrandName() {
    return this.brandName;
  }
}
