package com.example.fakenews.apiWeb;

import com.example.fakenews.apiModel.AddHechoBody;
import com.example.fakenews.apiModel.DTRespuesta;
import com.example.fakenews.apiModel.Hecho;
import com.example.fakenews.apiModel.LoginBody;
import com.example.fakenews.apiModel.LoginResponse;
import com.example.fakenews.apiModel.MailBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    //getHechosByEstado/{estado}
    @GET("getHechosByEstado/PUBLICADO/")
    Call<List<Hecho>> getAllHechos();

    @POST("citizen/addHecho/")
    Call<DTRespuesta> crearHecho(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body AddHechoBody addHechoBody);

    @POST("citizen/login/")
    Call<LoginResponse> login(@Body LoginBody loginBody);

    @POST("citizen/suscripcion/")
    Call<DTRespuesta> suscripcion(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body MailBody mail);


}
