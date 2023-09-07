package schedual.model;

import java.time.Month;

/**
 * MonthTypeCount Class Object provides a count of appointments for a given combination of Month and Type.
 * @author Ben Porter
 */


public class MonthTypeCount {

    private Month Month;
    private String Type;
    private int Count;

    /** Default Constructor for MonthTypeCount
     * @param Month Enum Month.
     * @param Type String Type value for the Object.
     * @param Count int count of appointments for the given Month and Type.
     * */
    public MonthTypeCount(Month Month, String Type, int Count) {
        this.Month = Month;
        this.Type = Type;
        this.Count = Count;
    }

    /**
     * Returns the Java.time Enum of the given month.
     * @return String Month
     */
    public Month getMonth() {
        return Month;
    }

    /**
     * Sets the Java.time Enum of the given month.
     * @param Month Enum Month Object Type
     */
    public void setMonth(Month Month) {
        this.Month = Month;
    }

    /**
     * Returns the String Category of the Appointment Type.
     * @return String Type for the Appointment.
     */

    public String getType() { return Type; }

    /**
     * Sets the String Category of the Appointment Type.
     * @param Type String Appointment Type.
     */

    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * Returns the int Count for the number of MonthType occurences for this combination.
     * @return int Count
     */
    public int getCount() { return Count; }

    /**
     * Gets the int Count for the number of MonthType occurences for this combination.
     * @param Count int Count
     */
    public void setCount(int Count) {
        this.Count = Count;
    }

}
