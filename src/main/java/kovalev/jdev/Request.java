package kovalev.jdev;

import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class Request implements Callable<TransactionData> {

    private Client client;
    private Server server;


    @Override
    public TransactionData call() {
        TransactionData transactionData = client.remove();
        transactionData = server.fillList(transactionData);

        return transactionData;
    }
}
