package entities;

/**
 * Holds data about an OptionalUpgrades entity
 */
public class OptionalUpgrades {

  //
  // Attributes
  //
  private String VIN, optionalUpgrade;
  private int cost;


  /**
   * OptionalUpgrades constructor
   * @param data contains relevant attributes for OptionalUpgrades class
   */
  public OptionalUpgrades(String[] data) {
    this.VIN = data[0];
    this.optionalUpgrade = data[1];
    this.cost = Integer.parseInt(data[2]);
  }

  public String getVIN() {
    return VIN;
  }

  public String getOptionalUpgrade() {
    return optionalUpgrade;
  }

  public int getCost() {
    return cost;
  }
}
