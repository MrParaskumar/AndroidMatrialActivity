package com.appnetwork.androidmatrialactivity;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout lnr1,lnr2;
    Animation uptodown,downtoup;
    EditText edtsignupname,edtsignupemail,edtsignuppassword,edtsignupconformpassword;
    Button btnsignup;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lnr1=findViewById(R.id.lnr1);
        lnr2=findViewById(R.id.lnr2);
        edtsignupname =findViewById(R.id.edtsignupname);
        edtsignupemail =findViewById(R.id.edtsignupemail);
        edtsignuppassword =findViewById(R.id.edtsignuppassword);
        edtsignupconformpassword =findViewById(R.id.edtsignupconformpassword);
        btnsignup =findViewById(R.id.btnsignup);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("login", false);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        lnr1.setAnimation(uptodown);
        lnr2.setAnimation(downtoup);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtsignupname.getText().toString();
                String email = edtsignupemail.getText().toString();
                String password = edtsignuppassword.getText().toString();
                String conpassword = edtsignupconformpassword.getText().toString();

                if (!isVallidName(name)) {
                    edtsignupname.setError("Enter Only Name");
                } else if (!isValidEmail(email)) {
                    edtsignupemail.setError("Enter Email");
                } else if (!isValidPasswoed(password)) {
                    edtsignuppassword.setError("Enter 6-15 Password");
                } else if (!isValidConpassword(conpassword)) {
                    edtsignupconformpassword.setError("Enter True Password");
                } else {

                    Call<ResponseBody> call=RetrofitClient.getInstance().getApi().register(name,email,password);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String s=null;
                            try {
                                if (response.code()==200){
                                    s=response.body().string();
                                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
//                                    Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_LONG).show();
                                }else {
                                    s=response.errorBody().string();
//                                    Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if (s==null){
                                try {
                                    JSONObject jsonObject=new JSONObject(s);
                                    Toast.makeText(RegisterActivity.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this,"Network check", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }

    private boolean isVallidName(String name) {
        String USER_NAME = "[a-zA-Z]+";
        if (name != null && name.matches(USER_NAME)) {
            return true;
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{3,})$";
        if (email != null && email.matches(EMAIL_PATTERN)) {
            return true;
        }
        return false;
    }

    private boolean isValidPasswoed(String password) {
        if (password != null && password.length() >= 6 && password.length() <= 15) {
            return true;
        }
        return false;
    }

    private boolean isValidConpassword(String conpassword) {
        String password = edtsignuppassword.getText().toString();
        if (conpassword != null && conpassword.equals(password)) {
            return true;
        }
        return false;
    }
}
