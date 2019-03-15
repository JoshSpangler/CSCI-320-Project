package entities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

/**
 * Holds data about a Company
 */
public class Company {

  //
  // Attributes
  private int ID, zip, companySize;
  private String name, street, county, state;

  /**
   * Company constructor
   * @param data contains relevant attributes for Company class
   */
  public Company(String[] data) {
    /*
      CSV FILE NOT PRESENT YET
     */
  }
}
