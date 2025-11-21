package avito.pr.reviewer.assignment.exceptions;

public class TeamExistsError extends RuntimeException implements MyError {
    private final String messageString;
    private final CodeError code;

    public TeamExistsError() {
        super();
        this.messageString = "team_name already exists";
        this.code = CodeError.TEAM_EXISTS;
    }

    public String getMessageString() {
        return messageString;
    }

    public CodeError getCodeError() {
        return code;
    }
}
