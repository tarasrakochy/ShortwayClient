package com.taras.shortway.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taras.shortway.client.model.User;
import com.taras.shortway.client.model.enums.Gender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditInfoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private static final String[] genders = {"Чоловіча", "Жіноча"};

    private User user;

    private AutoCompleteTextView gender;
    private EditText nameForEdit;
    private EditText surnameForEdit;
    private AutoCompleteTextView yearOfBirth;
    private EditText login;
    private EditText phone;
    private EditText email;
    private EditText myselfInfo;

    private Button choosePhotoButton;
    private CircleImageView avatar;

    private Button saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        setTitle("Редагування");

        gender = (AutoCompleteTextView) findViewById(R.id.gender_edit);
        nameForEdit = (EditText) findViewById(R.id.name_for_edit);
        surnameForEdit = (EditText) findViewById(R.id.surname_for_edit);
        yearOfBirth = (AutoCompleteTextView) findViewById(R.id.year_of_birth_edit);
        login = (EditText) findViewById(R.id.login_edit);
        phone = (EditText) findViewById(R.id.phone_edit);
        email = (EditText) findViewById(R.id.email_edit);
        myselfInfo = (EditText) findViewById(R.id.myself_edit);

        choosePhotoButton = (Button) findViewById(R.id.choose_photo_button);
        avatar = (CircleImageView) findViewById(R.id.avatar_profile);

        saveChangesButton = (Button) findViewById(R.id.save_changes_button);

        user = Globals.getUser();

        if (user.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
            avatar.setImageBitmap(Bitmap.createScaledBitmap(bmp, avatar.getWidth(), avatar.getHeight(), false));
        }

        if (user.getUserInfo() != null) {
            gender.setText(user.getUserInfo().getGender() == Gender.MALE ? "Чоловіча" : "Жіноча");
            yearOfBirth.setText(user.getUserInfo().getYear());
            myselfInfo.setText(user.getUserInfo().getInformation());
        }
        nameForEdit.setText(user.getName());
        surnameForEdit.setText(user.getSurname());
        login.setText(user.getLoginPass().getLogin());
        phone.setText(user.getPhone());
        email.setText(user.getEmail());

//        user.setName(nameForEdit.getText().toString());
//        user.setSurname(surnameForEdit.getText().toString());
//        user.setPhone(phone.getText().toString());
//        user.setEmail(email.getText().toString());

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

        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO server request
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    //TODO server request
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
