package elec5620.sydney.edu.au.smarthealth;
import java.util.Date;
/*
Represents a task with title and time
 */
public class Task implements Comparable<Task>{
    private String userEmail;
    private String title;
    private Date dueDate;

    public Task(String title, Date dueDate, String userEmail) {
        this.title = title;
        this.dueDate = dueDate;
        this.userEmail=userEmail;
    }
    /*
    return task's title
     */
    public String getTitle() {
        return this.title;
    }
    /*
    set task's title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /*
    return task's date
     */
    public Date getDueDate() {
        return this.dueDate;
    }
    /*
    set task's date
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getUserEmail() {
        return this.userEmail;
    }
    /*
    set task's date
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    /*
    override the compareTo method so when sort an arraylist<Task>,
    the sorting will based on the date
     */
    @Override
    public int compareTo(Task task) {
        return getDueDate().compareTo(task.dueDate);
    }
}
