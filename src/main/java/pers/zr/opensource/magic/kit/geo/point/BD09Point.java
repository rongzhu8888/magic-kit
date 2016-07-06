package pers.zr.opensource.magic.kit.geo.point;

import java.io.Serializable;

/**
 * 百度坐标系
 * Created by zhurong on 2016-7-6.
 */
public class BD09Point implements Serializable {
    private static final long serialVersionUID = -5220985202896517552L;

    private final double latitude;
    private final double longitude;

    public BD09Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
            throw new IllegalArgumentException("The supplied coordinates " + this + " are out of range.");
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("(" + latitude + "," + longitude + ")");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BD09Point bd09Point = (BD09Point) o;

        if (Double.compare(bd09Point.latitude, latitude) != 0) return false;
        return Double.compare(bd09Point.longitude, longitude) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
