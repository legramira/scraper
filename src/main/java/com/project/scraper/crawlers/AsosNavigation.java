package com.project.scraper.crawlers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.project.scraper.exceptions.NoResultsFoundException;
import com.project.scraper.exceptions.SingleAnswerException;
import com.project.scraper.models.ArticleSearchAgainst;
import com.project.scraper.parsers.ParsersUtils;
import org.apache.commons.math3.exception.NoDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;

/** Asos specific navigation until arrive to the appropriate page for the problem
 *
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public class AsosNavigation implements NavigationInterface {
  private static final Logger log = LoggerFactory.getLogger(AsosNavigation.class);

  private WebClient webClient;
  private String homeAsos;

  public AsosNavigation(WebClient webClient) {
    this.webClient = webClient;
    homeAsos = "http://www.asos.com/au";
  }


  @Override
  public HtmlPage navigateToFinalPage(String searchTerm) throws IOException, NoResultsFoundException, SingleAnswerException {
//    navigates to home // as it change procedure on the fly I leave first step to load canvas or cookies or something else
    log.info("opening asos home: " + homeAsos);
    final HtmlPage page = webClient.getPage(homeAsos);

//    final HtmlTextInput textField = (HtmlTextInput)page.getElementById("txtSearch");
//    page.setFocusedElement(textField);
//    textField.type("adidas original");
    String searchUrl = String.format("%s/search/%s?q=%s", homeAsos, searchTerm.replace(' ', '-'), searchTerm.replace(' ', '+'));
    log.info("opening asos search page: " + searchUrl);
    final HtmlPage page2 = webClient.getPage(searchUrl);

//    verify the existence of results from the query
    if ((page2.getWebResponse().getStatusCode() == 404) || page2.getElementsByTagName("div")
            .stream()
            .filter((x) -> x.getAttribute("class").toLowerCase().contains("no-results-message"))
            .findFirst().isPresent()
            ) {
      log.error("error opening asos search page: " + searchUrl);
      throw new NoResultsFoundException("Asos did not provide results to the search: " + searchTerm);
    }

//    verify that asos return not a single object, which about the search by similarity
    if (page2.getElementById("productImages") != null) {
      log.error("error asos search page contains only one article no point in compare elements");
      String stringName = ((HtmlMeta) page2.getFirstByXPath("//meta[@itemprop='name']")).getAttribute("content");
      String stringPrice = ((HtmlMeta) page2.getFirstByXPath("//meta[@itemprop='price']")).getAttribute("content");
      String stringSrc = ((HtmlImage) page2.getElementById("ctl00_ContentMainPage_imgMainImage")).getSrcAttribute();
      ArticleSearchAgainst singleAnswer = null;
      try {
        BigDecimal priceArticle = ParsersUtils.stringCurrencyToBigDecimal(stringPrice);
        singleAnswer = new ArticleSearchAgainst(stringName, priceArticle, stringSrc);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      throw new SingleAnswerException("Asos provide a single results to the search", singleAnswer);
    }

    return page2;
  }
}
