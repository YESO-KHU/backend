package api.server.domain.article.entity;

import lombok.Getter;

@Getter
public enum ArticleCategory {
    POLITICS("정치"),
    ECONOMY("경제"),
    SOCIETY("사회"),
    CULTURE("생활/문화"),
    IT("IT/과학"),
    GLOBAL("세계"),
    ETC("기타")
    ;

    private final String displayName;

    ArticleCategory(String displayName) {
        this.displayName = displayName;
    }

    public static ArticleCategory getArticleCategory(String category){
        if (ArticleCategory.POLITICS.displayName.equals(category)) return ArticleCategory.POLITICS;
        else if (ArticleCategory.ECONOMY.displayName.equals(category)) return ArticleCategory.ECONOMY;
        else if (ArticleCategory.SOCIETY.displayName.equals(category)) return ArticleCategory.SOCIETY;
        else if (ArticleCategory.CULTURE.displayName.equals(category)) return ArticleCategory.CULTURE;
        else if (ArticleCategory.IT.displayName.equals(category)) return ArticleCategory.IT;
        else if (ArticleCategory.GLOBAL.displayName.equals(category)) return ArticleCategory.GLOBAL;
        else return ArticleCategory.ETC;

    }
}
