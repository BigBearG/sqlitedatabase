package text.sqlitedatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button bt_creat,bt_insert,bt_select,bt_update,bt_delete;
    private EditText et_show;
    private MyDatabaseHelper helper =  new MyDatabaseHelper(MainActivity.this,"BookStore.db",null,1);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_creat = (Button) findViewById(R.id.bt_create);
        bt_insert = (Button) findViewById(R.id.bt_insert);
        bt_select = (Button) findViewById(R.id.bt_select);
        bt_update = (Button) findViewById(R.id.bt_updata);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        et_show = (EditText) findViewById(R.id.et_show);
        bt_creat.setOnClickListener(new MyOnclick());
        bt_insert.setOnClickListener(new MyOnclick());
        bt_select.setOnClickListener(new MyOnclick());
        bt_update.setOnClickListener(new MyOnclick());
        bt_delete.setOnClickListener(new MyOnclick());
    }

    public class MyOnclick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            SQLiteDatabase db = helper.getReadableDatabase();
            switch (view.getId()){
                case R.id.bt_select:
                    Cursor cursor = db.rawQuery("select * from Book",null);
                    if (cursor.moveToFirst()){
                        do {
                            String author = cursor.getString(cursor.getColumnIndex("author"));
                            int price = cursor.getInt(cursor.getColumnIndex("price"));
                            int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            String show = "author:"+author+",price:"+price+",pages:"+pages+",name:"+name;
                            et_show.setText(show);
                        }while (cursor.moveToNext());
                    }else {
                        et_show.setText("没有数据");
                    }
                    break;
                case R.id.bt_create:
                    helper.getReadableDatabase();
                    break;
                case R.id.bt_insert:
                    db.execSQL("insert into Book(author,price,pages,name) values(?,?,?,?)",new String[]{"lyh","18","22","android"});
                    break;
                case R.id.bt_updata:

                    db.execSQL("update Book set price = ? where name =?",new String[]{"4533","android"});
                    break;
                case R.id.bt_delete:
                    db.execSQL("delete from book where name = ?",new String[]{"android"});
                    break;
                default:
                    break;
            }
        }
    }
}