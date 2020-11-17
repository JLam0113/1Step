package com.example.a1step;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context ctx;
    private List<User> userList;

    public UserAdapter(Context ctx, List<User>userList){
        this.ctx = ctx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.recyclerview_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.email.setText(user.getEmail());
        holder.totalSteps.setText(Integer.toString(user.getTotalSteps()));
        DecimalFormat currency= new DecimalFormat("###,###.##");
        holder.totalCalories.setText(currency.format(user.getTotalCalories()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView email, totalSteps, totalCalories;

        public UserViewHolder(@NonNull View itemView){
            super(itemView);

            email = itemView.findViewById(R.id.textEmail);
            totalSteps = itemView.findViewById(R.id.textSteps);
            totalCalories = itemView.findViewById(R.id.textCalories);

        }
    }
}
