package wallet2.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WMain extends Activity {
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    public static int getStep() {
        return step;
    }

    public static void setStep(int step) {
        WMain.step = step;
    }

    private static int step;
    private static String day;
    private static String months;

    public static String getMonths() {
        return months;
    }

    public static void setMonths(String months) {
        WMain.months = months;
    }

    public static String getDay() {
        return day;
    }

    public static void setDay(String day) {
        WMain.day = day;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmain);

        this.setTitle("Wallet Kubritzki_Zsolt");
        setStep(0);
        final SQLiteDbase db = new SQLiteDbase(this);
        db.getReadableDatabase();


        if (db.getfirstrun().equals("true")){
            Intent frst = new Intent(WMain.this,Firstrun.class);
            startActivity(frst);

        }

        ArrayList<String> yearmonth = db.getallmonth();
        final ArrayAdapter<String> ymadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,yearmonth);
        TextView txtview = (TextView) findViewById(R.id.textView);
        txtview.setText(getResources().getString(R.string.uvenow)+db.getmymoney());
        db.close();
        final ListView lista = (ListView) findViewById(R.id.ALista);
        lista.setAdapter(ymadapter);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView a, View v, int position, long id) {
                if (getStep()==0){
                    setStep(1);
                    String clicked = new String();
                    clicked = lista.getItemAtPosition(position).toString();

                    setMonths(clicked);
                    setTitle(getMonths());
                    db.getReadableDatabase();
                    //ArrayList<HashMap<String,String>> daysinmonth = db.getdaysinmonth(clicked);
                    //Log.d("Lekérdezés ereménye: ", daysinmonth.toString());
                    SimpleAdapter daysadapter = new SimpleAdapter(ymadapter.getContext(),db.getdaysinmonth(clicked),
                            android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                            new int[] {android.R.id.text1, android.R.id.text2});
                    lista.setAdapter(daysadapter);
                    db.close();} else
                if (getStep()==1){
                    setStep(2);
                    TextView clickedView = (TextView) v.findViewById(android.R.id.text1);
                    String clicked = new String();
                    clicked = clickedView.getText().toString();
                    setDay(clicked);
                    setTitle(getMonths()+" "+getDay());
                    db.getReadableDatabase();
                    SimpleAdapter sa = new SimpleAdapter(ymadapter.getContext(),db.gettimeandvalue(clicked, getMonths()),
                            android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                            new int[] {android.R.id.text1, android.R.id.text2});
                    lista.setAdapter(sa);
                    db.close();
                } else if(getStep()==2){
                    Intent p = new Intent(WMain.this, props.class );
                    TextView clickedView = (TextView) v.findViewById(android.R.id.text1);
                    String[] stbanyag = db.getprops(getDay(), clickedView.getText().toString());
                    props.setOntitle(stbanyag[0]);
                    props.setTime(stbanyag[1]);
                    props.setAmount(stbanyag[2]);
                    props.setPropes(stbanyag[3]);
                    startActivity(p);
                    db.close();
                    //Log.d("listaEredmény", clickedView.getText().toString());
                }}

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SQLiteDbase db = new SQLiteDbase(this);
            db.getReadableDatabase();
            if (db.getuid()>0){ db.close();
           if (db.getrownum()!=0){
               new UploadData().execute();
               ListView lista = (ListView) findViewById(R.id.ALista);
               lista.setAdapter(null);



           } else {

               Toast.makeText(this, "You don't have records to upload!", Toast.LENGTH_LONG).show();
           }

            } else {
                db.close();
                Intent intent = new Intent(WMain.this,LogReg.class);
                startActivity(intent);

            }

        }

        if (id==R.id.logout){
            SQLiteDbase db = new SQLiteDbase(this);
            db.getReadableDatabase();
            if (db.getuid()!=0){

                db.getWritableDatabase();
                db.deluid();
                db.close();
                Toast.makeText(this,"You have been logged out",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"You were not logged in",Toast.LENGTH_LONG).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Day",getDay());
        outState.putString("Month", getMonths());
        outState.putInt("Step",getStep());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int Stage = savedInstanceState.getInt("Stage");
        setStep(savedInstanceState.getInt("Step"));
        setMonths(savedInstanceState.getString("Month"));
        setDay(savedInstanceState.getString("Day"));
        SQLiteDbase db = new SQLiteDbase(this);
//        db.getReadableDatabase();
        ListView lista = (ListView) findViewById(R.id.ALista);
        if (getStep()==2){
            setTitle(getMonths()+" "+getDay());
            //db.getReadableDatabase();
            SimpleAdapter sa = new SimpleAdapter(this,db.gettimeandvalue(getDay(),getMonths()),
                    android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                    new int[] {android.R.id.text1, android.R.id.text2});
            lista.setAdapter(sa);

        }else if(getStep()==1){
            this.setTitle(getMonths());
            //ArrayList<HashMap<String,String>> daysinmonth = db.getdaysinmonth(getMonths());
            //Log.d("Lekérdezés ereménye: ", daysinmonth.toString());
            SimpleAdapter daysadapter = new SimpleAdapter(this,db.getdaysinmonth(getMonths()),
                    android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                    new int[] {android.R.id.text1, android.R.id.text2});
            lista.setAdapter(daysadapter);
            //    db.close();
        } else if (getStep()==0){
            this.setTitle("Wallet Kubritzki_Zsolt");
            ArrayList<String> yearmonth = db.getallmonth();
            ArrayAdapter<String> ymadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,yearmonth);
            lista.setAdapter(ymadapter);

        }

    }

    @Override
    public void onResume(){
        super.onResume();
        SQLiteDbase db = new SQLiteDbase(this);
        TextView txtview = (TextView) findViewById(R.id.textView);
        txtview.setText(getResources().getString(R.string.uvenow)+db.getmymoney());
//        db.getReadableDatabase();
        ListView lista = (ListView) findViewById(R.id.ALista);
        if (getStep()==2){
            setTitle(getMonths()+" "+getDay());
            //db.getReadableDatabase();
            SimpleAdapter sa = new SimpleAdapter(this,db.gettimeandvalue(getDay(),getMonths()),
                    android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                    new int[] {android.R.id.text1, android.R.id.text2});
            lista.setAdapter(sa);

        }else if(getStep()==1){
            this.setTitle(getMonths());
            //ArrayList<HashMap<String,String>> daysinmonth = db.getdaysinmonth(getMonths());
            //Log.d("Lekérdezés ereménye: ", daysinmonth.toString());
            SimpleAdapter daysadapter = new SimpleAdapter(this,db.getdaysinmonth(getMonths()),
                    android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                    new int[] {android.R.id.text1, android.R.id.text2});
            lista.setAdapter(daysadapter);
            //    db.close();
        } else if (getStep()==0){
            this.setTitle("Wallet Kubritzki_Zsolt");
            ArrayList<String> yearmonth = db.getallmonth();
            ArrayAdapter<String> ymadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,yearmonth);
            lista.setAdapter(ymadapter);

        }


    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        SQLiteDbase db = new SQLiteDbase(this);
        db.getReadableDatabase();
        ListView lista = (ListView) findViewById(R.id.ALista);
        if (getStep()==2){
            setStep(1);
            this.setTitle(getMonths());
            //ArrayList<HashMap<String,String>> daysinmonth = db.getdaysinmonth(getMonths());
            //Log.d("Lekérdezés ereménye: ", daysinmonth.toString());
            SimpleAdapter daysadapter = new SimpleAdapter(this,db.getdaysinmonth(getMonths()),
                    android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                    new int[] {android.R.id.text1, android.R.id.text2});
            lista.setAdapter(daysadapter);
            db.close();

        } else if(getStep()==1){
            this.setTitle("Wallet Kubritzki_Zsolt");
            setStep(0);
            ArrayList<String> yearmonth = db.getallmonth();
            ArrayAdapter<String> ymadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,yearmonth);
            lista.setAdapter(ymadapter);
        } else if (getStep()==0) {

            db.close();
            finish();

        }



    }

    public void getfirstlist(){
        SQLiteDbase db = new SQLiteDbase(this);
        db.getReadableDatabase();
        ArrayList<String> yearmonth = db.getallmonth();
        ArrayAdapter<String> ymadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,yearmonth);
        ListView lista = (ListView) findViewById(R.id.ALista);
        lista.setAdapter(ymadapter);
        setStep(0);


    }

    public void incbtnClick(View v){
        Intent a = new Intent(WMain.this, Income.class);
        Income.setMelyik(1);
        startActivity(a);


    }

    public void onlbtnClick(View v){
        SQLiteDbase db = new SQLiteDbase(this);
        db.getReadableDatabase();
        if (db.getuid()!=0){ db.close();
            Intent a = new Intent(WMain.this, Online.class);
            startActivity(a);
            } else {
            db.close();
            Intent intent = new Intent(WMain.this,LogReg.class);
            startActivity(intent);

        }


    }

    public void ecpbtnclick(View v){
        Intent a = new Intent(WMain.this, Income.class);
        Income.setMelyik(0);
        startActivity(a);


    }



    class UploadData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(WMain.this);
            pDialog.setMessage("Uploading Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success = 0;
            String message = "";
            String jsonto = "";
            SQLiteDbase db = new SQLiteDbase(WMain.this);
            int mny = db.getmoney();
            db.delremain();
            db.insremain(mny);
            int rnum = db.getrownum();
            Log.d("RNumConst ",Integer.toString(rnum));

                try {


                    // Building Parameters
                    JSONObject json;
                    for (int i = 0; i < rnum; i++ ) {
                        List<NameValuePair> params = db.upload();

                        Log.d("request!", "starting");
                        //Posting user data to script
                         json = jsonParser.makeHttpRequest(
                                "http://192.168.1.2/Elo/insert.php", "POST", params);

                        // full json response
                        Log.d("Attempt upload", json.toString());
                        jsonto= json.toString();
                        message = json.getString("message");
                    // json success element
                    success = json.getInt("success");}
                    if (success == 1) {
                        Log.d("Upload Completed!", jsonto);

                        return message;
                    } else {
                        Log.d("Upload Failed!", message);
                        return message;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return null;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null){
                //WMain.this.recreate();
                Toast.makeText(WMain.this, "Upload done", Toast.LENGTH_LONG).show();
                new GetOnlMoney().execute();


            }

        }

}


    class GetOnlMoney extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(WMain.this);
            pDialog.setMessage("Getting Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            SQLiteDbase db = new SQLiteDbase(WMain.this);


                try {


                    // Building Parameters


                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.clear();
                    params.add(new BasicNameValuePair("uid", Integer.toString(db.getuid2())));
                    params.add(new BasicNameValuePair("pnem", db.getfavpnem()));
                    db.close();
                    Log.d("request!", "starting");
                    //Posting user data to script
                    JSONObject json = jsonParser.makeHttpRequest(
                            "http://192.168.1.2/Elo/getmoney.php", "POST", params);

                    // full json response
                    Log.d("Attempt Download", json.toString());

                    // json success element
                    success = json.getInt("success");
                    if (success == 1) {
                        Log.d("Download Completed!", json.toString());
                        db.delremain();
                        db.insremain(Integer.parseInt(json.getString("osszeg")));
                        db.close();

                        return json.getString("Message");
                    } else {
                        Log.d("Upload Failed!", json.getString("Message"));
                        return json.getString("Message");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return null;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null){
                WMain.this.recreate();
                Toast.makeText(getApplicationContext(), "Download done", Toast.LENGTH_LONG).show();

                WMain.this.onPause();
                WMain.this.onResume();



            }

        }

    }
}
