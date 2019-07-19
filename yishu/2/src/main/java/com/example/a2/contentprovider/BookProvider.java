package com.example.a2.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.ThemedSpinnerAdapter;

public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";
    public static final String AUTHORITY = "com.example.a2.contentprovider.bookprovider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDb;


    public BookProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete start");
        String tableName = getTableName(uri);
        if (tableName==null){
            throw new IllegalArgumentException("错误的Uri："+uri);
        }
        int count = mDb.delete(tableName, selection, selectionArgs);
        if (count > 0){
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert");
        String tableName = getTableName(uri);
        if (tableName == null){
            throw new IllegalArgumentException("错误的表名："+uri);
        }
        mDb.insert(tableName, null,values);
        mContext.getContentResolver().notifyChange(uri, null);
        Log.d(TAG, "插入之后的表的Uri是："+uri);
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate current thread:"+Thread.currentThread().getName());
        mContext=getContext();
        initProviderData();
        return true;
    }

    /**
     * 初始化出来一个数据库的数据
     */
    private void initProviderData(){
        mDb=new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from "+DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from "+DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values (3, 'android')");
        mDb.execSQL("insert into book values (4, 'ios')");
        mDb.execSQL("insert into book values (5, 'html')");
        mDb.execSQL("insert into user values (1, 'jake', 1)");
        mDb.execSQL("insert into user values (2, 'jasmine', 0)");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query current thread is " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName==null){
            throw new IllegalArgumentException("错误的Uri："+uri);
        }
        return mDb.query(tableName, projection, selection, selectionArgs, null, null
        ,sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update");
        String tableName = getTableName(uri);
        if (tableName==null){
            throw new IllegalArgumentException("错误的Uri:"+uri);
        }
        int row = mDb.update(tableName, values, selection, selectionArgs);
        if (row>0){
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    /**
     * 通过uri来得到查询的表名
     * @param uri 要查询的Uri
     * @return Uri的表名
     */
    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
