package feri.pora.datalib;

public class Message {
    protected String message;
    protected Person sender;

    public Message(String message, Person person) {
        this.sender = person;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }
}
