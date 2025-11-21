package avito.pr.reviewer.assignment.exceptions;

public class PrMergedError extends RuntimeException implements MyError {
    private final String messageString;
    private final CodeError code;

    public PrMergedError() {
        super();
        this.messageString = "cannot reassign on merged PR";
        this.code = CodeError.PR_MERGED;
    }

    public String getMessageString() {
        return messageString;
    }

    public CodeError getCodeError() {
        return code;
    }
}
