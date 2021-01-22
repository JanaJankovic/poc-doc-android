package feri.pora.pocket_doctor.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import feri.pora.datalib.Doctor;
import feri.pora.datalib.Message;
import feri.pora.datalib.Response;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.adapters.ChatAdapter;
import feri.pora.pocket_doctor.adapters.DoctorAdapter;
import feri.pora.pocket_doctor.events.OnListChanged;
import feri.pora.pocket_doctor.events.TriggerRefreshChat;
import feri.pora.pocket_doctor.network.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ChatFragment extends Fragment {
    private CompositeSubscription subscription;
    private RecyclerView messageRecycler;
    private ChatAdapter chatAdapter;

    private ArrayList<Message> messages;
    private Doctor doctor;

    private Button sendChatbox;
    private EditText editTextChat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_chat, null);

        Bundle bundle = getArguments();
        doctor = ApplicationState.getGson().fromJson(bundle.getString(getString(R.string.doctor2)),
                Doctor.class);
        String fullName[] = doctor.getFullName().split(" ");
        String title = getString(R.string.doctor3) + fullName[fullName.length - 1];
        ((UserNavigationActivity)requireActivity()).getSupportActionBar().setTitle(title);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.nav_host_fragment, new DoctorListFragment()).commit();
                        return true;
                    }
                }
                return false;
            }
        });

        messages = new ArrayList<>();
        subscription = new CompositeSubscription();
        bindGUI(rootView);
        getMessages();
        RefreshChat refreshChat = new RefreshChat();
        refreshChat.execute();

        sendChatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(editTextChat.getText().toString(),
                        ApplicationState.loadLoggedUser(), getString(R.string.sent));
                message.setReceiverId(doctor.GetId());
                sendMessage(message);
                editTextChat.setText("");
            }
        });



        return  rootView;
    }

    public void bindGUI(View view) {
        messageRecycler = (RecyclerView) view.findViewById(R.id.reyclerview_message_list);
        sendChatbox = (Button) view.findViewById(R.id.button_chatbox_send);
        editTextChat = (EditText) view.findViewById(R.id.edittext_chatbox);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TriggerRefreshChat event) {
        getMessages();
    }

    private void sendMessage(Message message) {
        subscription.add(NetworkUtil.getRetrofit().sendMessage(message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseMessage, this::handleError));
    }

    private void getMessages() {
        subscription.add(NetworkUtil.getRetrofit().getMessages(doctor.GetId(),
                ApplicationState.loadLoggedUser().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseMessages, this::handleError));
    }

    private void handleResponseMessages(ArrayList<Message> messages) {
        this.messages = messages;
        for (Message m : messages) {
            if (m.getStatus().equals(getString(R.string.received)))
                m.setSender(doctor);
            else if (m.getStatus().equals(getString(R.string.sent)))
                m.setSender(ApplicationState.loadLoggedUser());
        }
        System.out.println(getString(R.string.messages_recevvv) + messages.toString());
        chatAdapter = new ChatAdapter(this.messages);
        messageRecycler.setAdapter(chatAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void handleResponseMessage(Message message) {
        message.setSender(ApplicationState.loadLoggedUser());
        message.setStatus(getString(R.string.sent));
        System.out.println(getString(R.string.sentr_mssg) + message.toString());
        messages.add(message);
        chatAdapter.notifyDataSetChanged();
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = ApplicationState.getGson();
            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Log.i(getString(R.string.error), errorBody);
                //Response response = gson.fromJson(errorBody,Response.class);
                //Toast.makeText(requireContext(), response.getData(),  Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(getString(R.string.error), error.getLocalizedMessage());
            //Toast.makeText(requireContext(), error.getLocalizedMessage(),  Toast.LENGTH_LONG).show();
        }
    }

    public static class RefreshChat extends AsyncTask<Void, Void, Void> {
        private long lastRefresh;
        private final int refreshRate = 1000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lastRefresh = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while(true) {
                if (System.currentTimeMillis() - lastRefresh > refreshRate){
                    lastRefresh = System.currentTimeMillis();
                    EventBus.getDefault().post(new TriggerRefreshChat());
                }
            }
        }
    }

}
