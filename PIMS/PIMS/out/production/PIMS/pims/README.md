# Pharmacy Information Management System (PIMS)

A Desktop Pharmacy Information Management System built using **Java Swing** and connected to a **MySQL** database via **JDBC**. This application supports role-based access control for Administrators and Cashiers, full medicine inventory management, real-time POS sales processing with automatic stock deduction, and system reports.

---

## 👥 Default Login Credentials

Use the following pre-configured user credentials to access the system:

| Role | Username | Password | Access Level |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | Full access to users, medicine CRUD, suppliers, and system reports. |
| **Cashier** | `cashier` | `cash123` | Access to the Point of Sale (POS) checkout terminal and billing. |

---

## 📁 System Requirements & Prerequisites

* **Operating System**: Windows 10 / 11 (64-bit)
* **Java Runtime Environment (JRE)**: Minimum **JRE 1.8.0 (Java 8)** or higher
* **Database Server**: MySQL Server 5.7/8.0+ (via XAMPP, WAMP, or standalone MySQL)
* **JDBC Driver**: `mysql-connector-j-8.3.0.jar` (included in project dependencies)

---

## 🛠️ Database Setup Instructions

1. Start your local MySQL server (e.g., launch Apache and MySQL in **XAMPP Control Panel**).
2. Open **phpMyAdmin** in your browser at `http://localhost/phpmyadmin/`.
3. Create a new database named **`pims`**.
4. Select the `pims` database, navigate to the **Import** tab, and import the provided **`database.sql`** file located in the project root directory.
5. Ensure MySQL is running on default port `3306` with user `root` and no password (or update `DatabaseConnection.java` accordingly).

---

## 🚀 How to Run the Application

### Option A: Running via Windows Executable (Recommended)
1. Ensure your MySQL server is running in the background.
2. Double-click **`ReneNaidoo_pims.exe`** located in the root project directory.

### Option B: Running via Executable JAR
1. Open your command prompt/terminal in the project root folder.
2. Execute the following command:
   ```bash
   java -jar out/artifacts/PIMS_jar/PIMS.jar