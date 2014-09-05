package nl.ru.science.mariedroid.network.objects;

import android.os.AsyncTask;

import com.koushikdutta.async.future.FutureCallback;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Sebastiaan on 05-09-14.
 */
public class Request {

    public String title;
    public String artist;
    public String requester;
    public String length;

    public Request(String title, String artist, String requester, String length) {
        this.title = title;
        this.artist = artist;
        this.requester = requester;
        this.length = length;
    }

    public static ArrayList<Request> parseXML(String xml) throws ParserConfigurationException, SAXException, IOException{
        StringReader reader = new StringReader(xml);
        SAXParserFactory saxPF = SAXParserFactory.newInstance();
        SAXParser saxP = saxPF.newSAXParser();
        XMLReader xmlR = saxP.getXMLReader();
        Handler handler = new Handler();
        xmlR.setContentHandler(handler);
        xmlR.parse(new InputSource(reader));
        return handler.getRequests();
    }

    public static void parseXMLAsync(String result, final FutureCallback<ArrayList<Request>> callback) {
        new AsyncTask<String, Void, ArrayList<Request>>() {

            private Exception mException = null;

            @Override
            protected ArrayList<Request> doInBackground(String... result) {
                ArrayList<Request> requests = null;
                try {
                    requests = Request.parseXML(result[0]);
                } catch (Exception e) {
                    mException = e;
                }

                return requests;
            }

            @Override
            protected void onPostExecute(ArrayList<Request> requests) {
                super.onPostExecute(requests);
                callback.onCompleted(mException, requests);
            }

        }.execute(result);
    }

    public static class Handler extends DefaultHandler {

        private ArrayList<Request> mRequests;

        public void startDocument () {
            mRequests = new ArrayList<Request>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(localName.equals("request") || localName.equals("playing")) {
                String strBy = "by";
                if(localName.equals("playing")) strBy = "requestedBy";
                String by = attributes.getValue(strBy);
                if(by == null || by.isEmpty()) by = "marietje";

                Request request = new Request(attributes.getValue("title"), attributes.getValue("artist"), by, attributes.getValue("length"));
                mRequests.add(request);
            }
        }

        public ArrayList<Request> getRequests() {
            return mRequests;
        }

    }

}
