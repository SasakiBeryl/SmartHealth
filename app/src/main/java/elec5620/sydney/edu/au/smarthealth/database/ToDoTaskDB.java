package elec5620.sydney.edu.au.smarthealth.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/*
Represents database
 */
@Database(entities = {ToDoTask.class}, version = 1, exportSchema = false)
public abstract class ToDoTaskDB extends RoomDatabase{
    private static final String DATABASE_NAME = "todotask_db";
    private static ToDoTaskDB DBINSTANCE;

    public abstract ToDoTaskDao toDoTaskDao();

    public static ToDoTaskDB getDatabase(Context context){
        if (DBINSTANCE == null) {
            synchronized (ToDoTaskDB.class) {
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoTaskDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }

    public static void destroyInstance() {DBINSTANCE = null;}
}
