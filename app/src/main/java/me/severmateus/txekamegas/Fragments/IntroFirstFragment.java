package me.severmateus.txekamegas.Fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.severmateus.txekamegas.Activities.Intro;
import me.severmateus.txekamegas.Database.MySQLiteHelper;
import me.severmateus.txekamegas.R;

/**
 * Created by User on 05/08/2015.
 */
public class IntroFirstFragment extends Fragment {

    private TextView termosCondicoes;
    private Button continuar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.firsttimepart1, container, false);

        termosCondicoes = (TextView) rootView.findViewById(R.id.termosCondicoes);
        continuar = (Button) rootView.findViewById(R.id.btn_swipeLeft);

        continuar.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                ((Intro)getActivity()).setCurrentItem (1, true);

            } });

        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        termosCondicoes.setPaintFlags(termosCondicoes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        termosCondicoes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.201.1/hidroinfo/termos.html")); // http://192.168.77.1/hidroinfo/termos.html - link funciona, falta actualizar os dados
                startActivity(browserIntent);
            }
        });
    }

}