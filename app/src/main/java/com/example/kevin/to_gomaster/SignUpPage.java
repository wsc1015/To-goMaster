package com.example.kevin.to_gomaster;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignUpPage extends AppCompatActivity {
    EditText input_newUserName;
    EditText input_newPassword;
    EditText input_newConfirmPassword;
    EditText input_newPhone;
    EditText input_newAddress;
    EditText input_newEmail;
    Button btn_submitRegister;
    Button btn_cancelRegister;
    ProgressDialog progressDialog;
    public static final String url_register = "http://192.168.1.3/togodemo/sign_up.php";
    public static final String PWIcon = "Password is inconsistent!";
    public static final String FDMiss = "Required field(s) is missing!";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        input_newUserName = (EditText) findViewById(R.id.input_newUserName);
        input_newPassword = (EditText) findViewById(R.id.input_newPassword);
        input_newConfirmPassword = (EditText) findViewById(R.id.input_newConfirmPassword);
        input_newPhone = (EditText) findViewById(R.id.input_newPhone);
        input_newAddress = (EditText) findViewById(R.id.input_newAddress);
        input_newEmail = (EditText) findViewById(R.id.input_newEmail);
        btn_submitRegister = (Button) findViewById(R.id.btn_submitRegister);
        btn_cancelRegister = (Button) findViewById(R.id.btn_cancelRegister);
        btn_submitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserName = input_newUserName.getText().toString();
                String newPassword = input_newPassword.getText().toString();
                String newConfirmPassword = input_newConfirmPassword.getText().toString();
                String newPhone = input_newPhone.getText().toString();
                String newAddress = input_newAddress.getText().toString();
                String newEmail = input_newEmail.getText().toString();
                System.out.println("hahaha" + newPassword);
                new submitRegisterInformation(newUserName, newPassword, newConfirmPassword, newPhone, newAddress,newEmail).execute();
            }
        });
        btn_cancelRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    class submitRegisterInformation extends AsyncTask<String, String, String>{
        String newUserName;
        String newPassword;
        String newConfirmPassword;
        String newPhone;
        String newAddress;
        String newEmail;

        public submitRegisterInformation(String newUserName, String newPassword, String newConfirmPassword, String newPhone, String newAddress, String newEmail) {
            super();
            this.newAddress = newAddress;
            this.newConfirmPassword = newConfirmPassword;
            this.newEmail = newEmail;
            this.newPassword = newPassword;
            this.newPhone = newPhone;
            this.newUserName = newUserName;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignUpPage.this);
            progressDialog.setMessage("Creating Account ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> postData = new ArrayList<NameValuePair>();
            if(newUserName.length() == 0 || newConfirmPassword.length() == 0
                    || newPassword.length() == 0 || newAddress.length() == 0 || newPhone.length() == 0 || newEmail.length() == 0){
                return FDMiss;
            }
            if(!newPassword.equals(newConfirmPassword)) {
                return PWIcon;
            }
            postData.add(new BasicNameValuePair("user_name", newUserName));
            postData.add(new BasicNameValuePair("password", newPassword));
            postData.add(new BasicNameValuePair("phone", newPhone));
            postData.add(new BasicNameValuePair("address", newAddress));
            postData.add(new BasicNameValuePair("email", newEmail));
            JSONObject json = jsonParser.makeHTTPRequest(url_register, "POST", postData);
            System.out.println("Response: " + json.toString());
            try{
                int success = json.getInt(TAG_SUCCESS);
                if(success == 1){
                    Intent intent = new Intent(SignUpPage.this, UserPage.class);
                    startActivity(intent);
                    finish();
                }else{
                    return json.getString(TAG_MESSAGE);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s != null){
                Toast.makeText(SignUpPage.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
