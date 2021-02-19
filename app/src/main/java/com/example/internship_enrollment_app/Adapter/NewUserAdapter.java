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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.internship_enrollment_app.R;
import com.example.internship_enrollment_app.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class NewUserAdapter extends FirebaseRecyclerAdapter<
        User, UserAdapter.UserViewHolder> {

    Context context;


    public NewUserAdapter(@NonNull FirebaseRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position, @NonNull User user) {


        //viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(UserViewModel.class);

        holder.userName.setText(user.getFirstName() + " " + user.getLastName());


        int year = Calendar.getInstance().get(Calendar.YEAR);
        String date = user.getDob();
        date = date.substring(date.length() - 4);
        int dob_year = Integer.parseInt(date);
        year = year - dob_year;

        holder.userDetails.setText(user.getGender() + " | " + year + " | " + user.getCountry());


        Glide.with(context).load(Uri.parse(user.getImage())).placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.image);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user1 = dataSnapshot.getValue(User.class);
                            if (user1.getId().matches(user.getId())) {
                                dataSnapshot.getRef().removeValue();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);
        return new UserAdapter.UserViewHolder(view);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userDetails;
        ImageView image, delete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userDetails = itemView.findViewById(R.id.user_detials);
            image = itemView.findViewById(R.id.user_image);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
