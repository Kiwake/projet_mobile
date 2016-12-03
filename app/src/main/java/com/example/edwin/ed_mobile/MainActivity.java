package com.example.edwin.ed_mobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_hw = (TextView) findViewById(R.id.tv_hello_world);
        Typeface myCustomfont = Typeface.createFromAsset(getAssets(), "fonts/BEBAS__.TTF");
        tv_hw.setTypeface(myCustomfont);
    }


    public void ChangeDate(View v) {
      /*  final TextView tv_hw = (TextView) findViewById(R.id.tv_hello_world);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = (month % 12) + 1; //parce que la date déconne
                tv_hw.setText(getString(R.string.hello_world) + "\n" + "Date :" + dayOfMonth + "/" + month + "/" + year);
            }
        };
        DatePickerDialog dpd = new DatePickerDialog(this, listener, 2016, 11, 07);
        dpd.show();*/
        DialogFragment newFragment = new CustomDialog();
        newFragment.show(getFragmentManager(),"lol");
    }

    public void Search(View v){

        EditText edit =  (EditText) findViewById(R.id.web_text);
        String recherche = edit.getText().toString();

        Intent web = new Intent(Intent.ACTION_WEB_SEARCH );
        web.putExtra(SearchManager.QUERY, recherche);
        startActivity(web);
    }

    public void Notif_Me(View v) {
        Notif_Myself();
    }

    public void Notif_Myself() {
        int notif_id = 001;
        Toast.makeText(getApplicationContext(), "Envoyé", Toast.LENGTH_LONG).show();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(R.string.notification_title));
        mBuilder.setSmallIcon(R.drawable.ic_not);
        mBuilder.setContentText(getString(R.string.notification_desc));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notif_id, mBuilder.build());
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.icone) {
            Intent seconde = new Intent(getApplicationContext(), SecondeActivity.class);
            startActivity(seconde);
        }

        if (id == R.id.position) {
            Intent local = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:48.8,2.2"));
            startActivity(local);
        }
        return super.onOptionsItemSelected(item);
    }




    public class CustomDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            final JSONArray arr = SecondeActivity.getBiersFromFile();
            int test = 0;

            String[] job_str = new String[arr.length()];
            if(arr.length() == 0){
                job_str = new String[1];
                job_str[0]= "Télécharger la liste de cinémas";
                test = 1;
            }
            final JSONObject[] job = new JSONObject[arr.length()];
            try {
                //job= new JSONObject[arr.length()];
               // job_str = new String[]{};
                    for(int i=0; i<arr.length(); i++){
                        job[i]=arr.getJSONObject(i);
                       // job_str[i]=job[i].getString("title");
                       job_str[i]=arr.getJSONObject(i).getString("name");
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final int finalTest = test;
            builder.setTitle("Choisis ton cinéma :")
                    .setItems(job_str, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            if(finalTest==1){
                                Intent seconde = new Intent(getApplicationContext(), SecondeActivity.class);
                                startActivity(seconde);
                            }

                            JSONObject obj = job[which];
                            double x = 0,y=0;

                            try {
                                x = Double.parseDouble(obj.getString("x"));
                                y = Double.parseDouble(obj.getString("y"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                                    Toast.makeText(getApplicationContext(), "x:"+x+" "+y+"" , Toast.LENGTH_LONG).show();


                            Log.i("TOSt"," "+x + " "+y );
                            Intent local = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+x+","+y+""));
                            startActivity(local);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return alert;
        }
    }


}
