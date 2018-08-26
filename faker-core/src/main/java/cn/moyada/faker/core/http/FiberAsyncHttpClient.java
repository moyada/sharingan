package cn.moyada.faker.core.http;

import cn.moyada.dubbo.faker.core.model.FutureResult;
import cn.moyada.dubbo.faker.core.model.HttpInvokeInfo;
import co.paralleluniverse.fibers.httpasyncclient.FiberCloseableHttpAsyncClient;
import co.paralleluniverse.strands.SettableFuture;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xueyikang
 * @create 2018-02-03 20:36
 */
public class FiberAsyncHttpClient extends AbstractHttpClient {

    private CloseableHttpAsyncClient client;
    private HttpAsyncClientBuilder builder;

    public SettableFuture<FutureResult> excutePost(HttpInvokeInfo httpInvokeInfo) {
        SettableFuture<FutureResult> future = new SettableFuture<>();
        this.client.execute(
                super.buildPost(httpInvokeInfo),
                new WrapCallBackHandle(future));
        return future;
    }

    public void changeCookies(List<HttpCookie> httpCookies) {
        this.builder.setDefaultCookieStore(super.buildCookies(httpCookies));
        this.client = FiberCloseableHttpAsyncClient.wrap(this.builder.build());
        this.client.start();
    }

    public FiberAsyncHttpClient(int total, boolean ssl, List<HttpCookie> httpCookies) {
        this.builder = HttpAsyncClients
                .custom()
                .setMaxConnPerRoute(3)
                .setMaxConnTotal(total)
                .setDefaultRequestConfig(super.buildRequestConfig(super.DEFAULT_TIMEOUT))
                .setDefaultCookieStore(super.buildCookies(httpCookies))
                .setSSLContext(super.buildSSL(ssl));
        this.client = FiberCloseableHttpAsyncClient.wrap(this.builder.build());
        this.client.start();
    }

    class WrapCallBackHandle implements FutureCallback<HttpResponse> {

        private final Instant start;

        private final SettableFuture<FutureResult> future;

        public WrapCallBackHandle(final SettableFuture<FutureResult> future) {
            this.future = future;
            this.start = Instant.now();
        }

        @Override
        public void completed(HttpResponse result) {
            long spend = Duration.between(this.start, Instant.now()).toMillis();

            HttpEntity entity = result.getEntity();

            FutureResult futureResult;
            try {
                futureResult = FutureResult.success(toString(entity.getContent()), spend);
            } catch (Exception e) {
                futureResult = FutureResult.failed(e.getMessage(), spend);
            }
            future.set(futureResult);
        }

        @Override
        public void failed(Exception ex) {
            FutureResult futureResult = FutureResult.failed(ex.toString(),
                    Duration.between(this.start, Instant.now()).toMillis());
            future.set(futureResult);
        }

        @Override
        public void cancelled() {
            future.cancel(true);
        }

        private String toString(final InputStream inputStream) {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
