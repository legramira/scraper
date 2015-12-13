package com.project.scraper.exceptions;

import com.project.scraper.models.ArticleSearchAgainst;
import com.project.scraper.models.ArticleSearchFrom;

/**
 * Exception indicating that there were not catalog after the search and instead there is an article itself
 * the exception is to be build buble up
 *
 * Sydney-Australia / 13/12/2015.
 * Created by legramira
 */
public class SingleAnswerException extends Exception {
  private ArticleSearchAgainst singleArticleAgainst;
  private ArticleSearchFrom singleArticleFrom = null;

  public SingleAnswerException(String message, ArticleSearchAgainst singleArticleAgainst) {
    super(message);
    this.singleArticleAgainst = singleArticleAgainst;
  }

  public void setSingleArticleFrom(ArticleSearchFrom singleArticleFrom) {
    this.singleArticleFrom = singleArticleFrom;
  }

  public ArticleSearchAgainst getSingleArticleAgainst() {
    return singleArticleAgainst;
  }

  public ArticleSearchFrom getSingleArticleFrom() {
    return singleArticleFrom;
  }


}
