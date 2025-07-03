package api.server.domain.article.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articleId")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Lob
    private String summary;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleCategory articleCategory;

    @Column(nullable = false, unique = true)
    private String link;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private LocalDateTime publishDate;

    @Builder
    public Article(String title, String content, String summary, ArticleCategory articleCategory, String link, int likeCount, int viewCount, LocalDateTime publishDate) {
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.articleCategory = articleCategory;
        this.link = link;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.publishDate = publishDate;
    }

    public void updateSummary(String summary){
        this.summary = summary;
    }
}
