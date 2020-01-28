package com.pouillos.monpilulier.views.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.listmyx.ListMyMedecinActivity;
import com.pouillos.monpilulier.activities.newx.NewMedecinActivity;
import com.pouillos.monpilulier.entities.Association;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.views.viewholder.ListAllMedecinViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListMyMedecinAdapter extends RecyclerView.Adapter<ListAllMedecinViewHolder>{

        // FOR DATA
        private List<Medecin> listMyMedecin = new ArrayList<>();
        private Utilisateur utilisateur;
        private List<Association> listAllAssociation;


        // CONSTRUCTOR
        public ListMyMedecinAdapter( ) {
            this.utilisateur  = (new Utilisateur()).findActifUser();
            listAllAssociation = Association.find(Association.class,"utilisateur = ?", utilisateur.getId().toString());
            for (Association association : listAllAssociation) {
                if (association.getUtilisateur() == utilisateur.getId()) {
                    this.listMyMedecin.add(Medecin.findById(Medecin.class,association.getMedecin()));
                }
            }
            Collections.sort(this.listMyMedecin);
        }

        @Override
        public int getItemCount() {
            return listMyMedecin.size();
        }

        @Override
        public ListAllMedecinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllMedecinViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllMedecinViewHolder holder, int position) {
            Medecin medecin = listMyMedecin.get(position);
            holder.display(medecin);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newMedecinActivity = new Intent(v.getContext(), NewMedecinActivity.class);
                            newMedecinActivity.putExtra("activitySource", ListMyMedecinActivity.class);
                            newMedecinActivity.putExtra("medecinAModifId", medecin.getId());
                            newMedecinActivity.putExtra("associe", true);

                            v.getContext().startActivity(newMedecinActivity);
                            ((Activity)v.getContext()).finish();
                        }
                    }
            );

            holder.itemView.findViewById(R.id.buttonDelete).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(v.getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Suppression Medecin")
                                    .setMessage("Etes vous sûr de vouloir supprimer le medecin "+medecin.afficherTitre()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            medecin.delete();
                                            listMyMedecin = Medecin.listAll(Medecin.class,"name");
                                          //  Collections.sort(listAllMedecin);
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("Non", null)
                                    .show();
                        }
                    }
            );

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle(medecin.afficherTitre())
                            .setMessage(medecin.afficherDetail())
                            .show();
                }
            });
        }
    }










