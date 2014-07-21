package Dishant.c0633714_dishant_dave;


import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




public class MyActivity extends Activity {

    private ListView list_lv;
    private EditText product_name;
    private EditText product_prize;
    private Button sub_btn;
    private Button ref_btn;
    private DBclass db;

    private ArrayList<String> productname;
    private ArrayList<String> productprize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        productname = new ArrayList<String>();
        productprize = new ArrayList<String>();
        items();
        getData();
    }

    private void items() {
        sub_btn = (Button) findViewById(R.id.submit_btn);
        ref_btn = (Button) findViewById(R.id.refresh_btn);
        product_name = (EditText) findViewById(R.id.ed1);
        product_prize = (EditText) findViewById(R.id.ed2);
        list_lv = (ListView) findViewById(R.id.dblist);

        ref_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getData();
            }
        });

        sub_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    protected void submitData() {
        String a = product_name.getText().toString();
        String b = product_prize.getText().toString();

        db = new DBclass(this);
        long num;
        try {
            db.open();
            num = db.insertmaster(a, b);
            db.close();
        } catch (SQLException e) {
            num = -5;
        } finally {
            getData();
        }
        if (num > 0)
            Toast.makeText(this, "Row number: " + num, 2000).show();
        else if (num == -1)
            Toast.makeText(this, "Error Duplicate value", 4000).show();
        else
            Toast.makeText(this, "Error while inserting", 2000).show();
    }

    public void getData() {
        productname.clear();
        productprize.clear();

        db = new DBclass(this);
        try {
            db.open();
            Cursor cur = db.getAllTitles();
            while (cur.moveToNext()) {
                String valueofproductname = cur.getString(1);
                String valueofprize = cur.getString(2);
//              Log.e("---****---", "***********   col 1 = " + valueofproductname);
//              Log.e("---****---", "***********   col 2 = " + valueofprize);

                productname.add(valueofproductname);
                productprize.add(valueofprize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        printList();
        setDataIntoList();
    }

    private void printList() {
        for (int i = 0; i < productname.size(); i++) {
            Log.e("***************",
                    productname.get(i) + " --- " + productprize.get(i));
        }
    }

    private void setDataIntoList() {

        // create the list item mapping
        String[] from = new String[] { "col_1", "col_2" };
        int[] to = new int[] { R.id.ed1, R.id.ed2 };

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < productname.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("col_1", productname.get(i));
            map.put("col_2", productprize.get(i));
            fillMaps.add(map);
        }

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
                R.layout.custom, from, to);
        list_lv.setAdapter(adapter);
    }
}
