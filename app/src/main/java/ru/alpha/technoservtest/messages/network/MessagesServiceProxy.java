package ru.alpha.technoservtest.messages.network;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.Single;
import ru.alpha.technoservtest.messages.data.ResponseItem;

/**
 * Created by Aleksandr Sobol
 * asobol@golamago.com
 * on 27/06/2018.
 */

public class MessagesServiceProxy implements MessagesService {

    private String subject = "Новое сообщение";
    private String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc nec elit venenatis elit semper venenatis. Praesent elementum justo eget elit pharetra lobortis. Maecenas sed luctus ligula. Etiam blandit pellentesque diam at congue. Vestibulum fermentum pellentesque nisl, id porttitor massa. ";

    @Override
    public Single<List<ResponseItem>> getMessages() {
        List<ResponseItem> responseItemList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            responseItemList.add(generateResposneItem());
        }
        return Single.just(responseItemList);
    }

    private ResponseItem generateResposneItem() {
        int id = new Random().nextInt();
        Date startDate = new Date();
        startDate.setTime(System.currentTimeMillis());
        Log.d(MessagesService.class.getSimpleName(), "startDate: " + startDate.toString());
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 60000);
        Log.d(MessagesService.class.getSimpleName(), "endDate: " + endDate.toString());
        return new ResponseItem(id, subject, text, startDate, endDate);
    }
}
