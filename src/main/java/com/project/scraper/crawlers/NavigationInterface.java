package com.project.scraper.crawlers;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.project.scraper.exceptions.NoResultsFoundException;
import com.project.scraper.exceptions.SingleAnswerException;

import java.io.IOException;

/**
 * Interface describing the process of arriving to an appropriated page
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public interface NavigationInterface {

  HtmlPage navigateToFinalPage(String searchTerm) throws IOException, NoResultsFoundException, SingleAnswerException;
}
