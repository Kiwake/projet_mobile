package com.example.edwin.ed_mobile;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class SecondeActivity extends AppCompatActivity {
    private Context context;
    private TextView tv;
    private RecyclerView rview;
    private LoadToast lt;
    public static int [] geo;

    private GoogleApiClient client;


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Seconde Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //public static final String BIERS_UPDATE = "com.octip.cours.inf4042_11.BIERS_UPDATE";

    public class BierAdapter extends Adapter<BierAdapter.BierHolder> {
        private JSONArray biers;


        public BierAdapter(JSONArray biers) {
            this.biers = biers;
        }

        @Override
        public BierHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_bier_element, rview, false);
            BierHolder bierHolder = new BierHolder(view);
            return bierHolder;
        }

        @Override
        public void onBindViewHolder(BierHolder bierHolderolder, int position) {
            try {
                JSONObject item = biers.getJSONObject(position);
                String jsonname = item.getString("name");
                String jsonlocate = item.getString("location");

                bierHolderolder.title.setText(jsonname);
                bierHolderolder.location.setText(jsonlocate);


                // bierHolderolder.title.setText(jsonnote);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return biers.length();
        }

        public void setNewBiere() {
            this.notifyDataSetChanged();
        }

        public class BierHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView location;

            public BierHolder(View view) {
                super(view);
                this.title = (TextView) view.findViewById(R.id.name);
                this.location = (TextView) view.findViewById(R.id.location);
            }

        }
    }


    public BroadcastReceiver BierUpdate = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            if (b != null) {
                lt.success();
                Notif_DL();
            }
        }
    };


    public void Notif_DL() {
        int notif_id = 002;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Download complete");
        mBuilder.setSmallIcon(R.drawable.ic_dl);
        mBuilder.setContentText("Your download is complete !");
        Notification note = mBuilder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notif_id, note);
    }


    public static JSONArray getBiersFromFile() {
        try {
            //InputStream is = new FileInputStream(Environment.getExternalStorageDirectory()+"/"+"bieres.json");
            InputStream is = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + "cinemas.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconde);
        tv = (TextView) findViewById(R.id.txtmessage);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView tv2 = (TextView) findViewById(R.id.title_topfilm);
        Typeface myCustomfont = Typeface.createFromAsset(getAssets(), "fonts/BEBAS__.TTF");
        tv2.setTypeface(myCustomfont);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void AffichageRview(View v) {
        rview = (RecyclerView) findViewById(R.id.rv_biere);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(new BierAdapter(getBiersFromFile()));
    }

    //DOWNLOAD PART

    public void Service(View v) {
        context = this;
        Intent newIntent = new Intent(context, GetBiersServices.class);
        lt = new LoadToast(context);
        newIntent.setAction(GetBiersServices.ACTION_DOWNLOAD);
        // Start Download Service
        //tv.setText("Downloading...");
        lt.setText("Downloading...");
        lt.show();
        Toast.makeText(getApplicationContext(), "Downloading " + GetBiersServices.urlString, Toast.LENGTH_LONG).show();
        context.startService(newIntent);
        //GetBiersServices.startActionBiers(this);
    }


    protected void onResume() {
        super.onResume();
        // Register receiver to get message from DownloadService
        registerReceiver(BierUpdate, new IntentFilter(GetBiersServices.ACTION_DOWNLOAD));

    }

    protected void onPause() {
        super.onPause();
        // Unregister the receiver
        unregisterReceiver(BierUpdate);

    }

}
