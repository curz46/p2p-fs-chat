package me.dylancz.chatter.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SynchronousBus<E> /*implements IEventBus<E>, Runnable*/ {

//    private final IEventBus<E> underlyingBus;
//    private final BlockingQueue<E> queue = new LinkedBlockingQueue<>();
//
//    public SynchronousBus(final IEventBus<E> underlyingBus) {
//        this.underlyingBus = underlyingBus;
//    }
//
//    /**
//     * Adds the given Event to a Queue such that it can be executed when the Event thread polls the
//     * Queue for Events to process.
//     * @param event The Event to enqueue.
//     * @param <T> The type of the Event.
//     */
//    @Override
//    public <T extends E> void post(final T event) {
//        this.queue.add(event);
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                final E event = this.queue.take();
//                this.underlyingBus.post(event);
//            } catch (final InterruptedException e) {
//                throw new RuntimeException("SynchronousBus was interrupted while taking an Event.");
//            }
//        }
//    }

}
