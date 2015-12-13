package com.project.scraper.models;

import java.util.Set;

/**
 *
 * * Data class that describe the product to search from, in this case it describe multiple image (Store the path
 * for the images), store all images path to have more data to compare in the search by image
 *
 * Sydney-Australia - 10/12/2015.
 * Created by legramira
 */
public class ArticleSearchFrom extends ArticleAbstract<Set<String>> {

  private Set<String> pathImages;

  @Override
  public Set<String> getVisualDescriptor() {
    return pathImages;
  }

  @Override
  public void setVisualDescriptor(Set<String> visualDescriptor) {
    this.pathImages = visualDescriptor;
  }
}
