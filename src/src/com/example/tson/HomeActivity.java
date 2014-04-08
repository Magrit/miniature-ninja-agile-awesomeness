package com.example.tson;

//IMPORT OUR OWN CLASSES
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;
import android.R.array;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
//IMPORT ANDROID
import android.widget.TimePicker;

//IMPORT OTHER

public class HomeActivity extends Activity 
{
	int hour,min, newHour, newMin;
	int holder = 0;
	static final int TIME_DIALOG_ID=0;
	String[] hourmin;
	View currentPage;
	ListView projectListView;

	
	public static User user = new User("sdf@sdf.com", "Bosse", "b1337");
	List<Project> projectList = user.getProjects();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        
        setContentView(R.layout.activity_home);
        user.getProjects().get(0).addTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 1, 30);
        final LinearLayout rl=(LinearLayout) findViewById(R.id.rl);
        final TextView[] tv=new TextView[10];
        
        projectListView = (ListView) findViewById(R.id.projectListView);
        
        
        List<String> projectStrings = new ArrayList<String>();
        
        for(int i = 0; i < projectList.size(); i++)
        {
        	projectStrings.add(projectList.get(i).getName());
        }
        
        //ArrayAdapter<String> arrayAdapter = new Project<String>(this, android.R.layout.simple_list_item_1, projectStrings);
        
        ArrayAdapter<Project> projectAdapter = new ProjectListAdapter();
        
        projectListView.setAdapter(projectAdapter);
        
    }

    //Creating the dialog for the specific time
   	public void showTimeDialog(View v)
    {
   		//calculates what page and position we are at
   		holder = projectListView.getPositionForView(v);
   		currentPage = (View) v.getParent();
       	
       	//Calculate what hour and minute that we are at when we click
   		if((user.getProjects().get(holder).getTimeByDate(Calendar.getInstance())) != null){
	       	hourmin = (user.getProjects().get(holder).getTimeByDate(Calendar.getInstance())).getTimeAsString().split(" h : ");
	       	hourmin[1] = hourmin[1].replaceAll("m", "");
	       	hourmin[1] = hourmin[1].replaceAll(" ", "");
   		}
   		else
   		{

       		hourmin[0] = "0";
       		hourmin[1] = "0";
       	}
       	newHour = Integer.parseInt(hourmin[0]);
       	newMin = Integer.parseInt(hourmin[1]);
       	
       	//Calls the onCreateDialog
       	showDialog(holder);
    }
   	
   	//Creates the Dialog with the right time from which click
       protected Dialog onCreateDialog(int id)
       {
       	return new TimePickerDialog(this, timeSetListener, newHour, newMin, true);	
       }
       
       //When u click Done in the dialog it will save it in the user and print the time out
    private TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() 
    {
   		@Override
   		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
   		{
   			hour=hourOfDay;
   			min=minute;
   			Calendar c = Calendar.getInstance();
   			
   			user.getProjects().get(holder).addTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hour, min);
   			
   			TextView et=(TextView) currentPage.findViewById(R.id.projectTimeTextView);
   			et.setText(hour+ " h : "+min + " m");
   		}
   	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    /**
     * Opens the Create Project View when the "Create Project" button is clicked
     * @param view - View for Create Project Screen
     */
    public void openCreateProjectActivity(View view)
    {    	
    	Intent intent = new Intent(this, CreateProjectActivity.class);
    	startActivity(intent);
    	
    }
    
    private class ProjectListAdapter extends ArrayAdapter<Project>
    {
    	public ProjectListAdapter()
    	{
    		super(HomeActivity.this, R.layout.project_listview_item, projectList);
    	}
    	
    	@Override
    	public View getView(int position, View view, ViewGroup parent)
    	{
    		if(view == null)
    			view = getLayoutInflater().inflate(R.layout.project_listview_item, parent, false);
    		
    		Project currentProject = projectList.get(position);
    		
    		TextView projectName = (TextView) view.findViewById(R.id.projectNameTextView);
    		
    		projectName.setText(currentProject.getName());
    		
    		TextView projectTime = (TextView) view.findViewById(R.id.projectTimeTextView);
    		if((currentProject.getTimeByDate(Calendar.getInstance())) != null)
    			projectTime.setText((currentProject.getTimeByDate(Calendar.getInstance())).getTimeAsString());
    		
    		
    		Button editButton = (Button) view.findViewById(R.id.editTimeButton);		
    		
    		return view;
    	}
    }
   
    
}
