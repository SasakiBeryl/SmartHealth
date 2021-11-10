package elec5620.sydney.edu.au.smarthealth.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ToDoTaskDao {
    @Query("SELECT * FROM taskList")
    List<ToDoTask> listAll();

    @Insert
    void insert(ToDoTask toDoTask);

    @Insert
    void insertAll (ToDoTask... toDoTasks);

    @Query("DELETE FROM taskList")
    void deleteAll();
}
