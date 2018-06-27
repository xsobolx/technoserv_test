package ru.alpha.technoservtest.messages.network;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import ru.alpha.technoservtest.messages.data.ResponseItem;

/**
 * Created by Aleksandr Sobol
 * asobol@golamago.com
 * on 26/06/2018.
 */
public interface MessagesService {

    @GET
    Single<List<ResponseItem>> getMessages();

}
