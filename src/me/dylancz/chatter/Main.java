package me.dylancz.chatter;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        try {
            new Chatter("P:/p2ptest").start();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
