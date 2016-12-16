package wallet2.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import wallet2.app.R;

public class props extends Activity {

    public static String time;
    public static String ontitle;
    public static String amount;
    public static String propes;

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        props.time = time;
    }

    public static String getOntitle() {
        return ontitle;
    }

    public static void setOntitle(String ontitle) {
        props.ontitle = ontitle;
    }

    public static String getAmount() {
        return amount;
    }

    public static void setAmount(String amount) {
        props.amount = amount;
    }

    public static String getPropes() {
        return propes;
    }

    public static void setPropes(String propes) {
        props.propes = propes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_props);
        TextView tlt = (TextView) findViewById(R.id.textTitle);
        TextView tm = (TextView) findViewById(R.id.textTime);
        TextView amnt = (TextView) findViewById(R.id.textAmount);
        TextView prp = (TextView) findViewById(R.id.textProp);
        tlt.setText(getOntitle());
        tm.setText(getTime());
        amnt.setText(getAmount());
        prp.setText(getPropes());
        setTitle(WMain.getMonths()+" "+WMain.getDay());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.props, menu);
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

}