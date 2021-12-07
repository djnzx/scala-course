package se1;

public class NewFunctionality {

  interface Customer {
    int turnover();
    int balance();
  }

  public boolean isEligibleForDiscount(Customer customer) {
    return customer.balance() > 10000
        && customer.turnover() > 100000;
  }

}
