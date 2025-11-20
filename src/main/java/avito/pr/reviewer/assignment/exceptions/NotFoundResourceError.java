package avito.pr.reviewer.assignment.exceptions;

public class NotFoundResourceError extends RuntimeException {
    private final String messageString;
    private final TypeError type;

    public NotFoundResourceError() {
        super();
        this.messageString = "resource not found";
        this.type = TypeError.NOT_FOUND;
    }

    public String getMessageString() {
        return messageString;
    }

    public TypeError getTypeError() {
        return type;
    }
}
