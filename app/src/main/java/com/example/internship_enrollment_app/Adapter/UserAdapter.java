package com.example.internship_enrollment_app.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.internship_enrollment_app.R;
import com.example.internship_enrollment_app.User;
import com.example.internship_enrollment_app.UserViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private List<User> users = new ArrayList<>();
    private UserViewModel viewModel;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);

        return new UserViewHolder(view);

    }

    public void setData(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = users.get(position);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(UserViewModel.class);

        holder.userName.setText(user.getFirstName() + " " + user.getLastName());



        int year = Calendar.getInstance().get(Calendar.YEAR);
        String date = user.getDob();
        date = date.substring(date.length()-4);
        int dob_year = Integer.parseInt(date);
        year = year - dob_year;

        holder.userDetails.setText(user.getGender() + " | " + year  + " | " + user.getCountry());


        Glide.with(context).load(Uri.parse(user.getImage()))
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.delete(user);
            }
        });



    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userDetails;
        ImageView image,delete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            userDetails = itemView.findViewById(R.id.user_detials);
            image = itemView.findViewById(R.id.user_image);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
