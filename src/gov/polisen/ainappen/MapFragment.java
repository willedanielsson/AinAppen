package gov.polisen.ainappen;

import java.util.ArrayList;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MapFragment extends Activity implements LocationListener {

	String PROVIDER = LocationManager.GPS_PROVIDER;

	MapView mapView;
	MapController mapController;
	LocationManager locationManager;
	ArrayList<OverlayItem> overlayItemArray;
	MenuItem addObstacleMenuItem;
	Boolean addObstacleEnabled = false;

	MyLocationNewOverlay myLocationOverlay = null;

	OnItemGestureListener<OverlayItem> myOnItemGestureListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_map);

		enableMap();

		myLocationOverlay = new MyLocationNewOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();
		mapView.getOverlays().add(myLocationOverlay);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = locationManager.getLastKnownLocation(PROVIDER);
		if (location != null) {
			GeoPoint currentLocation = new GeoPoint(location.getLatitude(),
					location.getLongitude());

			mapController.setCenter(currentLocation);
		} else {
			GeoPoint startPoint = new GeoPoint(58.4109, 15.6216);
			mapController.setCenter(startPoint);
		}

		// Create an ArrayList with overlays to display objects on map
		overlayItemArray = new ArrayList<OverlayItem>();

		OverlayItem linkopingItem = new OverlayItem("Sten", "Blockerar Rydsvägen",
				new GeoPoint(58.4109, 15.6216));
		overlayItemArray.add(linkopingItem);

		OverlayItem rydItem = new OverlayItem("Träd", "Blockerar din mamma",
				new GeoPoint(58.407782, 15.5566854));
		overlayItemArray.add(rydItem);

		MapEventsReceiver mReceive = new MapEventsReceiver() {

			@Override
			public boolean longPressHelper(IGeoPoint arg0) {
				Log.w("AinAppen", "Tryckte låånngt");
				return false;
			}

			@Override
			public boolean singleTapUpHelper(IGeoPoint arg0) {
				Log.w("AinAppen", "Tryckte kårt");
				if (addObstacleEnabled == true) {
					GeoPoint touchedLocation = (GeoPoint) arg0;

					Log.w("AinAppen", "Vår tryckta location är " + touchedLocation);
					Toast.makeText(getApplicationContext(),
							"Location är " + touchedLocation, Toast.LENGTH_LONG).show();

					OverlayItem touchedItem = new OverlayItem("Test", "TestText",
							touchedLocation);

					Log.w("AinAppen", "Item: " + touchedItem);

				}
				return false;
			}

		};

		MapEventsOverlay OverlayEventos = new MapEventsOverlay(getBaseContext(),
				mReceive);
		mapView.getOverlays().add(OverlayEventos);

		// Add the Array to the IconOverlay
		ItemizedIconOverlay<OverlayItem> itemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(
				this, overlayItemArray,
				new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {

					@Override
					public boolean onItemLongPress(int index, OverlayItem item) {
						Toast.makeText(
								getApplicationContext(),
								"Overlay Titled: " + item.getTitle() + " Long Pressed" + "\n"
										+ "Description: " + item.getSnippet(), Toast.LENGTH_LONG)
								.show();
						return false;
					}

					@Override
					public boolean onItemSingleTapUp(int index, OverlayItem item) {
						Toast.makeText(getApplicationContext(),
								item.getTitle() + "\n" + item.getSnippet(), Toast.LENGTH_LONG)
								.show();
						return true;
					}

				});
		mapView.getOverlays().add(itemizedIconOverlay);
		mapView.invalidate();
	}

	private void enableMap() {
		mapView = (MapView) this.findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
		mapView.setClickable(true);
		mapController = (MapController) mapView.getController();
		mapController.setZoom(14);

	}

	// Adds an actionbar to the fragment
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar_fragment_map, menu);
		addObstacleMenuItem = menu.findItem(R.id.addobstacle_item);
		return true;

	}

	// This method handles onClick at our actionbar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		// If we press the addObstacle in the actionbar, call the addObstacle
		// function in MainActivity
		case R.id.addobstacle_item:
			enableAddObstacle();
			return true;
		case R.id.visualsettings_item:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void enableAddObstacle() {
		if (addObstacleEnabled == false) {
			addObstacleMenuItem.setIcon(R.drawable.icon_addobstacle_enabled);
			addObstacleEnabled = true;
		} else if (addObstacleEnabled == true) {
			addObstacleMenuItem.setIcon(R.drawable.icon_addobstacle);
			addObstacleEnabled = false;
		}

	}

	@Override
	public void onLocationChanged(Location location) {

		this.mapController.setCenter(new GeoPoint(location));

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
