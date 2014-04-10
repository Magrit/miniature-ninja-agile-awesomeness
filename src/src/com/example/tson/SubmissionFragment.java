package com.example.tson;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SubmissionFragment extends Fragment {
	ViewPager Tab;
    TabAdapter TabAdapter;
	ActionBar actionBar;

	public SubmissionFragment(){}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        View submission = inflater.inflate(R.layout.submission_fragment, container, false);
        
        HomeActivity.user.getProjects().get(0).addTime(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, 1, 30);
        
        TabAdapter = new TabAdapter(getFragmentManager());
        
        Tab = (ViewPager)submission.findViewById(R.id.pager);
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                       
                    	//actionBar = getActivity().getActionBar();
                    	actionBar.setSelectedNavigationItem(position);                    }
                });
        Tab.setAdapter(TabAdapter);
        
        actionBar = getActivity().getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){

			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

			@Override
			 public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	          
	            Tab.setCurrentItem(tab.getPosition());
	        }

			@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}};
			//Add New Tab
			if(actionBar.getTabCount() <2)
			{
				actionBar.addTab(actionBar.newTab().setText("List").setTabListener(tabListener));
				actionBar.addTab(actionBar.newTab().setText("Calendar").setTabListener(tabListener));
			}
			
			return submission;
    }   
    
    
}
