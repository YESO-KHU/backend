package api.server.external.gemini;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${external.gemini.api-key}")
    private String apiKey;
    @Value("${external.gemini.task}")
    private String task;

    public GeminiService(WebClient webClient) {
        this.webClient = webClient; // 설정된 WebClient 사용
    }

    public String summarizeArticleContent(String content) {
        String combinedText = "Task: " + task + "\nArticle Content: " + content;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", combinedText))
                ))
        );

        Map<String, Object> response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/models/gemini-2.0-flash:generateContent")
                        .queryParam("key", apiKey) // API 키를 쿼리 파라미터로 추가
                        .build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("candidates")) {
            throw new IllegalStateException("Gemini 응답이 비정상적입니다: " + response);
        }

        return extractSummary(response);
    }

    private String extractSummary(Map<String, Object> response) {
        return Optional.ofNullable((List<Map<String, Object>>) response.get("candidates"))
                .filter(candidates -> !candidates.isEmpty())
                .map(candidates -> candidates.get(0))
                .map(candidate -> (Map<String, Object>) candidate.get("content"))
                .map(content -> (List<Map<String, String>>) content.get("parts"))
                .filter(parts -> !parts.isEmpty())
                .map(parts -> parts.get(0).get("text"))
                .orElseThrow(() -> new IllegalStateException("Gemini 응답에서 요약 텍스트를 추출하지 못했습니다."));
    }
}
