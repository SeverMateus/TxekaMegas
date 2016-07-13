package me.severmateus.txekamegas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.severmateus.txekamegas.Activities.Home;
import me.severmateus.txekamegas.Database.MySQLiteHelper;
import me.severmateus.txekamegas.Model.Settings;
import me.severmateus.txekamegas.R;

/**
 * Created by User on 05/08/2015.
 */
public class IntroSecondFragment extends Fragment {

    Button iniciar;
    private MySQLiteHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MySQLiteHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.firsttimepart2, container, false);

        iniciar = (Button) rootView.findViewById(R.id.btn_iniciar);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
                         public void onClick(View v) {
                //Criar Settings pra dizer q ja criou (lol)
                db.addSettings(new Settings());
                Intent myIntent = new
                        Intent(v.getContext(), Home.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

}
