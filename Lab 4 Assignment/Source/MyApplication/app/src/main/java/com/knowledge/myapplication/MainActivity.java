package com.knowledge.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public String nameOth, userNameOth, emailIdOth, passwordOth, confirmPasswordOth;
    String contactPhone;

    public boolean validation()
    {

        boolean valid = true;
        if(nameOth.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Enter a valid name", Toast.LENGTH_SHORT).show();
        }

        if (emailIdOth.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailIdOth).matches())
        {
            Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if(contactPhone.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter a phone number", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(userNameOth.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Enter a username", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (passwordOth.isEmpty() || passwordOth.length() < 8)
        {

            Toast.makeText(getApplicationContext(), "Password should be atleast 8 characters minimum", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (confirmPasswordOth.isEmpty() || confirmPasswordOth.length() < 8)
        {

            Toast.makeText(getApplicationContext(), "Password should be atleast 8 characters minimum", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if(!passwordOth.equals(confirmPasswordOth)){
            valid=false;
            Toast.makeText(getApplicationContext(), "Password mismatch!Please enter the passwords again!", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    public void register(View v)
    {
        SharedPreferences sharedPreferences;

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        EditText name=(EditText) findViewById(R.id.name);
        EditText email=(EditText) findViewById(R.id.email);
        EditText phone=(EditText) findViewById(R.id.phone);
        EditText username=(EditText) findViewById(R.id.username1);
        EditText password=(EditText) findViewById(R.id.password1);
        EditText cpassword=(EditText) findViewById(R.id.cpassword);

        nameOth =name.getText().toString();
        emailIdOth =email.getText().toString();
        contactPhone =phone.getText().toString();
        userNameOth =username.getText().toString();
        passwordOth =password.getText().toString();
        confirmPasswordOth =cpassword.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("UserName", userNameOth);
        editor.putString("Password", passwordOth);
        editor.commit();

        if(validation())
        {
            Intent loginpage= new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginpage);
        }


    }


}