package repository;

/**
 * New type of exception when it provides from the repository
 * extends RunTimeException to be unchecked and don't force the programmer to handle this exception
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(String msg) {
        super(msg);
    }

}
