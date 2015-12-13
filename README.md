# scraper
Project comparison of stores via image and text similarity

This project is a crawler/scraper system designed to after taking a text by input, visit a store and search for the product, parsing the price, 
name and images, following it does the same process into another web store.

With this information it tries to determine the most suitable pair of element to compare looking similitudes via text and via image
recognition all wraped in a GUI build on JavaFX.

The system depends on HtmlUnit a "GUI-Less browser for Java programs", LIRE "An Open Source Java Content Based Image Retrieval Library" (see http://www.itec.uni-klu.ac.at/~mlux/lire-release/ to download the .jars), 
and a implementation of the Dice's comparison algorithm.

