package cafe.management.cafe;

import java.sql.Date;

public class EmployeeData {
    private String RegistrationID;
    private String FullName;
    private String PhoneNumber;
    private String Category;
    private double Salery;
    private Date Date;
    private String Status;

    public EmployeeData(String RegistrationID, String FullName, String PhoneNumber, String Category, double Salery, Date Date,String status) {
        this.RegistrationID = RegistrationID;
        this.FullName = FullName;
        this.PhoneNumber = PhoneNumber;
        this.Category = Category;
        this.Salery = Salery;
        this.Date = Date;
        this.Status = status;
    }

    public String getRegistrationID() { return RegistrationID; }
    public String getFullName() { return FullName; }
    public String getPhoneNumber() { return PhoneNumber; }
    public String getCategory() { return Category; }
    public double getSalery() { return Salery; }
    public Date getDate() { return Date; }
    public String getStatus() {  // ✅ Add this getter
        return Status;
    }
}
