package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.util.Constants;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        new LaunchTask().execute();
    }

    private class LaunchTask extends AsyncTask {
        Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(LaunchActivity.this, MapActivity.class);
            intent.putExtra(Constants.EXTRA_PARAM_FROM_LAUNCH_SCREEN, true);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Thread.sleep(Constants.LAUNCH_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            startActivity(intent);
            finish();
        }
    }
}
