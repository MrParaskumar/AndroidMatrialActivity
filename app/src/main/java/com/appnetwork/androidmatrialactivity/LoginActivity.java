package com.appnetwork.androidmatrialactivity;

import androidx.appcompat.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {

    LinearLayout lnr1,lnr2;
    Animation uptodown,downtoup;
    EditText edtsinginemail,edtsigninpassword;
    Button btnsignin,btnsignup;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lnr1=findViewById(R.id.lnr1);
        lnr2=findViewById(R.id.lnr2);
        edtsinginemail=findViewById(R.id.edtsinginemail);
        edtsigninpassword=findViewById(R.id.edtsigninpassword);
        btnsignin =findViewById(R.id.btnsignin);
        btnsignup =findViewById(R.id.btnsignup);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("login", false);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        lnr1.setAnimation(uptodown);
        lnr2.setAnimation(downtoup);

        if (b) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {

            btnsignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = edtsinginemail.getText().toString();
                    String password = edtsigninpassword.getText().toString();

                    if (!isValidEmail(email)) {
                        edtsinginemail.setError("Enter Valide Email");
                    } else if (!isValidPasswoed(password)) {
                        edtsigninpassword.setError("Enter 6-15 Password");
                    }else {

                        Call<LoginResponce> call = RetrofitClient.getInstance().getApi().login(email, password);
                        call.enqueue(new Callback<LoginResponce>() {
                            @Override
                            public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                                LoginResponce loginResponce = response.body();
                                if (response.isSuccessful()) {
                                    if (loginResponce.getMsg().equals("")) {
                                        editor=sharedPreferences.edit();
                                        editor.putBoolean("login",true);
                                        editor.apply();
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User is Incorrect", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponce> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Network check", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                }
            });

            btnsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
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
}
