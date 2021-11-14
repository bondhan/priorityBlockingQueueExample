package com.hjra;

import java.util.concurrent.PriorityBlockingQueue;

public class ConsumerBonusCalculation implements Runnable {

    volatile Boolean isDone = false;
    volatile double totalBonus = 0;
    double incrementBonus = 0;

    java.util.concurrent.PriorityBlockingQueue<Account> queueBonus = null;
    java.util.concurrent.PriorityBlockingQueue<Account> queueWriteFile = null;

    public ConsumerBonusCalculation(PriorityBlockingQueue<Account> queueBonus,
                                    PriorityBlockingQueue<Account> queueWriteFile,
                                    double totalBonus,
                                    double incrementBonus) {

        this.queueWriteFile = queueWriteFile;
        this.queueBonus = queueBonus;
        this.totalBonus = totalBonus;
        this.incrementBonus = incrementBonus;
    }

    @Override
    public void run() {
        while (!isDone) {
            try {
                Account acc = queueBonus.take();

                if (acc.getId() == Long.MAX_VALUE) {
                    synchronized (this) {
                        if (isDone) {
                            continue;
                        }
                        queueWriteFile.put(acc);
                        isDone = true;
                    }
                    continue;
                }

                /*
                    Bank memiliki budget 1.000 yang akan di bagikan ke 100 orang pertama (data urutan no 1-
                    100) akan mendapatkan tambahan balance sebesar 10, untuk case ini buatlah 8 thread yang
                    akan berjalan secara bersamaan (pastikan No Thread yang di gunakan tertulis di ‘No 3
                    Thread-No’).
                 */
                if (acc.getId() <= 100) {
                    synchronized (this) {
                        if (totalBonus >= incrementBonus) {
                            totalBonus -= incrementBonus;

                            acc.topupBalance(this.incrementBonus);
                            acc.setNo3ThreadNo(Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
                        }
                    }
                }

                System.out.println("Bonus: " + acc);

                //write to queueFile for writing
                queueWriteFile.put(acc);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
