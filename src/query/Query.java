package query;

import filters.Filter;
import jdk.net.SocketFlow;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import twitter.TwitterSource;
import twitter4j.Status;
import ui.MapMarkerEnhanced;
import ui.MapMarkerSimple;
import util.ImageCache;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static util.Util.statusCoordinate;

/**
 * A query over the twitter stream.
 */
public class Query implements Observer {
    // The map on which to display markers when the query matches
    private final JMapViewer map;
    // Each query has its own "layer" so they can be turned on and off all at once
    private Layer layer;
    // The color of the outside area of the marker
    private final Color color;
    // The string representing the filter for this query
    private final String queryString;
    // The filter parsed from the queryString
    private final Filter filter;
    // The checkBox in the UI corresponding to this query (so we can turn it on and off and delete it)
    private JCheckBox checkBox;

    public Color getColor() {
        return color;
    }
    public String getQueryString() {
        return queryString;
    }
    public Filter getFilter() {
        return filter;
    }
    public Layer getLayer() {
        return layer;
    }
    public JCheckBox getCheckBox() {
        return checkBox;
    }
    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }
    public void setVisible(boolean visible) {
        layer.setVisible(visible);
    }
    public boolean getVisible() { return layer.isVisible(); }
    
    private List<MapMarkerEnhanced> markerList;

    public Query(String queryString, Color color, JMapViewer map) {
        this.queryString = queryString;
        this.filter = Filter.parse(queryString);
        this.color = color;
        this.layer = new Layer(queryString);
        this.map = map;
        this.markerList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Query: " + queryString;
    }

    /**
     * This query is no longer interesting, so terminate it and remove all traces of its existence.
     *
     */
    public void terminate() {
        for (MapMarker marker : markerList) {
            map.removeMapMarker(marker);
        }
        markerList.clear();
    }

    @Override
    // update will be called when the arrival of a tweet.
    // it should determine whether the tweet text matches the users filter expression and if so,
    // place a marker, an instance of the ui.MapMarkerSimple class, at the location on the map of the user who tweeted the tweet.
    // This location is in the tweet (see the method util.Util.statusCoordinate()).
    public void update(Observable o, Object arg) {
        TwitterSource subject = (TwitterSource) o;
        Status status = (Status) arg;
        ImageCache image = null;

        if (filter.matches(status)) {
            //System.out.println("query: "+filter.toString()+" matched on tweet: "+status.getText());
            // create the marker of matched tweet
            Coordinate coordinate = statusCoordinate(status);
            MapMarkerEnhanced marker = new MapMarkerEnhanced(layer, this.color, coordinate, status, filter.toString());

           //add marker to the map for display
            map.addMapMarker(marker);
            markerList.add(marker);
        };
    }

}

