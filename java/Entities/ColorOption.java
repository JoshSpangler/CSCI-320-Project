package Entities;

/**
 * Holds data about a ColorOption
 */
public class ColorOption {

  //
  // Attributes
  //
  private String color;

  /**
   * ColorOption constructor
   * @param data contains relevant attributes for ColorOption class
   */
  public ColorOption(String[] data) {
    this.color = data[0];
  }

  public String getColor() {
    return color;
  }
}
