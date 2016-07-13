//package me.severmateus.txekamegas.Fragments;
//
//
//import android.app.ProgressDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
///*
//import com.android.edgencio.fly.Activities.Detalhes;
//
//import com.android.edgencio.fly.Adapters.ListAdapter;
//import com.android.edgencio.fly.Classes.JSONParser;
//import com.android.edgencio.fly.Classes.Location;
//import com.android.edgencio.fly.Classes.Voo;
//import com.android.edgencio.fly.Data.BDHelper;
//import com.android.edgencio.fly.Data.VooContract;
//import com.android.edgencio.fly.R;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//*/
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import me.severmateus.txekamegas.R;
//
///**
// * A simple {@link android.support.v4.app.Fragment} subclass.
// */
//public class ItemFragment extends Fragment {
//
//    private ListView mLista;
//    private ListAdapter adapter;
//
//    //Variáveis usadas pela Asynctask
//
//    /*
//    public ArrayList<Voo> voos;
//    public ArrayList<Location>pontos;
//
//    public ArrayList<Location>pontosBaseDados;
//    public ArrayList<Location>locais;
//    public ArrayList<Voo>voosBaseDados;
//*/
//    private  String texto;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        texto= getArguments().getString("texto_pesquisado");
//        Bundle bundle = new Bundle();
//        bundle.putString("texto_pesquisado",texto );
//
//        getLoaderManager().initLoader(0, bundle, this);
//
//        Toast.makeText(getActivity().getApplicationContext(),texto,Toast.LENGTH_LONG).show();
//        Log.e("Só para ver o log do search: ",texto);
//
//
//    }
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        //Desenho dos Layouts
//        View view= inflater.inflate(R.layout.lista_voos,container,false);
//        mLista=(ListView) view.findViewById(R.id.listView);
//
//
//
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
//        super.onViewCreated(view,savedInstanceState);
//        mLista.setAdapter(adapter);
//        mLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
///*
//                Intent intent = new Intent(getActivity(), Detalhes.class);
//                Bundle bundle = new Bundle();
//                Voo vooClicado = (Voo) parent.getItemAtPosition(position);
//                String nome = vooClicado.getNome();
//                String partida = vooClicado.getPartida();
//                String chegada = vooClicado.getChegada();
//                String hora_chegada = vooClicado.getHora_chegada();
//                String hora_partida = vooClicado.getHora_partida();
//                String dia = vooClicado.getDiaSemana();
//                String preco = vooClicado.getPreco();
//                String tipo = vooClicado.getTipo();
//                String classe = vooClicado.getClasse();
//                String descricao = vooClicado.getDescricao();
//
//                bundle.putString("nome", nome);
//                bundle.putString("partida", partida);
//                bundle.putString("chegada", chegada);
//                bundle.putString("hora_chegada", hora_chegada);
//                bundle.putString("hora_partida", hora_partida);
//                bundle.putString("tipo", tipo);
//                bundle.putString("dia", dia);
//                bundle.putString("preco", preco);
//                bundle.putString("classe", classe);
//                bundle.putString("descricao", descricao);
//
//                intent.putExtras(bundle);
//                getActivity().startActivity(intent);
//*/
//            }
//
//        }); }
///*
//
//    @Override
//    public Loader onCreateLoader(int id, Bundle args) {
//
//
//        Context context= getActivity().getApplicationContext();
//        SQLiteDatabase bd= new BDHelper(context).getWritableDatabase();
//        String textinho=args.getString("cidade_pesquisa");
//
//
//
//
//        CursorLoader cursorLoader=null;
//        String where;
//        String[] whereArgs;
//        where = VooContract.Voo.VOO_CHEGADA + "=?";
//        whereArgs = new String[]{texto};
//
//        cursorLoader = new CursorLoader(getActivity(), VooContract.Voo.CONTENT_URI, null, where, whereArgs, null);
//
//
//
//        bd.close();
//        return  cursorLoader;
//    }
//
//
//    @Override
//    public void onLoadFinished(Loader loader, Cursor cursor) {
//        voosBaseDados= new ArrayList<Voo>();
//        boolean gotSomething=false;
//        int cont=0;
//        while(cursor.moveToNext()){
//            gotSomething=true;
//
//
//            Voo vooBD=new Voo();
//            String nomeVoo = cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_NOME));
//            String preco = cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_PRECO));
//            String tipo = cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_TIPO));
//            String classe=cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_CLASSE));
//            String hora_partida=cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_HORA_PARTIDA));
//            String hora_chegada=cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_HORA_CHEGADA));
//            String loc_partida=cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_PARTIDA));
//            String loc_chegada=cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_CHEGADA));
//            String idLoacalPartida=cursor.getString(cursor.getColumnIndex(VooContract.Voo.ID_LOCP));
//            String idLocalChegada=cursor.getString(cursor.getColumnIndex(VooContract.Voo.ID_LOCC));
//            String diaVoo=cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_DIA));
//            String descricao=cursor.getString(cursor.getColumnIndex(VooContract.Voo.VOO_DESCRICAO));
//
//            vooBD.setIdLocChegada(Integer.parseInt(idLocalChegada));
//            vooBD.setIdLocPartida(Integer.parseInt(idLoacalPartida));
//            vooBD.setNome(nomeVoo);
//            vooBD.setClasse(classe);
//            vooBD.setDiaSemana(diaVoo);
//            vooBD.setTipo(tipo);
//            vooBD.setPreco(preco);
//            vooBD.setHora_chegada(hora_chegada);
//            vooBD.setHora_partida(hora_partida);
//            vooBD.setPartida(loc_partida);
//            vooBD.setChegada(loc_chegada);
//            vooBD.setDescricao(descricao);
//
//            voosBaseDados.add(vooBD);
//
//            Log.e("Mensagem: ",voosBaseDados.get(cont).getDiaSemana());
//            cont++;
//
//
//        }
//        if(gotSomething){
//
//
//            adapter= new ListAdapter(voosBaseDados);
//
//            mLista.setAdapter(adapter);
//
//
//        }
//        else if(!gotSomething){
//
//            Context context= getActivity().getApplicationContext();
//
//        }
//    }
//
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }
//*/
//
//}