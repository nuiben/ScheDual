package schedual.model;

/**
 * Engagement Class which holds a hybrid of Customer and Appointment Values for a custom report.
 * @author Ben Porter
 */

public class Engagement {
    private int Customer_ID;
    private String Customer_Name;
    private String Last_Appointment;
    private String Next_Appointment;

    /** Default Constructor for Engagement
     * @param Customer_ID The unique ID for the corresponding customer from the customers table.
     * @param Customer_Name String value indicating the name of the customer.
     * @param Last_Appointment String containing the Formatted DateTime of the Customer's Last Appointment.
     * @param Next_Appointment String containing the Formatted DateTime of the Customer's Next Appointment.
     * */

    public Engagement(int Customer_ID, String Customer_Name, String Last_Appointment, String Next_Appointment) {
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
        this.Last_Appointment = Last_Appointment;
        this.Next_Appointment = Next_Appointment;
    }

    /**
     * Returns the given id of the customer for this engagement.
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
     * Gets the name of the customer for this engagement.
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

    /**
     * Returns the Last Appointment for this Customer's Engagement.
     * @return id integer value.
     */
    public String getLast_Appointment() {
        return Last_Appointment;
    }

    /**
     * Sets the Last_Appointment for this Customer's Engagement.
     * @param Last_Appointment  id integer value.
     */
    public void setLast_Appointment(String Last_Appointment) {
        this.Last_Appointment = Last_Appointment;
    }

    /**
     * Returns the Next Appointment for this Customer's Engagement.
     * @return id integer value.
     */
    public String getNext_Appointment() {
        return Next_Appointment;
    }

    /**
     * Sets the Next Appointment for this Customer's Engagement.
     * @param Next_Appointment id integer value.
     */
    public void setNext_Appointment(String Next_Appointment) {
        this.Next_Appointment = Next_Appointment;
    }
}
