package schedual;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;

    /** Default Constructor for Appointment
     * @param id Unique integer value associated to the appointment
     * @param title String value indicating the name of the appointment
     * @param description String describing the appointment in more detail
     * @param location String that shows the location where the appointment is set
     * @param type String category for the appointment
     * @param start LocalDateTime object providing the start time and date of the meeting
     * @param end LocalDateTime object providing the end time and date of the meeting
     * */
    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * Returns the given id.
     * @return id integer value.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id variable to integer provided.
     * @param  id integer value.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title variable to String provided.
     * @param  title String value.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() { return description; }

    /**
     * Sets the description variable to String provided.
     * @param  description String value.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
