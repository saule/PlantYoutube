package plant.kz.aygolek.plantyoutube.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lagrange-Support on 03/04/2018.
 */

class PlantPlacesDAO extends SQLiteOpenHelper {
    public static final String PLANTS = "PLANTS";
    public static final String CACHE_ID = "CACHE_ID";
    public static final String GUID = "GUID";
    public static final String GENUS = "GENUS";
    public static final String SPECIES = "SPECIES";
    public static final String CULTIVAR = "CULTIVAR";
    public static final String COMMON = "COMMON";


    //specimen
    public static final String SPECIMENS = "specimens";
    public static final String PLANT_CACHE_ID = "PLANT_CACHE_ID";
    public static final String PLANT_GUID = "PLANT_GUID";
    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DESCRIPTION = "description";
    public static final String PICTURE_URI = "picture_uri";


    public PlantPlacesDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPlants = "CREATE TABLE " + PLANTS + " ( " + CACHE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GUID + " INTEGER, " + GENUS + " TEXT, " +
                SPECIES + " TEXT, " + CULTIVAR + " TEXT, " + COMMON + " TEXT " + " );";
        String createSpecimens = "CREATE TABLE " + SPECIMENS + " ( " + PLANT_CACHE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLANT_GUID + " INTEGER, " + LOCATION + " TEXT, " +
                LATITUDE + " TEXT, " + LONGITUDE + " TEXT, " + DESCRIPTION + " TEXT, "
                + PICTURE_URI + " TEXT " + " );";
        db.execSQL(createPlants);
        db.execSQL(createSpecimens);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
