package plant.kz.aygolek.plantyoutube.gpsaplant;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;

import plant.kz.aygolek.plantyoutube.R;

/**
 * Created by Lagrange-Support on 29/03/2018.
 */

public class SpecimenShowActivity extends Activity {

    private AutoCompleteTextView autoCompleteSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specimenshow);

        autoCompleteSearch = findViewById(R.id.autoCompleteTextSearch);

        SpecimenShowFragment frgSpecimenList = (SpecimenShowFragment) getFragmentManager().findFragmentById(R.id.frgSpecimenList);

        //frgSpecimenList.setSearchTerm(searchText);
        frgSpecimenList.search();
    }

    public void onSearchClicked(View v){
        String searchText = autoCompleteSearch.getText().toString();
        SpecimenShowFragment frgSpecimenList = (SpecimenShowFragment) getFragmentManager().findFragmentById(R.id.frgSpecimenList);

        frgSpecimenList.setSearchTerm(searchText);
        frgSpecimenList.search();
    }
}
