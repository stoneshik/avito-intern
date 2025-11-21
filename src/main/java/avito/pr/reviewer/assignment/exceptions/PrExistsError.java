package avito.pr.reviewer.assignment.exceptions;

public class PrExistsError extends RuntimeException implements MyError {
    private final String messageString;
    private final CodeError code;

    public PrExistsError() {
        super();
        this.messageString = "PR id already exists";
        this.code = CodeError.PR_EXISTS;
    }

    public String getMessageString() {
        return messageString;
    }

    public CodeError getCodeError() {
        return code;
    }
}
