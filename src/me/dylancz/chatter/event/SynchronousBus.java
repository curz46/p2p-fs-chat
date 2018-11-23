package me.dylancz.chatter.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SynchronousBus<E, L extends EventListener<E>> implements IEventBus<E, L>, Runnable {

    private final IEventBus<E, L> underlyingBus;
    private final BlockingQueue<E> queue = new LinkedBlockingQueue<>();

    private boolean running = true;

    public SynchronousBus(final IEventBus<E, L> underlyingBus) {
        this.underlyingBus = underlyingBus;
    }

    /**
     * Adds the given Event to a Queue such that it can be executed when the Event thread polls the
     * Queue for Events to process.
     * @param event The Event to enqueue.
     * @param <T> The type of the Event.
     */
    @Override
    public <T extends E> void post(T event) {
        this.queue.add(event);
    }

    @Override
    public void registerListener(final L listener) {
        this.underlyingBus.registerListener(listener);
    }

    @Override
    public void registerListeners(final Object object) {
        this.underlyingBus.registerListeners(object);
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                final E event = this.queue.take();
                this.underlyingBus.post(event);
            } catch (final InterruptedException e) {
                throw new RuntimeException("SynchronousBus was interrupted while taking an Event.");
            }
        }
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return this.running;
    }

}
