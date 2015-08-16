import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
class MyHandler extends DefaultHandler {
    public interface IArticleCallback {
        void article(String title, String content);
    }

    public MyHandler(IArticleCallback articleCallback) {
        this.articleCallback = articleCallback;
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        path.add(qName);
        temp = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if( qName.equals("title") ) {
            title = temp.toString();

        }
        else if( qName.equals("text") ) {
            articleCallback.article(title, temp.toString());
        }

        path.remove(path.size()-1);

        temp.setLength(0);
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        for( int i = start; i < start + length; i++ ) {
            temp.append(ch[i]);
        }
    }

    private StringBuilder temp = new StringBuilder();
    private String title = null;

    private List<String> path = new ArrayList<>();

    private final IArticleCallback articleCallback;
}