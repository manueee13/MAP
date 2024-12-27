package com.manueee.systembreach.model;

import java.util.concurrent.TimeUnit;
public class Timer implements Runnable {
    private int remainingTime;
    private boolean isRunning;
    private TimerListener listener;

    public Timer(int initialTime, TimerListener listener) {
        this.remainingTime = initialTime;
        this.listener = listener;
        this.isRunning = false;
    }

    public void start() {
        isRunning = true;
        Thread timerThread = new Thread(this);
        timerThread.start();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning && remainingTime > 0) {
            try {
                TimeUnit.SECONDS.sleep(1);
                remainingTime--;

                if (listener != null) {
                    listener.onTimeUpdate(remainingTime);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (remainingTime == 0 && listener != null) {
            listener.onTimeOut();
        }
    }

    /**
     * Interfaccia per la gestione degli eventi del timer
     */
    public interface TimerListener {
        void onTimeUpdate(int secondsLeft); // Aggiorna il timer
        void onTimeOut(); // Notifica il time out
    }

}
