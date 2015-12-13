package com.project.scraper.parsers;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Parser interface describing the elements basic for a parse of any page
 *
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public interface ParserHtmlPageInterface<T> {

  void setHtmlPage(HtmlPage htmlPage);

  T parseHtml(String directoryToStoreImages);

  T getArticleParsed();


}
