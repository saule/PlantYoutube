package plant.kz.aygolek.plantyoutube.DAO;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;
import plant.kz.aygolek.plantyoutube.DTO.SpecimenDTO;

/**
 * Created by Lagrange-Support on 26/03/2018.
 */

public class PlantDAOStub implements IPlantDAO {

    @Override
    public List<PlantDTO> fetchPlants(String searchTerm) throws JSONException {
       //declare our return type
        List<PlantDTO> allPlants = new ArrayList<>();

        //populate the list of allPlants with a hardcoded, known set of plants
        PlantDTO easternRedbud = new PlantDTO();
        easternRedbud.setGenus("Cercis");
        easternRedbud.setCommon("Eastern Redbud");
        easternRedbud.setSpecies("canadensis");

        //add eastern redbud to our collection
        allPlants.add(easternRedbud);

        PlantDTO chineseRedbud = new PlantDTO();
        chineseRedbud.setGenus("Cercis");
        chineseRedbud.setCommon("Chinese Redbud");
        chineseRedbud.setSpecies("chinensis");
        //add eastern redbud to our collection
        allPlants.add(chineseRedbud);


        PlantDTO lavendarTwistRedbud = new PlantDTO();
        lavendarTwistRedbud.setGenus("Cercis");
        lavendarTwistRedbud.setCommon("Lavendar Twist");
        lavendarTwistRedbud.setSpecies("canadensis");
        lavendarTwistRedbud.setCultivar("Lavendar Twist");
        //add eastern redbud to our collection
        allPlants.add(lavendarTwistRedbud);

        return allPlants;
    }
}
