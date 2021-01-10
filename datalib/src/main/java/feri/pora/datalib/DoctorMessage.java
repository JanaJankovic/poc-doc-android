package feri.pora.datalib;

public class DoctorMessage extends Message {
    private Doctor sender;

    public DoctorMessage(String message, Doctor sender) {
        super(message);
        this.sender = sender;
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

    public void setSender(Doctor sender) {
        this.sender = sender;
    }
}
