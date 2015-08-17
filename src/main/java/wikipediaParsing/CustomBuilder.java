package wikipediaParsing;

import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder;

/**
 * Document builder which extracts the (with xml and wikimedia markup dirted up) text
 */
public class CustomBuilder extends DocumentBuilder {
    public StringBuilder resultString;

    private boolean mute;

    @Override
    public void beginDocument() {
        // TODO< flush string builder >
        resultString = new StringBuilder();
        mute = false;
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void beginBlock(BlockType type, Attributes attributes) {
        //System.out.println("beginBlock() " + type.name() + " " + attributes.toString());
    }

    @Override
    public void endBlock() {
        //System.out.println("endBlock()");
    }

    @Override
    public void beginSpan(SpanType type, Attributes attributes) {
        //System.out.println("beginSpan() " + type.name() + " " + attributes.toString());
    }

    @Override
    public void endSpan() {
        //System.out.println("endSpan()");
    }

    @Override
    public void beginHeading(int level, Attributes attributes) {
        // we don't want to have headings in the output text
        mute = true;
    }

    @Override
    public void endHeading() {
        mute = false;
    }

    @Override
    public void characters(String text) {
        if( !mute ) {
            resultString.append(text);
        }
    }

    @Override
    public void entityReference(String entity) {

    }

    @Override
    public void image(Attributes attributes, String url) {

    }

    @Override
    public void link(Attributes attributes, String hrefOrHashName, String text) {
        if( !mute ) {
            resultString.append(text);
        }
    }

    @Override
    public void imageLink(Attributes linkAttributes, Attributes imageAttributes, String href, String imageUrl) {

    }

    @Override
    public void acronym(String text, String definition) {
        if( !mute ) {
            resultString.append(text);
        }
    }

    @Override
    public void lineBreak() {

    }

    @Override
    public void charactersUnescaped(String literal) {
    }
}
