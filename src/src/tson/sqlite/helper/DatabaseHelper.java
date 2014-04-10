/**
 * @author P�r Eriksson
 * A database helper class that takes care of all interaction with the SQLite database
 */
package tson.sqlite.helper;



import java.util.ArrayList;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.TimeBlock;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

	//Logcat tag
	private static final String LOG = "DatabaseHelper";
	
	//Database Version
	private static final int DATABASE_VERSION = 1;
	
	//Database Name
	private static final String DATABASE_NAME = "timeManager";
	
	//Table Names
	private static final String TABLE_PROJECT = "projects";
	private static final String TABLE_TIME_BLOCK = "time_blocks";
	
	// Common column names
	private static final String KEY_ID = "id";
	
	
	//PROJECTS Table
	private static final String KEY_PROJECT_NAME = "project_name";
	
	//TIME BLOCKS TABLE
	private static final String KEY_TIME_BLOCK_PROJECT_ID = "project_id";
	private static final String KEY_TIME_BLOCK_DATE = "date";
	private static final String KEY_TIME_BLOCK_MINUTES = "minutes";
	
	
	// Table Create Statements
	// Project table create statement
	private static final String CREATE_TABLE_PROJECT = "CREATE TABLE "
			+ TABLE_PROJECT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PROJECT_NAME
			+ " TEXT" + ")";
	
	private static final String CREATE_TABLE_TIME_BLOCK = "CREATE TABLE "
			+ TABLE_TIME_BLOCK + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME_BLOCK_PROJECT_ID + " INTEGER,"  + KEY_TIME_BLOCK_MINUTES
			+ " INTEGER" + ")";
	
	
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PROJECT);
		//IF MORE TABLES: OTHER TABLES WILL ALSO BE EXECUTED		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
		// IF MORE TABLES: OTHER TABLES WILL ALSO BE EXECUTED		
	
		//Create new tables
		onCreate(db);
	}
	
	
	//========================================================
	// PROJECT ===============================================
	//========================================================
	/**
	 * Creating a project
	 * @param project - the projec object
	 * @param tag_ids - the tags, e.g. name
	 * @return - returns the id of the row
	 */
	public long createProject(Project project)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_NAME, project.getName());
		
		//insert row
		long project_id = db.insert(TABLE_PROJECT, null, values);
		
		//assigning tags to project
	
		return project_id;
		
	}
	
	/**
	 * Get a single project by an ID
	 * @param project_id
	 * @return A new project with the key name
	 */
	public Project getProject(long project_id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_PROJECT + " WHERE "
				+ KEY_ID + " = " + project_id;
		Log.e(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
				
		if(c != null)
			c.moveToFirst();
		
		//Set all attributes before returning it
		// Create a project out of the row name
		Project p = new Project(c.getString(c.getColumnIndex(KEY_PROJECT_NAME)));
		
		return p;
	}
	/**
	 * Select all projects
	 * @return List<Project> 
	 */
	public List<Project> getAllProjects()
	{
		List<Project> projects = new ArrayList<Project>();
		String selectQuery = "SELECT * FROM " + TABLE_PROJECT;
		
		Log.e(LOG, selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		//Loop through all rows in the table and add to the list
		if(c.moveToFirst())
		{
			do
			{
				Project p = getProject(c.getInt(c.getColumnIndex(KEY_ID)));
				
				// Add to the projects list that will be returned
				projects.add(p);
			}while (c.moveToNext());
		}
		return projects;
	}
	
	public long getProjectId(String project_name){
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT "+ KEY_ID + " FROM " + TABLE_PROJECT + " WHERE " + KEY_PROJECT_NAME + " = " + project_name;
		Log.e(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
		Log.e(LOG, c.toString());
		if(c != null)
			c.moveToFirst();
		return c.getLong(0);
	}
	
	public void deleteProject(long project_id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROJECT, KEY_ID + " = ?",
				new String[] {String.valueOf(project_id) });
	}
	
	
	//========================================================
	// TIME BLOCK ============================================
	//========================================================
	
	public long createTimeBlock(TimeBlock timeblock, Project project)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TIME_BLOCK_PROJECT_ID, getProjectId(project.getName()));
		values.put(KEY_TIME_BLOCK_MINUTES, timeblock.getTimeInMinutes());
		
		//insert row
		long timeblock_id = db.insert(TABLE_TIME_BLOCK, null, values);
		
	
		return timeblock_id;
		
	}
	
	
	
	//Close database
	public void closeDB()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		if(db!= null && db.isOpen())
			db.close();
	}
	
	
	
	

}
