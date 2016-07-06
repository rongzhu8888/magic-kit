package pers.zr.opensource.magic.kit.geo.point;

import java.io.Serializable;

/**
 *
 * 火星坐标系（中国标准坐标）
 * Created by zhurong on 2016-7-6.
 */
public class GCJ02Point implements Serializable {

    private static final long serialVersionUID = 6687548989808133094L;

    private final double longitude;

    private final double latitude;

    public GCJ02Point(double latitude, double longitude) {
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

        GCJ02Point that = (GCJ02Point) o;

        if (Double.compare(that.longitude, longitude) != 0) return false;
        return Double.compare(that.latitude, latitude) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(longitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }





}
