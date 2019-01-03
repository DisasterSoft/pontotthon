package com.ewulusen.disastersoft.pontottohon;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * Created by diszterhoft.zoltan on 2018.10.11
 * Ebben a javafájlban fogom létrehozni az adatbázisokat amivel dolgozni fogunk
 */

public class databaseHelper extends SQLiteOpenHelper {
    /**
     * Előszőr is létrhozzuk az összes változót amivel dolgozni fogunk.
     */
    public static final String DatabaseName = "pontotthon.db";
    public static final String userTable = "user_PTT";
    public static final String bevetelTable = "bevetel_PTT";
    public static final String kiadasTable = "kiadas_PTT";
    public static final String savingTable = "saving_PTT";
    public static final String deptTable = "dept_PTT";
    private Context context;
    public databaseHelper(Context paramContext)

    {
        super(paramContext, DatabaseName, null, 2);
        this.context = paramContext;
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
        paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+userTable+" " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "First_Name TEXT," +
                "Last_Name TEXT," +
                "Hause TEXT, " +
                "Telephon TEXT," +
                "Email TEXT," +
                "SuperUser TEXT," +
                "username TEXT," +
                "password TEXT," +
                "Emelet TEXT," +
                "Ajto TEXT," +
                "nyelv TEXT," +
                "Last_Login TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "Now_Login TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
        paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+bevetelTable+" (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UID TEXT," +
                "HUID TEXT DEFAULT 0," +
                "EID TEXT DEFAULT 0, " +
                "Name TEXT," +
                "FIX TEXT DEFAULT 0," +
                "Cost TEXT," +
                "Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
        paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+kiadasTable+" (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UID TEXT," +
                "HUID TEXT DEFAULT 0," +
                "EID TEXT DEFAULT 0, " +
                "Name TEXT," +
                "FIX TEXT DEFAULT 0," +
                "Cost TEXT," +
                "Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
        paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+savingTable+" (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UID TEXT," +
                "HID TEXT DEFAULT 0," +
                "money TEXT," +
                "Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
        paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+deptTable+" (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UID TEXT," +
                "HID TEXT DEFAULT 0," +
                "money TEXT," +
                "Name TEXT," +
                "Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");


    }
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
        paramSQLiteDatabase.execSQL("DROP TABLE if EXISTS "+userTable);
        paramSQLiteDatabase.execSQL("DROP TABLE if EXISTS "+kiadasTable);
        paramSQLiteDatabase.execSQL("DROP TABLE if EXISTS "+bevetelTable);
        paramSQLiteDatabase.execSQL("DROP TABLE if EXISTS "+deptTable);
        paramSQLiteDatabase.execSQL("DROP TABLE if EXISTS "+savingTable);
        onCreate(paramSQLiteDatabase);
    }
    public void login(String Email, String Name)
    {

        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        SQLiteDatabase localWSQLiteDatabase = getWritableDatabase();
        String str1 = "SELECT * FROM "+userTable+" WHERE Email='"+Email+"'";
        Cursor localCursor = localSQLiteDatabase.rawQuery(str1, null);
        if(localCursor.getCount()==1) {
            ContentValues localContentValues = new ContentValues();
            localContentValues.put("Now_Login","CURRENT_TIMESTAMP");
            localSQLiteDatabase.update(userTable, localContentValues, "Email='"+Email+"'", null);
        }
        else
        {
            ContentValues localContentValues = new ContentValues();
            String[] seged_a;
            seged_a= Name.split(" ");
            localContentValues.put("First_Name", seged_a[0]);
            localContentValues.put("Last_Name", seged_a[1]);
            localContentValues.put("Email", Email);
            localSQLiteDatabase.insert(userTable, null, localContentValues);

        }
       // databasePrinter(userTable);
    }
    /**
     * hozzá ad egy sort az User bevétel táblájához
     * @id az email címe a felhasználónak
     * @return boolen
     */
    public void addBevetelUser(String id,String name,String fix,String cost)
    {
        String UID=getUser(id);
        Date now = new Date();//dateFormatter.format(now).toString();
        SimpleDateFormat dateM = new SimpleDateFormat("MM");
        SimpleDateFormat dateY = new SimpleDateFormat("y");
        SQLiteDatabase localSQLiteDatabaseR = getReadableDatabase();
        String  str2 = "SELECT * FROM "+bevetelTable+" where UID='"+UID+"' and" +
                " strftime('%m',Created)='"+dateM.format(now).toString()+"' and " +
                "strftime('%Y',Created)='"+dateY.format(now).toString()+"' " +
                "and Name='"+name+"'and fix='0'";
        Cursor cursor1=localSQLiteDatabaseR.rawQuery(str2, null);
        //   Log.d("bevetel ColumnCount",cursor1.getCount()+"");
        if(cursor1.getCount()>0)
        {
            Random r = new Random();
            int i1 = r.nextInt(100);
            name=name+i1;
        }
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("UID", UID);
        localContentValues.put("Name", name);
        localContentValues.put("FIX", fix);
        localContentValues.put("Cost", cost);
        localSQLiteDatabase.insert(bevetelTable, null, localContentValues);
        if(fix.equals("1"))
        {
            localContentValues = new ContentValues();
            localContentValues.put("UID",UID);
            localContentValues.put("Name", name);
            localContentValues.put("FIX", "0");
            localContentValues.put("Cost", cost);
            localSQLiteDatabase.insert(bevetelTable, null, localContentValues);
        }
        //databasePrinter(bevetelTable);
    }
    public Cursor getBevetelUser(String email,String fix)
    {
        Date now = new Date();//dateFormatter.format(now).toString();
        SimpleDateFormat dateM = new SimpleDateFormat("MM");
        SimpleDateFormat dateY = new SimpleDateFormat("y");
        String uid=getUser(email);
        String extra=" SELECT * FROM "+bevetelTable+" where UID='"+uid+"' and " +
            "strftime('%m',Created)='"+dateM.format(now).toString()+"' and "+
            "strftime('%Y',Created)='"+dateY.format(now).toString()+"' and fix<>'1'";
        if(fix.equals("1"))
        {
            extra=" SELECT * FROM "+bevetelTable+" where UID='"+uid+"' and fix='1'";
        }
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        String str1 = "SELECT * FROM "+bevetelTable+" where UID='"+uid+"' and fix='1'";
       // Log.d("bevetel str1",str1);
        Cursor cursor = localSQLiteDatabase.rawQuery(str1, null);
        while(cursor.moveToNext())
        {
          String  str2 = "SELECT * FROM "+bevetelTable+" where UID='"+uid+"' and" +
                    " strftime('%m',Created)='"+dateM.format(now).toString()+"' and " +
                    "strftime('%Y',Created)='"+dateY.format(now).toString()+"' " +
                  "and Name like '"+cursor.getString(cursor.getColumnIndex("Name"))+"%' and fix='0'";
         //   Log.d("bevetel str2",str2);
          Cursor cursor1=localSQLiteDatabase.rawQuery(str2, null);
         // Log.d("bevetel ColumnCount",cursor1.getCount()+"");
          if(cursor1.getCount()>0)
          {

          }
          else {
          //    Log.d("bevetel insert", "Bejött");
              addBevetelUser(email, cursor.getString(cursor.getColumnIndex("Name")), "0", cursor.getString(cursor.getColumnIndex("Cost")));
          }
        }
        str1 = extra;
        //Log.d("select", str1);
        cursor = localSQLiteDatabase.rawQuery(str1, null);
        return  cursor;
    }
    public void addKiadasUser(String id,String name,String fix,String cost)
    {
        String UID=getUser(id);
        Date now = new Date();//dateFormatter.format(now).toString();
        SimpleDateFormat dateM = new SimpleDateFormat("MM");
        SimpleDateFormat dateY = new SimpleDateFormat("y");
        SQLiteDatabase localSQLiteDatabaseR = getReadableDatabase();
        String  str2 = "SELECT * FROM "+kiadasTable+" where UID='"+UID+"' and" +
                " strftime('%m',Created)='"+dateM.format(now).toString()+"' and " +
                "strftime('%Y',Created)='"+dateY.format(now).toString()+"' " +
                "and Name='"+name+"' and fix='0'";
        Cursor cursor1=localSQLiteDatabaseR.rawQuery(str2, null);
        //   Log.d("bevetel ColumnCount",cursor1.getCount()+"");
        if(cursor1.getCount()>0)
        {
            Random r = new Random();
            int i1 = r.nextInt(100);
            name=name+i1;
        }


        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("UID",UID);
        localContentValues.put("Name", name);
        localContentValues.put("FIX", fix);
        localContentValues.put("Cost", cost);
        localSQLiteDatabase.insert(kiadasTable, null, localContentValues);
        if(fix.equals("1"))
        {
            localContentValues = new ContentValues();
            localContentValues.put("UID",UID);
            localContentValues.put("Name", name);
            localContentValues.put("FIX", "0");
            localContentValues.put("Cost", cost);
            localSQLiteDatabase.insert(kiadasTable, null, localContentValues);
        }

    }
    public Cursor getKiadasUser(String email,String fix)
    {
        Date now = new Date();//dateFormatter.format(now).toString();
        SimpleDateFormat dateM = new SimpleDateFormat("MM");
        SimpleDateFormat dateY = new SimpleDateFormat("y");
        String uid=getUser(email);
        String extra=" SELECT * FROM "+kiadasTable+" where UID='"+uid+"' and " +
                "strftime('%m',Created)='"+dateM.format(now).toString()+"' and "+
                "strftime('%Y',Created)='"+dateY.format(now).toString()+"' and fix<>'1'";
        if(fix.equals("1"))
        {
            extra=" SELECT * FROM "+kiadasTable+" where UID='"+uid+"' and fix='1'";
        }
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        String str1 = "SELECT * FROM "+kiadasTable+" where UID='"+uid+"' and fix='1'";
        Cursor cursor = localSQLiteDatabase.rawQuery(str1, null);
        while(cursor.moveToNext())
        {
            String  str2 = "SELECT * FROM "+kiadasTable+" where UID='"+uid+"' and" +
                    " strftime('%m',Created)='"+dateM.format(now).toString()+"' and " +
                    "strftime('%Y',Created)='"+dateY.format(now).toString()+"' " +
                    "and Name like '"+cursor.getString(cursor.getColumnIndex("Name"))+"%' and fix='0'";
            //Log.d("kiadás str2",str2);
            Cursor cursor1=localSQLiteDatabase.rawQuery(str2, null);
            if(cursor1.getCount()>0)
            {

            }
            else
            {
                addKiadasUser(email, cursor.getString(cursor.getColumnIndex("Name")), "0", cursor.getString(cursor.getColumnIndex("Cost")));
            }
            cursor1.close();
        }
        cursor.close();
        str1 = extra;
   // Log.d("kiadás",str1);
        cursor = localSQLiteDatabase.rawQuery(str1, null);
        return  cursor;
    }
    public String getUser(String email)
    {
        String id="0";
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        String str1 = "SELECT * FROM "+userTable+" where Email='"+email+"'";
        Cursor cursor = localSQLiteDatabase.rawQuery(str1, null);
        cursor.moveToNext();
        id=cursor.getString(cursor.getColumnIndex("ID"));
    return id;
    }
    public void databasePrinter(String tabla)
    {

        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        String str1 = "SELECT * FROM "+tabla+"";
        Cursor cursor = localSQLiteDatabase.rawQuery(str1, null);

        while (cursor.moveToNext()) {
           Log.d(tabla,cursor.getString(cursor.getColumnIndex("UID")));
           Log.d(tabla,cursor.getString(cursor.getColumnIndex("Name")));
           Log.d(tabla,cursor.getString(cursor.getColumnIndex("Created")));
           Log.d(tabla,cursor.getString(cursor.getColumnIndex("FIX")));
           Log.d(tabla,cursor.getString(cursor.getColumnIndex("Cost")));
        }
    }
    public void deleteFromBevetel(String name,String cost,String email)
    {
        Date now = new Date();//dateFormatter.format(now).toString();
        SimpleDateFormat dateM = new SimpleDateFormat("MM");
        SimpleDateFormat dateY = new SimpleDateFormat("y");
        String uid=getUser(email);
        SQLiteDatabase localSQLiteDatabaseW = getWritableDatabase();
        localSQLiteDatabaseW.delete(bevetelTable,"UID='"+uid+"' and  strftime('%m',Created)='"+dateM.format(now)+"' and " +
                "strftime('%Y',Created)='"+dateY.format(now)+"' and Name='"+name+"' and Cost='"+cost+"' and fix<>1",null);
    }
    public void deleteFromBevetelByID(String ID)
    {
         SQLiteDatabase localSQLiteDatabaseW = getWritableDatabase();
        localSQLiteDatabaseW.delete(bevetelTable,"ID='"+ID+"'",null);
    }
    public void deleteFromKiadas(String name,String cost,String email)
    {
        Date now = new Date();//dateFormatter.format(now).toString();
        SimpleDateFormat dateM = new SimpleDateFormat("MM");
        SimpleDateFormat dateY = new SimpleDateFormat("y");
        String uid=getUser(email);
        SQLiteDatabase localSQLiteDatabaseW = getWritableDatabase();
        localSQLiteDatabaseW.delete(kiadasTable,"UID='"+uid+"' and  strftime('%m',Created)='"+dateM.format(now)+"' and " +
                "strftime('%Y',Created)='"+dateY.format(now)+"' and Name='"+name+"' and Cost='"+cost+"' and fix<>1",null);
    }
    public void deleteFromKiadasByID(String ID)
    {
        SQLiteDatabase localSQLiteDatabaseW = getWritableDatabase();
        localSQLiteDatabaseW.delete(kiadasTable,"ID='"+ID+"'",null);
    }
    public void deleteFromHitelByID(String ID,String Name,String email)
    {
        String uid=getUser(email);
        SQLiteDatabase localSQLiteDatabaseW = getWritableDatabase();
        localSQLiteDatabaseW.delete(deptTable,"ID='"+ID+"' and Name='"+Name+"'" ,null);
        localSQLiteDatabaseW.delete(kiadasTable,"Name='"+Name+"' and UID='"+uid+"'",null);

        //és így tovább
    }

    public void addMegtakaritas(String ertek,String id)
    {
        String UID=getUser(id);
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("UID", UID);
        localContentValues.put("money",ertek);
        localSQLiteDatabase.insert(savingTable, null, localContentValues);
        localContentValues = new ContentValues();
        localContentValues.put("UID", UID);
        localContentValues.put("Name",this.context.getString(R.string.megtakaritas));
        localContentValues.put("Cost",ertek);
        localContentValues.put("FIX","0");
        localSQLiteDatabase.insert(kiadasTable, null, localContentValues);
    }
    public String getMegtakaritas(String ertek,String id)
    {
        String vissza="";
        String osszeg=getAllmegtakaritas(id);
        if(Integer.parseInt(osszeg)<Integer.parseInt(ertek))
        {
            vissza="tulsok";
        }
        else {
            String UID = getUser(id);
            SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
            ContentValues localContentValues = new ContentValues();
            localContentValues.put("UID", UID);
            localContentValues.put("money", "-" + ertek);
            localSQLiteDatabase.insert(savingTable, null, localContentValues);
            localContentValues = new ContentValues();
            localContentValues.put("UID", UID);
            localContentValues.put("Name", this.context.getString(R.string.megtakaritas));
            localContentValues.put("Cost", ertek);
            localContentValues.put("FIX", "0");
            localSQLiteDatabase.insert(bevetelTable, null, localContentValues);
        }
        return vissza;
    }
    public String getAllmegtakaritas(String id)
    {
        String UID=getUser(id);
        String osszeg="0";
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        String str1 = "SELECT * FROM "+savingTable+" where UID='"+UID+"'";

        Cursor cursor = localSQLiteDatabase.rawQuery(str1, null);

        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                osszeg=Integer.toString(Integer.parseInt(osszeg)+Integer.parseInt(cursor.getString(cursor.getColumnIndex("money"))));
            }
        }
        return osszeg;
    }
    public Cursor getHitelekUser(String email)
    {
        String uid=getUser(email);
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        String str1 = "SELECT * FROM "+deptTable+" where UID='"+uid+"'";
        //Log.d("select", str1);
       Cursor cursor = localSQLiteDatabase.rawQuery(str1, null);
        return  cursor;
    }
    public String addHitelekUser(String email,String kezdet,String veg,String Name,String Cost)
    {
        String eredmeny="";
        String uid=getUser(email);
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        String str1 = "SELECT * FROM "+deptTable+" where UID='"+uid+"' and Name='"+Name+"' ";
        //Log.d("select", str1);
       Cursor cursor = localSQLiteDatabase.rawQuery(str1, null);
       if(cursor.getCount()>0)
       {
           eredmeny="van_mar";
       }
       else
       {
           SQLiteDatabase localSQLiteDatabaseW = getWritableDatabase();
           ContentValues localContentValues = new ContentValues();
           localContentValues.put("UID", uid);
           localContentValues.put("money",Cost);
           localContentValues.put("Name",Name);
           localSQLiteDatabaseW.insert(deptTable, null, localContentValues);
           localContentValues.clear();
           String[] split_a=kezdet.split("/");
           int kezd_Y=Integer.parseInt(split_a[0]);
           int kezd_M=Integer.parseInt(split_a[1]);
           split_a=veg.split("/");
           int veg_Y=Integer.parseInt(split_a[0]);
           int veg_M=Integer.parseInt(split_a[1]);
           int vigyazz=0;
           for(int i=kezd_M;i<13;i++) {
              /* Log.d("kezdet_Y", ":" + kezd_Y);
               Log.d("kezdet_M", ":" + kezd_M);
               Log.d("veg_Y", ":" + veg_Y);
               Log.d("veg_M", ":" + veg_M);
               Log.d("vigyázz", ":" + vigyazz);
               Log.d("i", ":" + i);*/
               if (i == 12) {
                   localContentValues.put("UID", uid);
                   localContentValues.put("Cost", Cost);
                   localContentValues.put("Name", Name);
                   localContentValues.put("Created", Integer.toString(kezd_Y) + "-" + i + "-28T15:36:56.200");
                   localSQLiteDatabaseW.insert(kiadasTable, null, localContentValues);
                   localContentValues.clear();
                   if (kezd_Y < veg_Y) {
                       kezd_Y = kezd_Y + 1;
                       i = 0;
                   }
                   if (kezd_Y == veg_Y) {
                       vigyazz = 1;
                   }
               } else {
                   String created = Integer.toString(i);
                   if (i < 10) {
                       created = "0" + Integer.toString(i);
                   }
                   localContentValues.put("UID", uid);
                   localContentValues.put("Cost", Cost);
                   localContentValues.put("Name", Name);
                   localContentValues.put("Created", Integer.toString(kezd_Y) + "-" + created + "-28T15:36:56.200");
                   localSQLiteDatabaseW.insert(kiadasTable, null, localContentValues);
                   localContentValues.clear();
                   if (vigyazz == 1) {
                       if (i == veg_M) {
                           i=13;
                           break;
                       }
                   }
               }
           }
       }
        return  eredmeny;
    }
    public String getOldCfbyUser(String email)
    {
        String ered="üres";
        String uid=getUser(email);
        SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
        return ered;
    }
}
