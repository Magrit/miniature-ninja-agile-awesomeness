package com.example.tson;

import java.util.ArrayList;



import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tson_utilities.User;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
/**
 * 
 * This class is the fragment for the SubmissionLISTpage
 * Contains subClasses:
 * {@link SubmissionListAdapter}
 * {@link SubmissionDayListItem}
 * @author 
 *
 */
public class SubmissionListFragment extends Fragment {
	List<SubmissionDayListItem> subList = new ArrayList<SubmissionDayListItem>();
	ListView submissionListView;
	Calendar today = (Calendar) HomeActivity.getCal().clone();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View subListView = inflater.inflate(R.layout.submission_list, container, false);
		
		submissionListView = (ListView) subListView.findViewById(R.id.submittedDaysListView);
		
		for(int i=0;i<30;i++)
		{
			subList.add(new SubmissionDayListItem(today,User.getInstance().getTimeByDate(today), User.getInstance().isDateConfirmed(today)));
			today.add(Calendar.DAY_OF_YEAR, -1);
		}
		
		ArrayAdapter<SubmissionDayListItem> subAdapter = new SubmissionListAdapter();
		
		submissionListView.setAdapter(subAdapter);
		return subListView;
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.sub_list, menu);
		return true;
	}
	
	/**
	 * Class for adding SubmissionListItems. This takes a list of {@link SubmissionDayListItem} to read from
	 * and write them out with functionalities such as onClick.
	 *
	 */
	private class SubmissionListAdapter extends ArrayAdapter<SubmissionDayListItem>
    {
		/**
		 * Constructor to call.
		 */
		public SubmissionListAdapter()
    	{
    		super(getActivity(), R.layout.submissionlist_day_item, subList);
    		
    	}
    	
		/**
		 * This gets called at scrolling by default. Will print out the list and weeknumbers.
		 * Will also check if the {@link SubmissionDayListItem} is confirmed.
		 */
		@Override
    	public View getView(int position, View view, ViewGroup parent)
    	{
    		
    		if(view == null)
    			view = getActivity().getLayoutInflater().inflate(R.layout.submissionlist_day_item, parent, false);
    		final SubmissionDayListItem currentItem = subList.get(position);
    		TextView submissionDate = (TextView) view.findViewById(R.id.submissionDate);
    		TextView projectTime = (TextView) view.findViewById(R.id.workTime);
    		View orangeBelow = (View) view.findViewById(R.id.belowWeek);
    		View orangeAbove = (View) view.findViewById(R.id.aboveWeek);
    		TextView weekText = (TextView) view.findViewById(R.id.weekText);
    		ImageButton editButton = (ImageButton) view.findViewById(R.id.editDayButton);
    		
    		/**
    		 * Will check if this is the first SubMissionListItem or a SUNDAY
    		 * IF: 		Will write the week number.
    		 * ELSE: 	Default resetter of the TextView.
    		 */
    		if(position == 0 || currentItem.today.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
    		{
    			orangeAbove.setVisibility(View.VISIBLE);
    			weekText.setVisibility(TextView.VISIBLE);
    			weekText.setText("Week: "+currentItem.today.get(Calendar.WEEK_OF_YEAR));
    			orangeBelow.setVisibility(View.VISIBLE);
    		}
    		else
    		{
    			orangeAbove.setVisibility(View.GONE);
    			weekText.setVisibility(TextView.GONE);
    			weekText.setText("");
    			orangeBelow.setVisibility(View.GONE);
    		}
    		
    		submissionDate.setText(currentItem.today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH)+" "+currentItem.today.get(Calendar.DAY_OF_MONTH)+"/"+(currentItem.today.get(Calendar.MONTH)+1));
    		/**
    		 * Will check if the date is SATURDAY or SUNDAY
    		 * IF: Set text to orange-ish
    		 * ELSE: Black
    		 */
    		if(currentItem.today.get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY||currentItem.today.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
			{
				submissionDate.setTextColor(getResources().getColor(R.color.combitech_orange));
				projectTime.setTextColor(getResources().getColor(R.color.combitech_orange));
			}
    		else{
    			submissionDate.setTextColor(getResources().getColor(R.color.combitech_black));
    			projectTime.setTextColor(getResources().getColor(R.color.combitech_black));
    		}
    		projectTime.setText(currentItem.timeWorked/60 + " h : " +currentItem.timeWorked%60 + " m");
    		
    		projectTime.setOnClickListener(null);
    		/**
    		 * Will set the backgroundColor depending on confirmation of the SubListItem
    		 * IF: 		Green
    		 * ELSEIF: 	Yellow
    		 * ELSE:	Gray
    		 */
			if(currentItem.isConfirmed==1)
				view.setBackgroundColor(Color.rgb(145, 218, 149));
			else if(currentItem.isConfirmed==0)
				view.setBackgroundColor(Color.rgb(246, 241, 171)); 
   			else
				view.setBackgroundColor(Color.rgb(229, 229, 229)); 
			
			/**
			 * OnClickListener for edit Button
			 * Takes you to Homescreen for pressed DATE
			 */

    		editButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
								
					long dateDifference = -(Calendar.getInstance().getTimeInMillis() - currentItem.today.getTimeInMillis())/(1000*60*60*24);
					
					Fragment switchToFragment = new HomeFragment();
					Bundle bundle = new Bundle();
					bundle.putLong("dateDifference", dateDifference);
					bundle.putString("previousFragment", "Submission");
					switchToFragment.setArguments(bundle);
					
					ActionBar actionBar = getActivity().getActionBar();
					actionBar.removeAllTabs();
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					getActivity().setTitle("Report");					
					
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					fragmentManager.beginTransaction()
					.replace(R.id.frame_container, switchToFragment).commit();
				}
			});
    		
    		view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
								
					long dateDifference = -(Calendar.getInstance().getTimeInMillis() - currentItem.today.getTimeInMillis())/(1000*60*60*24);
					
					Fragment switchToFragment = new HomeFragment();
					Bundle bundle = new Bundle();
					bundle.putLong("dateDifference", dateDifference);
					bundle.putString("previousFragment", "Submission");
					switchToFragment.setArguments(bundle);
					
					ActionBar actionBar = getActivity().getActionBar();
					actionBar.removeAllTabs();
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					getActivity().setTitle("Report");					
					
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					fragmentManager.beginTransaction()
					.replace(R.id.frame_container, switchToFragment).commit();
				}
			});
    		
    		return view;
    	}
    }
	
	/**
	 * This class makes it easier to interact with the local variables, with only one call to read data in onCreate.
	 * @author mikpe201
	 *
	 */
	public class SubmissionDayListItem
	{
		Calendar today;
		int timeWorked;
		int isConfirmed;
		/**
		 * Constructor to read values into class
		 * @param c - Calendar of the date to be showed
		 * @param t - Time worked in minutes
		 * @param i - If the SubmissionItem is confirmed or not.
		 */
		public SubmissionDayListItem(Calendar c, int t, int i){
			today = (Calendar) c.clone();
			timeWorked=t;
			isConfirmed = i;
		}
	}

}
