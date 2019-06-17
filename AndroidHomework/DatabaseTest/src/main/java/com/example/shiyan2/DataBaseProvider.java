package com.example.shiyan2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DataBaseProvider extends ContentProvider {
    public static final int BOOK_DIR=0;
    public static final int BOOK_ITEM=1;
    public static final int CATEGORY_DIR=2;
    public static final int CATEGORY_ITEM=3;

    public static final String AUTHORITY="com.example.databasetest.provider";
    public static UriMatcher sUriMatcher;
    private MyDatabaseHelper dbhelper;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        sUriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        sUriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        sUriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }
    public DataBaseProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int deleteRows=0;
        switch (sUriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows=db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:

                String bookId = uri.getPathSegments().get(1);
                deleteRows=db.delete("Book", "id = ? ", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows=db.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:

                String categoryId = uri.getPathSegments().get(1);
                deleteRows=db.delete("Category", "id = ? ", new String[]{categoryId});
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category";

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (sUriMatcher.match(uri)){
            case BOOK_DIR:

            case BOOK_ITEM:

                long newBookId = db.insert("Book", null, values);
                uriReturn=Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_DIR:

            case CATEGORY_ITEM:

                long newCategoryId = db.insert("Category", null, values);
                uriReturn=Uri.parse("content://"+AUTHORITY+"/book/"+newCategoryId);
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        dbhelper=new MyDatabaseHelper(getContext(), "BookStore.db", null, 1);
        return true;
    }

    /**
     * 根据不同的类型来进行查询，最终返回游标
     * @param uri 查询的数据所对应的内容提供器的uri对象
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return 游标
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)){
            case BOOK_DIR:
                cursor=db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:

                String bookId = uri.getPathSegments().get(1);
                cursor=db.query("Book", projection, "id = ?", new String[]{bookId}, null, null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor=db.query("Category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:

                String categoryId = uri.getPathSegments().get(1);
                cursor=db.query("Category", projection, "id = ?", new String[]{categoryId}, null, null,sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int updateRow = 0;
        switch (sUriMatcher.match(uri)){
            case BOOK_DIR:
                updateRow=db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:

                String bookId = uri.getPathSegments().get(1);
                db.update("Book", values, "id = ? ", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRow=db.update("Category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:

                String categoryId = uri.getPathSegments().get(1);
                db.update("Category", values, "id = ? ", new String[]{categoryId});
                break;
        }
        return updateRow;
    }

    private void showURI( Uri uri){
        Log.i("xuanqisnow", "uri 是 "+uri);
    }
}
