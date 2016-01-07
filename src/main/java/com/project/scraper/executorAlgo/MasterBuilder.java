package com.project.scraper.executorAlgo;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.project.scraper.algorithm.SearchEngine;
import com.project.scraper.crawlers.AsosNavigation;
import com.project.scraper.crawlers.IconicNavigation;
import com.project.scraper.crawlers.NavigationInterface;
import com.project.scraper.exceptions.NoResultsFoundException;
import com.project.scraper.exceptions.SingleAnswerException;
import com.project.scraper.models.ArticleSearchAgainst;
import com.project.scraper.models.ArticleSearchFrom;
import com.project.scraper.parsers.AsosParser;
import com.project.scraper.parsers.IconicParser;
import com.project.scraper.parsers.ParserHtmlPageInterface;
import com.project.scraper.parsers.ParsersUtils;
import com.project.scraper.searches.SimilarityInterface;
import com.project.scraper.searches.byImage.ImageSimilarity;
import com.project.scraper.searches.byImage.IndexerImagesInterface;
import com.project.scraper.searches.byImage.ParallelIndexerImages;
import com.project.scraper.searches.byText.StringSimilarity;
import net.semanticmetadata.lire.imageanalysis.features.Extractor;
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Class builder that create the engine that will be use to solve the scraper problem
 *
 * Sydney-Australia / 12/12/2015.
 * Created by legramira
 */
public class MasterBuilder implements BuildInterface {

  private String entrySearch;

  public MasterBuilder(String entrySearch) {
    this.entrySearch = entrySearch;

  }

  @Override
  public SearchEngine createSearchEngine() throws NoResultsFoundException, SingleAnswerException {
    ArticleSearchFrom iconicArticleToCompare = null;
    List<ArticleSearchAgainst> asosArticleToCompare = null;

    try (final WebClient webClient = new WebClient(BrowserVersion.EDGE)) {
//      configuration of the browser
      LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
      java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
      java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
      java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
      webClient.getOptions().setJavaScriptEnabled(true);
      webClient.getOptions().setCssEnabled(false);
      webClient.getOptions().setUseInsecureSSL(true);
      webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
      webClient.getCookieManager().setCookiesEnabled(true);
      webClient.setAjaxController(new NicelyResynchronizingAjaxController());
      webClient.getOptions().setThrowExceptionOnScriptError(false);
      webClient.getCookieManager().setCookiesEnabled(true);
      webClient.setJavaScriptTimeout(5000);

      iconicArticleToCompare = processIconicSide(webClient);
      try {
        if (iconicArticleToCompare != null) {
          asosArticleToCompare = processAsosSide(webClient, iconicArticleToCompare.getName());
        }
      } catch (SingleAnswerException e) {
        e.setSingleArticleFrom(iconicArticleToCompare);
        throw e;
      }

    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("error creating the browser");
      System.exit(9);
    }


    Map<ArticleSearchAgainst, Double> mapValuesSimilitudes = null;
    if (asosArticleToCompare != null) {
      mapValuesSimilitudes = asosArticleToCompare.stream().collect(Collectors.toMap((x -> x), (x -> 0.0)));
    }

    Set<Class<? extends Extractor>> extractorsImages = new HashSet<>();
    extractorsImages.add(CEDD.class);
    extractorsImages.add(FCTH.class);
//    extractorsImages.add(SiftExtractor.class);
    extractorsImages.add(AutoColorCorrelogram.class);
//indexer images
    IndexerImagesInterface indexImages = new ParallelIndexerImages();
    indexImages.indexImages(ParsersUtils.INDEX_FOLDER, ParsersUtils.IMAGES_TO_INDEX_FOLDER, extractorsImages);

//  image similarity confi
    ImageSearcher searcher = null;
    if (asosArticleToCompare != null) {
      searcher = new GenericFastImageSearcher(asosArticleToCompare.size(), CEDD.class);
    }
    SimilarityInterface imageSimilarity = new ImageSimilarity(ParsersUtils.INDEX_FOLDER, searcher);
// string similarity
    StringSimilarity textSimilariry = new StringSimilarity();

//    creation of list of similarities to check for
    List<SimilarityInterface> similarityList = new ArrayList<>();
    similarityList.add(imageSimilarity);
    similarityList.add(textSimilariry);


    SearchEngine searchEngine = new SearchEngine();
    searchEngine.setSimilaritySearches(similarityList);
    searchEngine.setArticleFrom(iconicArticleToCompare);
    searchEngine.setArticleAgainst(mapValuesSimilitudes);


    return searchEngine;
  }


  private ArticleSearchFrom processIconicSide(WebClient webClient) throws IOException, NoResultsFoundException {
    final NavigationInterface iconicNavigation = new IconicNavigation(webClient);
    final HtmlPage iconicPage;
    try {
      iconicPage = iconicNavigation.navigateToFinalPage(entrySearch);
      System.out.println("Iconic navigating to " + iconicPage.getUrl());
      final ParserHtmlPageInterface<ArticleSearchFrom> iconicParser = new IconicParser(iconicPage);
      return iconicParser.parseHtml(ParsersUtils.IMAGES_TO_COMPARE_FOLDER);
    } catch (SingleAnswerException e) {
      e.printStackTrace();
    }
    return null;
  }

  private List<ArticleSearchAgainst> processAsosSide(WebClient webClient, String asosArticleName) throws IOException, NoResultsFoundException, SingleAnswerException {
    final NavigationInterface asosNavigation = new AsosNavigation(webClient);
    final HtmlPage asosPage = asosNavigation.navigateToFinalPage(asosArticleName);
    System.out.println("Asos navigating to " + asosPage.getUrl());
    final ParserHtmlPageInterface<List<ArticleSearchAgainst>> asosParser = new AsosParser(asosPage);
    return asosParser.parseHtml(ParsersUtils.IMAGES_TO_INDEX_FOLDER);
  }

}