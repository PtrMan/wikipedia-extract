import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import wikipediaParsing.CustomBuilder;

/**
 *
 */
public class TestMediawiki {
    public static void main(String[] args) {
        MarkupParser markupParser = new MarkupParser();
        markupParser.setMarkupLanguage(new MediaWikiLanguage());

        CustomBuilder customBuilder = new CustomBuilder();

        markupParser.setBuilder(customBuilder);
        //markupParser.parse(markupContent);

        //String htmlContent = markupParser.parseToHtml(markupContent);
    }
}
