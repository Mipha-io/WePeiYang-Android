package com.twt.service.ui.lostfound.post.lost;

import retrofit.RetrofitError;

/**
 * Created by Rex on 2015/8/10.
 */
public interface PostLostPresenter {
    void postLost(String authorization, String title, String name, String time, String place, String phone, String content, String lost_type);

    void onSuccess();

    void onFailure(RetrofitError error);

    void getLostDetails(int id);

    void editLost(String authorization,int id,String title,String name,String time,String place,String phone,String content,int lost_type);
}
