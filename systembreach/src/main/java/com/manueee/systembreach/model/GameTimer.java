package com.manueee.systembreach.model;

import java.util.concurrent.TimeUnit;
import java.util.Timer;

public class GameTimer implements Runnable {
    /**
     * Interfaccia per la gestione degli eventi del timer
     */
    public interface TimerListener {
        void onTimeUpdate(int secondsLeft); // Aggiorna il timer
        void onTimeOut(); // Notifica il time out
    }

    private final TimerListener listener;
    private final int initialTimer;
    private volatile boolean isRunning;
    private Thread timerThread;
    private int currentTime;

    public GameTimer(int seconds, TimerListener listener) {
        this.initialTimer = seconds;
        this.currentTime = seconds;
        this.listener = listener;
        this.isRunning = false;
    }

    public void start() {
        isRunning = true;
        timerThread = new Thread(this);
        timerThread.start();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning && currentTime > 0) {
            try {
                TimeUnit.SECONDS.sleep(1);
                currentTime--;
                listener.onTimeUpdate(currentTime);

                if (currentTime <= 0){
                    listener.onTimeOut();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
