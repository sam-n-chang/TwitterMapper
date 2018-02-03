package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.Style;
import twitter4j.Status;
import twitter4j.User;
import util.ImageCache;

import javax.swing.*;
import java.awt.*;

public class MapMarkerEnhanced extends MapMarkerCircle {
    public static final double defaultMarkerSize = 12.0;
    public static final Color defaultColor = Color.pink;
    public static final Color defaultTextColor = Color.WHITE;
    private Status status;
    private String iconUrl;
    private ImageIcon icon;

    public MapMarkerEnhanced(Layer layer, Color color, Coordinate coord, Status status, String keywords) {
        super(layer, keywords, coord, defaultMarkerSize, STYLE.FIXED, getDefaultStyle());

        setColor(Color.BLACK);
        setBackColor(color);

        this.status = status;
        User user = status.getUser();
        iconUrl = user.getBiggerProfileImageURL();
        this.icon = new ImageIcon(iconUrl);
}

    //get user image icon
    public ImageIcon getMarkerIcon() {
        return this.icon;
    }

    //get user image url
    public String getIconUrl() {
        return this.iconUrl;
    }

    @Override
    public String toString() {
        return status.getText();
    }

}
