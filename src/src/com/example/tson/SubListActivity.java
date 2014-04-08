package com.example.tson;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import android.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SubListActivity extends Activity {
	
	List<Calendar> calList = new ArrayList<Calendar>();
	ListView submissionListView;
	List<Project> projectList = HomeActivity.user.getProjects();
	Calendar today = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_list);
		
		
		submissionListView = (ListView) findViewById(R.id.submittedDaysListView);
		
		for(int i=0;i<8;i++)
		{
			today.add(Calendar.DAY_OF_YEAR, -1);
			calList.add(today);
		}
		ArrayAdapter<Calendar> subAdapter = new SubmissionListAdapter();
		submissionListView.setAdapter(subAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sub_list, menu);
		return true;
	}
	
	
	
	private class SubmissionListAdapter extends ArrayAdapter<Calendar>
    {
		
		public SubmissionListAdapter()
    	{
    		super(SubListActivity.this, R.layout.submissionlist_day_item, calList);
    	}
    	
    	@Override
    	public View getView(int position, View view, ViewGroup parent)
    	{
    		if(view == null)
    			view = getLayoutInflater().inflate(R.layout.project_listview_item, parent, false);
    		
    		Calendar currentDate = calList.get(position);
    		
    		TextView submissionDate = (TextView) view.findViewById(R.id.submissionDate);
    		TextView projectTime = (TextView) view.findViewById(R.id.workTime);
    		Button editButton = (Button) view.findViewById(R.id.editDayButton);	
    		
    		if(position == 0 || currentDate.get(Calendar.DAY_OF_WEEK)==7)
    		{
    			TextView weekText = (TextView) view.findViewById(R.id.weekText);
    			weekText.setText(currentDate.get(Calendar.DAY_OF_MONTH)+"/"+(currentDate.get(Calendar.MONTH)+1));
    			
    			RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams)submissionDate.getLayoutParams();
    			params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    			params.addRule(RelativeLayout.BELOW, R.id.weekText);
    			
    			RelativeLayout.LayoutParams paramsButton =  (RelativeLayout.LayoutParams)editButton.getLayoutParams();
    			paramsButton.addRule(RelativeLayout.BELOW, R.id.weekText);
    		}
    		
    		
    		submissionDate.setText(currentDate.get(Calendar.DAY_OF_MONTH)+"/"+(currentDate.get(Calendar.MONTH)+1));
    		
    		
    		projectTime.setText(HomeActivity.user.getTimeByDate(currentDate)/60 + ":" + +HomeActivity.user.getTimeByDate(currentDate)%60);
    		
    		
    		return view;
    	}
    }

}