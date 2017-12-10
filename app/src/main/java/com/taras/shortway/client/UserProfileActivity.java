package com.taras.shortway.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.taras.shortway.client.model.User;
import com.taras.shortway.client.model.enums.Gender;

import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private ImageButton menuButton;

    private TextView phone;
    private TextView email;
    private TextView login;
    private TextView gender;
    private TextView myselfInfo;

    private CircleImageView avatar;
    private TextView nameAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        phone = (TextView) findViewById(R.id.phone_user_profile);
        email = (TextView) findViewById(R.id.email_user_profile);
        login = (TextView) findViewById(R.id.login_user_profile);
        gender = (TextView) findViewById(R.id.gender_user_profile);
        myselfInfo = (TextView) findViewById(R.id.myself_in_profile);

        avatar = (CircleImageView) findViewById(R.id.avatar_profile);
        nameAge = (TextView) findViewById(R.id.name_age_profile);

        menuButton = (ImageButton) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(UserProfileActivity.this, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.profile_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(UserProfileActivity.this, EditInfoActivity.class);
                        startActivity(intent);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        User user = Globals.getUser();

        String shortUserInfo = getShortUserInfo(user);
        nameAge.setText(shortUserInfo);

        if (user.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length);
            avatar.setImageBitmap(Bitmap.createScaledBitmap(bmp, avatar.getWidth(), avatar.getHeight(), false));
        }

        phone.setText(user.getPhone());
        email.setText(user.getEmail());
        login.setText(user.getLoginPass().getLogin());
        if (user.getUserInfo() != null) {
            gender.setText(user.getUserInfo().getGender() == Gender.MALE ? "Чоловіча" : "Жіноча");
            myselfInfo.setText(user.getUserInfo().getInformation());
        }

    }

    private String getShortUserInfo(User user) {
        String name = user.getName();
        String lastName = user.getSurname();
        if (user.getUserInfo() != null && user.getUserInfo().getYear() != 0) {
            int years = Calendar.getInstance().get(Calendar.YEAR) - user.getUserInfo().getYear();
            return String.format(Locale.getDefault(), "%s %s (%d р.)", name, lastName, years);
        } else {
            return String.format(Locale.getDefault(), "%s %s", name, lastName);
        }
    }
}
