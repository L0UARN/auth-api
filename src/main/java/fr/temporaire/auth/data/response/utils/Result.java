package fr.temporaire.auth.data.response.utils;

public class Result<SuccessType, ErrorType> {
    private final SuccessType data;
    private final ErrorType error;
    private final boolean isSuccess;

    public Result(SuccessType data, ErrorType error, boolean isSuccess) {
        this.data = data;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    public static <SuccessType, ErrorType> Result<SuccessType, ErrorType> success(SuccessType data) {
        return new Result<>(data, null, true);
    }

    public static <SuccessType, ErrorType> Result<SuccessType, ErrorType> error(ErrorType error) {
        return new Result<>(null, error, false);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public SuccessType getData() {
        return data;
    }

    public ErrorType getError() {
        return error;
    }
}
