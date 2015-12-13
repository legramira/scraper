package com.project.scraper.executorAlgo;

import com.project.scraper.algorithm.SearchEngine;
import com.project.scraper.exceptions.NoResultsFoundException;
import com.project.scraper.exceptions.SingleAnswerException;

import java.io.IOException;

/**
 * Sydney-Australia / 12/12/2015.
 * Created by legramira
 */
public interface BuildInterface {

  SearchEngine createSearchEngine() throws IOException, NoResultsFoundException, SingleAnswerException;

}
