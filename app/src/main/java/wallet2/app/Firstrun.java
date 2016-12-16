package wallet2.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Firstrun extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstrun);
        SQLiteDbase db = new SQLiteDbase(this);
        ArrayList<String> pnems = db.getallpnem();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,pnems);

        Spinner spn1 = (Spinner) findViewById(R.id.frstspin);
        Spinner spn2 = (Spinner) findViewById(R.id.favsetspin);
        spn1.setAdapter(adapter);
        spn2.setAdapter(adapter);
        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.firstrun, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"Megnyomtad a gombot",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onokClick(View v){

        Spinner spn1 = (Spinner) findViewById(R.id.frstspin);
        Spinner spn2 = (Spinner) findViewById(R.id.favsetspin);
        EditText fram = (EditText) findViewById(R.id.framount);
        Log.d("Az érték:", fram.getText().toString());
        if (fram.getText().toString().equals("")) {
            Toast.makeText(this,"You have to add an amount!", Toast.LENGTH_LONG).show();
        } else {
            SQLiteDbase db = new SQLiteDbase(this);
            db.insertincome("My first run",Integer.parseInt(fram.getText().toString()),
                    spn1.getSelectedItem().toString(),"First run!",null,null);
            db.setfirstrun();
            db.setfavpnem(spn2.getSelectedItem().toString());
            db.close();
            Toast.makeText(this,"Thank You!", Toast.LENGTH_LONG).show();
            finish();

        }

    }

}