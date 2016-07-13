package me.severmateus.txekamegas.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.severmateus.txekamegas.Database.MySQLiteHelper;
import me.severmateus.txekamegas.Fragments.HomeStatsFragment;
import me.severmateus.txekamegas.Fragments.TimePickerFragment;
import me.severmateus.txekamegas.Model.Consumo;
import me.severmateus.txekamegas.R;
import me.severmateus.txekamegas.BroadcastService;

public class addConsumo extends FragmentActivity {

    private Button save, time, saldoActual;
    private static long tmpoFinal;
    private EditText consC;
    private RadioGroup radioGroup;
    private RadioButton day, week, month, getRadioSelected;
    private TimePicker timePickerino;

    public MySQLiteHelper db;
    public Calendar calendar;
    private long tmpo;

    private TelephonyManager tm;
    private String network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consumo);

        db = new MySQLiteHelper(this);

        tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        network = tm.getNetworkOperatorName();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupTime);
        getRadioSelected = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        save = (Button) findViewById(R.id.save);
        time = (Button) findViewById(R.id.time);
        saldoActual = (Button) findViewById(R.id.consulta);
        consC = (EditText) findViewById(R.id.consumoC);

        final Intent i = new Intent(this, BroadcastService.class);

        calendar = Calendar.getInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (getRadioSelected.getText().equals("Pacote Diario")) {
                    tmpo = 86400000;
                Toast.makeText(getBaseContext(), calendar.getTime()+"", Toast.LENGTH_SHORT).show();
                    long tmp = calendar.getTimeInMillis() - System.currentTimeMillis();
                    if (tmp < 0) {
                        tmpoFinal = tmpo + tmp;
                    }else
                        tmpoFinal = tmp;
//                }else
//                if (getRadioSelected.getText().equals("Pacote Semanal")) {
//                    tmpo = 604800000;
//                    tmpoFinal = tmpo + (calendar.getTimeInMillis() - System.currentTimeMillis());
//                }else
//                if (getRadioSelected.getText().equals("Pacote Mensal")){
//                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
//                    long tmp = calendar.getTimeInMillis() - System.currentTimeMillis();
//                    tmpoFinal =  tmp;
//                }

                //getRadioSelected.getText().equals("Set a Day")

                //Validações: tmp < 0 funciona se tiver tipo a hora configurada atraz da hora actual do cell.
					    					/* A ideia é de introduzir tipo esses dias que ta a ler ai, ele fazer a cena de aumentar +1 semana ou +1 mes em função e jobar.
					    					 * o tmp serve para casos de a hora der em negativo, e funciona de boa na lagoa. Mas para o caso de querer adicionar 1 semana, como fica?
					    					 * Entao: 1 semana tem que adicionar, querendo ou nao, o tmp. Ja que esta validacao funciona de boa para o dia, entao, farei a modificação lá em cima, onde estão a setar as horas em milisegundos.
					    					 */

                //Time pro Megas
                //Algo me diz que até ele chegar aki, o servico já estará a correr a LONG, percebes? HA UM BUG ENTAO? NAO? Posso Validar com se o consumo e tempo forem igual a 0 (Best Way)
                //Consumo consumo = db.getConsumo(1);
                Consumo consumo = new Consumo();
                consumo.setConsumoInicial(Long.parseLong(String.valueOf(consC.getText())));
                consumo.setTempoDefinido(tmpoFinal);
                //db.updateConsumo(consumo);
                db.addConsumo(consumo);

                startService(i);

                //Finalizando o Processo pra Actualizacao da Interface: Feixar a Interface Anterior; Ir para a nova interface; Apagar esta do Rastro.
                Intent myIntent = new Intent(v.getContext(),
                        HomeStatsFragment.class);
                ComponentName cn = myIntent.getComponent();
                /*
                Intent mainIntent = IntentCompat
                        .makeRestartActivityTask(cn);
                startActivity(mainIntent);
                */
                finish();
            }
        });
/*
       time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View vz = getLayoutInflater().inflate(R.layout.timepicker,
                        null);
                    new AlertDialog.Builder(addConsumo.this)
                            .setTitle("Definir o Tempo")
                            .setView(vz)
                            .setNegativeButton("Cancelar",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                        }
                                    })
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            //Validar os numeros, desde que o 1o n seja superior ao 2o lol
                                            timePickerino =(TimePicker) vz.findViewById(R.id.timePickerino);
                                            calendar.set(Calendar.HOUR_OF_DAY, timePickerino.getCurrentHour());
                                            calendar.set(Calendar.MINUTE, timePickerino.getCurrentMinute());
                                        }
                                    }).show();
            }
        });
*/
        saldoActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (network.equalsIgnoreCase("mCel") || network.equalsIgnoreCase("MOZ - mCel")) {
                    //2 Steps: Mensagem - Megas Activados e por USSD - Bonus Megas.

                    // Por USSD
                    String codMegabytes = "*132";

                    String encodedHash = Uri.encode("#");
                    String ussd = codMegabytes + encodedHash;
                    startActivityForResult(new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + ussd)), 1);

                }
                if (network.equalsIgnoreCase("Vodacom") || network.equalsIgnoreCase("VodaCom-MZ")) {
                    // Por USSD
                    String codMegabytes = "*100*03";

                    String encodedHash = Uri.encode("#");
                    String ussd = codMegabytes + encodedHash;
                    startActivityForResult(new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + ussd)), 1);
                }
                if (network.equalsIgnoreCase("MOVITEL")) {
                    //151 e 152?

                    // Por USSD
                    String codMegabytes = "*151";

                    String encodedHash = Uri.encode("#");
                    String ussd = codMegabytes + encodedHash;
                    startActivityForResult(new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + ussd)), 1);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_consumo, menu);
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

    public void showTimePickerDialog1(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setTime() {

    }


}
