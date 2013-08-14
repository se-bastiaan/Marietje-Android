package com.example.marietje;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QueueXMLParser {
    private final String ns = null;

    public static class Entry {
        public String title;
        public String artist;
        public String requester;

        public Entry(String title, String artist, String requester) {
            this.title = title;
            this.artist = artist;
            this.requester = requester;
        }
    }

    public Entry readEntry(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "request");
        String title = parser.getAttributeValue(null, "title");
        String artist = parser.getAttributeValue(null, "artist");
        String requester = parser.getAttributeValue(null, "by");

        if (requester == null) {
            requester = "marietje";
        }

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "request");

        return new Entry(title, artist, requester);
    }

    public List<Entry> readFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<Entry>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("request")) {
                entries.add(readEntry(parser));
            } else {
                System.out.println("Faal tag");
                // skip(parser);
            }
        }
        return entries;
    }

    public List<Entry> parse(InputStream in) throws XmlPullParserException,
            IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                    false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
}
