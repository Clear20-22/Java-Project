package cafe.management.cafe;

import java.sql.Date;

public class ProductData {
    private String ProductId;
    private String ProductName;
    private String Type;
    private int Stock;
    private double Price;
    private String Status;
    private Date DateAdded;
    private String Image;

   public ProductData(String ProductId, String ProductName, String Type, int Stock, double Price, String Status, Date DateAdded, String Image) {
       this.ProductId = ProductId;
       this.ProductName = ProductName;
       this.Type = Type;
       this.Stock = Stock;
       this.Price = Price;
       this.Status = Status;
       this.DateAdded = DateAdded;
       this.Image = Image;
   }


    public String getProductID() {
        return ProductId;
    }
    public String getProductName() {
    return ProductName;
    }
    public int getStock() {
        return Stock;
    }
    public double getPrice() {
        return Price;
    }
    public String getType() {
        return Type;
    }
    public String getStatus() {
        return Status;
    }
    public Date getDateAdded() {
        return DateAdded;
    }
    public String getImage() {
        return Image;
    }
}
