package entities;

/**
 * Holds data about a Sale
 */
public class Sale {

  //
  // Attributes
  //
  private int saleID, dealerID, customerID,
      totalCost, day, month, year;

  /**
   * Sale constructor
   * @param data contains relevant attributes for Sale class
   */
  public Sale(String[] data) {
    this.saleID = Integer.parseInt(data[0]);
    this.totalCost = Integer.parseInt(data[1]);
    this.day = Integer.parseInt(data[2]);
    this.month = Integer.parseInt(data[3]);
    this.year = Integer.parseInt(data[4]);
    this.dealerID = Integer.parseInt(data[5]);
    this.customerID = Integer.parseInt(data[6]);
  }

  public int getSaleID() {
    return saleID;
  }

  public int getDealerID() {
    return dealerID;
  }

  public int getCustomerID() {
    return customerID;
  }

  public int getTotalCost() {
    return totalCost;
  }

  public int getDay() {
    return day;
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }
}
