package com.example.fakenews.apiWeb;

import com.example.fakenews.apiModel.AddHechoBody;
import com.example.fakenews.apiModel.DTRespuesta;
import com.example.fakenews.apiModel.Hecho;
import com.example.fakenews.apiModel.LoginBody;
import com.example.fakenews.apiModel.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {


    @GET("getHechos/")
    Call<List<Hecho>> getAllHechos();

    @POST("citizen/addHecho/")
    Call<DTRespuesta> crearHecho(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body AddHechoBody addHechoBody);

    @POST("citizen/login/")
    Call<LoginResponse> login(@Body LoginBody loginBody);

    @POST("citizen/suscripcion/")
    Call<DTRespuesta> suscripcion(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body String mail);

/*
    @POST("estudiante/token/")
    Call<String> enviarTokenFirebase(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body TokenFirebaseBody tokenFirebaseBody);

    @GET("director/carrera/")
    public Call<List<DtCarrera>> getAllCarreras(@Header("Authorization") String authorization);

    @GET("estudiante/curso/{cedula}/")
    public Call<List<DtCurso>> getCursosByCedula(@Header("Authorization") String authorization, @Path("cedula") String cedula);

    @GET("estudiante/examen/{cedula}/")
    public Call<List<DtExamen>> getExamenesByCedula(@Header("Authorization") String authorization, @Path("cedula") String cedula);

    @GET("estudiante/consultarCalificaciones/{cedula}/")
    public Call<DtCalificaciones> getCalificacionesSAsig(@Header("Authorization") String authorization,
                                                         @Path("cedula") String cedula);

    @GET("estudiante/consultarCalificaciones/{cedula}/{idAsig_Carrera}/")
    public Call<DtCalificaciones> getCalificaciones(@Header("Authorization") String authorization,
                                                    @Path("cedula") String cedula,
                                                    @Path("idAsig_Carrera") Long idAsig_Carrera);

    @POST("estudiante/inscripcionCarrera/")
    public Call<String> inscripcionCarrera(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body InscripcionCarreraBody inscripcionCarreraBody);

    @POST("estudiante/inscripcionCurso/")
    public Call<String> inscripcionCurso(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body InscripcionCursoBody inscripcionCursoBody);

    @POST("estudiante/inscripcionExamen/")
    public Call<String> inscripcionExamen(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Body InscripcionExamenBody inscripcionExamenBody);
*/
}
