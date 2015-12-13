package com.project.scraper.parsers;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Different utils used for the process of parsing
 *
 * Sydney-Australia / 11/12/2015.
 * Created by legramira
 */
public class ParsersUtils {

  static public final String INDEX_FOLDER = System.getProperty("user.dir") + "/index";
  static public final String IMAGES_TO_INDEX_FOLDER = System.getProperty("user.dir") + "/imagesAsos";
  static public final String IMAGES_TO_COMPARE_FOLDER = System.getProperty("user.dir") + "/imagesIconic";

  /**
   * create a folder in the location specified,
   * @param directoryToStore
   */
  static public void createDirectory(String directoryToStore){
    try {
      final File testExistanceFile = new File(directoryToStore);
      if ((testExistanceFile).exists()) {
        FileUtils.deleteDirectory(testExistanceFile);
      }
      Path directoryTemporalPath = Files.createDirectory(Paths.get(directoryToStore));
      directoryTemporalPath.toFile().deleteOnExit();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * save image to the folder provided
   *
   * @param imageUrl: image url to be store
   * @param destinationFile: destination folder for the previous image
   * @throws IOException
   */
  static public void saveImage(String imageUrl, File destinationFile) throws IOException {
    URL url = new URL(imageUrl);
    try (InputStream is = url.openStream();
         OutputStream os = new FileOutputStream(destinationFile)
    ) {
      byte[] b = new byte[2048];
      int length;

      while ((length = is.read(b)) != -1) {
        os.write(b, 0, length);
      }
    }
  }

  /**
   * parse the string into a BigDecimal eliminating any currency or extra mal format number
   * @param numericString
   * @return: BigDecimal representation of the string provided
   *
   * @throws ParseException
   */
  static public BigDecimal stringCurrencyToBigDecimal(String numericString) throws ParseException {
    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();
    decimalFormat.setParseBigDecimal(true);
    return (BigDecimal) decimalFormat.parse(numericString.replaceAll("[^\\d.,]",""));
  }

}
