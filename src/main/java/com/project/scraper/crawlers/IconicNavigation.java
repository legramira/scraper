package com.project.scraper.crawlers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.project.scraper.exceptions.NoResultsFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Iconic specific navigation system until arrive to the appropriated page
 *
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public class IconicNavigation implements NavigationInterface {
  private static final Logger log = LoggerFactory.getLogger(IconicNavigation.class);

  private WebClient webClient;
  private String homeIconic;

  public IconicNavigation(WebClient webClient) {
    this.webClient = webClient;
    homeIconic = "http://www.theiconic.com.au";
  }


  @Override
  public HtmlPage navigateToFinalPage(String searchTerm) throws IOException, NoResultsFoundException {
//    navigates to home
    log.info("opening iconic home: " + homeIconic);
    final HtmlPage page = webClient.getPage(homeIconic);
//    capture the form components to navigate
    final HtmlForm form = (HtmlForm) page.getElementById("main-search-form");
    final HtmlTextInput textField = form.getInputByName("q");
    final HtmlButton button = form.getFirstByXPath(".//button");

//    change the text in the form to search for results
    textField.setValueAttribute(searchTerm);

//    open the new page search
    final HtmlPage page2 = button.click();
    log.info("searching for the query: " + searchTerm);
    log.debug("iconic page search open: " + page2.getUrl());
//    verify the existence of results from the query
    if ((page2.getWebResponse().getStatusCode() == 404) || page2.getElementsByTagName("section")
            .stream()
            .filter((x) -> x.getAttribute("class").toLowerCase().contains("no-results"))
            .findFirst().isPresent()
            ) {
      log.error("no results from the query: " + searchTerm);
      throw new NoResultsFoundException("Iconic did not provide results to the search: " + searchTerm);
    }
//    navigation goes to the first element of the search Which will be the article base to compare
    final String urlFirstElementOfTheSearch = ((HtmlAnchor) page2.getElementsByTagName("div")
            .stream()
            .filter((x) -> x.getAttribute("class").toLowerCase().contains("catalog-page"))
            .findFirst()
            .get()
            .getFirstByXPath(".//a")).getHrefAttribute();

    webClient.close();
    log.info("opening iconic page with product to search: " + homeIconic + urlFirstElementOfTheSearch);
    final HtmlPage page3 = webClient.getPage(homeIconic + urlFirstElementOfTheSearch);
//    verify the existence of results from the query
    if ((page3.getWebResponse().getStatusCode() == 404)) {
      log.error("Iconic did not have available the page: " + homeIconic + urlFirstElementOfTheSearch);
      throw new NoResultsFoundException("Iconic did not have available the page: " + homeIconic + urlFirstElementOfTheSearch);
    }
//    open and return the new page for the article to be compared
    return page3;
  }
}
