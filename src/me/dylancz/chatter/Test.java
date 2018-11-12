package me.dylancz.chatter;

import java.util.function.Supplier;

public class Test {

    public static class SomeClass {

    }

    public static void main(final String[] args) {
//        final PingPacket packet = new PingPacket();
//        System.out.println("Beginning write...");
//        long before = System.currentTimeMillis();
//        for (int i = 0; i < 3600; i++) {
//            PacketIO.write(Paths.get("P:/testfile.txt").toFile(), packet);
//        }
//        long after = System.currentTimeMillis();
//        System.out.println("Duration: " + (after - before) + "ms");
//        System.out.println("Beginning read...");
//        long before = System.currentTimeMillis();
//        final List<Packet> packets = PacketIO.read(Paths.get("P:/testfile.txt").toFile(), 0);
//        long after = System.currentTimeMillis();
//        System.out.println("Duration: " + (after - before) + "ms");
//        System.out.println("Num. packets=" + ((List) packets).size());
        final int numTimes = 10000000;
        final Class<SomeClass> clazz = SomeClass.class;

        long before = System.currentTimeMillis();
        for (int i = 0; i < numTimes; i++) {
            try {
                clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        long after = System.currentTimeMillis();
        System.out.println("Duration: " + (after - before) + "ms");

        final Supplier<SomeClass> ref = SomeClass::new;
        before = System.currentTimeMillis();
        for (int i = 0; i < numTimes; i++) {
            ref.get();
        }
        after = System.currentTimeMillis();
        System.out.println("Duration: " + (after - before) + "ms");

        before = System.currentTimeMillis();
        for (int i = 0; i < numTimes; i++) {
            new SomeClass();
        }
        after = System.currentTimeMillis();
        System.out.println("Duration: " + (after - before) + "ms");
    }

}
