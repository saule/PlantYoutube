package plant.kz.aygolek.plantyoutube.DAO;

import org.json.JSONException;

import java.util.List;

import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;

/**
 * Created by Lagrange-Support on 26/03/2018.
 */

public interface IPlantDAO {
    List<PlantDTO> fetchPlants(String searchTerm) throws JSONException;
}
