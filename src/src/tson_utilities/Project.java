package tson_utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson.sqlite.helper.DatabaseHelper;
import android.util.Log;

import com.example.tson.HomeActivity;


/**
 * Project class is used as an object for each project created
 * by the user. User can add time, get time and get the name
 * of the project.
 * @author Mike
 *
 */
public class Project {
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	private String name;
	private List<TimeBlock> submissionList = new ArrayList<TimeBlock>();
	private long id;
	//Int, instead of boolean because of database. Flag for internal time can be 0 or 1
	private int isInternalTime = 0;
	private int isHidden = 0;  //1 for hidden, 0 for not hidden
	
	 /***********************
	  *  	CONSTRUCTORS 	*/	
	 /***********************/
	
	/**
	 * Constructor for Project Class, submissions are added
	 * at a later point.
	 * @param name Adds name to Project
	 */
	public Project(String name)
	{
		this.name = name;
	}
	 
	 /***********************
	  *  	SETTERS  		*/	
	 /***********************
	
	/**
	 * Adds a TimeBlock to a project
	 * @param d - date
	 * @param h - hours
	 * @param m - minutes
	 */
	public void addTime(Calendar c, int h, int m)
	{

		// Create temporary time block object, will only be added if the update fails (time object doesn't exist)
		TimeBlock t = new TimeBlock(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), h, m); 
		
		//Update in database, if returns > 0 it has updated existing row
		int update = HomeActivity.db.updateTimeBlock(t, this);
		
		if( update > 0)
		{
			Log.d("UPDATE TIME", "UPDATE TIME RETURNED: " + update);
			//UPDATE CURRENT TIME BLOCK OBJECT
			t = getTimeByDate(c);
			t.setHours(h);
			t.setMinutes(m);
			
			
		}
		else
		{
			Log.d("ADD TIME", "ADDING TIME");
			// CREATE NEW TIME BLOCK OBJECT AND ADD IT TO SUBMISSION LIST
			submissionList.add(t);
			HomeActivity.db.createTimeBlock(t, this);
			
		}
		
	}
	
	/**
	 * Setter for setting the flag isInternal
	 * @param isInternal
	 */
	public void setInternalTime(int isInternal)
	{
		isInternalTime = isInternal;
	}
	/**
	 * Setter for setting a flag for isHidden
	 * @param isHidden_
	 */
	public void setIsHidden(int isHidden_)
	{
		this.isHidden = isHidden_;
	}
	
	/**
	 * Set an id correspondent to its id in the database
	 * @param id_
	 */
	public void setId(long id_)
	{
		this.id = id_;
	}
	
	public void setSubmissionList(List<TimeBlock> list)
	{
		this.submissionList.addAll(list);
	}
		 
	 /***********************
	  *  	GETTTERS  		*/	
	 /************************/
	
	/**
	 * Getter of string
	 * @param d  date the time should correspond to
	 * @return  the TimeBlock of that date OR null if no TimeBlock exists.
	 */
	public TimeBlock getTimeByDate(Calendar cal)
	{
		
		if(!submissionList.isEmpty())		
			for(int i=submissionList.size()-1; i>=0 ;i--)
			{				
				if(this.submissionList.get(i).isDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)))
					return this.submissionList.get(i);
				
			}

		return null;
	}
	
	/**
	 * Getter
	 * @return  An int indicating if the time is internal time. 1 == the project is internal time
	 */
	public int getIsInternal()
	{
		return isInternalTime;
	}
	
	public int getIsHidden()
	{
		return this.isHidden;
	}
	
	/**
	 * Getter
	 * @return  a String of the name of the project.
	 */
	public String getName()
	{
		return name;
	}

	public List<TimeBlock> getSubmissionList()
	{
		return this.submissionList;
	}
	
	/**
	 * Get id of object
	 * @return <long> id
	 */
	public long getId()
	{
		return this.id;
	}
	
	//TODO
	public void editTime(Calendar cal,int h, int m)
	{
	 TimeBlock t = null;
	 if(!this.submissionList.isEmpty())			
			for(int i=this.submissionList.size()-1; i>=0 ;i--)
			{
				
				if(this.submissionList.get(i).isDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)))
					t = this.submissionList.get(i);
				
			}
		t.setDuration(h, m);
	}
	
	public void setName(String newName)
	{
		name = newName;
	}

}

