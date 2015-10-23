package com.example.kevin.to_gomaster;

import android.app.ProgressDialog;
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


public class LoginPage extends AppCompatActivity {
    Button btn_cancel;
    Button btn_login;
    EditText input_userName;
    EditText input_password;
    ProgressDialog pd;
    private static String url_login = "http://192.168.1.3/togodemo/login.php";
    private static String FDMiss = "Required field(s) is missing!";
    private static String TAG_SUCCESS = "success";
    private static String TAG_MESSAGE = "message";
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_login = (Button) findViewById(R.id.btn_login);
        input_userName = (EditText) findViewById(R.id.input_userName);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = input_userName.getText().toString();
                String password = input_password.getText().toString();
                new checkLoginInformation(userName, password).execute();

            }
        });
    }

    class checkLoginInformation extends AsyncTask<String, String, String> {

        String username;
        String password;
        public checkLoginInformation(String username, String password){
            super();
            this.username = username;
            this.password = password;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginPage.this);
            pd.setMessage("Login ...");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(username.length() == 0 || password.length() == 0){
                return FDMiss ;
            }
            List<NameValuePair> postData = new ArrayList<NameValuePair>();
            postData.add(new BasicNameValuePair("user_name", username));
            postData.add(new BasicNameValuePair("password", password));
            JSONObject json = jsonParser.makeHTTPRequest(url_login, "POST", postData);
            try{
                int success = json.getInt(TAG_SUCCESS);
                if(success == 1){
                    Intent i = new Intent(LoginPage.this, UserPage.class);
                    startActivity(i);
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
            pd.dismiss();
            if(s != null){
                Toast.makeText(LoginPage.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
