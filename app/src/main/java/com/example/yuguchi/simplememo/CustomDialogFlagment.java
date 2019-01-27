package com.example.yuguchi.simplememo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.UUID;

public class CustomDialogFlagment extends DialogFragment {
    // ダイアログが生成された時に呼ばれるメソッド
/**
    //MEMO_TABLE呼び出し
    CustomDialogFlagment CDFhelper = null;
    boolean newFlag = false;
    String id = "";

    if(CDFhelper == null){
        CDFhelper = new MemoOpenHelper(CustomDialogFlagment.this);
    }
*/

    public Dialog onCreateDialog(Bundle savedInstanceState){
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle("フォルダ名入力画面");
        // 表示する文章設定
        dialogBuilder.setMessage("フォルダ名を入力してください");

        //入力フィールド
        final EditText editText = new EditText(getActivity());
        dialogBuilder.setView(editText);
        // OKボタン作成
        dialogBuilder.setPositiveButton("入力完了", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //値取得
                String returnValue = editText.getText().toString();
                //データベースに登録
                SQLiteDatabase db =
                db.execSQL("insert into MEMO_TABLE(uuid, body, parentId, isFolder) VALUES('"+ id +"', '"+ returnValue +"', '"+ id +"', '"+ 0 +"')");

            }
        });

        // 戻るボタン作成
        dialogBuilder.setNegativeButton("戻る", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 何もしないで閉じる
            }
        });

        // dialogBulderを返す
        return dialogBuilder.create();
    }
}
