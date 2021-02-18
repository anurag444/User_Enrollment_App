package com.example.internship_enrollment_app.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.internship_enrollment_app.MainActivity;
import com.example.internship_enrollment_app.R;
import com.example.internship_enrollment_app.User;
import com.example.internship_enrollment_app.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.zip.Inflater;

public class EnrollFragment extends Fragment {

    public static final int PICK_IMAGE = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Uri contentUri;
    private ImageView userImage;
    private MaterialTextView selectImage;
    private TextInputEditText firstName, lastName, dob, gender, country, state, homeTown, phoneNumber;
    private MaterialButton addUser;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enroll, container, false);

        //Init Views
        userImage = view.findViewById(R.id.user_image);
        selectImage = view.findViewById(R.id.select_image);
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        dob = view.findViewById(R.id.dob);
        gender = view.findViewById(R.id.gender);
        country = view.findViewById(R.id.country);
        state = view.findViewById(R.id.state);
        homeTown = view.findViewById(R.id.home_town);
        phoneNumber = view.findViewById(R.id.phone_number);
        addUser = view.findViewById(R.id.add_user);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);


        selectImage.setOnClickListener(view1 -> askGalleryPermission());


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewUser();
            }
        });


        return  view;
    }

    private void addNewUser() {
        String first_name, last_name, d_o_b, Gender, Country, State, home_town, phone_number;
        first_name = firstName.getText().toString();
        last_name = lastName.getText().toString();
        d_o_b = dob.getText().toString();
        Gender = gender.getText().toString();
        Country = country.getText().toString();
        State = state.getText().toString();
        home_town = homeTown.getText().toString();
        phone_number = phoneNumber.getText().toString();


        User user = new User(contentUri.toString(), first_name, last_name, d_o_b, Gender, Country, State, home_town, phone_number);
        userViewModel.insert(user);
        Toast.makeText(getContext(), "User Enrolled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            contentUri = data.getData();

            userImage.setImageURI(contentUri);
        }
    }
    private void askGalleryPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
    }
}
