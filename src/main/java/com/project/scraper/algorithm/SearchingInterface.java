package com.project.scraper.algorithm;

import com.project.scraper.models.ArticleAbstract;
import com.project.scraper.searches.SimilarityInterface;

import java.util.List;
import java.util.Map;

/**
 * Interface that describe minimun elements for a search of similarities, from against and algorithms
 *
 * Sydney-Australia / 10/12/2015.
 * Created by legramira
 */
public interface SearchingInterface<A extends ArticleAbstract, B extends ArticleAbstract, C extends SimilarityInterface> {

  void setArticleFrom(A articleFrom);

  A getArticleFrom();

  void setArticleAgainst(Map<B, Double> articleAgainst);

  Map<B, Double> getArticleAgainst();

  void setSimilaritySearches(List<C> listOfSimilaritySearches);

  List<C> getSimilaritySearches();

}
