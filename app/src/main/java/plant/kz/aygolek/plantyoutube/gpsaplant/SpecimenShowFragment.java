package plant.kz.aygolek.plantyoutube.gpsaplant;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import plant.kz.aygolek.plantyoutube.DAO.ISpecimenDAO;
import plant.kz.aygolek.plantyoutube.DAO.OfflineSpecimenDAO;
import plant.kz.aygolek.plantyoutube.DAO.SpecimenDAOStub;
import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;
import plant.kz.aygolek.plantyoutube.DTO.SpecimenDTO;

/**
 *  dauren anay on 29/03/2018.
 */

public class SpecimenShowFragment extends ListFragment {

    private ISpecimenDAO specimenDAO;
    private String searchTerm="e";

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //specimenDAO = new SpecimenDAOStub();
        //specimenDAO = new OfflineSpecimenDAO(getActivity());

    }

    public void search() {
        final List<SpecimenDTO> specimens = new ArrayList<>();


        FirebaseDatabase firebaseDbInstance = FirebaseDatabase.getInstance();
        DatabaseReference firebaseDbInstanceReference = firebaseDbInstance.getReference();


        DatabaseReference fooDb = firebaseDbInstanceReference.child("foo");
        fooDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot child:children){
                    SpecimenDTO specimenDTO = child.getValue(SpecimenDTO.class);
                    specimens.add(specimenDTO);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //search();

        ArrayAdapter<SpecimenDTO> specimenDTOArrayAdapter =
                new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, specimens);
        setListAdapter(specimenDTOArrayAdapter);
    }
}
