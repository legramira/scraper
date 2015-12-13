package com.project.scraper.searches;

import com.project.scraper.models.ArticleAbstract;

import java.util.Map;

/**
 * interface to be implemented to search similarities between and object and another object base
 * <p>
 * Sydney-Australia - 9/12/2015.
 * Created by legramira
 */
public interface SimilarityInterface<A extends ArticleAbstract, B extends ArticleAbstract> {
  /**
   * @param rankingSimilarity: parameter ArticleSearchFrom A to compare from with the Map of Object to be compare against
   * @return: return a new value ArticleSearchFrom that indicates how similar is the against the rest
   */
  Map<B, Double> search(final A objectToCompareFrom, final Map<B, Double> similarityToOthers);

}
