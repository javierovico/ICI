package com.javierovico.ici.extra.network;

import com.javierovico.ici.extra.entities.AccessToken;
import com.javierovico.ici.extra.entities.Especialidad;
import com.javierovico.ici.extra.entities.PostResponse;
import com.javierovico.ici.extra.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiService {

    @POST("auth/register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @POST("auth/login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("email") String username, @Field("password") String password);

    @POST("auth/social_auth")
    @FormUrlEncoded
    Call<AccessToken> socialAuth(@Field("name") String name,
                                 @Field("email") String email,
                                 @Field("provider") String provider,
                                 @Field("provider_user_id") String providerUserId);

    @POST("auth/refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);

    @GET("auth/posts")
    Call<PostResponse> posts();

    @GET("auth/user")
    Call<User> user();

    @POST("auth/signup")
    @FormUrlEncoded
    Call<Object> signup(@Field("email") String email, @Field("password") String password, @Field("password_confirmation") String passwordConfirmation, @Field("name") String nombre, @Field("apellido") String apellido, @Field("cedula") String cedula, @Field("telefono") String telefono);

    @GET("admin/usuarios")
    Call<List<User>> usuarios();

    @DELETE("admin/usuarios/{id}")
    Call<Map<String,Object>> borrar(@Path("id") long id);

    @POST("admin/crear-doctor")
    @FormUrlEncoded
    Call<Map<String,Object>> crearDoctor(@Field("cedula") long cedula);

    @POST("admin/sacar-doctor")
    @FormUrlEncoded
    Call<Map<String,Object>> sacarDoctor(@Field("cedula") long cedula);

    @GET("auth/logout")
    Call<Map<String,Object>> logout();

    @GET("doctor/especialidad/{cedula}")
    Call<List<Especialidad>> especialidades(@Path("cedula") long cedula);

}
