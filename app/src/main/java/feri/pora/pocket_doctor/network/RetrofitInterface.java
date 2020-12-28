package feri.pora.pocket_doctor.network;

import feri.pora.datalib.Response;
import feri.pora.datalib.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {
    @POST("patients/")
    Observable<User> register(@Body User user);

    @POST("patients/login")
    Observable<User> login();

    @GET("patients/{id}")
    Observable<User> getProfile(@Path("id") String id);
}
