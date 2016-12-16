package wallet2.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class SQLiteDbase extends SQLiteOpenHelper {
    private static final String TABLE_pnemDB = "pnem";

    private static final String KEY_ID = "id";
    private static final String KEY_pnem = "pnem";
    private static final String Key_used="used";

    private static final String[] COLUMNS = {KEY_ID,KEY_pnem,Key_used};

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PnemDB";
    public SQLiteDbase(Context context) {
        super(context, TABLE_pnemDB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PNEM_TABLE = "CREATE TABLE pnem ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pnem TEXT, used INTEGER )";

        String CREATE_INOUT_TABLE = "CREATE TABLE inout ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "cimke TEXT, befizetes INTEGER, "+
                "osszeg INTEGER, pnem TEXT, yearmonth Text,"+
                "date TEXT, time TEXT, etc Text)";

        db.execSQL(CREATE_PNEM_TABLE);
        db.execSQL(CREATE_INOUT_TABLE);

        Log.d("Db létrehozva", "Az adatbázis létrehozva");

        String CRATE_EUR_PN = "Insert into pnem VALUES ('0','EUR','0')";
        String CRATE_HUF_PN = "Insert into pnem VALUES ('1','HUF','1')";
        String CRATE_GBP_PN = "Insert into pnem VALUES ('2','GBP','0')";
        //SQLiteDatabase db2 = this.getWritableDatabase();
        db.execSQL(CRATE_EUR_PN);
        db.execSQL(CRATE_GBP_PN);
        db.execSQL(CRATE_HUF_PN);

        String CREATE_TABLE_MUST = "Create table must (" +
                "id Integer primary key, title text, value text)";
        String Firstrun = "Insert into must values('0','firstrun','true')";
        String Favpnem = "Insert into must values('1','favpnem','HUF')";
        db.execSQL(CREATE_TABLE_MUST);
        db.execSQL(Firstrun);
        db.execSQL(Favpnem);

        String Crate_table_uid = "Create table uid(id INTEGER PRIMARY KEY AUTOINCREMENT, uid Integer)";
        db.execSQL(Crate_table_uid);

        String Crate_table_lastremainder = "Create table lastremainder(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "remaind Integer)";
        db.execSQL(Crate_table_lastremainder);
        //db.close();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS pnem");

        this.onCreate(db);

    }

    public void insuid(int a) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insuid = "Insert into uid (`uid`) Values ('"+ a +"')";
        db.execSQL(insuid);
        db.close();
    }

    public void deluid() {
        SQLiteDatabase db = this.getWritableDatabase();
        String deluid = "Delete from uid";
        db.execSQL(deluid);
        db.close();
    }

    public int getuid() {
        SQLiteDatabase db = this.getReadableDatabase();
        String getusid = "Select uid from uid";
        Cursor cursor = db.rawQuery(getusid, null);
        cursor.moveToFirst();
        int a = 0;
        if (cursor.getCount()>0){
            a = Integer.parseInt(cursor.getString(0));}


        return a;
    }

    public int getuid2() {
        SQLiteDatabase db = this.getReadableDatabase();
        String getusid = "Select uid from uid";
        Cursor cursor = db.rawQuery(getusid, null);
        cursor.moveToFirst();
        int a = 0;
        if (cursor.getCount()>0){
            a = Integer.parseInt(cursor.getString(0));}


        return a;
    }

    public void insremain(int a) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insrem = "Insert into lastremainder (`remaind`) Values (" + a + ")";
        db.execSQL(insrem);
        db.close();
    }

    public void delremain() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delrem = "Delete from lastremainder";
        db.execSQL(delrem);
        db.close();
    }

    public int getremain() {
        SQLiteDatabase db = this.getReadableDatabase();
        String getrem = "Select remaind from lastremainder";
        Cursor cursor = db.rawQuery(getrem, null);
        cursor.moveToFirst();
        int a = 0;
        if (cursor.getCount()>0){
            a = Integer.parseInt(cursor.getString(0));}
        db.close();

        return a;
    }


    public void raisepnem(String a){
        SQLiteDatabase db = this.getWritableDatabase();
        String updtpnem="Update pnem Set used = used + 1 where pnem = '"+a+"'";
        db.execSQL(updtpnem);
        db.close();

    }

    public String getfavpnem(){
        String getfavpnem = "Select value from must where title = 'favpnem'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getfavpnem, null);
        cursor.moveToFirst();
        String fvpnem = cursor.getString(0);

        //db.close();
        cursor.close();
        return fvpnem;
    }

    public void setfavpnem(String a){
        String updtfvpnem="Update must Set value = '"+a+"' where title='favpnem'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updtfvpnem);
        db.close();
    }


    public String getfirstrun(){
        String getfirstrun = "Select value from must where title = 'firstrun'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getfirstrun, null);
        cursor.moveToFirst();

        String firstrun = cursor.getString(0);
        db.close();
        cursor.close();
        return firstrun;
    }

    public void setfirstrun(){
        String updtfirstrun="Update must Set value = 'false' where title = 'firstrun'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updtfirstrun);
        db.close();
    }

    public String[] getprops(String dy, String txt){
        String cimke=null, ido=null, osszeg=null, stb=null;
        int total = 0;
        int negal = -1;
        String query = "Select cimke, time, befizetes, osszeg, pnem, etc From inout " +
                "Where date = '"+dy+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                if (txt.contains(cursor.getString(0)) && txt.contains(cursor.getString(1))){
                    cimke=cursor.getString(0);
                    ido=cursor.getString(1);
                    total=Integer.parseInt(cursor.getString(3));
                    if (Integer.parseInt(cursor.getString(2))==0){
                        total=total*negal;
                    }
                    osszeg=Integer.toString(total)+" "+cursor.getString(4);
                    stb=cursor.getString(5);
                }

            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        String[] props = {cimke, ido, osszeg, stb};
        return props;
    }

    public ArrayList<HashMap<String,String>> gettimeandvalue(String day, String month){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        String query = "Select time, befizetes, osszeg, pnem, cimke from inout where date='"+day+"' and yearmonth='"+month+"' order by time " +
                "ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int osszeg = 0;
        int negal = -1;



        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> timeandvalue= new HashMap<String,String>();
                timeandvalue.put("Line1", cursor.getString(4)+"  "+cursor.getString(0));
                osszeg=Integer.parseInt(cursor.getString(2));
                if (Integer.parseInt(cursor.getString(1))==0){
                    osszeg=osszeg*negal;
                }
                timeandvalue.put("Line2",Integer.toString(osszeg)+" "+cursor.getString(3));
                list.add(timeandvalue);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();


        return list;
    }

    public String getmymoney(){

        int all = this.getmoney()+this.getremain();

        String mmoney = all+" "+this.getfavpnem();
        Log.d("String Mymoney",mmoney);

        return mmoney;
    }

    public int getintallmoney(){

        int all = this.getmoney()+this.getremain();

        Log.d("INTRemain", Integer.toString(this.getremain()));
        Log.d("INTMyMonex", Integer.toString(this.getmoney()));


        return all;
    }

    public int getmoney(){
        Integer money = 0 ;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select befizetes, osszeg from inout where pnem = '"+this.getfavpnem()+"'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do { if (Integer.parseInt(cursor.getString(0))==1){

                money=money+Integer.parseInt(cursor.getString(1));

            } else{

                money=money-Integer.parseInt(cursor.getString(1));
            }


            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return money;
    }

    public ArrayList<String> getallpnem(){
        final ArrayList<String> list = new ArrayList<String>();
        String query = "SELECT  * FROM " + TABLE_pnemDB+" ORDER BY used DESC ";
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(i, cursor.getString(1));


                i++;
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        return list;
    }

    public ArrayList<String> getallmonth(){
        final ArrayList<String> monthlist = new ArrayList<String>();
        String honaplek = "Select DISTINCT yearmonth from inout ORDER BY yearmonth DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(honaplek, null);
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                monthlist.add(i, cursor.getString(0));

                i++;

            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        return monthlist;


    }

    public ArrayList<HashMap<String,String>> getdaysinmonth(String a){
        //final ArrayList<String> days = new ArrayList<String>();
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        String honaplek = "Select DISTINCT date from inout where yearmonth = '"+a+"' ORDER BY date ASC ";

        int negal = -1;
        final SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(honaplek, null);
        if (cursor.moveToFirst()) {
            do {
                int pozosszeg = 0;
                int negosszeg = 0;
                int rec = 0;
                HashMap<String,String> timeandvalue= new HashMap<String,String>();
                //days.add(i, cursor.getString(0));
                String mntvlu = "Select befizetes, osszeg, pnem from inout" +
                        " where yearmonth = '"+a+"' and date = '"+cursor.getString(0)+"' and pnem = '"+this.getfavpnem()+"'";

                timeandvalue.put("Line1", cursor.getString(0));
                Cursor cursor2 = db.rawQuery(mntvlu,null);
                if (cursor2.moveToFirst()) {
                    do {

                        if (Integer.parseInt(cursor2.getString(0))==0){
                            negosszeg=negosszeg+Integer.parseInt(cursor2.getString(1));
                            rec++;
                        } else {
                            pozosszeg=pozosszeg+Integer.parseInt(cursor2.getString(1));
                            rec++;

                        }

                    } while (cursor2.moveToNext());
                }
                timeandvalue.put("Line2",rec+" record  income: "+Integer.toString(pozosszeg)+" "+this.getfavpnem()+
                        " and expense: "+Integer.toString(negosszeg*negal)+" "+this.getfavpnem());
                list.add(timeandvalue);

            } while (cursor.moveToNext());
        }

        Log.d("Lekérdezés :", list.toString());


        db.close();
        cursor.close();

        return list;


    }

    public void insertincome(String cimke, Integer osszeg, String pnem, String etc, String date, String day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        if (date ==null && day ==null ) {
            date = sdf.format(new java.util.Date());
            sdf = new SimpleDateFormat("dd");
            day = sdf.format(new Date());
        }

        sdf = new SimpleDateFormat("HH:mm.ss");
        String time = sdf.format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        String incquery = "Insert into inout (cimke, befizetes, osszeg, pnem, yearmonth, date, time, etc)" +
                "VALUES ('"+cimke+"','1', '"+osszeg+"' , '"+pnem+"' , '"+date+"' , '"+day+"' , '"+time+"' , '"+etc+"')";
        Log.d("Hozzáadás","Hozzáadás "+incquery);
        db.execSQL(incquery);
        db.close();
        super.close();

    }

    public void insertexpense(String cimke, Integer osszeg, String pnem, String etc, String date, String day){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM");
        if (date ==null && day ==null ) {
            date = sdf.format(new java.util.Date());
            sdf = new SimpleDateFormat("dd");
            day = sdf.format(new Date());
        }

        sdf = new SimpleDateFormat("HH:mm.ss");
        String time = sdf.format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        String expquery = "Insert into inout (cimke, befizetes, osszeg, pnem, yearmonth, date, time, etc)" +
                "VALUES ('"+cimke+"','0', '"+osszeg+"' , '"+pnem+"' , '"+date+"' , '"+day+"' , '"+time+"' , '"+etc+"')";
        Log.d("Hozzáadás","Hozzáadás "+expquery);
        db.execSQL(expquery);
        db.close();
        super.close();



    }

    public int getrownum(){
        String rownum = "Select count(id) from inout";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(rownum, null);
        cursor.moveToFirst();
        int count = Integer.parseInt(cursor.getString(0));
        Log.d("Rownum:",Integer.toString(count));
        db.close();
        super.close();
        return count;
    }

    public List<NameValuePair> upload(){
        List<NameValuePair> adat = new ArrayList<NameValuePair>();
        String gettoprow = "Select * from inout limit 1";
        SQLiteDatabase db = this.getReadableDatabase();
        int a = this.getuid();
        Cursor cursor = db.rawQuery(gettoprow, null);
        cursor.moveToFirst();
        int id = Integer.parseInt(cursor.getString(0));
        adat.add(new BasicNameValuePair("cimke", cursor.getString(1)));
        adat.add(new BasicNameValuePair("befizetes", cursor.getString(2)));
        adat.add(new BasicNameValuePair("osszeg", cursor.getString(3)));
        adat.add(new BasicNameValuePair("pnem", cursor.getString(4)));
        adat.add(new BasicNameValuePair("yearmonth", cursor.getString(5)));
        adat.add(new BasicNameValuePair("date", cursor.getString(6)));
        adat.add(new BasicNameValuePair("time", cursor.getString(7)));
        adat.add(new BasicNameValuePair("etc", cursor.getString(8)));
        adat.add(new BasicNameValuePair("uid", Integer.toString(a) ));
        db.close();
        String delrow = "delete from inout where id = "+id;
        SQLiteDatabase db2 = this.getWritableDatabase();
        db2.execSQL(delrow);
        db2.close();
        Log.d("Az adat: ", adat.toString());
        super.close();
        return adat;
    }
}