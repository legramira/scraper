package com.project.scraper.algorithm;

import com.project.scraper.models.ArticleSearchAgainst;
import com.project.scraper.models.ArticleSearchFrom;
import com.project.scraper.searches.SimilarityInterface;

import java.util.List;
import java.util.Map;

/**
 * Search engine containing the elements specific for the problem proposed
 *
 * Sydney-Australia / 10/12/2015.
 * Created by legramira
 */
public class SearchEngine implements SearchingInterface<ArticleSearchFrom, ArticleSearchAgainst, SimilarityInterface> {

  private ArticleSearchFrom articleFrom;
  private Map<ArticleSearchAgainst, Double> articleAgainst;
  private List<SimilarityInterface> listOfSimilaritySearches;

  @Override
  public void setArticleFrom(ArticleSearchFrom articleFrom) {
    this.articleFrom = articleFrom;
  }

  @Override
  public ArticleSearchFrom getArticleFrom() {
    return articleFrom;
  }

  @Override
  public void setArticleAgainst(Map<ArticleSearchAgainst, Double> articleAgainst) {
    this.articleAgainst = articleAgainst;
  }

  @Override
  public Map<ArticleSearchAgainst, Double> getArticleAgainst() {
    return articleAgainst;
  }

  @Override
  public void setSimilaritySearches(List<SimilarityInterface> listOfSimilaritySearches) {
    this.listOfSimilaritySearches = listOfSimilaritySearches;
  }

  @Override
  public List<SimilarityInterface> getSimilaritySearches() {
    return listOfSimilaritySearches;
  }
}
