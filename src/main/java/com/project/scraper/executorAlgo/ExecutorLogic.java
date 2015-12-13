package com.project.scraper.executorAlgo;

import com.project.scraper.algorithm.SearchEngine;
import com.project.scraper.models.ArticleAbstract;
import com.project.scraper.models.ArticleSearchAgainst;
import com.project.scraper.searches.RankingUtils;
import com.project.scraper.searches.SimilarityInterface;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * class to execute and process the search engine
 *
 * Sydney-Australia / 12/12/2015.
 * Created by legramira
 */
public class ExecutorLogic {
  private SearchEngine searchEngine;

  public ExecutorLogic(SearchEngine searchEngine) {
    this.searchEngine = searchEngine;
  }

  public Pair<ArticleAbstract, ArticleAbstract> calculateMatch() {
    List<SimilarityInterface> similaritiesToSearch = searchEngine.getSimilaritySearches();
    similaritiesToSearch
            .forEach(x -> {
              final Map partialSearch = x.search(searchEngine.getArticleFrom(), searchEngine.getArticleAgainst());

              searchEngine.setArticleAgainst(partialSearch);
            });

    RankingUtils.sortFinalMap(searchEngine.getArticleAgainst()).forEach(x ->{
      ArticleSearchAgainst a = x.getKey();
      Double b = x.getValue();

      System.out.println(String.format("Article name: %s value: %s",a.getName(),b));
    });

    ArticleAbstract finalArticleMatched = RankingUtils.sortFinalMap(searchEngine.getArticleAgainst()).get(0).getKey();

    return new Pair<>(searchEngine.getArticleFrom(), finalArticleMatched);
  }

}
