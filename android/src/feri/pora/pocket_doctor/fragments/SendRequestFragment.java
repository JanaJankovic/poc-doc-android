package feri.pora.pocket_doctor.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.util.Base64;

import org.apache.commons.io.FileUtils;

import feri.pora.datalib.ResponsePython;
import feri.pora.pocket_doctor.ApplicationState;
import feri.pora.pocket_doctor.R;
import feri.pora.pocket_doctor.activities.UserNavigationActivity;
import feri.pora.pocket_doctor.config.ApplicationConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendRequestFragment extends Fragment {

    private ImageView imageView;
    private Button buttonCancel;
    private Button buttonSendRequest;

    private String filepath;

    public SendRequestFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_send_request, null);

        bindGUI(rootView);
        ((UserNavigationActivity) requireActivity()).getSupportActionBar().hide();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = this.getArguments();
        filepath = bundle.getString("filepath");

        setPictureBackground(rootView);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserNavigationActivity) requireActivity()).getSupportActionBar().show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.nav_host_fragment, new RequestAnalysisFragment()).commit();
            }
        });

        buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return  rootView;
    }

    public void bindGUI(View view) {
        imageView = (ImageView) view.findViewById(R.id.chosenImage);
        buttonCancel = (Button) view.findViewById(R.id.buttonClose);
        buttonSendRequest = (Button) view.findViewById(R.id.buttonSendRequest);
    }

    public void setPictureBackground(View view) {
        Log.i("TAGGGG", filepath);
        if (filepath.length() > 0) {
            File imageFile = new File(filepath);
            Picasso.get().load(imageFile).fit().centerCrop().into(imageView);
        }
    }

    public void saveFile(String base64) throws IOException {
        String path[] = filepath.split("/");
        String file[] = path[path.length  - 1].split("\\.");
        String newPath = "";
        for(int i = 0; i < path.length - 2; i++){
            newPath+= path[i] + "/";
        }
        newPath += file[0] + "_analyzed." + file[1];
        File newFile = new File(newPath);

        if (!newFile.exists()) {
            newFile.createNewFile();
        }

        try (FileOutputStream stream = new FileOutputStream(newFile)) {
            stream.write(Base64.decode(base64, Base64.DEFAULT));
        }
    }

    public void postRequest() throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("image/jpg");
        String url = ApplicationConfig.HOST_IP_PYTHON + "/poc-doc/recognise";
        OkHttpClient client = new OkHttpClient();

        File file = new File(filepath);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image","profile.jpg",
                        RequestBody.create(MEDIA_TYPE, file)).build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "multipart/form-data;boundary=\"boundary\"")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponsePython responsePython = ApplicationState.getGson()
                        .fromJson(response.body().string(), ResponsePython.class);
                saveFile(responsePython.getImageBytes());
                //make post request to node

            }
        });
    }
}