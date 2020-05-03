package com.pouillos.monpilulier.activities.enregistrer;

import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EnregistrerRdvMedecinOfficielActivity extends AppCompatActivity implements BasicUtils {

    private Contact mContact;
    private TextView textDate;

    private TextView textNote;
    private TextView textMedecin;
    private TextView textProfession;
    private TextView textSavoirFaire;
    private TextView textCabinet;
    private TextView textComplement;
    private TextView textAdresse1;
    private TextView textAdresse2;
    private TextView textTel;
    private TextView textFax;
    private TextView textEmail;


    private Date date = new Date();
    private Intent intent;
    private Rdv rdv;
    private Utilisateur utilisateur;

    TimePickerDialog picker;
    EditText editTextHeure;
    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_rdv);

        utilisateur = (new Utilisateur()).findActifUser();


        editTextDate= findViewById(R.id.editTextDate);
        editTextHeure= findViewById(R.id.editTextHeure);
        //editTextHeure.setInputType(InputType.TYPE_NULL);



        ImageButton buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        ImageButton buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);

        textNote = findViewById(R.id.textNote);
        textMedecin = findViewById(R.id.textMedecin);
        textProfession = findViewById(R.id.textProfession);
        textSavoirFaire = findViewById(R.id.textSavoirFaire);
        textCabinet = findViewById(R.id.textCabinet);
        textComplement = findViewById(R.id.textComplement);
        textAdresse1 = findViewById(R.id.textAdresse1);
        textAdresse2 = findViewById(R.id.textAdresse2);
        textTel = findViewById(R.id.textTel);
        textFax = findViewById(R.id.textFax);
        textEmail = findViewById(R.id.textEmail);
        //textDate = findViewById(R.id.textDate);
        Button buttonWaze = findViewById(R.id.buttonWaze);
        Button buttonGoogleMap = findViewById(R.id.buttonGoogleMap);
        traiterIntent();

        buttonGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    // Launch GoogleMap to look for Hawaii:

                    //String url = "https://www.google.com/maps/search/?api=1&query=";
                    String url = "geo:";
                    String addr = "";
                    if (mContact.getLatitude() != 0 && mContact.getLongitude() != 0) {
                        url += mContact.getLatitude()+","+ mContact.getLongitude();
                    } else if (mContact.getAdresse() != null && mContact.getCp() != null && mContact.getVille() != null) {
                        url += "0,0?q=";
                        addr += Uri.parse(mContact.getAdresse()+", "+ mContact.getCp()+", "+ mContact.getVille()+", FRANCE");
                        url += addr;
                    }
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                    intent.setPackage("com.google.android.apps.maps");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                catch ( ActivityNotFoundException ex  )
                {
                    // If Waze is not installed, open it in Google Play:
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                    startActivity(intent);
                }
            }
        });

        buttonWaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    // Launch Waze to look for Hawaii:

                    String url = "https://waze.com/ul?q=";
                    url += mContact.getAdresse()+"%20"+ mContact.getCp()+"%20"+ mContact.getVille();

                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                    startActivity( intent );
                }
                catch ( ActivityNotFoundException ex  )
                {
                    // If Waze is not installed, open it in Google Play:
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                    startActivity(intent);
                }
            }
        });


        editTextHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                //int hour = cldr.get(Calendar.HOUR_OF_DAY);
                //int minutes = cldr.get(Calendar.MINUTE);
                int hour = 8;
                int minutes = 0;
                // time picker dialog
                picker = new TimePickerDialog(EnregistrerRdvMedecinOfficielActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String hour = "";
                                String minute = "";
                                if (sHour<10){
                                    hour+="0";
                                }
                                if (sMinute<10){
                                    minute+="0";
                                }
                                hour+=sHour;
                                minute+=sMinute;

                                editTextHeure.setText(hour + ":" + minute);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                calendar.add(Calendar.HOUR_OF_DAY, sHour);
                                calendar.add(Calendar.MINUTE, sMinute);
                                date = calendar.getTime();
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retourPagePrecedente();
            }
        });

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rediger les verifs de remplissage des champs
                //if (!isRempli(textDate, date)) {
                //    return;
                //}

                //enregistrer en bdd
                //saveToDb(textDescription);
                saveToDb();
                //retour
                retourPagePrecedente();
            }
        });
    }

    @Override
    public void retourPagePrecedente() {

        finish();
    }



    @Override
    public void saveToDb() {
        Rdv rdv = new Rdv();
        rdv.setNote(textNote.getText().toString());
        rdv.setContact(mContact);
        rdv.setUtilisateur(utilisateur);
        rdv.setDate(date);
        rdv.save();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void traiterIntent() {
        intent = getIntent();
      //  activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("medecinOfficiel")) {

            Long medecinOfficielId = intent.getLongExtra("medecinOfficiel",0);
            //rdvAModif = Rdv.findById(Rdv.class,rdvAModifId);
            mContact = Contact.findById(Contact.class,medecinOfficielId);



            //textDescription.setText(rdvAModif.getDetail());
            //textDate.setText(DateUtils.ecrireDate(rdvAModif.getDate()));
            //date = rdvAModif.getDate();
            String nomMedecin = "";
            if (mContact.getCodeCivilite() !=null){
                nomMedecin += mContact.getCodeCivilite();
            }
            nomMedecin += mContact.getNom()+" "+ mContact.getPrenom();
            textMedecin.setText(nomMedecin);
            if (mContact.getSavoirFaire() != null) {
                textSavoirFaire.setText(mContact.getSavoirFaire().getName());
                textProfession.setVisibility(View.GONE);
            } else {
                textSavoirFaire.setVisibility(View.GONE);
                textProfession.setText(mContact.getProfession().getName());
            }


            if (!mContact.getComplement().equals("")) {
                textCabinet.setText(mContact.getRaisonSocial());
            } else {
                textCabinet.setVisibility(View.GONE);
            }



            if (!mContact.getComplement().equals("")) {
                textComplement.setText(mContact.getComplement());
            } else {
                textComplement.setVisibility(View.GONE);
            }


            if (!mContact.getAdresse().equals("")) {
                textAdresse1.setText(mContact.getAdresse());
            } else {
                textAdresse1.setVisibility(View.GONE);
            }

            if (!mContact.getCp().equals("") & !mContact.getVille().equals("")) {
                textAdresse2.setText(mContact.getCp()+" "+ mContact.getVille());
            } else {
                textAdresse2.setVisibility(View.GONE);
            }

            if (!mContact.getTelephone().equals("")) {
                textTel.setText("Tel: "+ mContact.getTelephone());
            } else {
                textTel.setVisibility(View.GONE);
            }

            if (!mContact.getFax().equals("")) {
                textFax.setText("Fax: "+ mContact.getFax());
            } else {
                textFax.setVisibility(View.GONE);
            }

            if (!mContact.getEmail().equals("")) {
                textEmail.setText("@: "+ mContact.getEmail());
            } else {
                textEmail.setVisibility(View.GONE);
            }

        }
    }


    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        //newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.show(getSupportFragmentManager(), "editTexteDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMinDate(new Date().getTime());
               // TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()+1<10) {
                    dateMois = "0"+dateMois;
                }
                Calendar c1 = Calendar.getInstance();
                // set Month
                // MONTH starts with 0 i.e. ( 0 - Jan)
                c1.set(Calendar.MONTH, datePicker.getMonth());
                // set Date
                c1.set(Calendar.DATE, datePicker.getDayOfMonth());
                // set Year
                c1.set(Calendar.YEAR, datePicker.getYear());
                // creating a date object with specified time.
                date = c1.getTime();

                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                //tv1.setText("date: "+dateString);
                editTextDate.setText(dateString);
                editTextDate.setError(null);
                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                try{
                    date = df.parse(dateString);
                }catch(ParseException e){
                    System.out.println("ERROR");
                }
            }
        });
    }



}
