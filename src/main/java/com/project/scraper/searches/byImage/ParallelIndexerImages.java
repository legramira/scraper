package com.project.scraper.searches.byImage;

import com.project.scraper.parsers.ParsersUtils;
import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.Extractor;
import net.semanticmetadata.lire.indexers.parallel.ParallelIndexer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * create a simple index in a parallel way applying the different extractors
 *
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public class ParallelIndexerImages implements IndexerImagesInterface {

  @Override
  public void indexImages(String pathIndex, String pathDirectoryImages, Set<Class<? extends Extractor>> extractors) {
//    to have new index every time erase a previous one if exist
    try {
      File file = new File(pathIndex);
      if (file.exists()) {
        FileUtils.deleteDirectory(file);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
//    use ParallelIndexer to index all photos from into "index" ... use 5 threads to index
    ParallelIndexer indexer = new ParallelIndexer(5, pathIndex, pathDirectoryImages);
//    change previous line for this to set max number of threads depending on the machine
//    ParallelIndexer indexer = new ParallelIndexer(DocumentBuilder.NUM_OF_THREADS, pathIndex, pathDirectoryImages);

//    Add extractors to the index (kind of features that the images will be indexed)
    extractors.forEach(indexer::addExtractor);
//    Run the indexer threads
    indexer.run();
  }

}
