package cn.xueyikang.dubbo.faker.core.quasar;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberAsync;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.httpasyncclient.FiberCloseableHttpAsyncClient;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import co.paralleluniverse.strands.SuspendableCallable;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.IntChannel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class QuasarIncreasingEchoApp {

    public static void main(String[] args) {
        test();
    }

    public static Integer doAll() throws ExecutionException, InterruptedException {
        final IntChannel increasingToEcho = Channels.newIntChannel(0); // Synchronizing channel (buffer = 0)
        final IntChannel echoToIncreasing = Channels.newIntChannel(0); // Synchronizing channel (buffer = 0)

        Fiber<Integer> increasing = new Fiber<>("INCREASER", (SuspendableCallable<Integer>) () -> {
            ////// The following is enough to test instrumentation of synchronizing methods
            // synchronized(new Object()) {}

            int curr = 0;
            for (int i = 0; i < 10 ; i++) {
                Fiber.sleep(10);
                System.out.println("INCREASER sending: " + curr);
                increasingToEcho.send(curr);
                curr = echoToIncreasing.receive();
                System.out.println("INCREASER received: " + curr);
                curr++;
                System.out.println("INCREASER now: " + curr);
            }
            System.out.println("INCREASER closing channel and exiting");
            increasingToEcho.close();
            return curr;
        }).start();

        Fiber<Void> echo = new Fiber<Void>("ECHO", (SuspendableRunnable) () -> {
            Integer curr;
            while (true) {
                Fiber.sleep(1000);
                curr = increasingToEcho.receive();
                System.out.println("ECHO received: " + curr);

                if (curr != null) {
                    System.out.println("ECHO sending: " + curr);
                    echoToIncreasing.send(curr);
                } else {
                    System.out.println("ECHO detected closed channel, closing and exiting");
                    echoToIncreasing.close();
                    return;
                }
            }
        }).start();

        try {
            increasing.join();
            echo.join();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return increasing.get();
    }

    public static void test() {
        FiberForkJoinScheduler scheduler = new FiberForkJoinScheduler("33", 34);
        FiberAsync<String, Exception> fiberAsync = new FiberAsync<String, Exception>() {
            private static final long serialVersionUID = -5155510116129848366L;

            @Override
            protected void requestAsync() {
                System.out.printf("run");
            }
        };
        try {
            fiberAsync.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void http() {
        final CloseableHttpClient client = FiberHttpClientBuilder.
                create(2). // use 2 io threads
                setMaxConnPerRoute(5).
                setMaxConnTotal(5).build();


        try {
            String response = client.execute(new HttpGet("http://cm.sqaproxy.souche-inc.com/sync/getModel.json?series_code=series-1696"), new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    HttpEntity entity = response.getEntity();
                    entity.getContent();
                    return null;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void asynHttp() {
        final CloseableHttpAsyncClient client = FiberCloseableHttpAsyncClient.wrap(HttpAsyncClients.
                custom().
                setMaxConnPerRoute(5).
                setMaxConnTotal(5).
                build());

        client.start();
    }
}
