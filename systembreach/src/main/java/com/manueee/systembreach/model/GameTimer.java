package com.manueee.systembreach.model;

import java.util.concurrent.TimeUnit;

public class GameTimer implements Runnable {
    /**
     * Interfaccia per la gestione degli eventi del timer
     */
    public interface TimerListener {
        void onTimeUpdate(int secondsLeft); // Aggiorna il timer
        void onTimeOut(); // Notifica il time out+
    }

    private final TimerListener listener;
    private volatile boolean isRunning;
    private Thread timerThread;
    private int currentTime;

    public GameTimer(int seconds, TimerListener listener) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Timer must be greater than 0");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }
        this.currentTime = seconds;
        this.listener = listener;
        this.isRunning = false;
    }

    public void start() {
        isRunning = true;
        timerThread = new Thread(this);
        timerThread.start();
    }

    public synchronized void stopTimer() {
        isRunning = false;
        if (timerThread != null) {
            timerThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (isRunning && currentTime > 0) {
            try {
                TimeUnit.SECONDS.sleep(1);
                currentTime--;
                listener.onTimeUpdate(currentTime);

                if (currentTime <= 0){
                    stopTimer();
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
