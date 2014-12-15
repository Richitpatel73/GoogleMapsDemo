package com.example.mapsdemo;

import java.util.ArrayList;

import model.NavDrawerItem;
import adapter.NavDrawerListAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	GoogleMap map;
	Marker marker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps_layout);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.maps_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		/*
		 * if (savedInstanceState == null) { // on first time display view for
		 * first nav item displayView(0); }
		 */

		final Context context = this.getBaseContext();
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		Toast.makeText(this, ":) My maps started", 2000).show();

		if (resultCode == 0) {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			map.setMyLocationEnabled(true);

			LocationManager lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			Criteria c = new Criteria();
			String provider = lm.getBestProvider(c, true);
			Location l = lm.getLastKnownLocation(provider);
			double latitude = 0;
			double longitude = 0;
			if (l != null) {
				latitude = l.getLatitude();
				longitude = l.getLongitude();
			}
			LatLng now = new LatLng(latitude, longitude);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(now, 5));
			map.addMarker(new MarkerOptions().position(now).title(
					"current position"));

			LatLng Bangaluru = new LatLng(12.9667, 77.5667);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Bangaluru, 5));
			marker = map.addMarker(new MarkerOptions().title("Bangaluru")
					.snippet("Silicon Valley of India.").position(Bangaluru));

		} else {
			Toast.makeText(this, "Sorry Play services Not Available",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * Slide menu item click listener
	 * */
	public class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent ContactMe = new Intent(getBaseContext(), ContactMe.class);
			startActivity(ContactMe);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	public void displayView(int position) {
		// TODO Auto-generated method stub

		switch (position) {
		case 0:
			if (marker != null) {
				marker.remove();
			}

			LatLng Mumbai = new LatLng(18.975, 72.858);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Mumbai, 5));
			marker = map.addMarker(new MarkerOptions().title("Mumbai")
					.snippet("Dream city of India.").position(Mumbai));
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 1:
			if (marker != null) {
				marker.remove();
			}
			LatLng Delhi = new LatLng(28.6100, 77.2300);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Delhi, 5));
			marker = map.addMarker(new MarkerOptions().title("Delhi")
					.snippet("Central Govt. of India.").position(Delhi));
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 2:
			if (marker != null) {
				marker.remove();
			}
			LatLng kolkata = new LatLng(22.5667, 88.3667);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(kolkata, 5));
			marker = map.addMarker(new MarkerOptions().title("kolkata")
					.snippet("Most crowded city of India.").position(kolkata));
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 3:
			if (marker != null) {
				marker.remove();
			}
			LatLng chennai = new LatLng(13.0839, 80.250);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(chennai, 5));
			marker = map.addMarker(new MarkerOptions().title("chennai")
					.snippet("One of the most beautiful city of India.")
					.position(chennai));
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		default:
			break;
		}

	}

}
