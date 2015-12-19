package eliteheberg.sora.org.kuizu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by GaSs on 10/12/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "yunoquiz.db";
    public static final String LOGIN_TABLE_NAME = "login";
    public static final String LOGIN_COLUMN_loginid = "loginid";
    public static final String LOGIN_COLUMN_pwd = "password";

    public static final String HISTO_TABLE_NAME = "histo";
    public static final String HISTO_COLUMN_id = "id";
    public static final String HISTO_COLUMN_idadvers = "idadvers";
    public static final String HISTO_COLUMN_score = "score";
    public static final String HISTO_COLUMN_scoreadvers = "scoreadvers";
    public static final String HISTO_COLUMN_dateduel = "dateduel";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LOGIN_TABLE_NAME + "(" + LOGIN_COLUMN_loginid + " text primary key, " +
                LOGIN_COLUMN_pwd + " text)");
        db.execSQL("create table " + HISTO_TABLE_NAME + "(" + HISTO_COLUMN_id + " integer primary key autoincrement, " +
                HISTO_COLUMN_idadvers + " text, " + HISTO_COLUMN_score + " integer, " + HISTO_COLUMN_scoreadvers + " integer" +
                ", " + HISTO_COLUMN_dateduel + " text)");

        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTO_COLUMN_idadvers, "ksibro");
        contentValues.put(HISTO_COLUMN_score, 200);
        contentValues.put(HISTO_COLUMN_scoreadvers, 300);
        contentValues.put(HISTO_COLUMN_dateduel, "12/12/2012");
        db.insert(HISTO_TABLE_NAME, null, contentValues);

        contentValues.put(HISTO_COLUMN_idadvers, "ziad");
        contentValues.put(HISTO_COLUMN_score, 500);
        contentValues.put(HISTO_COLUMN_scoreadvers, 900);
        contentValues.put(HISTO_COLUMN_dateduel, "13/12/2012");
        db.insert(HISTO_TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + LOGIN_TABLE_NAME);
        db.execSQL("drop table if exists " + HISTO_TABLE_NAME);
        onCreate(db);
    }

    void rememberme(String loginid, String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (!getrememberme()) {
            contentValues.put(LOGIN_COLUMN_pwd, pwd);
            contentValues.put(LOGIN_COLUMN_loginid, loginid);

            db.insert(LOGIN_TABLE_NAME, null, contentValues);
        }
    }

    boolean getrememberme() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + LOGIN_TABLE_NAME, null);
        return res.moveToFirst();
    }

    String getloginid() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + LOGIN_COLUMN_loginid + " from " + LOGIN_TABLE_NAME, null);
        res.moveToFirst();
        return res.getString(0);
    }

    String getpwd() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + LOGIN_COLUMN_pwd + " from " + LOGIN_TABLE_NAME, null);
        res.moveToFirst();
        return res.getString(0);
    }

    void logout () {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(LOGIN_TABLE_NAME, LOGIN_COLUMN_loginid + "='" + Client.login + "'", null);
    }

    void addHisto (String idadvers, int score, int scoreadvers, String dateduel) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTO_COLUMN_idadvers, idadvers);
        contentValues.put(HISTO_COLUMN_score, score);
        contentValues.put(HISTO_COLUMN_scoreadvers, scoreadvers);
        contentValues.put(HISTO_COLUMN_dateduel, dateduel);
        db.insert(HISTO_TABLE_NAME, null, contentValues);
    }

    ArrayList<Match> gethisto() {
        ArrayList<Match> listhisto = new ArrayList<Match>();
        Match histo;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + HISTO_TABLE_NAME, null);

        if (cr.moveToFirst()) {
            do {
                histo = new Match(cr.getString(cr.getColumnIndex(HISTO_COLUMN_idadvers)), cr.getInt(cr.getColumnIndex(HISTO_COLUMN_score)),
                        cr.getInt(cr.getColumnIndex(HISTO_COLUMN_scoreadvers)), cr.getString(cr.getColumnIndex(HISTO_COLUMN_dateduel)));
                listhisto.add(histo);
            } while (cr.moveToNext());
            return listhisto;
        }
        return null;
    }
}