package com.project.scraper.parsers;

import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.project.scraper.models.ArticleSearchAgainst;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Specific parser for the asos pages
 *
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public class AsosParser implements ParserHtmlPageInterface<List<ArticleSearchAgainst>> {

  private HtmlPage htmlPage;
  private List<ArticleSearchAgainst> articlesSearchAgainst;

  public AsosParser(HtmlPage page) {
    this.htmlPage = page;
    articlesSearchAgainst = new ArrayList<>();
  }

  @Override
  public void setHtmlPage(HtmlPage htmlPage) {
    this.htmlPage = htmlPage;
  }


  /**
   * parser the page to have the asos product detail
   *
   * @param directoryToStore: path to store the images for asos catalog
   * @return
   */
  @Override
  public List<ArticleSearchAgainst> parseHtml(String directoryToStore) {
    ParsersUtils.createDirectory( directoryToStore);
    Map<String, ArticleSearchAgainst> auxiliaryMap = new ConcurrentHashMap<>();
    htmlPage.getElementsByTagName("li")
            .stream()
            .filter((x) -> x.getAttribute("class").toLowerCase().contains("product-container"))
            .forEach((x) -> {
              String nameArticle = ((HtmlSpan) x.getFirstByXPath(".//div[@class='name-fade']/span[@class='name']")).getTextContent();
              if (!auxiliaryMap.containsKey(nameArticle)) {
                String imageArticleSrc = ((HtmlImage) x.getFirstByXPath(".//img[@class='product-img']")).getSrcAttribute();
                String stringPriceArticle = ((HtmlSpan) x.getFirstByXPath(".//div[@class='price-wrap price-current']/span[@class='price']")).getTextContent();
                try {
                  BigDecimal priceArticle = ParsersUtils.stringCurrencyToBigDecimal(stringPriceArticle);
                  Path imageTemporalPath = Files.createFile(Paths.get(directoryToStore + String.format("/%s-image.jpg", nameArticle)));
                  imageTemporalPath.toFile().deleteOnExit();
                  ParsersUtils.saveImage(imageArticleSrc, imageTemporalPath.toFile());
                  ArticleSearchAgainst valueToStore = new ArticleSearchAgainst(nameArticle, priceArticle, imageTemporalPath.toString());
                  auxiliaryMap.put(nameArticle, valueToStore);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
    articlesSearchAgainst = new ArrayList<>(auxiliaryMap.values());
    return articlesSearchAgainst;
  }

  @Override
  public List<ArticleSearchAgainst> getArticleParsed() {
    return articlesSearchAgainst;
  }
}
