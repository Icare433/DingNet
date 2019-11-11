package GUI.MapViewer;

import GUI.util.ImageLoader;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.viewer.Waypoint;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Paints waypoints on the JXMapViewer. This is an
 * instance of Painter that only can draw on to JXMapViewers.
 * @param <W> the waypoint type
 * @author rbair
 */
public class GatewayPainter<W extends Waypoint> extends AbstractPainter<JXMapViewer> {
    private Set<W> waypoints = new HashSet<>();
    private BufferedImage img;


    public GatewayPainter() {
        setAntialiasing(true);
        setCacheable(false);
        img = ImageLoader.IMAGE_GATEWAY;
    }

    /**
     * Gets the current set of waypoints to paint
     * @return a typed Set of Waypoints
     */
    public Set<W> getWaypoints() {
        return Collections.unmodifiableSet(waypoints);
    }

    /**
     * Sets the current set of waypoints to paint
     * @param waypoints the new Set of Waypoints to use
     */
    public void setWaypoints(Set<? extends W> waypoints) {
        this.waypoints.clear();
        this.waypoints.addAll(waypoints);
    }

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        Rectangle viewportBounds = map.getViewportBounds();
        g.translate(-viewportBounds.getX(), -viewportBounds.getY());

        for (var wp : this.getWaypoints()) {
            Point2D point = map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());

            int x = (int) (point.getX() - (img.getWidth() * 0.1));
            int y = (int) (point.getY() - (img.getHeight() * 0.2));

            g.drawImage(img, x, y, null);
        }

        g.translate(viewportBounds.getX(), viewportBounds.getY());
    }

}
