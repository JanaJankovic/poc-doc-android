package feri.pora.pocket_doctor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import feri.pora.datalib.Message;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.adapters.ChatAdapter;

public class ChatFragment extends Fragment {
    private RecyclerView mMessageRecycler;
    private ChatAdapter mChatAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_chat, null);

        bindGui(rootView);

        return  rootView;
    }

    public void bindGui(View view) {
        // TODO - read from mongoDb - currently problem: android wont connect to node.js application to send get request for messages
        List<Message> messageList = null;

        mMessageRecycler = (RecyclerView) view.findViewById(R.id.reyclerview_message_list);
        mChatAdapter = new ChatAdapter(messageList);
        //mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
