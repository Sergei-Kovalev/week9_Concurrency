package kovalev.jdev;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Client {

    private final AtomicInteger accumulator = new AtomicInteger();

    private static AtomicInteger n;

    private static List<Integer> numbersList;

    private final Lock lock = new ReentrantLock();


    public Client(int n) {
        Client.n = new AtomicInteger(n);
        numbersList = fillNumbers(n);
    }

    public TransactionData remove() {
        lock.lock();
        TransactionData transactionData;
        try {
            int timeToSleep = (int) (Math.random() * 400 + 100);
            System.out.println("The client sends the request with a delay = " + timeToSleep);
            Thread.sleep(timeToSleep);

            int randomIndex = (int) (Math.random() * n.get());
            Integer numberFromList = numbersList.get(randomIndex);
            numbersList.remove(randomIndex);
            n.decrementAndGet();
            transactionData = new TransactionData(numberFromList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return transactionData;
    }

    public void accumulate(TransactionData transactionData) {
        accumulator.addAndGet(transactionData.getNumber());
    }

    private static List<Integer> fillNumbers(int n) {
        List<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        return list;
    }
}
