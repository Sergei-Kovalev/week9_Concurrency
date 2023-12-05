package kovalev.jdev;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private static final List<Integer> list = new CopyOnWriteArrayList<>();
    private final Lock lock = new ReentrantLock();

    public TransactionData fillList(TransactionData transactionData){
        lock.lock();
        try {
            int timeToSleep = (int) (Math.random() * 900 + 100);
            System.out.println("The server sends a response with a delay = " + timeToSleep);
            Thread.sleep(timeToSleep);

            list.add(transactionData.getNumber());
            System.out.println(list);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return new TransactionData(list.size());
    }
}
