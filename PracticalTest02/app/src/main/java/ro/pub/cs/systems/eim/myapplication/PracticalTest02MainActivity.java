package ro.pub.cs.systems.eim.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cz.msebera.android.httpclient.client.ClientProtocolException;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText portEditText;
    EditText currencyEditText;
    Button startServerButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        portEditText = findViewById(R.id.editTextPort);
        startServerButton = findViewById(R.id.buttonStartServer);
        currencyEditText = findViewById(R.id.currency);
        textView = findViewById(R.id.textViewResult);
    }

    public void startServer(View view) {
        requestData("https://api.coindesk.com/v1/bpi/currentprice/EUR.json");
    }

    private void requestData(String url) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("GET");
        requestPackage.setUrl(url);

        Downloader downloader = new Downloader(); //Instantiation of the Async task
        //that’s defined below

        downloader.execute(requestPackage);
    }

    private class Downloader extends AsyncTask<RequestPackage, String, String> {
        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        //The String that is returned in the doInBackground() method is sent to the
        // onPostExecute() method below. The String should contain JSON data.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                String dollars = jsonObject.getJSONObject("bpi").getJSONObject("USD").getString("rate");
                String euro = jsonObject.getJSONObject("bpi").getJSONObject("EUR").getString("rate");

                String currency = currencyEditText.getText().toString();
                if (currency.equals("dollars")) {
                    textView.setText(dollars);
                } else if (currency.equals("euro")) {
                    textView.setText(euro);
                } else {
                    textView.setText("Provide a valid currency (euro or dollars)");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}