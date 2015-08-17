import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import wikipediaParsing.CustomBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Parsing {
    /**
     * Takes each article and
     * * puts it into a wikimedia parser
     * * removes xml and wikimedia markup to extract the raw text
     *
     */
    private static class WikitextHandler implements MyHandler.IArticleCallback {
        public WikitextHandler() {
            markupParser = new MarkupParser();
            markupParser.setMarkupLanguage(new MediaWikiLanguage());

            customBuilder = new CustomBuilder();

            markupParser.setBuilder(customBuilder);

        }

        @Override
        public void article(String title, String wikitext) {
            System.out.println(title);

            markupParser.parse(wikitext);


            final String wikimediaParserResult = customBuilder.resultString.toString();
            final String afterRemovingWikimediaReferenceNotation = removeStringsBetween(wikimediaParserResult, "{{", "}}");

            // TODO< parse with real xml parser to remove all xml things, because findstring doesn't work >
            final String afterRemovingXml = afterRemovingWikimediaReferenceNotation;

            final String lowercase = afterRemovingXml.toLowerCase();

            final String withoutPunctation = removePunctation(lowercase);

            // TODO< better algorithm for getting all words >
            // TODO< get words >

            final Set<String> keywords = getKeywords(withoutPunctation);

            final String fileTitle = convertTitleToFilenameTitle(title);

            try {
                Writer writer = null;
                writer = new FileWriter(Entry.outputFolder + fileTitle + ".json");

                Gson gson = new GsonBuilder().create();
                gson.toJson(keywords, writer);

                writer.close();
            } catch (IOException e) {
                throw new RuntimeException();
            }

            int x = 0;
        }

        private static String removePunctation(final String input) {
            final char[] replace = {',', '.', ';', ':', '(', ')', '-', '[', ']', '?', '!'};

            String result = input;

            for( final char iterationReplace : replace ) {
                result = result.replace(iterationReplace, ' ');
            }

            return result;
        }


        private MarkupParser markupParser;
        private CustomBuilder customBuilder;
    }

    public void parseXmlAndParseWikitextAndCallHandler() {
        WikitextHandler wikitextHandler = new WikitextHandler();

        SAXParser parser = null;
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException();
        } catch (SAXException e) {
            throw new RuntimeException();
        }
        DefaultHandler handler = new MyHandler(wikitextHandler);
        try {
            parser.parse("/media/r0b3/BTRFS_RAID/root/enwiki/enwiki-20150702-pages-meta-current1.xml-p000000010p000010001", handler);
        } catch (SAXException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // helper
    private static String convertTitleToFilenameTitle(String title) {
        return title.replace('/','-');
    }

    // helper
    private static Set<String> getKeywords(final String input) {
        Set<String> result = new HashSet<>();

        final String[] splited = input.split(" ");
        for( final String iterationSplited : splited ) {
            result.add(iterationSplited);
        }

        return result;
    }

    // helper
    private static String removeStringsBetween(final String input, final String start, final String end) {
        int currentIndex = 0;
        StringBuilder result = new StringBuilder();

        for(;;) {
            final int startIndex = input.indexOf(start, currentIndex);

            if( startIndex == -1 ) {
                int stringEndIndex = input.length()-1;

                if( currentIndex < stringEndIndex ) {
                    result.append(input.substring(currentIndex, stringEndIndex));
                }
                break;
            }
            else {
                result.append(input.substring(currentIndex, startIndex));

                final int afterStartIndex = startIndex + start.length();
                final int endIndex = input.indexOf(end, afterStartIndex);
                if(endIndex == -1) {
                    // HACK< if it can't find the end >
                    result.append(input.substring(startIndex, input.length()-1));

                    break;
                }
                else {
                    currentIndex = endIndex + end.length();
                }

            }
        }

        return result.toString();
    }
}
