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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nl.ru.science.mariedroid.utils.AppUtils;
import nl.ru.science.mariedroid.utils.LogUtils;

public class Request {

    public String title;
    public String artist;
    public String requester;
    public Long length;
    public Long serverTime;
    public Long endTime;
    public Long startTime;
    public Boolean nowPlaying = false;

    public Request(String title, String artist, String requester, Long length) {
        this.title = title;
        this.artist = artist;
        this.requester = requester;
        this.length = length;
    }

    public Request(String title, String artist, String requester, Long length, Long serverTime, Long endTime) {
        this.title = title;
        this.artist = artist;
        this.requester = requester;
        this.length = length;
        this.serverTime = serverTime;
        this.endTime = endTime;
        this.nowPlaying = true;
    }

    public String getRemainingTime(Long serverTime) {
        String time = "";
        try {
            if (!nowPlaying) {
                long outputTime = Math.abs(serverTime - startTime);
                int minutes = (int) Math.floor(outputTime / 60);
                int seconds = (int) outputTime % 60;
                time = minutes + ":" + AppUtils.setStringLength(Integer.toString(seconds), 2);
                //time = Long.toString(outputTime);
                //LogUtils.d("Request", "startTime: " + outputTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * Parse the request XML using a SAXParser
     * @param xml String XML from server
     * @return ArrayList<Request> List with requests found in XML
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
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

    /**
     * Parse the XML from server in background
     * @param result String XML from server
     * @param callback FutureCallback<ArrayList<Request>> List with requests found in XML
     */
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
                Boolean nowPlaying = false;
                if(localName.equals("playing")) {
                    strBy = "requestedBy";
                    nowPlaying = true;
                }
                String by = attributes.getValue(strBy);
                if(by == null || by.isEmpty()) by = "marietje";

                Request request;
                if(nowPlaying) {
                    request = new Request(attributes.getValue("title"), attributes.getValue("artist"), by, (long) Double.parseDouble(attributes.getValue("length")), (long) Double.parseDouble(attributes.getValue("serverTime")), (long) Double.parseDouble(attributes.getValue("endTime")));
                } else {
                    request = new Request(attributes.getValue("title"), attributes.getValue("artist"), by, (long) Double.parseDouble(attributes.getValue("length")));
                }
                mRequests.add(request);
            }
        }

        public ArrayList<Request> getRequests() {
            return mRequests;
        }

    }

}
