package com.example.cs441_program6;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NewUserActivity extends AppCompatActivity implements NetResponse{
    NetTask netTask;
    EditText inputFn;
    EditText inputLn;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputConfirmPassword;
    Button computeButton;
    Button newUserButton;
    NewUserActivity handle;
    TextView computeResult;
    String updateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        inputFn = findViewById(R.id.fn);
        inputLn = findViewById(R.id.ln);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfirmPassword = findViewById(R.id.confirmPassword);
        computeButton = findViewById(R.id.create);
        newUserButton = findViewById(R.id.new_user);
        computeResult = findViewById(R.id.computeresult);
        handle = this;

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewUserActivity.this, MainActivity.class));
            }
        });

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NetTask netTask;
                System.out.println("Clicked the compute2 button");
                String fn = inputFn.getText().toString();
                String ln = inputLn.getText().toString();
                String e = inputEmail.getText().toString();
                String p = inputPassword.getText().toString();
                String cp = inputConfirmPassword.getText().toString();
                String request = "fn=" + fn + "&ln=" + ln + "&e=" + e + "&p=" + p + "&cp=" + cp;
                netTask = new NewUserActivity.NetTask("https://cs.binghamton.edu/~cglashe1/addUser.php", request, handle);

                netTask.execute((Void) null);

            }
        });
    }

    public void netResult(Integer code, JSONArray json)
    {
        System.out.println("Got a result from the web");
        updateString = "";

        for (int i = 0; i < json.length(); ++i)
        {
            try {
                JSONObject item = json.getJSONObject(i);

                if (item.getInt("status") == 1) {
                    updateString = "All fields must be filled";
                }
                else if (item.getInt("status") == 2) {
                    updateString = "Reconfirm password please";
                }
                else if (item.getInt("status") == 3) {
                    updateString = "Account already exists";
                }
                else{
                    updateString = "good";
                    startActivity(new Intent(NewUserActivity.this, MainActivity.class));
                }
            }
            catch (JSONException e)
            {
                updateString = "JSON Error!";
            }

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    computeResult.setText(updateString);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class NetTask extends AsyncTask<Void, Void, Boolean> {
        private final String urlString;
        private final String reqString;
        private NetResponse changeListener;

        NetTask(String url, String request, NetResponse responseListener) {
            urlString = url; reqString = request; changeListener = responseListener;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                System.out.println("JSON Query: " + reqString);
                // JSONObject json = readJsonFromUrl("https://graph.facebook.com/19292868552");
                // JSONObject json = readJsonFromUrl("https://cnn.com");
                // System.out.println(reqString);
                JSONArray json = readJsonFromUrl(reqString);
                System.out.println("Finished getting json.");
                if (json != null)
                    System.out.println(json.toString());

                if (changeListener != null)
                    changeListener.netResult(0, json);

                //System.out.println("Notify that JSON has come in");
                // if (noteConnector != null)
                //    noteConnector.ncnotify(0, "");

            } catch (IOException e) {
                System.out.println("IO exception");
                //System.out.println(e);
                if (changeListener != null)
                    changeListener.netResult(1, null);
            } catch (JSONException e) {
                System.out.println("JSON Didn't work");
                //System.out.println(e);
                if (changeListener != null)
                    changeListener.netResult(2, null);
            }
            return true;
        }

        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            System.out.println("Read from the URL");
            System.out.println(sb.toString());
            System.out.println("Going to try to turn it into json");
            return sb.toString();
        }

        public JSONArray readJsonFromUrl(String request) throws IOException, JSONException {
            URL nurl = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) nurl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.out.println("Network request to " + urlString + " with request " + reqString);
            OutputStream urlout = connection.getOutputStream();

            //String s = "id=3452&second=fjfjfjfj";
            urlout.write(request.getBytes());
            urlout.close();
            InputStream is = connection.getInputStream();

            System.out.println("Waiting for network stream");
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String jsonText = readAll(rd);
                System.out.println("JSON is " + jsonText);

                JSONArray jarray = new JSONArray(jsonText);


                System.out.println("Got the object");
                return jarray;
            } finally {
                is.close();
                // System.out.println("Did not get the object.");
            }


        }
    }
}
