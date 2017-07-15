package text.sqlitedatabase;

import android.content.ContentProvider;
        import android.content.ContentValues;
        import android.content.UriMatcher;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.net.Uri;
        import android.os.CancellationSignal;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;

public class DatabaseProvider extends ContentProvider {
    public static final int BOOK_DIR=0;
    public static final int BOOk_ITEM=1;
    private static final String AUTHORITY="text.sqlitedatabase.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbhelper;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"Book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"Book/#",BOOk_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbhelper=new MyDatabaseHelper(getContext(),"BookStore.db",null,1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor=db.query("Book",strings,s,strings1,null,null,s1);
                break;
            case BOOk_ITEM:
                String id=uri.getPathSegments().get(1);
                cursor= db.query("Book",strings,"id=?",new String[]{id},null,null,s1);
                break;
        }
        return cursor;
    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        Uri urireturn=null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOk_ITEM:
                long newBoookId=db.insert("Book",null,contentValues);
                urireturn=Uri.parse("content://"+AUTHORITY+"/Book/"+newBoookId);
                break;
        }
        return urireturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        int deletedRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deletedRows=db.delete("Book",s,strings);
                break;
            case BOOk_ITEM:
                String id=uri.getPathSegments().get(1);
                deletedRows=db.delete("Book","id=?",new String[]{id});
                break;
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows=db.update("Book",contentValues,s,strings);
                break;
            case BOOk_ITEM:
                String id=uri.getPathSegments().get(1);
                updateRows=db.update("Book",contentValues,"id=?",new String[]{id});
                break;
        }
        return updateRows;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.text.sqlitedatabase.provider.Book";
            case BOOk_ITEM:
                return "vnd.android.cursor.item/vnd.text.sqlitedatabase.provider.Book";
        }
        return null;
    }
}
