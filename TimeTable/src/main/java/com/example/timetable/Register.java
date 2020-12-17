package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.timetable.control.EditTextWithDelete;
import com.example.timetable.control.EditTextWithEye;
import com.example.timetable.data.TimeTableHelper;

public class Register extends AppCompatActivity {
    private TimeTableHelper ttHelper;
    private Button register;
    private EditTextWithDelete user_id;
    private EditTextWithEye register_setPassword;
    private EditTextWithEye register_confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_phonenum);
        setTitle("注册");
        /*user_id=(EditTextWithDelete)findViewById(R.id.user_id);
        register_setPassword=(EditTextWithEye)findViewById(R.id.register_setPassword);
        register_confirmPassword=(EditTextWithEye)findViewById(R.id.register_confirmPassword);
        ttHelper=new TimeTableHelper(this,"TimeTable.db",null,1);
        register=(Button)findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ttHelper.getWritableDatabase();
                if(isSame(register_setPassword.getText().toString(),register_confirmPassword.getText().toString())){
                    SQLiteDatabase db=ttHelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("user_id",user_id.getText().toString());
                    values.put("user_password",register_setPassword.getText().toString());
                    db.insert("USER",null,values);
                    Toast.makeText(Register.this,"注册成功！",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
    public boolean isSame(String password1,String password2){
        return password1.equals(password2);
    }
}