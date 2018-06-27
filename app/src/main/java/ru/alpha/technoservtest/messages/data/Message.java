package ru.alpha.technoservtest.messages.data;

import java.util.Objects;

/**
 * Created by Aleksandr Sobol
 * asobol@golamago.com
 * on 26/06/2018.
 */
public class Message {

    private final int id;
    private final String subject;
    private final String text;
    private final Long startDate;
    private final Long endDate;

    public Message(int id, String subject, String text, Long startDate, Long endDate) {
        this.id = id;
        this.subject = subject;
        this.text = text;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) &&
                Objects.equals(subject, message.subject) &&
                Objects.equals(text, message.text) &&
                Objects.equals(startDate, message.startDate) &&
                Objects.equals(endDate, message.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, text, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
