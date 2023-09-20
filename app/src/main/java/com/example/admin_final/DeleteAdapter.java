package com.example.admin_final;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.myViewHolder> {

    List<NoticeData> list;
    Context context;


    public DeleteAdapter(List<NoticeData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewdata,parent,false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NoticeData data=list.get(position);
        holder.title.setText(data.getTitle());
        holder.date.setText(data.getDate());
        holder.time.setText(data.getTime());
        String imageURL = null;
        imageURL = data.getImage();
        Picasso.get().load(imageURL).into(holder.image);
        String key= data.getKey();

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(key);
                

            }
        });



    }
    private void delete(String key) {
        // creating a variable for our Database
        // Reference for Firebase.
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Update_notice").child(key);
        // we are use add listerner
        // for event listener method

        // which is called with query.

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,date,time;
        Button button;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView) itemView.findViewById(R.id.img_notice);
            title=(TextView) itemView.findViewById(R.id.text_notice);
            date=(TextView) itemView.findViewById(R.id.text_Date);
            time=(TextView) itemView.findViewById(R.id.text_Time);
            button=(Button) itemView.findViewById(R.id.dltbtn);
        }
    }
}
