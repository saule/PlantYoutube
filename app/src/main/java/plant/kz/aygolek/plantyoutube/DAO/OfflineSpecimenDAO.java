package plant.kz.aygolek.plantyoutube.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;
import plant.kz.aygolek.plantyoutube.DTO.SpecimenDTO;

/**
 * Created by Lagrange-Support on 30/03/2018.
 */

public class OfflineSpecimenDAO extends PlantPlacesDAO implements ISpecimenDAO {

    //extends PlantsPlacesDAO



    public OfflineSpecimenDAO(Context ctx){
        super(ctx, "plantplaces2.db", null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //  String createSpecimens = "CREATE TABLE " + SPECIMENS + " ( " + CACHE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             //   PLANT_GUID + " INTEGER, " + LOCATION + " TEXT, " +
               // LATITUDE + " REAL, " + LONGITUDE + " REAL, " + DESCRIPTION + " TEXT, "
               // + PICTURE_URI + " TEXT " + " );";
     //   db.execSQL(createSpecimens);
    }

    @Override
    public void save(SpecimenDTO specimenDTO){
        ContentValues contentValues=new ContentValues();

        contentValues.put(PLANT_CACHE_ID, specimenDTO.getCache_id());
        contentValues.put(PLANT_GUID, specimenDTO.getGuid());
        contentValues.put(LOCATION,specimenDTO.getLocation());
        contentValues.put(LATITUDE, specimenDTO.getLatitude());
        contentValues.put(LONGITUDE,specimenDTO.getLongitude());
        contentValues.put(DESCRIPTION,specimenDTO.getDescription());
        contentValues.put(PICTURE_URI,specimenDTO.getPicture_URI()); //getPhoto();

        long cache_id = getWritableDatabase().insert(SPECIMENS, LATITUDE, contentValues);
        specimenDTO.setCache_id(cache_id);
    }

    @Override
    public List<PlantDTO> search(String searchTerm){
        String sql="SELECT " + "p." + GENUS + ", " + "p." + SPECIES + ", " + "p." + CULTIVAR + ", " + "p." + COMMON + ", " +
                "s." + LATITUDE + ", "+"s." + LONGITUDE + ", " + "s." + LOCATION + ", " + "s." + DESCRIPTION +
                "FROM " + SPECIMENS + " s " + " JOIN " + PLANTS +" p "+
                " ON s." + CACHE_ID + " = p." + CACHE_ID +
                " WHERE p." + GENUS + " LIKE '%" + searchTerm + "%' OR "
                + " p." + SPECIES + " LIKE '%" + searchTerm + "%' OR "
                + " p." + CULTIVAR + " LIKE '%" + searchTerm + "%' OR "
                + " p." + COMMON + " LIKE '%" + searchTerm + "%' OR ";

        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        ArrayList<PlantDTO> allPlants = new ArrayList<PlantDTO>();

        if (cursor.getCount()>0){
            cursor.moveToFirst();
            while(cursor.isAfterLast()){
                //plant data
                String genus = cursor.getString(0);
                String species = cursor.getString(1);
                String cultivar = cursor.getString(2);
                String common = cursor.getString(3);

                PlantDTO plant = new PlantDTO();
                plant.setGenus(genus);
                plant.setSpecies(species);
                plant.setCultivar(cultivar);
                plant.setCommon(common);


                //specimen data
                String latitude = cursor.getString(4);
                String longitude = cursor.getString(5);
                String location = cursor.getString(6);
                String description = cursor.getString(7);

                SpecimenDTO specimen = new SpecimenDTO();
                specimen.setLatitude(latitude);
                specimen.setLongitude(longitude);
                specimen.setLocation(location);
                specimen.setDescription(description);

                List<SpecimenDTO> allSpecimens = new ArrayList<SpecimenDTO>();
                allSpecimens.add(specimen);

                plant.setSpecimenDTO(allSpecimens);
                allPlants.add(plant);
                cursor.moveToNext();
            }
        }

        cursor.close();

        return allPlants;
    }

    @Override
    public List<PlantDTO> search(double latitude, double longitude, double range) {
       //TODO
        return null;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
