package com.project.scraper.searches.byImage;

import net.semanticmetadata.lire.imageanalysis.features.Extractor;

import java.util.Set;

/**
 * class that implements this interface will create a specific index for the images in a folder path and store the
 * index in the path provided applying the all different kind of extractors
 *
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public interface IndexerImagesInterface {

  void indexImages(String pathIndex, String pathDirectoryImages, Set<Class<? extends Extractor>> extractors);

}