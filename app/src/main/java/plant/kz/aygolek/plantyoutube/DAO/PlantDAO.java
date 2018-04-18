package plant.kz.aygolek.plantyoutube.DAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;

/**
 * Created by Lagrange-Support on 26/03/2018.
 */

public class PlantDAO implements IPlantDAO {
    private final NetworkDAO networkDAO;

    public PlantDAO(){
        networkDAO=new NetworkDAO();

    }

    @Override
    public List<PlantDTO> fetchPlants(String searchTerm) throws JSONException{
        String uri ="http://plantplaces.com/perl/mobile/viewplantsjson.pl?Combined_Name="+searchTerm;
        //string result von unserem request
        String request = networkDAO.request(uri);
        //variable to holld our return data.
        List<PlantDTO> allPlants=new ArrayList<PlantDTO>();
        //return all Plants
        JSONObject root=new JSONObject(request);
        //jason file object is plants
        JSONArray plants=root.getJSONArray("plants");
        //in the array objects
        for (int i=0;i<plants.length();i++){
            //parse json objects in fields and vales
            JSONObject jsonPlant = plants.getJSONObject(i);
            int guid = jsonPlant.getInt("id");
            String genus = jsonPlant.getString("genus");
            String species=jsonPlant.getString("species");
            String cultivar = jsonPlant.getString("cultivar");
            String common = jsonPlant.getString("common");

            //create dta plant object we wll populate with json data
            PlantDTO plant=new PlantDTO();
            plant.setGenus(genus);
            plant.setCultivar(cultivar);
            plant.setGuid(guid);
            plant.setCommon(common);
            plant.setSpecies(species);

            //add to our collection
            allPlants.add(plant);

        }
        return allPlants;
    }
}
