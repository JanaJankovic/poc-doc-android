package feri.pora.datalib;

public class Message {
    private String content;
    private Person sender;
    private String receiverId;
    private String status;

    public Message(String message, Person person, String status) {
        this.sender = person;
        this.content = message;
        this.status = status;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + content + '\'' +
                ", sender=" + sender +
                ", status='" + status + '\'' +
                '}';
    }
}
