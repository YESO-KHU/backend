package api.server.external.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import article.ArticleServiceGrpc;
import article.ArticleServiceOuterClass.ArticleResponse;
import article.ArticleServiceOuterClass.ArticleRequest;
import article.ArticleServiceOuterClass.ArticleItem;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class GrpcService {

    private final ManagedChannel channel;
    private final ArticleServiceGrpc.ArticleServiceBlockingStub stub;
    private final ArticleServiceGrpc.ArticleServiceFutureStub futureStub;

    public GrpcService() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();
        this.stub = ArticleServiceGrpc.newBlockingStub(channel);
        this.futureStub = ArticleServiceGrpc.newFutureStub(channel);
    }

    // 동기식 기사 데이터 가공
    public List<ArticleItem> getArticleItemsSync(String query, int display, int start) {
        log.info("[GrpcService - getArticleItemsSync]");

        ArticleRequest request = ArticleRequest.newBuilder()
                .setQuery(query)
                .setDisplay(display)
                .setStart(start)
                .build();

        ArticleResponse articleResponse = stub.getArticles(request);

        return articleResponse.getItemsList();
    }

    // 비동기식 기사 데이터 가공
    public CompletableFuture<List<ArticleItem>> getArticleItemsAsync(String query, int display, int start) {
        log.info("[GrpcService - getArticleItemsAsync]");

        ArticleRequest request = ArticleRequest.newBuilder()
                .setQuery(query)
                .setDisplay(display)
                .setStart(start)
                .build();

        ListenableFuture<ArticleResponse> responseFuture = futureStub.getArticles(request);

        // Guava ListenableFuture → Java CompletableFuture 변환
        CompletableFuture<ArticleResponse> completableFuture = new CompletableFuture<>();
        responseFuture.addListener(() -> {
            try {
                completableFuture.complete(responseFuture.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, Runnable::run);

        return completableFuture.thenApply(ArticleResponse::getItemsList);
    }

    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }
}