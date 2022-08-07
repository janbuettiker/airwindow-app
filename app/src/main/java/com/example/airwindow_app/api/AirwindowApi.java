package com.example.airwindow_app.api;

import com.example.airwindow_app.models.Room;
import com.example.airwindow_app.models.Window;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AirwindowApi {

    String BASE_URL = "https://airwindow-api.jblabs.ch/";
    /*
        So.. we cannot use the BASE_URL custom domain because I do not have
        a properly signed SSL certificate. OpenSSL didn't do the trick.
        So yes, to get access to our API with android, we need to use the backend url BASE_URL_BACKEND.
     */
    String BASE_URL_BACKEND = "https://airwindow-api-jdk.prouddune-5091e507.westeurope.azurecontainerapps.io/";

    @GET("homes/{homeid}/rooms")
    Call<List<Room>> getAllRooms(@Path("homeid") Long homeId);

    @PUT("homes/{homeid}/rooms/{roomid}")
    Call<Room> putRoom(@Path("homeid") Long homeId,
                       @Path("roomid") Long roomId,
                       @Body Room room);

    @GET("rooms/1/windows")
    Call<List<Window>> getAllWindows();

    @POST("rooms/1/windows")
    Call<Window> createWindow(@Body Window window);

    @PUT("rooms/{roomid}/windows/{windowid}")
    Call<Window> putWindow(@Path("roomid") Long roomId,
                           @Path("windowid") Long windowId,
                           @Body Window window);

    @DELETE("rooms/{roomid}/windows/{windowid}")
    Call<String> deleteWindow(@Path("roomid") Long roomId,
                              @Path("windowid") Long windowId);

    @PATCH("windows/{windowid}/state")
    Call<String> patchWindowState(@Path("windowid") Long windowId,
                                  @Query("state") String stateType,
                                  @Query("val") String stateValue);

    /*
        Creates a one timed task in the given minutes in the future from now.
        For example: close window with id 2 in 10 minutes from now
     */
    @POST("windows/{windowid}/tasks/ot")
    Call<String> postOneTimeTask(@Path("windowid") Long windowId,
                                 @Query("inMin") Integer inMin,
                                 @Query("state") String stateValue);

    /*
        Creates a scheduled task that will be run daily at the given time in hours and minutes.
        For example: open window with id 2 daily at 15:00
     */
    @POST("windows/{windowid}/tasks/st")
    Call<String> postScheduledTask(@Path("windowid") Long windowId,
                                   @Query("h") Integer hour,
                                   @Query("m") Integer minute,
                                   @Query("state") String stateValue);
}
