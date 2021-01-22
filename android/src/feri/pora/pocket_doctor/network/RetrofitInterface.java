package feri.pora.pocket_doctor.network;

import java.util.ArrayList;

import feri.pora.datalib.Data;
import feri.pora.datalib.Diagnosis;
import feri.pora.datalib.Doctor;
import feri.pora.datalib.Message;
import feri.pora.datalib.Prediction;
import feri.pora.datalib.Therapy;
import feri.pora.datalib.User;
import feri.pora.pocket_doctor.events.OnMeasurementSend;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {
    @POST("/patients/")
    Observable<User> register(@Body User user);

    @POST("/patients/login")
    Observable<User> login(@Body User user);

    @GET("/patients/{id}")
    Observable<User> getProfile(@Path("id") String id);

    @PUT("/patients/{id}")
    Observable<User> updateProfile(@Path("id") String id, @Body User user);

    @GET("/doctors/")
    Observable<ArrayList<Doctor>> getDoctors();

    @GET("messages/{doctorId}/{patientId}")
    Observable<ArrayList<Message>> getMessages(@Path("doctorId") String doctorId,
                                               @Path("patientId") String patientId);
    @POST("/messages/")
    Observable<Message> sendMessage(@Body Message message);

    @POST("/prediction/")
    Observable<Prediction> postPrediction(@Body Prediction prediction);

    @PUT("/prediction/{id}")
    Observable<Prediction> updatePrediction(@Path("id") String predictionId, @Body Prediction prediction);

    @DELETE("/prediction/{id}")
    Observable<Void> deletePrediction(@Path("id") String predictionId);

    @GET("/prediction/user/{id}")
    Observable<ArrayList<Prediction>> getPending(@Path("id") String userId);

    @POST("/transaction/newMeasureDataTransaction")
    Observable<Void> postMeasureData(@Body OnMeasurementSend body);

    @POST("/blockchain/getTherapiesPatient")
    Observable<ArrayList<Therapy>> getTherapies(@Body Data data);

    @POST("/blockchain/getDiagnosisPatient")
    Observable<ArrayList<Diagnosis>> getDiagnosis(@Body Data data);
}


