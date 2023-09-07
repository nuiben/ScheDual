package schedual.model;

import java.time.LocalDateTime;

/**
 * Customer Class which holds input parameters for a standard record in MySQL customers table.
 * @author Ben Porter
 */

public class Customer {
    private String Postal_Code;
    private int Customer_ID;
    private String Customer_Name;
    private String Address;
    private String Phone;
    private LocalDateTime Create_Date;
    private String Created_By;
    private LocalDateTime Last_Update;
    private String Last_Updated_By;
    private int Division_ID;

    private String Division_Name;
    private int Country_ID;

    private String Country_Name;

    /** Default Constructor for Customer
     * @param Customer_ID Unique integer value associated to the appointment
     * @param Customer_Name String value indicating the name of the appointment
     * @param Address String describing the appointment in more detail
     * @param Postal_Code String of the postal code of the customer's address
     * @param Phone String of the customer's phone number
     * @param Division_ID integer ID of the division this customer belongs to
     * @param Division_Name String Name of the division this customer belongs to
     * @param Country_ID ID of Country
     * @param Country_Name String of Country
     * */
    public Customer(int Customer_ID, String Customer_Name, String Address, String Postal_Code, String Phone, int Division_ID, String Division_Name, int Country_ID, String Country_Name) {
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
        this.Address = Address;
        this.Postal_Code = Postal_Code;
        this.Phone = Phone;
        this.Division_ID = Division_ID;
        this.Division_Name = Division_Name;
        this.Country_ID = Country_ID;
        this.Country_Name = Country_Name;
    }

    /**
     * Returns the given id.
     * @return id integer value.
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /**
     * Sets the id variable to integer provided.
     * @param  Customer_ID integer value.
     */
    public void setCustomer_ID(int Customer_ID) {
        this.Customer_ID = Customer_ID;
    }

    /**
     * Gets the customers First and Last name.
     * @return the customer name
     */
    public String getCustomer_Name() {
        return Customer_Name;
    }

    /**
     * Sets the customer name variable to String provided.
     * @param  Customer_Name String value.
     */
    public void setCustomer_Name(String Customer_Name) {
        this.Customer_Name = Customer_Name;
    }

    /** Gets the current address of the customer.
     * @return the Customer's Address.
     */
    public String getAddress() { return Address; }

    /**
     * Sets the customer address variable.
     * @param  Address String value.
     */
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     * Gets the Customer's Postal Cade.
     * @return the customer's postal code
     */
    public String getPostal_Code() { return Postal_Code; }

    /**
     * Sets the Customer's Postal Cade.
     * @param Postal_Code the Postal Code
     */
    public void setPostal_Code(String Postal_Code) {
        this.Address = Postal_Code;
    }

    /**
     * Gets the Customer's Phone Number.
     * @return the customer's phone number
     */
    public String getPhone() { return Phone; }

    /**
     * Sets the Customer's Phone Number.
     * @param Phone String of the customer's phone number.
     */
    public void setPhone(String Phone) {
        this.Address = Phone;
    }

    /**
     * Gets the First Level Division ID for the Customer.
     * @return the division ID number for this customer
     */
    public int getDivision_ID() { return Division_ID; }
    /**
     * Sets the First Level Division ID.
     * @param Division_ID  the integer Division ID
     */
    public void setDivision_ID(int Division_ID) {
        this.Division_ID = Division_ID;
    }

    /**
     * Gets the Level One Division Name.
     * @return  Division_Name the String value of the Level One Division Name.
    */
    public String getDivision_Name() { return Division_Name; }

    /**
     * Sets the Country Name.
     * @param Division_Name updates the String value of the Level One Division Name.
     */
    public void setDivision_Name(String Division_Name) {
        this.Division_Name = Division_Name;
    }

    /**
     * Gets the Country ID for the Customer.
     * @return the Country ID
     */
    public int getCountry_ID() { return Country_ID; }
    /**
     * Sets the Country ID.
     * @param Country_ID updates the int value of the Country ID.
     */
    public void setCountry_ID(int Country_ID) {
        this.Country_ID = Country_ID;
    }

    /**
     * Gets the Country Name.
     * @return the String Country Name
     */

    public String getCountry_Name() { return Country_Name; }

    /**
     * Sets the Country Name.
     * @param Country_Name updates the String value of the Country Name.
     */
    public void setCountry_Name(String Country_Name) {
        this.Country_Name = Country_Name;
    }




}
