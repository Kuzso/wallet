package wallet2.app;

import android.app.Activity;
import android.app.ProgressDialog;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Online extends Activity {
    private ProgressDialog pDialog;
    private String yearmnt;

    public String getMyday() {
        return myday;
    }

    public void setMyday(String myday) {
        this.myday = myday;
    }

    public String getYearmnt() {
        return yearmnt;
    }

    public void setYearmnt(String yearmnt) {
        this.yearmnt = yearmnt;
    }

    private String myday;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    private int step = 0;

    public String getClicked() {
        return clicked;
    }

    public void setClicked(String clicked) {
        this.clicked = clicked;
    }

    private String clicked;
    ArrayList<HashMap<String, String>> List;
    JSONParser jsonParser = new JSONParser();
    ArrayList<String> map = new ArrayList();
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        lista = (ListView) findViewById(R.id.fainlista);
        List = new ArrayList<HashMap<String, String>>();
        new LoadMonths().execute();


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView a, View v, int position, long id) {
                TextView clickedView = (TextView) v.findViewById(android.R.id.text1);
                // setClicked(lista.getItemAtPosition(position).toString());
                if (getStep()==0){
                    setYearmnt(clickedView.getText().toString());
                    new LoadDays().execute();
                    setStep(1);

                } else if (getStep()==1){
                    setStep(2);
                    setMyday(clickedView.getText().toString());
                    new LoadRecs().execute();

                }
            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.online, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if (getStep()==2){
            new LoadDays().execute();
            setStep(1);

        } else if (getStep()==1){
            new LoadMonths().execute();
            setStep(0);

        } else {

            finish();
        }

    }


    class LoadMonths extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Online.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            SQLiteDbase db = new SQLiteDbase(Online.this);
            db.getReadableDatabase();
            params.add(new BasicNameValuePair("uid", Integer.toString(db.getuid2())));
            db.close();
            // getting JSON string from URL
            Log.d("cucc: ", params.toString());
            JSONObject json = jsonParser.makeHttpRequest("http://10.0.2.2/wallet/getdatum.php", "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray months = json.getJSONArray("products");
                    map.clear();

                    // looping through All Products
                    for (int i = 0; i < months.length(); i++) {
                        JSONObject c = months.getJSONObject(i);

                        // Storing each json item in variable

                        String name = c.getString("name");



                        // adding each child node to HashMap key => value
                        map.add(name);


                    }
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Online.this,android.R.layout.simple_list_item_1,android.R.id.text1,map);
                    lista.setAdapter(adapter);
                }
            });

        }

    }

    class LoadDays extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Online.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            SQLiteDbase db = new SQLiteDbase(Online.this);
            db.getReadableDatabase();
            params.add(new BasicNameValuePair("uid", Integer.toString(db.getuid2())));
            params.add(new BasicNameValuePair("yearmonth", getYearmnt()));
            db.close();
            List.clear();
            // getting JSON string from URL
            Log.d("cucc: ", params.toString());
            JSONObject json = jsonParser.makeHttpRequest("http://10.0.2.2/wallet/getnap.php", "POST", params);


            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray months = json.getJSONArray("products");

                    map.clear();

                    // looping through All Products
                    for (int i = 0; i < months.length(); i++) {
                        JSONObject c = months.getJSONObject(i);

                        // Storing each json item in variable
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        String name = c.getString("name");
                        String rec = c.getString("props");




                        // adding each child node to HashMap key => value
                        map.add(name);
                        map2.put("Line1",name);
                        map2.put("Line2",rec);
                        List.add(map2);


                    }

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
            lista.setAdapter(null);
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    SimpleAdapter dayadapter = new SimpleAdapter(Online.this,List,
                            android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                            new int[] {android.R.id.text1, android.R.id.text2});


                    lista.setAdapter(dayadapter);

                }
            });

        }

    }


    class LoadRecs extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Online.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            SQLiteDbase db = new SQLiteDbase(Online.this);
            db.getReadableDatabase();
            params.add(new BasicNameValuePair("uid", Integer.toString(db.getuid2())));
            params.add(new BasicNameValuePair("yearmonth", getYearmnt()));
            params.add(new BasicNameValuePair("day", getMyday()));
            db.close();
            List.clear();
            // getting JSON string from URL
            Log.d("cucc: ", params.toString());
            JSONObject json = jsonParser.makeHttpRequest("http://10.0.2.2/wallet/getrec.php", "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray months = json.getJSONArray("products");
                    map.clear();
                    //List.clear();
                    // looping through All Products
                    for (int i = 0; i < months.length(); i++) {
                        JSONObject c = months.getJSONObject(i);

                        // Storing each json item in variable
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        String name = c.getString("name");
                        String rec = c.getString("props");




                        // adding each child node to HashMap key => value
                        map.add(name);
                        map2.put("Line1",name);
                        map2.put("Line2",rec);
                        List.add(map2);



                    }

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
            lista.setAdapter(null);
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    SimpleAdapter propadapter = new SimpleAdapter(Online.this,List,
                            android.R.layout.two_line_list_item, new String[] { "Line1","Line2" },
                            new int[] {android.R.id.text1, android.R.id.text2});


                    lista.setAdapter(propadapter);

                }
            });

        }

    }
}


