package Entities;

/**
 * Holds data about a BodyDesignOption
 */
public class BodyDesignOption {

  //
  // Attributes
  //
  private String bdName;

  /**
   * BodyDesignOption constructor
   * @param data contains relevant attributes for BodyDesignOption class
   */
  public BodyDesignOption(String[] data) {
    this.bdName = data[0];
  }

  public String getBdName() {
    return bdName;
  }
}
