package com.example.internship_enrollment_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internship_enrollment_app.Adapter.NewUserAdapter;
import com.example.internship_enrollment_app.R;
import com.example.internship_enrollment_app.Model.User;
import com.example.internship_enrollment_app.RoomDatabase.UserViewModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private List<User> users;
    private RecyclerView usersRecyclerView;
    private UserViewModel viewModel;
    private DatabaseReference reference;
    private NewUserAdapter newUserAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        users = new ArrayList<>();
        usersRecyclerView = view.findViewById(R.id.users_list);
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        reference = FirebaseDatabase.getInstance().getReference("users");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        usersRecyclerView.setLayoutManager(linearLayoutManager);


        FirebaseRecyclerOptions<User> options
                = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(reference, User.class)
                .build();


        newUserAdapter = new NewUserAdapter(options, getActivity());
        usersRecyclerView.setAdapter(newUserAdapter);


        //Fetching data from Room Database
//        viewModel.getAllNotes().observe(getActivity(), new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> usersList) {
//                Collections.sort(usersList, new Comparator<User>() {
//                    public int compare(User obj1, User obj2) {
//                        // Descending order
//                        return Integer.valueOf(obj2.getId()).compareTo(obj1.getId()); // To compare integer values
//                    }
//                });
//                userAdapter.setData(usersList);
//            }
//        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        newUserAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        newUserAdapter.startListening();

    }


}