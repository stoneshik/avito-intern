package avito.pr.reviewer.assignment.exceptions;

public class NotAssignedError extends RuntimeException implements MyError {
    private final String messageString;
    private final CodeError code;

    public NotAssignedError() {
        super();
        this.messageString = "reviewer is not assigned to this PR";
        this.code = CodeError.NOT_ASSIGNED;
    }

    public String getMessageString() {
        return messageString;
    }

    public CodeError getCodeError() {
        return code;
    }
}
