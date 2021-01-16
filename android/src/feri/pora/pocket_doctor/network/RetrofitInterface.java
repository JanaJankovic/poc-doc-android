package feri.pora.pocket_doctor.network;

import java.util.ArrayList;

import feri.pora.datalib.Doctor;
import feri.pora.datalib.Message;
import feri.pora.datalib.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {
    @POST("/patients/")
    Observable<User> register(@Body User user);

    @POST("/patients/login")
    Observable<User> login(@Body User user);

    @GET("/patients/{id}")
    Observable<User> getProfile(@Path("id") String id);

    @GET("/doctors/")
    Observable<ArrayList<Doctor>> getDoctors();

    @GET("messages/{doctorId}/{patientId}")
    Observable<ArrayList<Message>> getMessages(@Path("doctorId") String doctorId,
                                               @Path("patientId") String patientId);

    @POST("/messages/")
    Observable<Message> sendMessage(@Body Message message);
}


