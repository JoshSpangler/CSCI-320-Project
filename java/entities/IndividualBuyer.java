package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Holds data about an IndividualBuyer
 */
public class IndividualBuyer {

  //
  // Attributes
  //
  private int ID, zip, annualIncome;
  private String name, street, county, state, gender;

  /**
   * IndividualBuyer constructor
   * @param data contains relevant attributes for IndividualBuyer class
   */
  public IndividualBuyer(String[] data) {
    /*
      CSV FILE NOT PRESENT YET
     */
  }
}
