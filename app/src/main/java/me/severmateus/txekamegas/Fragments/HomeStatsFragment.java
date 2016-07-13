package me.severmateus.txekamegas.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import me.severmateus.txekamegas.Activities.Home;
import me.severmateus.txekamegas.Activities.addConsumo;
import me.severmateus.txekamegas.BroadcastService;
import me.severmateus.txekamegas.Database.MySQLiteHelper;
import me.severmateus.txekamegas.Model.Consumo;
import me.severmateus.txekamegas.R;

/**
 * Created by User on 05/08/2015.
 */
public class HomeStatsFragment extends Fragment {

    Button iniciar;
    private Toolbar mToolbar;
    private TextView name, age;
    private ActionButton actionButton;
    private TextView megasUi, megasRi, megasCi, txtMegasU, txtMegasR, txtMegasC, timeVi, txtStats;

    public static String timeTestV;

    private MySQLiteHelper db;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MySQLiteHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.stats, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        txtMegasC = (TextView) rootView.findViewById(R.id.home_test_megasC);
        txtMegasR = (TextView) rootView.findViewById(R.id.home_test_megasR);
        txtMegasU = (TextView) rootView.findViewById(R.id.home_test_megasU);
        megasCi = (TextView) rootView.findViewById(R.id.home_test_txtmegasC);
        megasRi = (TextView) rootView.findViewById(R.id.home_test_txtmegasR);
        megasUi = (TextView) rootView.findViewById(R.id.home_test_txtmegasU);
        txtStats = (TextView) rootView.findViewById(R.id.home_test_txtStats);
        timeVi = (TextView) rootView.findViewById(R.id.home_test_timeV);

        actionButton = (ActionButton) rootView.findViewById(R.id.action_button);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(BroadcastService.COPA_MESSAGE);
                Consumo consumo = db.getConsumo(1);
                if (!txtStats.getText().toString().isEmpty())
                    txtStats.setText("");
                txtMegasC.setText("Megas Contratados:            ");
                txtMegasU.setText("Megas Utilizados:                 ");
                txtMegasR.setText("Megas Remanescentes:     ");
                megasCi.setText(consumo.getConsumoInicial()+" MB.");
                megasUi.setText(consumo.getConsumoGasto()+" MB.");
                megasRi.setText((consumo.getConsumoInicial() - consumo.getConsumoGasto())+" MB.");
                timeVi.setText("Valido por "+convertTime(consumo.getTempoDefinido()));
            }
        };

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        if (db.getAllConsumo().size()!=0) {
            Consumo consumo = db.getConsumo(1);
            //txtStats.setText("Estatisticas");
            txtMegasC.setText("Megas Contratados:            ");
            txtMegasU.setText("Megas Utilizados:                 ");
            txtMegasR.setText("Megas Remanescentes:     ");
            megasCi.setText(consumo.getConsumoInicial()+" MB.");
            megasUi.setText(consumo.getConsumoGasto()+" MB.");
            megasRi.setText((consumo.getConsumoInicial() - consumo.getConsumoGasto())+" MB.");
            timeVi.setText("Valido por "+convertTime(consumo.getTempoDefinido()));
        }
        else {
            txtStats.setText("Ainda não tem um pacote Activado.\nPor Favor, active um pacote clicando no botão '+'.");
            txtStats.setGravity(Gravity.CENTER);
        }

        //Fazer If's, pro caso de existir na BD um Consumo, aparecer o simbolo da caneta, senao, o mais, e permitir com que adicione e/ou modifique o estado. ;)
        actionButton.setImageResource(R.drawable.ic_add);
        actionButton.setButtonColor(getResources().getColor(R.color.fab_material_lime_500));
        actionButton.setButtonColorPressed(getResources().getColor(R.color.fab_material_lime_900));
        actionButton.setShowAnimation(ActionButton.Animations.FADE_IN);
        actionButton.setRippleEffectEnabled(true);
        actionButton.setShadowResponsiveEffectEnabled(true);

        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new
                        Intent(v.getContext(), addConsumo.class);
                startActivityForResult(myIntent, 0);

            }
        });

    }

    private String convertTime(long tmpoRestante) {
        String txt;
        long m, d, h, min, sec;

        if (tmpoRestante <60)//Max a Minute Long
            txt = tmpoRestante+" s.";
        else
        if (tmpoRestante < 3600) {//Max a Hour Long
            min = tmpoRestante/60;
            sec = tmpoRestante - 60*min;
            txt = min+" min e "+sec+" s.";
        }
        else {
            if (tmpoRestante < 86400) { // Max a Day Long
                h = tmpoRestante/3600;
                min = (tmpoRestante - 3600*h)/60;
                sec = tmpoRestante - 3600*h - 60*min;
                txt = h+" h, "+min+" min e "+sec+" s.";
            }
            else{
                if (tmpoRestante < 2592000) {//Untested - Max a Month Long
                    d = tmpoRestante/86400;
                    h = (tmpoRestante - 86400*d)/3600;
                    min = (tmpoRestante - 86400*d - 3600*h)/60;
                    sec = tmpoRestante - 86400*d - 3600*h - 60*min;
                    if (d<=1)
                        txt = d+" dia, "+h+" h, "+min+" min e "+sec+" s.";
                    else
                        txt = d+" dias, "+h+" h, "+min+" min e "+sec+" s.";
                }
                else {//Untested - Meses, ate anos..
                    m = tmpoRestante/2592000;
                    d = (tmpoRestante - 2592000*m)/86400;
                    h = (tmpoRestante - 2592000*m - 86400*d)/3600;
                    min = (tmpoRestante - 2592000*m - 86400*d - 3600*h)/60;
                    sec = tmpoRestante - 2592000*m - 86400*d - 3600*h - 60*min;
                    if (m <=1 && d <=1)
                        if (d == 0)
                            txt = m+" mes, "+h+" h, "+min+" min e "+sec+" s.";
                        else
                            txt = m+" mes, "+d+" dia, "+h+" h, "+min+" min e "+sec+" s.";
                    else
                    if (m <=1 && d>1)
                        txt = m+" mes, "+d+" dias, "+h+" h, "+min+" min e "+sec+" s.";
                    else
                    if (m>1 && d<=1)
                        txt = m+" meses, "+d+" dia, "+h+" h, "+min+" min e "+sec+" s.";
                    else
                        txt = m+" meses, "+d+" dias, "+h+" h, "+min+" min e "+sec+" s.";
                }
            }
        }

    return txt;
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver),
                new IntentFilter(BroadcastService.COPA_RESULT)
        );
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        super.onStop();
    }
}
