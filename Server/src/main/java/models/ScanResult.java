package models;

/**
 * Class defining a scan result.
 */
public class ScanResult {
    /**
     * Message shown to the players.
     */
    private String mMessage;

    /**
     * Initialize scan result.
     * @param message The message to be sent to player
     */
    public ScanResult(final String message) {
        mMessage = message;
    }

    /**
     * Returns the message.
     *
     * @return message to be returned
     */
    public String getMessage() {
        return mMessage;
    }
}
