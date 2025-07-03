package api.server.domain.article.service;

import api.server.domain.article.dto.response.GetArticleListResponse;
import api.server.domain.article.dto.response.GetArticleResponse;
import api.server.external.grpc.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleApplicationService {

    private final GrpcService grpcService;
    private final ArticleCommandService articleCommandService;
    private final ArticleQueryService articleQueryService;

    private final List<String> queryList = List.of("정치", "경제", "사회", "과학", "스포츠", "1기");


    // @Scheduled(cron = "0 0 0 * * *")  // 매일 자정에 실행
    // @Scheduled(cron = "0 0/1 * * * *")  // 매 1분마다 실행
    // @Scheduled(cron = "0 0 * * * *") // 매 1시간 마다
    @Scheduled(initialDelay = 0, fixedRate = 60 * 60 * 1000)  // 0초 후 실행, 이후 1시간마다
    public void uploadArticles() {
        log.info("[ArticleApplicationService - uploadArticles]");
        List<CompletableFuture<Void>> futures = queryList.stream()
                .map(query -> grpcService.getArticleItemsAsync(query, 100, 1)
                        .thenAccept(articleCommandService::uploadArticles))
                .toList();

        // 모든 작업이 끝날 때까지 대기 (필요시)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    public GetArticleListResponse getArticleList(int page, int size, String search, String category){
        log.info("[ArticleApplicationService - getArticleList]");
        return articleQueryService.getArticleList(page, size, search, category);
    }

    public GetArticleResponse getArticle(Long articleId){
        log.info("[ArticleApplicationService - getArticle]");
        GetArticleResponse getArticleResponse = articleQueryService.getArticle(articleId);
        if (getArticleResponse.getSummary().isEmpty()){
            String summarizeArticleContent = articleCommandService.summarizeArticleContent(articleId);

            return getArticleResponse.toBuilder()
                    .summary(summarizeArticleContent)
                    .build();
        }
        return getArticleResponse;
    }

}
