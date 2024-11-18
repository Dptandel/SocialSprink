package com.imobile.socialsprink.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imobile.socialsprink.Adapter.NotificationAdapter;
import com.imobile.socialsprink.Model.Notification;
import com.imobile.socialsprink.R;

import java.util.ArrayList;

public class Notification2Fragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Notification> list;

    FirebaseDatabase database;

    public Notification2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification2, container, false);

        recyclerView = view.findViewById(R.id.notification2RV);

        list = new ArrayList<>();
        /*list.add(new Notification(R.drawable.profile, "<b>Fenil</b> mention you in a comment : Nice Try",
                "just now"));
        list.add(new Notification(R.drawable.deaf, "<b>Dharmin</b> Liked your picture.",
                "40 minutes ago"));
        list.add(new Notification(R.drawable.cover, "<b>Manan</b> Commented on your post.",
                "2 hours"));
        list.add(new Notification(R.drawable.profile, "<b>Manisha</b> mention you in a comment : Nice Try",
                "3 hours"));
        list.add(new Notification(R.drawable.deaf, "<b>Dharmin</b> Liked your picture.",
                "3 hours"));
        list.add(new Notification(R.drawable.cover, "<b>Kaushik</b> Commented on your post.",
                "4 hours"));
        list.add(new Notification(R.drawable.profile, "<b>Fenil</b> mention you in a comment : Nice Try",
                "6 hours"));
        list.add(new Notification(R.drawable.deaf, "<b>Dharmin</b> mention you in a comment : try again",
                "just now"));*/

        NotificationAdapter adapter = new NotificationAdapter(list, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        database.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            notification.setNotificationID(dataSnapshot.getKey());
                            list.add(notification);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }
}