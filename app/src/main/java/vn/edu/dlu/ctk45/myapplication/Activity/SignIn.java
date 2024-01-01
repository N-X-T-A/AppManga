package vn.edu.dlu.ctk45.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.Model.ApiResponseToken;
import vn.edu.dlu.ctk45.myapplication.R;

public class SignIn extends AppCompatActivity {
    Button btnSignUp,btnLogin;
    String token;
    TextInputEditText txtUserName,txtUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtUserName = findViewById(R.id.txtUserName);
        txtUserPassword = findViewById(R.id.txtUserPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = txtUserName.getText().toString();
                String Password = txtUserPassword.getText().toString();

                new LoginAsyncTask().execute(UserName, Password);
            }
        });


        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String userName = params[0];
            String password = params[1];

            try {
                URL url = new URL(  Home.url + "/login");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("tendangnhap", userName);
                jsonParam.put("matkhau", password);

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                out.flush();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = urlConnection.getInputStream();

                    ApiResponseToken response = parseResponse(in);
                    token = response.getToken();
                } else {
                    // Handle error
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return token;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("LoginAsyncTask", "Token: " + result);
            Intent intent = new Intent(SignIn.this,MainActivity.class);
            intent.putExtra("token",result);
            intent.putExtra("isSignIn",true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        private ApiResponseToken parseResponse(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON using a library like Gson
            Gson gson = new Gson();
            return gson.fromJson(response.toString(), ApiResponseToken.class);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent get = getIntent();
        int id;
        Class<?> activity = (Class<?>) get.getSerializableExtra("Activity");
        Intent intent;
        if (activity == null)
            intent = new Intent(SignIn.this,MainActivity.class);
        else {
            intent = new Intent(SignIn.this, activity);
            id = get.getIntExtra("id",0);
            Log.d("id_signIn",String.valueOf(id));
            intent.putExtra("id",id);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}