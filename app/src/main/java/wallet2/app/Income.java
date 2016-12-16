package wallet2.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Income extends Activity {
    public static Integer getMelyik() {
        return melyik;
    }

    public static void setMelyik(Integer melyik) {
        Income.melyik = melyik;
    }

    public static Integer melyik;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_add);
        SQLiteDbase db = new SQLiteDbase(this);
        Spinner spinner = (Spinner)findViewById(R.id.incpn);
        if (getMelyik()==1){setTitle("Add income");} else
        {setTitle("Add expense");}


        ArrayList<String> pnems = db.getallpnem();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,pnems);
        spinner.setAdapter(adapter);
        DatePicker dpick = (DatePicker) findViewById(R.id.datePicker);
        dpick.setMaxDate(new Date().getTime());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.


        //getMenuInflater().inflate(R.menu.income_add, menu);
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

    public void chngtitle(View v){
        Spinner spinner = (Spinner) findViewById(R.id.incpn);
        String spn = spinner.getSelectedItem().toString();
        setTitle(spn);
    }

    public void incaddOnclick(View v){
        final EditText osszeg = (EditText) findViewById(R.id.incAm);
        if (osszeg.getText().toString().equals("")){
            Toast.makeText(this,"You have to input amount!", Toast.LENGTH_LONG).show();
        } else {
            if (getMelyik()==1){
                SQLiteDbase db = new SQLiteDbase(this);
                EditText cimke = (EditText) findViewById(R.id.incTitle);
                String Date = null, Day=null;
                CheckBox chkbox = (CheckBox) findViewById(R.id.checkBox);
                if (chkbox.isChecked()) {
                    DatePicker dpick = (DatePicker) findViewById(R.id.datePicker);
                    Date = Integer.toString(dpick.getYear())+"/0"+Integer.toString(dpick.getMonth());
                    Day = Integer.toString(dpick.getDayOfMonth());
                }

                Spinner spinner = (Spinner) findViewById(R.id.incpn);
                String spn = spinner.getSelectedItem().toString();
                EditText etc = (EditText) findViewById(R.id.incetc);
                db.insertincome(cimke.getText().toString(), Integer.parseInt(osszeg.getText().toString()), spn, etc.getText().toString(), Date, Day);
                db.raisepnem(spn);
                Log.d("Hozzáadva", "Hozzáadva");
                Toast.makeText(this,"Income has been added!", Toast.LENGTH_LONG).show();
                db.close();
                finish();
            } else {
                final SQLiteDbase db = new SQLiteDbase(this);
                if (db.getintallmoney()<Integer.parseInt(osszeg.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder
                            .setTitle("The amount is more than your money!")
                            .setMessage("Are you sure, you want to add this record?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText cimke = (EditText) findViewById(R.id.incTitle);
                                    String Date = null, Day = null;
                                    CheckBox chkbox = (CheckBox) findViewById(R.id.checkBox);
                                    if (chkbox.isChecked()) {
                                        DatePicker dpick = (DatePicker) findViewById(R.id.datePicker);
                                        Date = Integer.toString(dpick.getYear()) + "/0" + Integer.toString(dpick.getMonth());
                                        Day = Integer.toString(dpick.getDayOfMonth());
                                    }

                                    Spinner spinner = (Spinner) findViewById(R.id.incpn);
                                    String spn = spinner.getSelectedItem().toString();
                                    EditText etc = (EditText) findViewById(R.id.incetc);
                                    db.insertexpense(cimke.getText().toString(), Integer.parseInt(osszeg.getText().toString()), spn, etc.getText().toString(), Date, Day);
                                    db.raisepnem(spn);
                                    Log.d("Hozzáadva", "Hozzáadva");
                                    Toast.makeText(Income.this, "Expense has been added!", Toast.LENGTH_LONG).show();
                                    db.close();

                                    finish();

                                }
                            })
                            .setNegativeButton("No", null)						//Do nothing on no
                            .show();

                } else {
                    EditText cimke = (EditText) findViewById(R.id.incTitle);
                    String Date = null, Day = null;
                    CheckBox chkbox = (CheckBox) findViewById(R.id.checkBox);
                    if (chkbox.isChecked()) {
                        DatePicker dpick = (DatePicker) findViewById(R.id.datePicker);
                        Date = Integer.toString(dpick.getYear()) + "/0" + Integer.toString(dpick.getMonth());
                        Day = Integer.toString(dpick.getDayOfMonth());
                    }

                    Spinner spinner = (Spinner) findViewById(R.id.incpn);
                    String spn = spinner.getSelectedItem().toString();
                    EditText etc = (EditText) findViewById(R.id.incetc);
                    db.insertexpense(cimke.getText().toString(), Integer.parseInt(osszeg.getText().toString()), spn, etc.getText().toString(), Date, Day);
                    db.raisepnem(spn);
                    Log.d("Hozzáadva", "Hozzáadva");
                    Toast.makeText(this, "Expense has been added!", Toast.LENGTH_LONG).show();
                    db.close();

                    finish();
                }

            }


        }


    }

    public void DateCheck(View v){
        CheckBox chkbox = (CheckBox) findViewById(R.id.checkBox);
        LinearLayout lnlay = (LinearLayout) findViewById(R.id.linearLayout4);
        if (chkbox.isChecked()){
            lnlay.setVisibility(View.VISIBLE);

        } else {
            lnlay.setVisibility(View.GONE);

        }


    }

}