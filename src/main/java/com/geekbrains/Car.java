package com.geekbrains;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private static final Lock lock = new ReentrantLock();
    private static int CARS_COUNT;
    private static boolean hasWinner = false;
    private final CyclicBarrier raceStart;
    private final CountDownLatch raceEnd;
    private Race race;
    private int speed;
    private String name;

    public Car(Race race, int speed, CyclicBarrier raceStart, CountDownLatch raceEnd) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.raceStart = raceStart;
        this.raceEnd = raceEnd;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            raceStart.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        try {
            lock.lock();
            if (!hasWinner) {
                System.out.println(this.name + " - WIN");
                hasWinner = true;
            }
        } finally {
            lock.unlock();
        }
        raceEnd.countDown();

    }
}