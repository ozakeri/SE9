package com.example.sino.api;

import com.example.sino.model.chatgroup.SuccessChatGroupBean;
import com.example.sino.model.chatgroupmember.SuccessChatGroupMemberBean;
import com.example.sino.model.neshan.NeshanRequentBean;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NeshanApi {

    @Headers("Api-Key: service.8323fd6b906a4265a1e5d68c46495d1d")
    @GET("/v1/distance-matrix?")
    Observable<NeshanRequentBean> getDetailLocation(@Query("type") String type, @Query("origins") String latLngList, @Query("destinations") String destinations);


    @Headers("Api-Key: service.8323fd6b906a4265a1e5d68c46495d1d")
    @GET("/v5/reverse?")
    Observable<NeshanRequentBean> getReverseGeocoding(@Query("lat") String lat, @Query("lng") String lng);


}
