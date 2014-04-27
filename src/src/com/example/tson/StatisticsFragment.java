package com.example.tson;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tson_utilities.TimeBlock;
import tson_utilities.User;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatisticsFragment extends Fragment{
	
	 /***********************
	  *  	VARIABLES		*/	
	 /***********************/
	
	ImageButton btnStart, btnEnd;
	Button btnGo;
	Calendar startDate;
	Calendar endDate;
	int i;
	int j = 0;
	int totalMinutes;
    int updateWidth = 0;
    int k = 0;
    
    int projectHours = 0;
    
    DecimalFormat decimalFormat=new DecimalFormat("#0.#");
    
	EditText startTime, endTime;
	ListView projectListView;
	ArrayAdapter<Project> statsAdapter;
	
	public static User user = HomeActivity.user;
	List<Project> projectListStats = user.getProjects();
	double[] projectMinutes = new double[projectListStats.size()];
	
	 /***********************
	  *  	OTHERS			*/	
	 /************************/
	
	public StatisticsFragment(){}

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	         Bundle savedInstanceState) {
		 
		  super.onCreate(savedInstanceState);
		  View statistics = inflater.inflate(R.layout.statistics_fragment, container, false);
		  startDate = Calendar.getInstance();
		  endDate = Calendar.getInstance();
		  btnStart = (ImageButton) statistics.findViewById(R.id.imageButtonStart);
		  btnEnd = (ImageButton) statistics.findViewById(R.id.imageButtonEnd);
		  btnGo = (Button) statistics.findViewById(R.id.goStatistics);
		  startTime = (EditText) statistics.findViewById(R.id.startTime);
		  endTime = (EditText) statistics.findViewById(R.id.endTime);
		  projectListView = (ListView) statistics.findViewById(R.id.statistics_view);

		  //onClick on btnStart
		  btnStart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					i = 0;
					showDateDialog(v, startDate);					
				}
		  });		
		  
		  //onClick on btnEnd
		  btnEnd.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					i = 1;
					showDateDialog(v, endDate);					
				}
		  });	
		  
		  //onClick on btnGo
		  btnGo.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					long daysSinceStartDate = -(Calendar.getInstance().getTimeInMillis() - startDate.getTimeInMillis())/(1000*60*60*24);
					long daysSinceEndDate = -(Calendar.getInstance().getTimeInMillis() - endDate.getTimeInMillis())/(1000*60*60*24);
					Log.d("StartDifference", ""+daysSinceStartDate);
					Log.d("EndDifference", ""+daysSinceEndDate);
				}
		  });
		  
		  
		  
		  return statistics;
		 }
		
	 	/**
	 	 * When u click on btnStart or btnEnd u will jump in here and create a DatePickerDialog
	 	 * @param v -- Send in the View
	 	 * @param theYear -- Get what year you have
	 	 * @param theMonth -- Get what month you have
	 	 * @param theDay -- Get what day you have
	 	 */
	 	public void showDateDialog(View v, Calendar theCalendar)
	    {
	    	//Visar dialogrutan med datum
	 		if(i==0)
	 		{
	 			DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,theCalendar.get(Calendar.YEAR), theCalendar.get(Calendar.MONTH), theCalendar.get(Calendar.DAY_OF_MONTH));
	 			dialog.getDatePicker().setMaxDate(endDate.getTimeInMillis());
	 			dialog.show();
	 		}
	 		else
	 		{
	 			DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,theCalendar.get(Calendar.YEAR), theCalendar.get(Calendar.MONTH), theCalendar.get(Calendar.DAY_OF_MONTH));
		    	dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
		    	dialog.getDatePicker().setMinDate(startDate.getTimeInMillis());
		    	dialog.show();
	 		}
	    	
	    }
	    
	 	/**
	 	 * When u click Done in the dialog it will save it in the user and print the time out 
	 	 */
	 	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() 
	 	{
	 		public void onDateSet(DatePicker view, int selectedYear,
		    int selectedMonth, int selectedDay) 
	 		{
	 			
	 			Calendar temp = Calendar.getInstance();
	 			
	 			temp.set(selectedYear, selectedMonth, selectedDay);
	 			
	 			//Sets and shows the chosen date 	 			
				if(i==0)
				{	
					totalMinutes = 0;
					for(int i=0;i<projectListStats.size();i++){
						List<TimeBlock> tb = projectListStats.get(i).getSubmissionList();
						  for(int j=0;j < tb.size(); j++)
						  {
							  Calendar compareDate = tb.get(j).getDate();
							  // 0 if equals, -1 if the time of this calendar is before the other one,
							  // 1 if the time of this calendar is after the other one.
							  if(temp.compareTo(compareDate) == -1 && endDate.compareTo(compareDate) == 1)
							  {
								  //Log.d("TEMP", ""+temp.getTime());
								  //Log.d("COMPAREDATE", ""+compareDate.getTime());
								  //Log.d("ENDDATE:",""+endDate.getTime());
								  //Log.d("--------", "-----------");
								  Log.d("IF","IF");
								  totalMinutes = totalMinutes + tb.get(j).getTimeInMinutes();
								  projectMinutes[i] += tb.get(j).getTimeInMinutes();
								  Log.d("------------","-----------");
								  
							  }
							  else if(compareDate == temp || compareDate == endDate)
							  {
								  Log.d("ELSE","ELSE");
								  totalMinutes = totalMinutes + tb.get(j).getTimeInMinutes();
								  projectMinutes[i] += tb.get(j).getTimeInMinutes();
							  }
							  
						  }
					}
					startTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
							  			+ selectedYear);
					startDate = (Calendar) temp.clone();
					
					statsAdapter = new statsAdapter();
				    projectListView.setAdapter(statsAdapter);
					
					
				}
				else
				{
					totalMinutes = 0;
					for(int i=0;i<projectListStats.size();i++){
						List<TimeBlock> tb = projectListStats.get(i).getSubmissionList();
						  for(int j=0;j < tb.size(); j++)
						  {
							  //gets the day from each timeblock for each project.
							  Calendar compareDate = tb.get(j).getDate();
							  
							  if(startDate.compareTo(compareDate) == -1 && temp.compareTo(compareDate) == 1)
							  {
								  totalMinutes = totalMinutes + tb.get(j).getTimeInMinutes();
								  
								  projectMinutes[i] += tb.get(j).getTimeInMinutes();
								  
							  }
							  
							  else if(startDate.compareTo(compareDate) == 1 && temp.compareTo(compareDate) == -1)
							  {
								  totalMinutes = totalMinutes + tb.get(j).getTimeInMinutes();
								  projectMinutes[i] += tb.get(j).getTimeInMinutes();
							  }
							  
						  }
					}					
					endTime.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
							  + selectedYear);
					endDate = (Calendar) temp.clone();
					
					statsAdapter = new statsAdapter();
				    projectListView.setAdapter(statsAdapter);
				}
	 		}
		 };
		 
		 public static int dpToPx(int dp)
		    {
		        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
		    }
		    
		    private class statsAdapter extends ArrayAdapter<Project>{
		    	
		    	
				public statsAdapter() {
					super(getActivity(), R.layout.statistics_listview_item, projectListStats);
					// TODO Auto-generated constructor stub
				}
				
				@Override
				public View getView(int position, View view, ViewGroup parent){
					if(view == null)
						view = getActivity().getLayoutInflater().inflate(R.layout.statistics_listview_item, parent, false);
				
			        TextView progBar = (TextView) view.findViewById(R.id.statistics_progress_bar);
			        TextView projectName = (TextView) view.findViewById(R.id.textProject);
			        TextView percentValue = (TextView) view.findViewById(R.id.percantValue);
			        TextView hourValue = (TextView) view.findViewById(R.id.hourValue);
			        TextView minuteValue = (TextView) view.findViewById(R.id.minuteValue);
			        projectName.setText(user.getProjects().get(position).getName());
			        hourValue.setText(""+(int) projectMinutes[position]/60 + " h");
			        minuteValue.setText(""+(int) Math.round(projectMinutes[position]%60) + " m");
			        
			        double widthHolder = parent.getWidth();
			        Log.d("widthHolder", ""+widthHolder);
			        double percent = 0;
			        updateWidth = 0;
			        if(totalMinutes != 0){
			        	percent = projectMinutes[position]/totalMinutes;
			        }
			        Log.d("projectMinutes", ""+projectMinutes[position]);
			        projectMinutes[position] = 0; //resets the amount of project minutes
			        updateWidth = (int)(percent*widthHolder);//sets the width of percent bar
			        
			        percentValue.setText(decimalFormat.format(percent*100) + " %");
			        
			        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(updateWidth, dpToPx(40));
			        rl.setMargins(0,0,0,dpToPx(5));
			    
			        progBar.setLayoutParams(rl);
			        
			        return view;
					
				}
		    }

}
