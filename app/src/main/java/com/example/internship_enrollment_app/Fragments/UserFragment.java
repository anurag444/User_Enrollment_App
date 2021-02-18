package com.example.internship_enrollment_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internship_enrollment_app.Adapter.UserAdapter;
import com.example.internship_enrollment_app.R;
import com.example.internship_enrollment_app.User;
import com.example.internship_enrollment_app.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserFragment extends Fragment {

    private List<User> users;
    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private UserViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        users = new ArrayList<>();
        usersRecyclerView = view.findViewById(R.id.users_list);
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        userAdapter = new UserAdapter(getContext(), users);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        usersRecyclerView.setAdapter(userAdapter);

        viewModel.getAllNotes().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> usersList) {
                Collections.sort(usersList, new Comparator<User>() {
                    public int compare(User obj1, User obj2) {
                        // Descending order
                        return Integer.valueOf(obj2.getId()).compareTo(obj1.getId()); // To compare integer values
                    }
                });
                userAdapter.setData(usersList);
            }
        });

        return view;
    }
}