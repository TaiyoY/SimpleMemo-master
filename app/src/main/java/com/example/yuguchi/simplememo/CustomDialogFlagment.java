package com.example.yuguchi.simplememo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;


public class CustomDialogFlagment extends DialogFragment {
    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle("ダイアログタイトル");
        // 表示する文章設定
        dialogBuilder.setMessage("フォルダ名を入力してください");

        //入力フィールド
        final EditText editText = new EditText(getActivity());
        dialogBuilder.setView(editText);
        // OKボタン作成
        dialogBuilder.setPositiveButton("入力完了", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //editTextの内容を元画面に反映、値取得
                String returnValue = editText.getText().toString();
                ListActivity listActivity = (ListActivity) getActivity();
                listActivity.setTextView(returnValue);
            }
        });

        // NGボタン作成
        dialogBuilder.setNegativeButton("NG", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 何もしないで閉じる
            }
        });

        // dialogBulderを返す
        return dialogBuilder.create();
    }
}
