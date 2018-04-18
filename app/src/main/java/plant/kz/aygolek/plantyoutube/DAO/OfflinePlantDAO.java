package plant.kz.aygolek.plantyoutube.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;

/**
 * Created by Lagrange-Support on 26/03/2018.
 */

public class OfflinePlantDAO extends PlantPlacesDAO implements IOfflinePlantDAO {


    public OfflinePlantDAO(Context ctx ){
        super(ctx,"plantplaces2.db",null,2);
    }

    @Override
    public List<PlantDTO> fetchPlants(String searchTerm) throws JSONException {
        List<PlantDTO> allPlants = new ArrayList<>();

        String sql = "SELECT * "+" FROM "+PLANTS;

        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                PlantDTO plantDTO = new PlantDTO();

                int guid = cursor.getInt(cursor.getColumnIndex(GUID));
                String genus = cursor.getString(cursor.getColumnIndex(GENUS));
                String species = cursor.getString(cursor.getColumnIndex(SPECIES));
                String cultivar = cursor.getString(cursor.getColumnIndex(CULTIVAR));
                String common = cursor.getString(cursor.getColumnIndex(COMMON));
                long cache_id = cursor.getLong(cursor.getColumnIndex(CACHE_ID));

                plantDTO.setCultivar(cultivar);
                plantDTO.setSpecies(species);
                plantDTO.setCommon(common);
                plantDTO.setGenus(genus);
                plantDTO.setGuid(guid);
                plantDTO.setCacheID(cache_id);


                //allGuids.add(Integer.valueOf(guid));
                allPlants.add(plantDTO);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return allPlants;
    }

    @Override
    public void insert(PlantDTO plant){
        ContentValues cv= new ContentValues();
        cv.put(GUID, plant.getGuid());
        cv.put(GENUS, plant.getGenus());
        cv.put(SPECIES, plant.getSpecies());
        cv.put(CULTIVAR, plant.getCultivar());
        cv.put(COMMON, plant.getCommon());

        long cache_id=getWritableDatabase().insert(PLANTS, GENUS, cv);

        plant.setCacheID(cache_id);

    }

    @Override
    public Set<Integer> fetchAllGuids(){
        Set<Integer> allGuids = new HashSet<Integer>();

        String sql = "SELECT "+GUID+" FROM "+PLANTS;

        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                int guid = cursor.getInt(cursor.getColumnIndex(GUID));
                allGuids.add(Integer.valueOf(guid));

                cursor.moveToNext();
            }
        }

        cursor.close();
        return allGuids;
    }

    @Override
    public int countPlants(){
        int plantCount=0;
        String sql="SELECT COUNT(*) FROM "+PLANTS;
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        //did we get result?
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            plantCount=cursor.getInt(0);//columnidenx 0, because only 1 column

        }
        cursor.close();
        return plantCount;

    }
}
