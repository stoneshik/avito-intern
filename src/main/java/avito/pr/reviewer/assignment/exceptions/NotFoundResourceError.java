package avito.pr.reviewer.assignment.exceptions;

public class NotFoundResourceError extends RuntimeException implements MyError {
    private final String messageString;
    private final CodeError code;

    public NotFoundResourceError() {
        super();
        this.messageString = "resource not found";
        this.code = CodeError.NOT_FOUND;
    }

    public String getMessageString() {
        return messageString;
    }

    public CodeError getCodeError() {
        return code;
    }
}
