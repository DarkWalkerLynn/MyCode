package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.timetable.control.EditTextWithDelete;
import com.example.timetable.control.EditTextWithEye;
import com.example.timetable.data.TimeTableHelper;

public class Login_AP extends AppCompatActivity {
    private TimeTableHelper ttHelper;
    EditTextWithDelete login_account;
    EditTextWithEye login_password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ap);
        setTitle("登录");
        ttHelper=new TimeTableHelper(this,"TimeTable.db",null,1);
        login_account=(EditTextWithDelete)findViewById(R.id.login_account);
        login_password=(EditTextWithEye)findViewById(R.id.login_password);
        login=(Button)findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=login_account.getText().toString();
                String password=login_password.getText().toString();
                if(isNotEmpty(account)&&isNotEmpty(password)){
                    SQLiteDatabase db=ttHelper.getWritableDatabase();
                    String selection="user_id='"+account+"'";
                    Log.i("Login_AP",account);
                    Cursor cursor=db.query("USER",null,selection,null,null,null,null);
                    if(cursor.moveToFirst()){
                        if(password.equals(cursor.getString(cursor.getColumnIndex("user_password")))){
                            Toast.makeText(Login_AP.this,"登录成功！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    private boolean isNotEmpty(String string){
        if(string.equals("")||string.equals(" ")){
            return false;
        }
        return true;
    }
}