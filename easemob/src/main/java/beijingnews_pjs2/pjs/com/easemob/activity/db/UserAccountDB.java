package beijingnews_pjs2.pjs.com.easemob.activity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import beijingnews_pjs2.pjs.com.easemob.activity.bean.IMUser;

/**
 * Created by pjs984312808 on 2016/6/17.
 */
public class UserAccountDB {

    private DBHelper dbHelper;

    public static final int VERSION = 1;

    private Context context;

    public UserAccountDB(Context context) {
        this.context = context;

        dbHelper = new DBHelper(context);
    }

    /**
     * 添加用户
     *
     * @param immuser
     */
    public void addIMUser(IMUser immuser) {
        SQLiteDatabase wdb = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(AccountTable.COL_APP_USER_NAME, immuser.getAppUser());
        values.put(AccountTable.COL_HUANX_ID, immuser.getHxId());
        values.put(AccountTable.COL_NICK, immuser.getNick());
        values.put(AccountTable.COL_AVARTAR, immuser.getAvartar());

        wdb.replace(AccountTable.TABLE_NAME, null, values);
    }

    /**
     * 获取用户信息
     *
     * @param mUserName
     * @return
     */
    public IMUser getIMUser(String mUserName) {

        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        String sql = "select * from " + AccountTable.TABLE_NAME + " where " + AccountTable.COL_APP_USER_NAME + " =?";
        Cursor cursor = rdb.rawQuery(sql, new String[]{mUserName});

        if (cursor == null) {
            Toast.makeText(context, "此账号还没注册", Toast.LENGTH_SHORT).show();
            return null;
        }

        while (cursor.moveToNext()) {
            IMUser imuser = new IMUser();
            imuser.setAppUser(cursor.getString(cursor.getColumnIndex(AccountTable.COL_APP_USER_NAME)));
            imuser.setHxId(cursor.getString(cursor.getColumnIndex(AccountTable.COL_HUANX_ID)));
            imuser.setNick(cursor.getString(cursor.getColumnIndex(AccountTable.COL_NICK)));
            imuser.setAvartar(cursor.getString(cursor.getColumnIndex(AccountTable.COL_AVARTAR)));

            cursor.close();
            return imuser;
        }

        return null;
    }

    public IMUser getIMUserHxId(String hxId) {
        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        String sql = "select * from " + AccountTable.TABLE_NAME + " where " + AccountTable.COL_HUANX_ID + " =?";
        Cursor cursor = rdb.rawQuery(sql, new String[]{hxId});

       /* if (cursor == null) {
            Toast.makeText(context, "此账号还没注册", Toast.LENGTH_SHORT).show();
            return null;
        }*/

        while (cursor.moveToNext()) {
            IMUser imuser = new IMUser();
            imuser.setAppUser(cursor.getString(cursor.getColumnIndex(AccountTable.COL_APP_USER_NAME)));
            imuser.setHxId(cursor.getString(cursor.getColumnIndex(AccountTable.COL_HUANX_ID)));
            imuser.setNick(cursor.getString(cursor.getColumnIndex(AccountTable.COL_NICK)));
            imuser.setAvartar(cursor.getString(cursor.getColumnIndex(AccountTable.COL_AVARTAR)));

            cursor.close();
            return imuser;
        }
        return null;
    }


    static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "_account", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(AccountTable.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

class AccountTable {
    static final String TABLE_NAME = "_user_account";
    static final String COL_APP_USER_NAME = "_app_user_name";
    static final String COL_HUANX_ID = "_hx_id";
    static final String COL_NICK = "_nick";
    static final String COL_AVARTAR = "_avartar";

    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            + " ( " + COL_APP_USER_NAME + " TEXT PRIMARY KEY,"
            + COL_HUANX_ID + " TEXT,"
            + COL_NICK + " TEXT,"
            + COL_AVARTAR + " TEXT);";
}
