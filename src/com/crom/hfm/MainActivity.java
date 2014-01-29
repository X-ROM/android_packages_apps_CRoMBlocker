/*
 * Copyright (C) 2013 MazWoz Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crom.hfm;

import java.util.ArrayList;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the ViewPager with the sections adapter.
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView (mViewPager);
		ActionBar actionbar = getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);
        mTabsAdapter = new TabsAdapter(this, mViewPager);
        mTabsAdapter.addTab(actionbar.newTab().setText(R.string.title_section1),mainFragment.class, null);
        //mTabsAdapter.addTab(actionbar.newTab().setText(R.string.title_section2),customFragment.class, null);
        
	}

	public static class TabsAdapter extends FragmentPagerAdapter
    implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    	@SuppressWarnings("unused")
		private static final String TAG = "Hosts File Manager";
    	private final Context mContext;
    	private final ActionBar mActionBar;
    	private final ViewPager mViewPager;
    	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

    	static final class TabInfo {
    		private final Class<?> clss;
    		private final Bundle args;

    		TabInfo(Class<?> _class, Bundle _args) {
    			clss = _class;
    			args = _args;
    		}
    	}

    	public TabsAdapter(FragmentActivity activity, ViewPager pager) {
    		super(activity.getSupportFragmentManager());
    		mContext = activity;
    		mActionBar = activity.getActionBar();
    		mViewPager = pager;
    		mViewPager.setAdapter(this);
    		mViewPager.setOnPageChangeListener(this);
    	}

    	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
    		TabInfo info = new TabInfo(clss, args);
    		tab.setTag(info);
    		tab.setTabListener(this);
    		mTabs.add(info);
    		mActionBar.addTab(tab);
    		notifyDataSetChanged();
    	}


    	public int getCount() {
    		return mTabs.size();
    	}

    	public Fragment getItem(int position) {
    		TabInfo info = mTabs.get(position);
    		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    	}


    	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    	}


    	public void onPageSelected(int position) {
    		mActionBar.setSelectedNavigationItem(position);
    	}


    	public void onPageScrollStateChanged(int state) {
    	}


    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
    		mViewPager.setCurrentItem(tab.getPosition());
    		Object tag = tab.getTag();
    		for (int i=0; i<mTabs.size(); i++) {
    			if (mTabs.get(i) == tag) {
    				mViewPager.setCurrentItem(i);
    			}
    		}
    	}

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {


		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {


		}
    }
}
