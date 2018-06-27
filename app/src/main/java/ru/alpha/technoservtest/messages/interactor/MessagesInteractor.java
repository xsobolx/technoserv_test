package ru.alpha.technoservtest.messages.interactor;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import ru.alpha.technoservtest.messages.data.Message;
import ru.alpha.technoservtest.messages.data.ResponseItem;
import ru.alpha.technoservtest.messages.network.MessagesService;

/**
 * Created by Aleksandr Sobol
 * asobol@golamago.com
 * on 26/06/2018.
 */
public class MessagesInteractor {

    private static MessagesInteractor sInteractor;
    private final MessagesService messagesService;

    @NonNull
    public static MessagesInteractor getMessagesInteractor(MessagesService messagesService) {
        MessagesInteractor interactor = sInteractor;

        if (interactor == null) {
            synchronized (MessagesInteractor.class) {
                interactor = sInteractor;
                if (interactor == null) {
                    interactor = sInteractor = new MessagesInteractor(messagesService);
                }
            }
        }
        return interactor;
    }

    private MessagesInteractor(@NonNull MessagesService messageService) {
        this.messagesService = messageService;
    }

    public Observable<Message> getMessage() {
        return messagesService.getMessages()
                .flatMapObservable(Observable::fromIterable)
                .map(ResponseItem::transform);
    }
}
