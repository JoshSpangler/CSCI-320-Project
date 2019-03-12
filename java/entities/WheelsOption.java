package entities;

/**
 * Holds data about a WheelsOption
 */
public class WheelsOption {

  //
  // Attributes
  //
  private int id, diameter;
  private String woName, style, runFlat;

  /**
   * WheelsOption constructor
   * @param data contains relevant attributes for WheelsOption class
   */
  public WheelsOption(String[] data) {
    this.id = Integer.parseInt(data[0]);
    this.diameter = Integer.parseInt(data[1]);
    this.woName = data[2];
    this.style = data[3];
    this.runFlat = data[4];
  }

  public int getId() {
    return id;
  }

  public int getDiameter() {
    return diameter;
  }

  public String getWoName() {
    return woName;
  }

  public String getStyle() {
    return style;
  }

  public String getRunFlat() {
    return runFlat;
  }
}
