package com.imobile.socialsprink.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imobile.socialsprink.Adapter.FollowersAdapter;
import com.imobile.socialsprink.Model.Follow;
import com.imobile.socialsprink.Model.User;
import com.imobile.socialsprink.R;
import com.imobile.socialsprink.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    ArrayList<Follow> list;

    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getCoverPhoto())
                            .placeholder(R.drawable.placeholder)
                            .into(binding.coverPhoto);

                    Picasso.get()
                            .load(user.getProfile())
                            .placeholder(R.drawable.placeholder2)
                            .into(binding.profileImage);

                    binding.name.setText(user.getName());
                    binding.profession.setText(user.getProfession());
                    binding.followers.setText(user.getFollowerCount()+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list = new ArrayList<>();

        FollowersAdapter adapter = new FollowersAdapter(list, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.followersRV.setLayoutManager(linearLayoutManager);
        binding.followersRV.setAdapter(adapter);

        database.getReference().child("Users")
                        .child(auth.getUid())
                                .child("followers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Follow follow = dataSnapshot.getValue(Follow.class);
                            list.add(follow);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.changeCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 11);
            }
        });

        binding.verifiedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 22);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11){
            if (data.getData()!=null){
                Uri uri = data.getData();
                binding.coverPhoto.setImageURI(uri);

                final StorageReference reference = storage.getReference().child("cover_photo")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Cover Photo Saved", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        } else {
            if (data.getData()!=null){
                Uri uri = data.getData();
                binding.profileImage.setImageURI(uri);

                final StorageReference reference = storage.getReference().child("profile_image")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Profile Photo Saved", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(auth.getUid()).child("profile").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        }

    }
}