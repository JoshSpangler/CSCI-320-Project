package Entities;

/**
 * Holds data about an EngineOption
 */
public class EngineOption {

  //
  // Attributes
  //
  private int id, numLitres, numCylinders, prodCost;
  private String engineName;

  /**
   * EngineOption constructor
   * @param data contains relevant attributes for EngineOption class
   */
  public EngineOption(String[] data) {
    this.id = Integer.parseInt(data[0]);
    this.engineName = data[1];
    this.numLitres = Integer.parseInt(data[2]);
    this.numCylinders = Integer.parseInt(data[3]);
    this.prodCost = Integer.parseInt(data[5]);
  }

  public int getId() {
    return id;
  }

  public int getNumLitres() {
    return numLitres;
  }

  public int getNumCylinders() {
    return numCylinders;
  }

  public int getProdCost() {
    return prodCost;
  }

  public String getEngineName() {
    return engineName;
  }
}
