package api.server.domain.scrap.entity;

import api.server.domain.article.entity.Article;
import api.server.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_scraps")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articleScrapId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", nullable = false)
    private Article article;

    @Column
    private String memo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Scrap(User user, Article article, String memo, LocalDateTime createdAt) {
        this.user = user;
        this.article = article;
        this.memo = memo;
        this.createdAt = createdAt;
    }
}
