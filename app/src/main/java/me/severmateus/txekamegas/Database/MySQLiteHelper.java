package me.severmateus.txekamegas.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.severmateus.txekamegas.Model.*;

import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TchekaMegasBD";
 
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_CONSUMO_TABLE = "CREATE TABLE consumo ( " +
                "codConsumo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "consumoInicial LONG, "+
                "consumoGasto LONG, "+
                "consumoInicialCell LONG, "+
                "tempoDefinido LONG )";

        // create settings table
        db.execSQL(CREATE_CONSUMO_TABLE);

        // SQL statement to create book table
        String CREATE_SETTINGS_TABLE = "CREATE TABLE settings ( " +
                "codSettings INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "operadora TEXT, "+
                "corte BOOLEAN, "+
                "mensagens BOOLEAN )";
 
        // create settings table
        db.execSQL(CREATE_SETTINGS_TABLE);
                   
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS consumo");
        // Drop older books table if existed
    	db.execSQL("DROP TABLE IF EXISTS settings");
 
        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------
 
    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */
 
    // Nome das Tabelas
    private static final String TABLE_CONSUMO = "consumo";
    private static final String TABLE_SETTINGS = "settings";
 
    // Nome das Colunas das Tabelas
    //Consumo
    private static final String KEY_CODCONSUMO= "codConsumo";
    private static final String KEY_CONSUMO_INICIAL = "consumoInicial";
    private static final String KEY_CONSUMO_GASTO = "consumoGasto";
    private static final String KEY_CONSUMO_INICIAL_CELL = "consumoInicialCell";
    private static final String KEY_TEMPO_DEFINIDO = "tempoDefinido";

    //Settings
    private static final String KEY_CODSETTINGS = "codSettings";
    private static final String KEY_OPERADORA = "operadora";
    private static final String KEY_CORTE = "corte";
    private static final String KEY_MENSAGENS = "mensagens";

    private static final String[] COLUMNSCONSUMO = {KEY_CODCONSUMO,KEY_CONSUMO_INICIAL,KEY_CONSUMO_GASTO,KEY_CONSUMO_INICIAL_CELL,KEY_TEMPO_DEFINIDO};
    private static final String[] COLUMNSSETTINGS = {KEY_CODSETTINGS,KEY_OPERADORA,KEY_CORTE,KEY_MENSAGENS};

    //Settings
    public void addConsumo(Consumo consumo){

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CONSUMO_INICIAL, consumo.getConsumoInicial()); // get author
        values.put(KEY_CONSUMO_GASTO, consumo.getConsumoGasto()); // get author
        values.put(KEY_CONSUMO_INICIAL_CELL, consumo.getConsumoInicialCell()); // get author
        values.put(KEY_TEMPO_DEFINIDO, consumo.getTempoDefinido()); // get author

        // 3. insert
        db.insert(TABLE_CONSUMO, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Consumo getConsumo(int codConsumo){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_CONSUMO, // a. table
                        COLUMNSCONSUMO, // b. column names
                        " codConsumo = ?", // c. selections
                        new String[] { String.valueOf(codConsumo) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build cliente object
        Consumo consumo = new Consumo();
        consumo.setCodConsumo(Integer.parseInt(cursor.getString(0)));
        consumo.setConsumoInicial(Long.parseLong(cursor.getString(1)));
        consumo.setConsumoGasto(Long.parseLong(cursor.getString(2)));
        consumo.setConsumoInicialCell(Long.parseLong(cursor.getString(3)));
        consumo.setTempoDefinido(Long.parseLong(cursor.getString(4)));

        //Log.d("getBook("+id+")", book.toString());

        // 5. return book
        return consumo;
    }

    // Get All Clients
    public List<Consumo> getAllConsumo() { // Sabendo que so tera 1 apenas, ta reservado para as outras versoes do aplicativo.
        List<Consumo> consumos= new LinkedList<Consumo>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_CONSUMO;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Consumo consumo = null;
        if (cursor.moveToFirst()) {
            do {
                consumo = new Consumo();
                consumo.setCodConsumo(Integer.parseInt(cursor.getString(0)));
                consumo.setConsumoInicial(Long.parseLong(cursor.getString(1)));
                consumo.setConsumoGasto(Long.parseLong(cursor.getString(2)));
                consumo.setConsumoInicialCell(Long.parseLong(cursor.getString(3)));
                consumo.setTempoDefinido(Long.parseLong(cursor.getString(4)));

                // Add book to books
                consumos.add(consumo);
            } while (cursor.moveToNext());
        }

        //Log.d("getAllBooks()", books.toString());

        // return the list of clients
        return consumos;
    }

    // Updating single book
    public int updateConsumo(Consumo consumo) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CONSUMO_INICIAL, consumo.getConsumoInicial()); // get author
        values.put(KEY_CONSUMO_GASTO, consumo.getConsumoGasto()); // get author
        values.put(KEY_CONSUMO_INICIAL_CELL, consumo.getConsumoInicialCell()); // get author
        values.put(KEY_TEMPO_DEFINIDO, consumo.getTempoDefinido()); // get author

        // 3. updating row
        int i = db.update(TABLE_CONSUMO, //table
                values, // column/value
                KEY_CODCONSUMO+" = ?", // selections
                new String[] { String.valueOf(consumo.getCodConsumo()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single book
    public void deleteConsumo(Consumo consumo) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_CONSUMO,
                KEY_CODCONSUMO + " = ?",
                new String[] { String.valueOf(consumo.getCodConsumo()) });

        // 3. close
        db.close();

    }

    //Settings
    public void addSettings(Settings settings){
        
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_OPERADORA, settings.getOperadora()); // get author
        values.put(KEY_CORTE, settings.isCorte()); // get author
        values.put(KEY_MENSAGENS, settings.isMensagens()); // get author
        
        // 3. insert
        db.insert(TABLE_SETTINGS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close();
    }
 
    public Settings getSettings(int codSettings){
 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor =
                db.query(TABLE_SETTINGS, // a. table
                COLUMNSSETTINGS, // b. column names
                " codSettings = ?", // c. selections
                new String[] { String.valueOf(codSettings) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build cliente object
        Settings settings = new Settings();
        settings.setCodSettings(Integer.parseInt(cursor.getString(0)));
        settings.setOperadora(cursor.getString(1));
        settings.setCorte(Boolean.parseBoolean(cursor.getString(2)));
        settings.setMensagens(Boolean.parseBoolean(cursor.getString(3)));
        
        //Log.d("getBook("+id+")", book.toString());
 
        // 5. return book
        return settings;
    }
 
    // Get All Clients
    public List<Settings> getAllSettings() { // Sabendo que so tera 1 apenas, ta reservado para as outras versoes do aplicativo.
        List<Settings> settingss= new LinkedList<Settings>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_SETTINGS;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        Settings settings = null;
        if (cursor.moveToFirst()) {
            do {
                settings = new Settings();
                settings.setCodSettings(Integer.parseInt(cursor.getString(0)));
                settings.setOperadora(cursor.getString(1));
                settings.setCorte(Boolean.parseBoolean(cursor.getString(2)));
                settings.setMensagens(Boolean.parseBoolean(cursor.getString(3)));
 
                // Add book to books
                settingss.add(settings);
            } while (cursor.moveToNext());
        }
 
        //Log.d("getAllBooks()", books.toString());
 
        // return the list of clients
        return settingss;
    }
 
     // Updating single book
    public int updateSettings(Settings settings) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_OPERADORA, settings.getOperadora()); // get author
        values.put(KEY_CORTE, settings.isCorte()); // get author
        values.put(KEY_MENSAGENS, settings.isMensagens()); // get author
 
        // 3. updating row
        int i = db.update(TABLE_SETTINGS, //table
                values, // column/value
                KEY_CODSETTINGS+" = ?", // selections
                new String[] { String.valueOf(settings.getCodSettings()) }); //selection args
 
        // 4. close
        db.close();
 
        return i;
 
    }
 
    // Deleting single book
    public void deleteSettings(Settings settings) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete(TABLE_SETTINGS,
                KEY_CODSETTINGS+" = ?",
                new String[] { String.valueOf(settings.getCodSettings()) });
 
        // 3. close
        db.close();

    }
}