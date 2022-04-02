package java_code.event.events;


class IllegalCallException extends RuntimeException {
    public IllegalCallException(String s) {
        super(s);
    }
}


public class Event {
    private boolean canceled = false;

    public static boolean canBeCanceled () {
        return true;
    }

    public void cancel() {
        if (!canBeCanceled()) {
            throw new IllegalCallException("This event cannot be cancelled");
        }

        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
