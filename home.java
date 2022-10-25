package com.volvain.yash;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.volvain.yash.DAO.Database;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {
static MenuItem logoutBtn;
    Database db;
//FloatingActionButton btn;
 CheckLocation checkLoc;
 Class c=null;
 String[] args=null;
 @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Server.serverUri="https://projectmctibers.appspot.com";
       db= new Database(this);
        //TODO if id exists
        super.onCreate(savedInstanceState);
        Server.serverUri=this.getString(R.string.server);



        setContentView(R.layout.activity_main2);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        if(db.checkId()){
            BackgroundWork.sync();
       if(this.getIntent().hasExtra("fragmentNo")){
            if(this.getIntent().getStringExtra("fragmentNo").equals("NotificationFragment"))
                loadFragment(new notificationsFragment());}
        else loadFragment(new homeFragment());
        }

        else{
            loadFragment(new loginFragment());
        }
        checkLoc=new CheckLocation(this,this);
//        logoutBtn=getMenu().findItem(R.id.login_menu_id).
    }

    private  boolean loadFragment(Fragment fragment){
        if (fragment !=null){
getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment =null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment= new homeFragment();
                break;
            case  R.id.navigation_dashboard:

                fragment=new settingsFragment();
                break;
            case R.id.navigation_notifications:

                fragment=new notificationsFragment();
                break;
            case  R.id.login:
                if(new Database(this).getId()!=0l)
                    fragment=new Profile();

               else fragment=new loginFragment();
                break;
                case R.id.logout_top:
                    logout();
        }
        return loadFragment(fragment);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            checkLoc.displayLocationSettingsRequest(c,args);
           // new CheckLocation(this,this,PinLocation.class).displayLocationSettingsRequest();
            //Log.i("checkLoc","here");
            //Intent i=new Intent(this,PinLocation.class);
            //startActivity(i);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermissions(Class c,String... s){
     this.c=c;
     this.args=s;
   checkLoc.displayLocationSettingsRequest(c,s);
        }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        if (requestCode == 2) {

            checkLoc.displayLocationSettingsRequest(c,args);

                }
            }
          //  checkLoc.requestPermission();
          private void logout() {
              WorkManager.getInstance().cancelAllWork();
              Database obj = new Database(this);
              obj.logout();
              logoutBtn.setVisible(false);
              Intent i = new Intent(this, Home.class);
              startActivity(i);
          }
          @Override
          public boolean onCreateOptionsMenu(Menu menu){
             MenuInflater inflater=getMenuInflater();
              inflater.inflate(R.menu.top_menu,menu);
              super.onCreateOptionsMenu(menu);
             logoutBtn=menu.getItem(0);
             if(!db.checkId())
              logoutBtn.setVisible(false);
     return true;
          }
          @Override
    public boolean onOptionsItemSelected(MenuItem item){

     logout();
     item.setVisible(false);
     return super.onOptionsItemSelected(item);
          }
            }
