package cafe.management.cafe;

public class CustomerSummary {
    private String customerId;
    private String date;
    private int totalQuantity;
    private double totalPrice;
    private String customerName;

    public CustomerSummary(String customerId, String date, int totalQuantity, double totalPrice, String customerName) {
        this.customerId = customerId;
        this.date = date;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
    }
    public String getCustomerId() { return customerId; }
    public String getDate() { return date; }
    public int getTotalQuantity() { return totalQuantity; }
    public double getTotalPrice() { return totalPrice; }
    public String getCustomerName(){ return customerName; }

}
