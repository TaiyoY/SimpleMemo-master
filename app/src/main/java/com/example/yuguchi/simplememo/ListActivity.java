package com.example.yuguchi.simplememo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import java.util.ArrayList;
import java.util.HashMap;


public class ListActivity extends AppCompatActivity {

    // MemoOpenHelperクラスを定義
    MemoOpenHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // データベースから値を取得する
        if(helper == null){
            helper = new MemoOpenHelper(ListActivity.this);
        }
        // メモリストデータを格納する変数
        final ArrayList<HashMap<String, String>> memoList = new ArrayList<>();
        // データベースを取得する
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            Cursor c = db.rawQuery("select uuid, body, parentID, isFolder from MEMO_TABLE order by id", null);
            // Cursorの先頭行があるかどうか確認
            boolean next = c.moveToFirst();

            // 取得した全ての行を取得
            while (next) {
                HashMap<String,String> data = new HashMap<>();
                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                String uuid = c.getString(0);
                String body = c.getString(1);
                String parentID = String.valueOf(c.getInt(2));
                String isFolder = String.valueOf(c.getInt(3));
                String id = String.valueOf(c.getInt(4));
                if(body.length() > 10){
                    // リストに表示するのは10文字まで
                    body = body.substring(0, 11) + "...";
                }
                // 引数には、(名前,実際の値)という組合せで指定します　名前はSimpleAdapterの引数で使用します
                data.put("body",body);
                data.put("id",uuid);
                data.put("parentID", parentID);
                data.put("isFolder", isFolder);
                memoList.add(data);
                int ID = Integer.valueOf(id);
                // 次の行が存在するか確認
                next = c.moveToNext();
            }
        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close();
        }

        // Adapter生成
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                memoList, // 使用するデータ
                R.layout.custom_list_layout, // 使用するレイアウト
                new String[]{"body","id","parentID","isFolder"}, // どの項目を
                new int[]{R.id.mainText, R.id.subText, R.id.pID, R.id.isF} // どのidの項目に入れるか
        );

        // idがmemoListのListViewを取得
        ListView listView = (ListView) findViewById(R.id.memoList);
        listView.setAdapter(simpleAdapter);

        // リスト項目をクリックした時の処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int isFolder = R.id.isF;
                if (isFolder == 0) {
                    // インテント作成  第二引数にはパッケージ名からの指定で、遷移先クラスを指定
                    Intent intent = new Intent(ListActivity.this, com.example.yuguchi.simplememo.CreateMemoActivity.class);

                    // 選択されたビューを取得 TwoLineListItemを取得した後、text2の値を取得する
                    TwoLineListItem two = (TwoLineListItem) view;
                    TextView idTextView = (TextView) two.getText2();
                    String idStr = (String) idTextView.getText();
                    // 値を引き渡す (識別名, 値)の順番で指定します
                    intent.putExtra("id", idStr);
                    // Activity起動
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ListActivity.this, com.example.yuguchi.simplememo.ListActivity.class);
                    startActivity(intent);
                }
            }
        });

        // リスト項目を長押しクリックした時の処理
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 選択されたビューを取得 TwoLineListItemを取得した後、text2の値を取得する
                TwoLineListItem two = (TwoLineListItem)view;
                TextView idTextView = (TextView)two.getText2();
                String idStr = (String) idTextView.getText();

                // 長押しした項目をデータベースから削除
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    db.execSQL("DELETE FROM MEMO_TABLE WHERE uuid = '"+ idStr +"'");
                } finally {
                    db.close();
                }
                // 長押しした項目を画面から削除
                memoList.remove(position);
                simpleAdapter.notifyDataSetChanged();

                // trueにすることで通常のクリックイベントを発生させない
                return true;
            }
        });

        /**
         * メモ新規作成ボタン処理
         */
        // idがnewButtonのボタンを取得
        Button newButton = (Button) findViewById(R.id.newButton1);
        // clickイベント追加
        newButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // CreateMemoActivityへ遷移
                Intent intent = new Intent(ListActivity.this,com.example.yuguchi.simplememo.CreateMemoActivity.class);
                intent.putExtra("id", "");
                startActivity(intent);
            }
        });

        /**
         * フォルダ新規作成ボタン処理
         */
        //idがdialogButtonのButtonを取得
        Button dialogBtn = (Button) findViewById(R.id.newButton2);
        //clickイベント追加
        dialogBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            //クリックでダイヤログ表示
            public void onClick(View v){
                CustomDialogFlagment dialog = new CustomDialogFlagment();
                dialog.show(getFragmentManager(), "new_folder");
            }
        });

    }
}