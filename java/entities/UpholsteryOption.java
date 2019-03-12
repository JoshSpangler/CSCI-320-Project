package entities;

/**
 * Holds data about an UpholsteryOption
 */
public class UpholsteryOption {

  //
  // Attributes
  //
  private String name;

  /**
   * UpholsteryOption constructor
   * @param data contains relevant attributes for UpholsteryOption class
   */
  public UpholsteryOption(String[] data) {
    this.name = data[0];
  }

  public String getName() {
    return name;
  }
}
