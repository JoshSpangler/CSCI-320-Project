package Entities;

/**
 * Holds data about a Model
 */
public class Model {

  //
  // Attributes
  //
  private String modelName, series, driveTrain, brandName, transmission;
  private int year, baseCost, engineID;

  /**
   * Model constructor
   * @param data contains relevant attributes for Model class
   */
  public Model(String[] data) {
    this.modelName = data[0];
    this.series = data[1];
    this.year = Integer.parseInt(data[2]);
    this.driveTrain = data[3];
    this.transmission = data[4];
    this.baseCost = Integer.parseInt(data[5]);
    this.brandName = data[6];
    this.engineID = Integer.parseInt(data[7]);
  }

  public String getModelName() {
    return modelName;
  }

  public String getSeries() {
    return series;
  }

  public String getDriveTrain() {
    return driveTrain;
  }

  public String getBrandName() {
    return brandName;
  }

  public String getTransmission() {
    return transmission;
  }

  public int getYear() {
    return year;
  }

  public int getBaseCost() {
    return baseCost;
  }

  public int getEngineID() {
    return engineID;
  }
}
