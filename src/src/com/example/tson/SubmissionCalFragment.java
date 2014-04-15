package com.example.tson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tson_utilities.Project;
import tyczj.extendedcalendarview.Day;
import tyczj.extendedcalendarview.ExtendedCalendarView;
import tyczj.extendedcalendarview.ExtendedCalendarView.OnDayClickListener;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SubmissionCalFragment extends Fragment {

	List<Calendar> calList = new ArrayList<Calendar>();
	ListView submissionCalendarView;
	List<Project> projectList = HomeActivity.user.getProjects();
	Calendar today = Calendar.getInstance();
	ExtendedCalendarView cal;
		
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View subCalView = inflater.inflate(R.layout.submission_cal_fragment, container, false);
        
        cal = (ExtendedCalendarView) subCalView.findViewById(R.id.calendar);
		
		cal.setOnDayClickListener(new OnDayClickListener(){

			@Override
			public void onDayClicked(AdapterView<?> adapter, View view,
					int position, long id, Day day) {
				Calendar tempCal = Calendar.getInstance();
				tempCal.set(day.getYear(), day.getMonth(), day.getDay());
				int dateDifference = -(today.get(Calendar.DAY_OF_YEAR) - tempCal.get(Calendar.DAY_OF_YEAR));	
				
				Fragment switchToFragment = new HomeFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("dateDifference", dateDifference);
				switchToFragment.setArguments(bundle);
				
				ActionBar actionBar = getActivity().getActionBar();
				actionBar.removeAllTabs();
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				getActivity().setTitle("Home");					
				
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				fragmentManager.beginTransaction()
				.replace(R.id.frame_container, switchToFragment).commit();
			}
			
		});
        
        //((TextView)subCalView.findViewById(R.id.textView)).setText("Calendar");
        return subCalView;
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.sub_list, menu);
		return true;
	}

}
