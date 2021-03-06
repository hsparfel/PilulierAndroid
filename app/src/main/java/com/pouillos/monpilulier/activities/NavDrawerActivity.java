package com.pouillos.monpilulier.activities;


import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.orm.SugarRecord;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.add.AddAnalyseActivity;
import com.pouillos.monpilulier.activities.add.AddExamenActivity;
import com.pouillos.monpilulier.activities.add.AddProfilActivity;
import com.pouillos.monpilulier.activities.add.AddUserActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherAnalyseActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherContactActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherExamenActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherGraphiqueActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherProfilActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvAnalyseActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvExamenActivity;
import com.pouillos.monpilulier.activities.recherche.ChercherContactActivity;

import com.pouillos.monpilulier.activities.tools.RdvAnalyseNotificationBroadcastReceiver;
import com.pouillos.monpilulier.activities.tools.RdvExamenNotificationBroadcastReceiver;
import com.pouillos.monpilulier.activities.tools.RdvNotificationBroadcastReceiver;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.AlarmRdv;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.Echeance;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import icepick.Icepick;
import icepick.State;

public class NavDrawerActivity extends AppCompatActivity implements BasicUtils, NavigationView.OnNavigationItemSelectedListener {
    //FOR DESIGN

    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @State
    protected Utilisateur activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //a redefinit à chq fois
        super.onCreate(savedInstanceState);

        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() != 0) {
            activeUser = listUserActif.get(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        //getMenuInflater().inflate(R.menu.menu_add_item_to_db, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_home:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AccueilActivity.class);
                break;
            case R.id.activity_main_drawer_profile:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AddProfilActivity.class);
                break;
            case R.id.activity_main_drawer_evolution:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherGraphiqueActivity.class);
                break;
            case R.id.activity_main_drawer_account:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AddUserActivity.class, getResources().getString(R.string.id_user), activeUser.getId());
                break;
            case R.id.activity_main_drawer_change_account:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AuthentificationActivity.class, getResources().getString(R.string.id_user), activeUser.getId());
                break;
            case R.id.activity_main_drawer_treatments:
                Toast.makeText(this, "à implementer 2", Toast.LENGTH_LONG).show();
                break;

            case R.id.activity_main_drawer_contact_appointments:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherRdvActivity.class);
                break;
            case R.id.activity_main_drawer_analysis_appointments:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherRdvAnalyseActivity.class);
                break;
            case R.id.activity_main_drawer_exam_appointments:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherRdvExamenActivity.class);
                break;
            case R.id.activity_main_drawer_contacts:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherContactActivity.class);
                break;
            case R.id.activity_main_drawer_oldAccueil:
                ouvrirActiviteSuivante(NavDrawerActivity.this, MainActivity.class);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myProfilActivity = new Intent(NavDrawerActivity.this, ChercherContactActivity.class);
        startActivity(myProfilActivity);
        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            /*case R.id.menu_activity_main_params:
                Toast.makeText(this, "Il n'y a rien à paramétrer ici, passez votre chemin...", Toast.LENGTH_LONG).show();
                return true;*/
            case R.id.menu_activity_main_search:
                //Toast.makeText(this, "Recherche indisponible, demandez plutôt l'avis de Google, c'est mieux et plus rapide.", Toast.LENGTH_LONG).show();
                myProfilActivity = new Intent(NavDrawerActivity.this, ChercherContactActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.addAnalyse:
                myProfilActivity = new Intent(NavDrawerActivity.this, AddAnalyseActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.addExamen:
                myProfilActivity = new Intent(NavDrawerActivity.this, AddExamenActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.listAllAnalyse:
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherAnalyseActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.listAllExamen:
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherExamenActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.listMyProfil:
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherProfilActivity.class);
                startActivity(myProfilActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    public void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

    }

    // 2 - Configure Drawer Layout
    public void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    public void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void ouvrirActiviteSuivante(Context context, Class classe) {
        Intent intent = new Intent(context, classe);
        startActivity(intent);
        finish();
    }

    @Override
    public void ouvrirActiviteSuivante(Context context, Class classe, String nomExtra, Long objetIdExtra) {
        Intent intent = new Intent(context, classe);
        intent.putExtra(nomExtra, objetIdExtra);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected Utilisateur findActiveUser() {
        Utilisateur user = null;
        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() !=0){
            user = listUserActif.get(0);
        }
       return user;
    }

    protected <T> void buildDropdownMenu(List<T> listObj, Context context, AutoCompleteTextView textView) {
        List<String> listString = new ArrayList<>();
        String[] listDeroulante;
        listDeroulante = new String[listObj.size()];
        for (T object : listObj) {
            listString.add(object.toString());
        }
        listString.toArray(listDeroulante);
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.list_item, listDeroulante);
        textView.setAdapter(adapter);
    }

    @Override
    public Executor getMainExecutor() {
        return super.getMainExecutor();
    }

    protected <T extends SugarRecord> void deleteItem(Context context, T item, Class classe) {

        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setNegativeButton(R.string.dialog_delete_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, R.string.dialog_delete_negative_toast, Toast.LENGTH_LONG).show();

                    }
                })
                .setPositiveButton(R.string.dialog_delete_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, R.string.dialog_delete_positive_toast, Toast.LENGTH_LONG).show();
                        item.delete();


                        ouvrirActiviteSuivante(context, classe);

                    }
                })
                .show();


    }

    protected boolean isChecked(ChipGroup chipGroup) {
        boolean bool;
        if (chipGroup.getCheckedChipId() != -1) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }


    protected boolean isFilled(TextInputEditText textInputEditText){
        boolean bool;
        if (textInputEditText.length()>0) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    protected boolean isFilled(Object object){
        boolean bool;
        if (object!=null) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    protected boolean isValidTel(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && textView.getText().length() <10) {
            //textView.requestFocus();
            //textView.setError("Saisie Non Valide  (10 chiffres)");
            return false;
        } else {
            return true;
        }
    }

    protected boolean isValidZip(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && textView.getText().length() <5) {
            //textView.requestFocus();
            //textView.setError("Saisie Non Valide  (5 chiffres)");
            return false;
        } else {
            return true;
        }
    }

    protected boolean isEmailAdress(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();
    }
    protected boolean isValidEmail(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && !isEmailAdress(textView.getText().toString())) {
            //textView.requestFocus();
            //textView.setError("Saisie Non Valide (email)");
            return false;
        } else {
            return true;
        }
    }

    protected static float floatArrondi(float number, int decimalPlace) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


    protected<T> void activerNotification(Class classe, Date dateRdv, T object, Context context) {
        if (classe == RdvNotificationBroadcastReceiver.class) {
           startAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Contact) object).toStringShort(), Echeance.OneHourAfter.toString(),context);
           startAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Contact) object).toStringShort(), Echeance.OneDayAfter.toString(),context);
        } else if (classe == RdvAnalyseNotificationBroadcastReceiver.class) {
            startAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Analyse) object).toString(), Echeance.OneHourAfter.toString(),context);
            startAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Analyse) object).toString(), Echeance.OneDayAfter.toString(),context);
        } else if (classe == RdvExamenNotificationBroadcastReceiver.class) {
            startAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Examen) object).toString(), Echeance.OneHourAfter.toString(),context);
            startAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Examen) object).toString(), Echeance.OneDayAfter.toString(),context);
        }

    }

    protected<T> void supprimerNotification(Class classe, Date dateRdv, T object, Context context) {
        if (classe == RdvNotificationBroadcastReceiver.class) {
            List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterHeure(dateRdv,-1).toString(),((Contact) object).toStringShort(),Echeance.OneHourAfter.toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Contact) object).toStringShort(), Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());

            listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterJourArrondi(dateRdv,-1,7).toString(),((Contact) object).toStringShort(),Echeance.OneDayAfter.toString());
            alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Contact) object).toStringShort(), Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
        } else if (classe == RdvAnalyseNotificationBroadcastReceiver.class) {
            List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterHeure(dateRdv,-1).toString(),((Analyse) object).toString(),Echeance.OneHourAfter.toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Analyse) object).toString(), Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());

            listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterJourArrondi(dateRdv,-1,7).toString(),((Analyse) object).toString(),Echeance.OneDayAfter.toString());
            alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Analyse) object).toString(), Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
        } else if (classe == RdvExamenNotificationBroadcastReceiver.class) {
            List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterHeure(dateRdv,-1).toString(),((Examen) object).toString(),Echeance.OneHourAfter.toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Examen) object).toString(), Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());

            listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterJourArrondi(dateRdv,-1,7).toString(),((Examen) object).toString(),Echeance.OneDayAfter.toString());
            alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Examen) object).toString(), Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
        }
        //todo prevoir les autres cas pour les autres rdv
    }

    private void startAlert(Class classe, Date dateRdv, String detail, String echeance, Context context) {
        Intent intent = new Intent(this, classe);
        intent.putExtra("detail",detail);
        intent.putExtra("echeance",echeance);
        Date dateJour = new Date();
        Long dateJourLong = dateJour.getTime();
        int requestCode =dateJourLong.intValue();
        Log.i("requestCode",""+requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateRdv.getTime(), pendingIntent);
        AlarmRdv alarmRdv = new AlarmRdv();
        alarmRdv.setClasse(classe.getName());
        alarmRdv.setDate(dateRdv);
        alarmRdv.setDateString(dateRdv.toString());
        alarmRdv.setDetail(detail);
        alarmRdv.setEcheance(echeance);
        alarmRdv.setRequestCode(requestCode);
        alarmRdv.save();

        Toast.makeText(this, "Alarm set : " + dateRdv.toString(), Toast.LENGTH_LONG).show();
    }

    private void cancelAlert(Class classe, Date dateRdv, String detail, String echeance, Context context,int requestCode) {
        Intent intent = new Intent(this, classe);
        intent.putExtra("detail",detail);
        intent.putExtra("echeance",echeance);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"request_code = ?", ""+requestCode);
        if (listAlarmRdv.size()>0) {
            AlarmRdv alarmRdv = listAlarmRdv.get(0);
            alarmRdv.delete();
        }
        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_LONG).show();
    }

}
