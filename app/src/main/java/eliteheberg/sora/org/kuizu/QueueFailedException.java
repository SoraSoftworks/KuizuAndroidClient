package eliteheberg.sora.org.kuizu;

/**
 * Created by sora on 12/10/2015.
 */
public class QueueFailedException extends Exception {
    public String getMessage()
    {
        return "Unable to join queue.";
    }
}
