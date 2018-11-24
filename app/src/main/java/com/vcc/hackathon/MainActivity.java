package com.vcc.hackathon;

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
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.vcc.hackathon.utils.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
	private static final String LOG_TAG = "MainActivity";
	private static final int REQUEST_LOCATION_CODE = 100;
	private static final int PLACE_PICKER_REQUEST = 101;
	private static final int PICK_CONTACT_REQUEST = 102;
	private static final int SEND_SMS_REQUEST = 103;
	@BindView(R.id.btnSend)
	Button btnSend;
	@BindView(R.id.edtMessage)
	TextInputEditText edtMessage;
	@BindView(R.id.view_parent)
	ConstraintLayout viewParent;
	@BindView(R.id.editText_sear_place)
	EditText editTextPlaceSearch;
	@BindView(R.id.spinnerTask)
	Spinner spinnerTask;
	@BindView(R.id.btnSelf)
	Button btnSelf;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initViews();

	}

	private void initViews() {
		editTextPlaceSearch.setKeyListener(null);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.task, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTask.setAdapter(adapter);
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

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case PLACE_PICKER_REQUEST:

				if (resultCode == RESULT_OK) {
					Place place = PlacePicker.getPlace(this, data);
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

	private void prepateContent(String mobileNumber) {
		if (TextUtils.isEmpty(editTextPlaceSearch.getText())) {
			editTextPlaceSearch.setError("Please select the place");
			return;
		} else if (TextUtils.isEmpty(edtMessage.getText())) {
			edtMessage.setError("Describe Message!");
			return;
		}

		String content = edtMessage.getText().toString();
		sendSms(mobileNumber, content);

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

	}
}
