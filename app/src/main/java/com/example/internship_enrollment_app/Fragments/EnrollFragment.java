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

import com.bumptech.glide.Glide;
import com.example.internship_enrollment_app.R;
import com.example.internship_enrollment_app.Model.User;
import com.example.internship_enrollment_app.RoomDatabase.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EnrollFragment extends Fragment {

    public static final int PICK_IMAGE = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Uri contentUri;
    private ImageView userImage;
    private MaterialTextView selectImage;
    private TextInputEditText firstName, lastName, dob, gender, country, state, homeTown, phoneNumber;
    private MaterialButton addUser;
    private UserViewModel userViewModel;
    private DatabaseReference reference;

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
        reference = FirebaseDatabase.getInstance().getReference().child("users");


        selectImage.setOnClickListener(view1 -> askGalleryPermission());

        //Pick up date
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        dob.setOnClickListener(view13 -> materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> dob.setText(materialDatePicker.getHeaderText()));

        addUser.setOnClickListener(view12 -> addNewUser());


        return view;
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

        if (contentUri == null) {
            Toast.makeText(getContext(), "Please Select an Image", Toast.LENGTH_SHORT).show();
        }

        if (first_name.matches("") || last_name.matches("") ||
                d_o_b.matches("") || Gender.matches("") || Country.matches("") ||
                State.matches("") || home_town.matches("") || phone_number.matches("")) {

            Toast.makeText(getContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone_number.length() != 10) {
            Toast.makeText(getContext(), "Phone Number Not Valid", Toast.LENGTH_SHORT).show();
            return;
        }


        User new_user = new User(contentUri.toString(), first_name, last_name, d_o_b, Gender, Country, State, home_town, phone_number);



        //Adding Data to Firebase and checking for existing phone number
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isExist = false;
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.getPhone().equals(phone_number)) {
                            Toast.makeText(getContext(), "This phone number exists", Toast.LENGTH_SHORT).show();
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        String id = reference.push().getKey();
                        new_user.setId(id);
                        //userViewModel.insert(new_user);
                        reference.push().setValue(new_user);
                        Toast.makeText(getContext(), "User Enrolled", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                } else {
                    String id = reference.push().getKey();
                    new_user.setId(id);
                    reference.push().setValue(new_user);
                    Toast.makeText(getContext(), "User Enrolled", Toast.LENGTH_SHORT).show();
                    clearFields();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clearFields() {
        //Clear all fields
        TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabhost.getTabAt(0).select();
        firstName.setText("");
        lastName.setText("");
        dob.setText("");
        gender.setText("");
        country.setText("");
        state.setText("");
        homeTown.setText("");
        phoneNumber.setText("");
        Glide.with(getContext()).load(R.drawable.select_image)
                .into(userImage);
        contentUri = null;
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
