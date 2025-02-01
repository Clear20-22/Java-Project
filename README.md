# Cafeteria Management, DU
## Overview
The Cafeteria Management, DU, is a Java-based application designed to streamline cafeteria operations at the Science Complex, University of Dhaka.

It provides a secure and efficient platform for teachers, students, admins, and employees to manage cafeteria services.
The system features user authentication, food ordering, purchase tracking, and administrative controls, ensuring a seamless 
experience for both customers and staff. With role-based access, real-time updates, and secure login mechanisms, this system 
enhances efficiency, transparency, and user convenience.

## Tools
- IDE: IntelliJ
- Language: JAVA
- Framework: JavaFX
- Database: MySQL
- Testing: Manual testing for the overall 
system functionality


## Workflow
![image alt](https://github.com/Clear20-22/Java-Project/blob/5ae01b462834a38334f3274799ee52cdaafe6912/Blank%20diagram.png)

## Directory Layout

```
cafe/
│
├── src/                        # Source code
│   ├── main/                   
│   │   ├── java/
|   |   |      |── module-info.java            
│   │   │      |── cafe.management.cafe/  
│   │   │           ├── CafeManagement.java       # Main entry point of the 
                                                    application
│   │   │           ├── DatabaseUsers.java        # Database connection logic
│   │   │           ├── SurfaceLayoutContoller.java   
│   │   │           ├── CustomerController.java  
│   │   │           ├── cardProductController.java 
│   │   │           └── ProductData.java
│   │   └── resources/          
│   │       ├── cafe.management/        
│   │                      ├── cafe/    # JavaFX FXML & CSS files
|   |                           ├────── cardProduct.fxml
|   |                           ├────── CustomerLayout.fxml
|   |                           ├────── LoginPage.fxml
|   |                           ├────── LoginStyle.css
|   |                           ├────── mainForm1Design.css
|   |                           ├────── SurfaceLayout.fxml
|   |                           ├────── test.fxml
│   │                      ├── img/              
│   └── lib/                         # External libraries (e.g., MySQL JDBC)
│       └── mysql-connector-java-x.x.x.jar  # MySQL JDBC driver
├── .gitignore                       
├── README.md                       
├── pom.xml          

```

## Key Features

### For Teachers & Students:
- **Secure Login & Sign-up** – Access the system using a registration ID and password.  
- **View Menu** – Browse available food items with prices.  
- **Order Food** – Add items to the cart and complete purchases.  
- **Purchase History** – View past orders and payments.  
- **Leave Reviews & Ratings** – Provide feedback on food quality.  
- **Profile Management** – Update personal details.  
- **Password Recovery** – Reset password using a predefined codeword.  

### For Admin & Employees:
- **User Management** – Admin can approve new employees and track users.  
- **Inventory Management** – Update product availability and pricing.  
- **Sales Tracking** – Monitor daily and total income.  
- **Order Management** – Process and track customer orders.  
- **Employee Access Control** – Admin approves employee logins to prevent unauthorized access.  

## Additional Features:
- **Sign-out Confirmation** – Ensures secure logout.  
- **"About Us" Page** – Provides details about the cafeteria and its services.  



## Download and Setup Instructions

### **1. Download the ZIP File:**
Download the project ZIP file from the [repository](#).

### **2. Extract the Files:**
Extract the contents of the ZIP file to your preferred directory.

### **3. Environment Setup:**

Ensure that you have **JavaFX** set up in your IDE. You may need to manually configure JavaFX if it is not bundled with your IDE. Refer to the [JavaFX installation guide](https://openjfx.io/) if necessary.

- **For IntelliJ IDEA or Eclipse:**  
    Add the **JavaFX SDK** to your project libraries.
    Set the VM options to include JavaFX modules like:
    ```bash
    --module-path "path-to-javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
    ```

### **4. Database Setup:**

1. **Import the Database Schema:**  
   Import the provided SQL script from the **Database Schema** folder into your MySQL database. This will create the necessary tables and structure for your application.

2. **Modify Database Credentials:**

   - Open the `DatabaseUsers.java ` file located in your project folder.
   - Find the following lines and update them with your **MySQL username** and **password**:
   
     ```java
     String jdbcUsername = "root";  // MySQL username
     String jdbcPassword = "root";  // MySQL password
     String jdbcURL = "jdbc:mysql://localhost:3306/cafeteria_db";  // Database URL
     ```

   - Replace `"root"` with your actual MySQL username and password. Example:
   
     ```java
     String jdbcUsername = "your_username";  // MySQL username
     String jdbcPassword = "your_password";  // MySQL password
     String jdbcURL = "jdbc:mysql://localhost:3306/cafeteria_db";  // Database URL
     ```

3. **Save the file** after modifying the credentials.

### **5. Run the Application:**

After setting up the database and modifying the credentials, follow these steps to run the application:

1. **Run the server** (for chat functionality or backend processes).
2. **Run the Admin and Client sections** of the application as needed.

---

## Troubleshooting

- **Database Connection Error?**  
  Make sure MySQL is running and that the database URL, username, and password are correctly set in the `Database.java` file.

- **JavaFX Issues?**  
  Ensure you have added the JavaFX SDK and set the VM options correctly in your IDE.

---

## License

Feel free to fork, clone, and modify the project. If you encounter any issues, please open an issue on this repository.

---

🚀 **Enjoy using the Cafeteria Management, DU.**


## Authors
- Suraya Jannat Mim (Roll: 17)<br>
- Anisha Tabassum (Roll: 19)<br>
- Jubayer Ahmed Sojib (Roll: 23)<br>
- Tamal Kanti Sarker (Roll: 39)
