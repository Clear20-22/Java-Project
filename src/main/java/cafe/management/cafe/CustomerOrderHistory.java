package cafe.management.cafe;

public class CustomerOrderHistory {

    private String productId;
    private String productName;
    private String customerId;
    private String customerName;
    private String date;
    private int quantity;
    private double totalPrice;

    public CustomerOrderHistory(String productId, String productName, String customerId,
                           String customerName, String date, int quantity, double totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.date = date;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters (must match TableColumn property names!)
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getDate() { return date; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
}
