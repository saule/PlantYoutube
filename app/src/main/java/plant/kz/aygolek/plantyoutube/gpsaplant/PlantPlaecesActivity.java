package plant.kz.aygolek.plantyoutube.gpsaplant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import plant.kz.aygolek.plantyoutube.R;

/**
 * Created by Lagrange-Support on 20/03/2018.
 */

abstract class PlantPlaecesActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gpsaplant, menu);
        int currentMenuId = getCurrentMenuId();
        if(getCurrentMenuId()!=0){
            menu.removeItem(currentMenuId);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void gpsaPlantClicked(MenuItem menuItem){
        Intent gpsaPlantIntent = new Intent(this, GPSAPLant.class);
        startActivity(gpsaPlantIntent);
    }

    public void searchByColorClicked(MenuItem menuItem){
        Intent searchByColorIntent = new Intent(this, activity_color_capture.class);
        startActivity(searchByColorIntent);
    }

    public abstract int getCurrentMenuId();
}
