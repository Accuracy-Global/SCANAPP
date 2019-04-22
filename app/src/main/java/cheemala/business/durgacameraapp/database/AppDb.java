package cheemala.business.durgacameraapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import cheemala.business.durgacameraapp.model.ScannedTextData;
import cheemala.business.durgacameraapp.utils.Constants;

public class AppDb extends SQLiteOpenHelper {

    private String TAG = "App_Db_";
    private Context context;
    private String CREATE_TABLE_COMMAND = "CREATE TABLE "+Constants.getDbTableName()+"("+Constants.getDbColumnIdName()+" INTEGER PRIMARY KEY AUTOINCREMENT,"+Constants.getDbColumnTextValue()+" TEXT,"+Constants.getDbColumnTimeStamp()+" TEXT)";
    private String DROP_TABLE_COMMAND = "DROP TABLE IF EXISTS "+Constants.getDbTableName();
    private String FETCH_ALL_SCANNED_DATA_COMMAND = "SELECT * FROM "+Constants.getDbTableName();

    public AppDb(Context context) {
        super(context, Constants.getDbName(), null, Constants.getDbVersion());
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMMAND);
        Log.d("table_","created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_COMMAND);
        onCreate(db);
    }

    public long addCapturedText(ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(Constants.getDbTableName(),null,contentValues);
    }

    public List<ScannedTextData> fetchAllScannedDta(){

        List<ScannedTextData> alScndDagta = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor usersCursor = db.rawQuery(FETCH_ALL_SCANNED_DATA_COMMAND,null);
        if (usersCursor != null && usersCursor.getCount() > 0){
            alScndDagta = new ArrayList<>();
            usersCursor.moveToFirst();
            do{
                //ShowMsg.showMsg(context,String.valueOf(usersCursor.getString(usersCursor.getColumnIndexOrThrow(Constants.getDbIdName()))));
                alScndDagta.add(new ScannedTextData(usersCursor.getString(usersCursor.getColumnIndexOrThrow(Constants.getDbColumnIdName())),usersCursor.getString(usersCursor.getColumnIndexOrThrow(Constants.getDbColumnTextValue())),usersCursor.getString(usersCursor.getColumnIndexOrThrow(Constants.getDbColumnTimeStamp()))));
            }while (usersCursor.moveToNext());
            return alScndDagta;
        }
        return null;

    }

    public long deleteUser(String recordId){
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.d("pos","_"+actlPos);
        return db.delete(Constants.getDbTableName(),Constants.getDbColumnIdName()+ "=" +recordId,null);
    }

}
