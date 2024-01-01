package vn.edu.dlu.ctk45.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.R;

public class SignUp extends AppCompatActivity {
    Button btnSignIn,btnSubmit;
    TextInputEditText txtUserName,txtUserNameRegister,txtUserPassword,txtUserGmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtUserName = findViewById(R.id.txtUserName);
        txtUserNameRegister = findViewById(R.id.txtUserNameRegister);
        txtUserPassword = findViewById(R.id.txtUserPassword);
        txtUserGmail = findViewById(R.id.txtUserGmail);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = txtUserName.getText().toString();
                String userName = txtUserNameRegister.getText().toString();
                String password = txtUserPassword.getText().toString();
                String email = txtUserGmail.getText().toString();

                new SendDataToServerTask().execute(fullName, userName, password, email);

                Intent intent = new Intent(SignUp.this,SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });


    }
    private class SendDataToServerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String fullName = params[0];
                String userName = params[1];
                String password = params[2];
                String email = params[3];

                // Replace with your Node.js server URL
                String serverUrl = Home.url + "/register";

                URL url = new URL(serverUrl);

                // Create a new HttpURLConnection for each attempt
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);

                    // Create the data to be sent
                    String postData = "hoten=" + fullName +
                            "&tendangnhap=" + userName +
                            "&matkhau=" + password +
                            "&email=" + email;

                    // Write the data to the server
                    try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                        wr.writeBytes(postData);
                        wr.flush();
                    }

                    // Get the response from the server
                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Successfully sent data to the server
                        return true;
                    } else {
                        // Handle the error case
                        Log.e("ServerResponse", "HTTP error code: " + responseCode);
                        return false;
                    }
                } finally {
                    // Disconnect to release resources
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ServerResponse", "Exception: " + e.getMessage());
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Handle success, for example, show a Toast
                Toast.makeText(SignUp.this, "Registration successful", Toast.LENGTH_SHORT).show();
            } else {
                // Handle failure, for example, show a Toast
                Toast.makeText(SignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUp.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}