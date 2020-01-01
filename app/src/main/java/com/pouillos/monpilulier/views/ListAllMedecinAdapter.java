package com.pouillos.monpilulier.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.ListAllMedecinActivity;
import com.pouillos.monpilulier.activities.NewMedecinActivity;
import com.pouillos.monpilulier.entities.Association;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Utilisateur;

import java.util.Collections;
import java.util.List;

public class ListAllMedecinAdapter extends RecyclerView.Adapter<ListAllMedecinViewHolder>{

        // FOR DATA
        private List<Medecin> listAllMedecin;
        private Utilisateur utilisateur;

        // CONSTRUCTOR
        public ListAllMedecinAdapter( ) {
            this.utilisateur  = (new Utilisateur()).findActifUser();
            this.listAllMedecin = Medecin.listAll(Medecin.class);
            Collections.sort(this.listAllMedecin);
        }

        @Override
        public int getItemCount() {
            return listAllMedecin.size();
        }

        @Override
        public ListAllMedecinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_list_all_x_item, parent, false);
            return new ListAllMedecinViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListAllMedecinViewHolder holder, int position) {
            Medecin medecin = listAllMedecin.get(position);
            holder.display(medecin);

            holder.itemView.findViewById(R.id.buttonModify).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent newMedecinActivity = new Intent(v.getContext(), NewMedecinActivity.class);
                            newMedecinActivity.putExtra("precedent", ListAllMedecinActivity.class);
                            newMedecinActivity.putExtra("medecinToUpdate", medecin);

                            List<Association> listAssociation = Association.find(Association.class,"utilisateur = ? and medecin = ?", utilisateur.getId().toString(), medecin.getId().toString());
                            if (listAssociation.size() >0) {
                                newMedecinActivity.putExtra("associe", true);
                            } else {
                                newMedecinActivity.putExtra("associe", false);
                            }



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
                                    .setMessage("Etes vous sûr de vouloir supprimer le medecin "+medecin.getName()+" ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            medecin.delete();
                                            listAllMedecin = Medecin.listAll(Medecin.class,"name");
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
                            .setTitle(medecin.getName())
                            .setMessage(medecin.toString())
                            .show();
                }
            });
        }
    }











