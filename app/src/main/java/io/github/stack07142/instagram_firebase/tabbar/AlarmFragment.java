package io.github.stack07142.instagram_firebase.tabbar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.github.stack07142.instagram_firebase.R;
import io.github.stack07142.instagram_firebase.model.AlarmDTO;

public class AlarmFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.alarmframgent_recyclerview);
        recyclerView.setAdapter(new AlarmRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<AlarmDTO> alarmDTOList = new ArrayList<>();

        public AlarmRecyclerViewAdapter() {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("alarms")
                    .orderByChild("destinationUid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            alarmDTOList.add(snapshot.getValue(AlarmDTO.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentview, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            switch (alarmDTOList.get(position).kind) {

                case 0:

                    String str_0 = alarmDTOList.get(position).userId + "님이 좋아요를 눌렀습니다.";

                    ((CustomViewHolder) holder).profileTextView.setText(str_0);
                    break;

                case 1:

                    String str_1 = alarmDTOList.get(position).userId + "님이" + alarmDTOList.get(position).message + "메세지를 남겼습니다.";

                    ((CustomViewHolder) holder).profileTextView.setText(str_1);
                    break;

                case 2:

                    String str_2 = alarmDTOList.get(position).userId + "님이 당신의 계정을 팔로워하기 시작했습니다.";

                    ((CustomViewHolder) holder).profileTextView.setText(str_2);
                    break;

            }
        }

        @Override
        public int getItemCount() {

            return alarmDTOList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView profileImageView;
            public TextView profileTextView;

            public CustomViewHolder(View itemView) {
                super(itemView);

                profileImageView = (ImageView) itemView.findViewById(R.id.commentviewitem_imageview_profile);
                profileTextView = (TextView) itemView.findViewById(R.id.commentviewitem_textview_profile);

            }

        }

    }
}
