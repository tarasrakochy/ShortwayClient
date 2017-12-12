package com.taras.shortway.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taras.shortway.client.model.LoginPass;
import com.taras.shortway.client.model.User;
import com.taras.shortway.client.model.UserInfo;
import com.taras.shortway.client.model.enums.Gender;
import com.taras.shortway.client.rest.ApiService;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private static final String[] genders = {"Чоловіча", "Жіноча"};

    private AutoCompleteTextView gender;
    private EditText nameForRegistration;
    private EditText surnameForRegistration;
    private AutoCompleteTextView yearOfBirth;
    private EditText login;
    private EditText phone;
    private EditText email;
    private EditText password;
    private Button acceptRegistrationButton;

    private ApiService apiService = new ApiService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setTitle("Реєстрація");

        gender = (AutoCompleteTextView) findViewById(R.id.gender);
        nameForRegistration = (EditText) findViewById(R.id.name_for_registration);
        surnameForRegistration = (EditText) findViewById(R.id.surname_for_registration);
        yearOfBirth = (AutoCompleteTextView) findViewById(R.id.year_of_birth);
        login = (EditText) findViewById(R.id.login);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        acceptRegistrationButton = (Button) findViewById(R.id.accept_registration_button);

        ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        gender.setAdapter(genderArrayAdapter);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).showDropDown();
            }
        });

        ArrayAdapter<Integer> yearArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getYearsOfBirth());
        yearOfBirth.setAdapter(yearArrayAdapter);
        yearOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView) v).showDropDown();
            }
        });

        acceptRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String genderText = gender.getText().toString();
                Gender genderForRegistration = genderText.equals("Чоловіча") ? Gender.MALE : Gender.FEMALE;

                String name = nameForRegistration.getText().toString();
                String surname = surnameForRegistration.getText().toString();
                int year = Integer.parseInt(yearOfBirth.getText().toString());
                String loginText = login.getText().toString();
                String phoneText = phone.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setEmail(emailText);
                user.setPhone(phoneText);

                UserInfo userInfo = new UserInfo();
                userInfo.setYear(year);
                userInfo.setGender(genderForRegistration);
                user.setUserInfo(userInfo);

                LoginPass loginPass = new LoginPass();
                loginPass.setLogin(loginText);
                loginPass.setPassword(passwordText);
                user.setLoginPass(loginPass);

                User newUser = apiService.addUser(user);
                if (newUser != null) {
                    Globals.setUser(newUser);
                    Toast.makeText(RegistrationActivity.this, R.string.user_registrated_info, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegistrationActivity.this, R.string.registration_fail, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private List<Integer> getYearsOfBirth() {
        List<Integer> years = new ArrayList<>();
        for (int i = 1999; i >= 1917; i--) {
            years.add(i);
        }
        return years;
    }

}
