package plant.kz.aygolek.plantyoutube.test;

import android.test.InstrumentationTestCase;

import java.util.List;

import plant.kz.aygolek.plantyoutube.DAO.ISpecimenDAO;
import plant.kz.aygolek.plantyoutube.DAO.SpecimenDAOStub;
import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;
import plant.kz.aygolek.plantyoutube.DTO.SpecimenDTO;

/**
 * Created by Lagrange-Support on 01/04/2018.
 */

public class SpecimenDAOTest extends InstrumentationTestCase {

    private SpecimenDTO specimenDTO;
    private ISpecimenDAO specimenDAO;
    private String searchTerm;
    private List<PlantDTO> plantDTOs;
    private boolean specimenHasSaved;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        specimenDAO = new SpecimenDAOStub();

    }

    public void testSaveValidSpecimen(){
        givenSpecimenIsInitializedWithData();
        whenSpecimenIsSaved();
        thenVerifyNoException();
    }




    public void testInvalidSpecimenThrowsExceptionOnSave(){
        givenSpecimenIsNotInitialized();
        whenSpecimenIsSaved();
        thenVerifyException();
    }

    public void testRedbudSearchReturnRedbud(){
        givenSearchTermInitializedToRedbud();
        whenSearched();
        thenVerifyRedBudReturned();
    }

    public void testRedbudSearchDoesNotReturnPawpaw(){
        givenSearchTermInitializedToRedbud();
        whenSearched();
        thenVerifyPawpawNotReturned();
    }

    private void thenVerifyPawpawNotReturned(){
        boolean powpowReturned = false;

        for (PlantDTO plantDTO:plantDTOs){
            if (plantDTO.getCommon().contains("powpow")){
                powpowReturned=true;
                break;
            }
        }
        assertFalse(powpowReturned);
    }

    private void thenVerifyRedBudReturned(){
        boolean redBudReturned = false;

        for (PlantDTO plantDTO:plantDTOs){
            if (plantDTO.getCommon().contains("Redbud")){
                redBudReturned=true;
                break;
            }
        }
        assertTrue(redBudReturned);
    }

    private void whenSearched(){
        plantDTOs = specimenDAO.search(searchTerm);
    }

    private void givenSearchTermInitializedToRedbud(){
        searchTerm="Redbud";
    }

    private void thenVerifyException(){
        assertFalse(specimenHasSaved);
    }



    private void givenSpecimenIsNotInitialized(){
        specimenDTO = new SpecimenDTO();
    }

    private void thenVerifyNoException(){
        assertTrue(specimenHasSaved);
    }

    private void whenSpecimenIsSaved() {
        try{
            specimenDAO.save(specimenDTO);
            specimenHasSaved=true;
        } catch (Exception e) {
            e.printStackTrace();
            specimenHasSaved=false;
        }

    }



    private void givenSpecimenIsInitializedWithData() {
        specimenDTO=new SpecimenDTO();
        specimenDTO.setDescription("Test Specimen");
        specimenDTO.setLongitude("111");
        specimenDTO.setLatitude("222");
        specimenDTO.setLocation("astana");
        specimenDTO.setGuid(83);
        specimenDTO.setCache_id(1110001);
    }
}
