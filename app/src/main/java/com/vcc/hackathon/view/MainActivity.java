package com.vcc.hackathon.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.vcc.hackathon.R;
import com.vcc.hackathon.datamanager.AppDatabase;
import com.vcc.hackathon.datamanager.DatabaseClient;
import com.vcc.hackathon.datamanager.GeoRemainderEntity;
import com.vcc.hackathon.utils.PermissionUtils;

import java.util.concurrent.CompletableFuture;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
	private static final String LOG_TAG = "MainActivity";
	private static final int REQUEST_LOCATION_CODE = 100;
	private static final int PLACE_PICKER_REQUEST = 101;
	private static final int PICK_CONTACT_REQUEST = 102;
	private static final int PERMISSION_GROUP_REQUEST = 103;
	@BindView(R.id.btnSend)
	Button btnSend;
	@BindView(R.id.edtMessage)
	EditText edtMessage;
	@BindView(R.id.view_parent)
	ConstraintLayout viewParent;
	@BindView(R.id.editText_sear_place)
	EditText editTextPlaceSearch;
	@BindView(R.id.spinnerTask)
	Spinner spinnerTask;
	@BindView(R.id.btnSelf)
	Button btnSelf;
	@BindArray(R.array.task)
	String[] task;
	@BindArray(R.array.radius)
	String[] radiusArr;
	@BindView(R.id.spinner_radius)
	Spinner spinnerRadius;

	private LatLng mLatLng;
	private AppDatabase databaseClient;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle("Set Remainder");

		}
		databaseClient = DatabaseClient.getInstance(this).getAppDatabase();
		initViews();
		permissionChecK();
	}

	private void permissionChecK() {
		if (!PermissionUtils.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS)) {
			PermissionUtils.requestPermissions(this, PERMISSION_GROUP_REQUEST, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS);
		}
	}

	private void initViews() {
		editTextPlaceSearch.setKeyListener(null);
		bindAdapterToSpinner(spinnerTask, getArrayAdapter(task));
		bindAdapterToSpinner(spinnerRadius, getArrayAdapter(radiusArr));
	}

	private void bindAdapterToSpinner(Spinner spinner, ArrayAdapter<CharSequence> adapter) {
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getCount());
	}

	private ArrayAdapter<CharSequence> getArrayAdapter(String[] arr) {
		return new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, arr) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView) v.findViewById(android.R.id.text1)).setText("");
					((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
				}

				return v;
			}

			@Override
			public int getCount() {
				return super.getCount() - 1; // you dont display last item. It is used as hint.
			}

		};
	}

	private void placePicker() {
		try {

			PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
			startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@OnClick(R.id.btnSend)
	public void onViewClicked() {
		if (PermissionUtils.hasPermissions(this, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS)) {
			contactPicker();
		} else {
			PermissionUtils.requestPermissions(this, PICK_CONTACT_REQUEST, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch (requestCode) {
			case REQUEST_LOCATION_CODE:

				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					placePicker();

				} else {
					handleError("Permission Denied!");
				}
				break;
			case PICK_CONTACT_REQUEST:

				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					contactPicker();
				} else {
					handleError("Permission Denied!");
				}
				break;

			case PERMISSION_GROUP_REQUEST:

				if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

					handleError("Permission Denied!");
				}
				break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case PLACE_PICKER_REQUEST:

				if (resultCode == RESULT_OK) {
					Place place = PlacePicker.getPlace(this, data);
					mLatLng = place.getLatLng();
					if (editTextPlaceSearch.getError() != null) {
						editTextPlaceSearch.setError(null);
					}
					editTextPlaceSearch.setText(place.getName());
				}
				break;
			case PICK_CONTACT_REQUEST:

				if (resultCode == RESULT_OK) {
					prepateContent(getPhoneNumber(data.getData()));
				}
				break;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// todo: goto back activity from here
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private boolean isValidateForm() {
		if (TextUtils.isEmpty(editTextPlaceSearch.getText())) {
			editTextPlaceSearch.setError("Please select the place");
			return false;
		} else if (TextUtils.isEmpty(edtMessage.getText())) {
			edtMessage.setError("Describe Message!");
			return false;
		}

		return true;
	}

	private void prepateContent(String mobileNumber) {
		if (isValidateForm()) {
			String content = "tag:VCC\n Radius:" + spinnerRadius.getSelectedItem() + "\n Message:" + edtMessage.getText().toString() + "\nLocation url: http://maps.google.com/?q=" + mLatLng.latitude + "," + mLatLng.longitude;
			sendSms(mobileNumber, content);
		}

	}


	private void sendSms(String mobileNo, String content) {
		try {

			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(mobileNo, null, content, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void contactPicker() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}


	private void handleError(String msg) {
		Snackbar mySnackbar = Snackbar.make(viewParent,
				msg, Snackbar.LENGTH_SHORT);
		mySnackbar.show();


	}

	private String getPhoneNumber(Uri uri) {
		String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
		Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection,
				null, null, null);
		cursor.moveToFirst();
		int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		String number = cursor.getString(numberColumnIndex);
		cursor.close();
		return number;
	}

	@OnClick(R.id.editText_sear_place)
	public void onPlaceSearchClicked() {
		if (PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
			placePicker();
		} else {
			PermissionUtils.requestPermission(this, REQUEST_LOCATION_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
		}
	}

	@OnClick(R.id.btnSelf)
	public void onSelfClicked() {

		if (isValidateForm()) {
			final String msg = edtMessage.getText().toString();
			final String task = spinnerTask.getSelectedItem().toString();
			final int radius = Integer.parseInt(spinnerRadius.getSelectedItem().toString());
			final String url = "http://maps.google.com/?q=" + mLatLng.latitude + "," + mLatLng.longitude;
			CompletableFuture.runAsync(new Runnable() {
				@Override
				public void run() {
					GeoRemainderEntity entry = new GeoRemainderEntity();
					entry.setLocationUrl(url);
					entry.setMessage(msg);
					entry.setTask(task);
					entry.setRadius(radius);
					databaseClient.geoRemainderDao().insert(entry);
					Toast.makeText(MainActivity.this,"Remainder saved!",Toast.LENGTH_LONG).show();
				}
			});
		}

	}
}
