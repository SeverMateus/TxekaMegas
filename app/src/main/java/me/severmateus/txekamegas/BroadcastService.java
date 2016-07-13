package me.severmateus.txekamegas;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import me.severmateus.txekamegas.Database.MySQLiteHelper;
import me.severmateus.txekamegas.Fragments.HomeStatsFragment;
import me.severmateus.txekamegas.Model.Consumo;
import me.severmateus.txekamegas.R;

import me.severmateus.txekamegas.Activities.Home;

public class BroadcastService extends Service {

    static final public String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";

    static final public String COPA_MESSAGE = "com.controlj.copame.backend.COPAService.COPA_MSG";

    private MySQLiteHelper db;

	private static final Class<?>[] mSetForegroundSignature = new Class[] {
	    boolean.class};
	private static final Class<?>[] mStartForegroundSignature = new Class[] {
	    int.class, Notification.class};
	private static final Class<?>[] mStopForegroundSignature = new Class[] {
	    boolean.class};

	private NotificationManager mNM;
	private Method mSetForeground;
	private Method mStartForeground;
	private Method mStopForeground;
	private Object[] mSetForegroundArgs = new Object[1];
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.example.testcrono.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);
    private NotificationCompat.Builder mBuilder;

    CountDownTimer cdt = null;
    private LocalBroadcastManager broadcaster;

    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        }
    }
    
    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
        // If we have the new startForeground API, then use it.
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = Integer.valueOf(id);
            mStartForegroundArgs[1] = notification;
            invokeMethod(mStartForeground, mStartForegroundArgs);
            return;
        }

        // Fall back on the old API.
        mSetForegroundArgs[0] = Boolean.TRUE;
        invokeMethod(mSetForeground, mSetForegroundArgs);
        mNM.notify(id, notification);
    }


    @Override
        public void onCreate() {       
            super.onCreate();

        broadcaster = LocalBroadcastManager.getInstance(this);

        db = new MySQLiteHelper(this);

            Log.i(TAG, "Starting timer...");
            mBuilder = novaNotificacao();

            cdt = new CountDownTimer(db.getConsumo(1).getTempoDefinido(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {


                    notificationCount(millisUntilFinished/1000);
   		    	 	lanxaFogo();
                    Consumo cons = db.getConsumo(1);
                    cons.setTempoDefinido(millisUntilFinished / 1000);
					updateUI();
                    db.updateConsumo(cons);
                    bi.putExtra("countdown", millisUntilFinished);
                    sendBroadcast(bi);
                }

                @Override
                public void onFinish() {
                	notificationFinish();
   		    	 lanxaFogo();
                }
            };

            cdt.start();
            
            //startNotificationCountdown();
        }

        @Override
        public void onDestroy() {

            cdt.cancel();
            notificationCancel();
	    	 lanxaFogo();
            super.onDestroy();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {       
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent arg0) {       
            return null;
        }
        
    	private void notificationCount (long tmpoRestante) {
    		String txt;
    		long m, d, h, min, sec;

            Consumo consumo = db.getConsumo(1);
    		
    		if (tmpoRestante <60)//Max a Minute Long
    			txt = "Faltam "+(consumo.getConsumoInicial()- consumo.getConsumoGasto())+" ou "+tmpoRestante+" s.";
    		else
    			if (tmpoRestante < 3600) {//Max a Hour Long
    				min = tmpoRestante/60;
    				sec = tmpoRestante - 60*min;
    				txt = "Faltam "+(consumo.getConsumoInicial()- consumo.getConsumoGasto())+" ou "+min+" min e "+sec+" s.";
    			}
    			else {
    				if (tmpoRestante < 86400) { // Max a Day Long 
        				h = tmpoRestante/3600;
        				min = (tmpoRestante - 3600*h)/60;
        				sec = tmpoRestante - 3600*h - 60*min;
        				txt = "Faltam "+(consumo.getConsumoInicial()- consumo.getConsumoGasto())+" ou "+h+" h, "+min+" min e "+sec+" s.";
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
    		HomeStatsFragment.timeTestV = txt;
    		mBuilder.setContentText(txt);		
    	}
    	
    	private void notificationFinish () {
    		//Travar net ofc!
    		try {
				Home.setMobileDataEnabled(Home.getContext(), false);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//Melhorar a mensagem de saida
    		mBuilder.setContentText("O Seu Pacote Terminou!!");
    		mBuilder.setOngoing(false);
    	}
    	
    	private void notificationCancel() {
    		//Ver uma maneira de fazer com que ela nao seja cancelada... Servico nao pode parar por nada.
    		mBuilder.setContentText("Cancelado!!");
    		mBuilder.setOngoing(false);
    	}
    	
    	private NotificationCompat.Builder novaNotificacao() {
    		NotificationCompat.Builder mBuilder =
    			    new NotificationCompat.Builder(this)
    			    .setSmallIcon(R.mipmap.ic_launcher)
    			    .setContentTitle("TchekaMegas!")
    			    .setContentText("")
    			    .setOngoing(true)
    			    .setAutoCancel(false);
    		
    		return mBuilder;
    	}
    	
    	private void lanxaFogo() {
    		int mNotificationId = 001;
    		NotificationManager mNotifyMgr = 
    		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    		// Builds the notification and issues it.
    		mNotifyMgr.notify(mNotificationId, mBuilder.build());

    	}

    public void updateUI() {
        Intent intent = new Intent(COPA_RESULT);

        //if(message != null)
            intent.putExtra(COPA_MESSAGE, "testingtheNotNull");

        broadcaster.sendBroadcast(intent);
    }
}