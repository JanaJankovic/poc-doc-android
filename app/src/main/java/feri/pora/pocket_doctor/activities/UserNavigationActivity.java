package feri.pora.pocket_doctor.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import feri.pora.datalib.User;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.fragments.HomeFragment;
import feri.pora.pocket_doctor.fragments.ListAnalysisFragment;
import feri.pora.pocket_doctor.fragments.RequestAnalysisFragment;
import feri.pora.pocket_doctor.ApplicationState;

public class UserNavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static Toolbar toolbar;
    private DrawerLayout drawer;
    public static NavigationView navigationView;
    private  NavController navController;
    private TextView textViewUserFullname;
    private TextView textViewUserMedicalNumber;

    private ApplicationState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindGUI();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        textViewUserFullname.setText(ApplicationState.loadLoggedUser().getFullName());
        textViewUserMedicalNumber.setText(ApplicationState.loadLoggedUser().getMedicalNumber());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home :
                        toolbar.setTitle(R.string.menu_home);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_request_analysis:
                        toolbar.setTitle("Request analysis");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, new RequestAnalysisFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_list_analysis :
                        toolbar.setTitle("List analysis");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, new ListAnalysisFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_logout :
                       User user = new User();
                       ApplicationState.saveLoggedUser(user);
                       finish();
                        break;
                }
                return true;
            }
        });

    }

    public void bindGUI() {
        setContentView(R.layout.activity_user_navigation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_request_analysis, R.id.nav_list_analysis)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        textViewUserFullname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userFullName);
        textViewUserMedicalNumber = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userMedicalNumber);

        state = (ApplicationState)getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}