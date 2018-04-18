package plant.kz.aygolek.plantyoutube.DAO;

import java.util.Set;

import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;

/**
 * Created by Lagrange-Support on 28/03/2018.
 */

public interface IOfflinePlantDAO extends IPlantDAO {
    void insert(PlantDTO plant);

    Set<Integer> fetchAllGuids();

    int countPlants();
}
