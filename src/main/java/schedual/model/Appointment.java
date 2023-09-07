package schedual.model;

/**
 * Appointment Class which holds input parameters for a standard record in MySQL appointments table.
 * @author Ben Porter
 */

public class Appointment {
    private int Appointment_ID;
    private String Title;
    private String Description;
    private String Location;
    private String Type;
    private String Start;
    private String End;
    private int Customer_ID;
    private int User_ID;
    private int Contact_ID;

    /** Default Constructor for Appointment
     * @param Appointment_ID Unique integer value associated to the appointment
     * @param Title String value indicating the name of the appointment
     * @param Description String describing the appointment in more detail
     * @param Location String that shows the location where the appointment is set
     * @param Type String category for the appointment
     * @param Start LocalDateTime object providing the start time and date of the meeting
     * @param End LocalDateTime object providing the end time and date of the meeting
     * @param Customer_ID integer customer id
     * @param User_ID integer user id
     * @param Contact_ID integer contact id
     * */
    public Appointment(int Appointment_ID, String Title, String Description, String Location, String Type, String Start, String End, int Customer_ID, int User_ID, int Contact_ID) {
        this.Appointment_ID = Appointment_ID;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.Type = Type;
        this.Start = Start;
        this.End = End;
        this.Customer_ID = Customer_ID;
        this.User_ID = User_ID;
        this.Contact_ID = Contact_ID;
    }

    /**
     * Returns the given id.
     * @return id integer value.
     */
    public int getAppointment_ID() {
        return Appointment_ID;
    }

    /**
     * Sets the id variable to integer provided.
     * @param  Appointment_ID integer value.
     */
    public void setAppointment_ID(int Appointment_ID) {
        this.Appointment_ID = Appointment_ID;
    }

    /**
     * Gets the Title of the Appointment
     * @return the title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Sets the title variable to String provided.
     * @param  Title String value.
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     * Gets a brief description of the Appointment.
     * @return the description
     */
    public String getDescription() { return Description; }

    /**
     * Sets the description variable to String provided.
     * @param  Description String value.
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * Gets the Location where the Appointment is taking place.
     * @return the location
     */
    public String getLocation() { return Location; }

    /**
     * Gets the categorical type of the Appointment.
     * @return the type
     */
    public String getType() { return Type; }

    /**
     * Returns a String of a Formatted DateTime for when the Appointment begins.
     * @return the start time
     */
    public String getStart() { return Start; }

    /**
     * Returns a String of a Formatted DateTime for when the Appointment ends.
     * @return the end time
     */
    public String getEnd() { return End; }

    /**
     * Returns the customer id.
     * @return id integer value.
     */
    public int getCustomerID() {
        return Customer_ID;
    }

    /**
     * Returns the user id.
     * @return id integer value.
     */
    public int getUserID() {
        return User_ID;
    }

    /**
     * Returns the contact id.
     * @return id integer value.
     */
    public int getContactID() {
        return Contact_ID;
    }
}
