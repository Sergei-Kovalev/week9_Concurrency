package kovalev.jdev;

import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class Request implements Callable<TransactionData> {

    private Client client;
    private Server server;

    @Override
    public TransactionData call() {
        TransactionData requestData = client.remove();
        int serverListSize = server.fillList(requestData);

        return new TransactionData(serverListSize);
    }
}
