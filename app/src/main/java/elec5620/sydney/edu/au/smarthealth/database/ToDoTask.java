package elec5620.sydney.edu.au.smarthealth.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
Represents a task entity.
A task entity has ID and Gson serialized content(including title and time)
 */

@Entity (tableName = "taskList")
public class ToDoTask {
    @PrimaryKey (autoGenerate = true)
    @NonNull

    @ColumnInfo (name="toDoTaskID")
    private int toDoTaskID;
    /*
    Constructor for ToDoTask
     */
    public ToDoTask (String toDoTaskContent) { this.toDoTaskContent = toDoTaskContent;}



    /*
    Repsresnet serialized task
     */
    @ColumnInfo (name="toDoTaskContent")
    private String toDoTaskContent;
    /*
    return the serialized task
     */
    public String getToDoTaskContent(){return toDoTaskContent;}
    /*
    set the serialized task
     */
    public void setToDoTaskContent(String content) {this.toDoTaskContent = content;}
    /*
    return ToDoTaskID
     */
    public int getToDoTaskID() { return toDoTaskID; }
    /*
    set ToDoTaskID
     */
    public void setToDoTaskID(int toDoTaskID) { this.toDoTaskID = toDoTaskID; }


}
