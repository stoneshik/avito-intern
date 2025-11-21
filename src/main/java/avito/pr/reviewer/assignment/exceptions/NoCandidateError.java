package avito.pr.reviewer.assignment.exceptions;

public class NoCandidateError extends RuntimeException implements MyError {
    private final String messageString;
    private final CodeError code;

    public NoCandidateError() {
        super();
        this.messageString = "no active replacement candidate in team";
        this.code = CodeError.NO_CANDIDATE;
    }

    public String getMessageString() {
        return messageString;
    }

    public CodeError getCodeError() {
        return code;
    }
}
