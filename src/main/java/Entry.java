/**
 *
 */
public class Entry {
    // TODO< input folder >
    public static String outputFolder = "/home/r0b3/temp/wikioutput/";

    public static void main(String[] args) {
        Parsing parsing = new Parsing();
        parsing.parseXmlAndParseWikitextAndCallHandler();
    }
}
