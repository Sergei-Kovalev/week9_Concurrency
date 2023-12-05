package kovalev.jdev.transaction;

import kovalev.jdev.client.Client;
import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class Response implements Callable<Boolean> {

    private Client client;

    private TransactionData transactionData;

    @Override
    public Boolean call() {
        boolean allResponseSent = false;

        client.accumulate(transactionData);
        if (client.getN() == client.getCounter().get()) {
            allResponseSent = true;
        }
        return allResponseSent;
    }
}
