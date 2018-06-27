package ru.alpha.technoservtest.messages.data;

import java.util.Date;
import java.util.Objects;

import ru.alpha.technoservtest.messages.Transformable;

/**
 * Created by Aleksandr Sobol
 * asobol@golamago.com
 * on 26/06/2018.
 */
public class ResponseItem implements Transformable<Message> {

    private final int id;
    private final String subject;
    private final String text;
    private final Date startDateTime;
    private final Date endDateTime;

    public ResponseItem(int id, String subject, String text, Date startDateTime, Date endDateTime) {
        this.id = id;
        this.subject = subject;
        this.text = text;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseItem that = (ResponseItem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(text, that.text) &&
                Objects.equals(startDateTime, that.startDateTime) &&
                Objects.equals(endDateTime, that.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, text, startDateTime, endDateTime);
    }

    @Override
    public String toString() {
        return "ResponseItem{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }

    @Override
    public Message transform() {
        return new Message(
                id,
                subject,
                text,
                startDateTime.getTime(),
                endDateTime.getTime());
    }
}
