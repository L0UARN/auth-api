package fr.temporaire.auth.data.response.utils;

/**
 * A generic class representing the result of an operation.
 * It can either be a success with associated data or an error with associated error information.
 * 
 * @param <SuccessType> The type of data for successful operations.
 * @param <ErrorType> The type of error information for failed operations.
 */
public class Result<SuccessType, ErrorType> {
    private final SuccessType data;
    private final ErrorType error;
    private final boolean isSuccess;

    /**
     * Constructs a Result with the provided success data, error data, and success flag.
     *
     * @param data The data to be returned in case of success.
     * @param error The error information to be returned in case of failure.
     * @param isSuccess A flag indicating whether the operation was successful.
     */
    public Result(SuccessType data, ErrorType error, boolean isSuccess) {
        this.data = data;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    /**
     * Creates a Result indicating success with the provided data.
     *
     * @param <SuccessType> The type of the success data.
     * @param <ErrorType> The type of the error data.
     * @param data The success data.
     * @return A Result indicating success.
     */
    public static <SuccessType, ErrorType> Result<SuccessType, ErrorType> success(SuccessType data) {
        return new Result<>(data, null, true);
    }

    /**
     * Creates a Result indicating failure with the provided error information.
     *
     * @param <SuccessType> The type of the success data.
     * @param <ErrorType> The type of the error data.
     * @param error The error information.
     * @return A Result indicating failure.
     */
    public static <SuccessType, ErrorType> Result<SuccessType, ErrorType> error(ErrorType error) {
        return new Result<>(null, error, false);
    }

    /**
     * Checks if the operation was successful.
     *
     * @return {@code true} if the operation was successful, {@code false} otherwise.
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Gets the data associated with the successful operation.
     *
     * @return The data if the operation was successful, or {@code null} if the operation failed.
     */
    public SuccessType getData() {
        return data;
    }

    /**
     * Gets the error associated with the failed operation.
     *
     * @return The error if the operation failed, or {@code null} if the operation was successful.
     */
    public ErrorType getError() {
        return error;
    }
}