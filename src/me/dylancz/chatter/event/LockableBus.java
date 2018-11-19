package me.dylancz.chatter.event;

public abstract class LockableBus {

    private Thread lockedThread;

    public void lock() {
        this.lockedThread = Thread.currentThread();
    }

    public void verifyThread() {
        if (!Thread.currentThread().equals(this.lockedThread)) {
            throw new RuntimeException(
                "The Thread that this EventBus is locked to is not equal to the current Thread."
            );
        }
    }

}
