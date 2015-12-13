package com.project.scraper.parsers;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.project.scraper.models.ArticleSearchFrom;
import org.apache.commons.math3.exception.NoDataException;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Specific class to parse the iconic page
 *
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public class IconicParser implements ParserHtmlPageInterface<ArticleSearchFrom> {

  private HtmlPage htmlPage;
  private ArticleSearchFrom articleSearchFrom;

  public IconicParser(HtmlPage htmlPage) {
    this.htmlPage = htmlPage;
    articleSearchFrom = new ArticleSearchFrom();
  }


  @Override
  public void setHtmlPage(HtmlPage htmlPage) {
    this.htmlPage = htmlPage;
  }


  /**
   * method to parse and return the element from iconic base used to search
   *
   * @param directoryToStore: path to store the images from iconic
   * @return
   */
  @Override
  public ArticleSearchFrom parseHtml(String directoryToStore) {
    parseNameArticle();
    parsePriceArticle();
    parseImageArticle(directoryToStore);
    return articleSearchFrom;
  }

  @Override
  public ArticleSearchFrom getArticleParsed() {
    return articleSearchFrom;
  }


  private void parseNameArticle() {
    System.out.println("parsing iconic name");

    String name = htmlPage.getElementsByTagName("span")
            .stream()
            .filter((x) -> x.getAttribute("itemprop").equalsIgnoreCase("name"))
            .findFirst()
            .orElseThrow(NoDataException::new)
            .getTextContent();
    System.out.println("parsed iconic name: " + name);


    String brand = htmlPage.getElementsByTagName("meta")
            .stream()
            .filter((x) -> x.getAttribute("itemprop").equalsIgnoreCase("brand"))
            .findFirst()
            .orElseThrow(NoDataException::new)
            .getAttribute("content");
    articleSearchFrom.setName(String.format("%s %s", brand, name));
    System.out.println("name iconic parsed: " + String.format("%s %s", brand, name));
  }

  private void parsePriceArticle() {
    System.out.println("parsing iconic price");

    String stringPrice = htmlPage.getElementsByTagName("meta")
            .stream()
            .filter((x) -> x.getAttribute("itemprop").equalsIgnoreCase("price"))
            .findFirst()
            .orElseThrow(NoDataException::new)
            .getAttribute("content");
    BigDecimal bigDecimalPrice = null;
    try {
      bigDecimalPrice = ParsersUtils.stringCurrencyToBigDecimal(stringPrice);
      articleSearchFrom.setPrice(bigDecimalPrice);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    System.out.println("name iconic parsed: " + bigDecimalPrice.toString());
  }

  private void parseImageArticle(String directoryToStore) {
    System.out.println("parsing iconic image");
    AtomicInteger indexImage = new AtomicInteger();
    ParsersUtils.createDirectory( directoryToStore);

    Set<String> pathImages = new CopyOnWriteArraySet<>();
    htmlPage.getElementsByTagName("div")
            .stream()
            .filter((x) -> x.getAttribute("class").toLowerCase().contains("owl-item"))
            .map((x) -> "http:" + (((HtmlAnchor) x.getFirstElementChild()).getHrefAttribute()))
            .collect(Collectors.toList())
            .forEach((x) -> {
              try {
                Path imageTemporalPath = Files.createFile(Paths.get(directoryToStore + String.format("/%d-image.jpg", indexImage.incrementAndGet())));
                imageTemporalPath.toFile().deleteOnExit();
                ParsersUtils.saveImage(x, imageTemporalPath.toFile());
                pathImages.add(imageTemporalPath.toString());
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
    articleSearchFrom.setVisualDescriptor(pathImages);
    System.out.println("name iconic parsed:\n " );
    pathImages.forEach(System.out::println);
  }

}
