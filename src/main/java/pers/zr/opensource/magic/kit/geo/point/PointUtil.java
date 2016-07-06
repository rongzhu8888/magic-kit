package pers.zr.opensource.magic.kit.geo.point;

/**
 *
 *
 * 坐标转换、计算距离
 * <p/>
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。Earth
 * <p/>
 * GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系。Mars
 * <p/>
 * BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系。  Bd09
 * <p/>
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。
 * <p/>
 * 百度地图API        百度坐标
 * 腾讯搜搜地图API 火星坐标
 * 搜狐搜狗地图API 搜狗坐标*
 * 阿里云地图API     火星坐标
 * 图吧MapBar地图API 图吧坐标
 * 高德MapABC地图API 火星坐标
 * 灵图51ditu地图API 火星坐标

 *
 * Created by zhurong on 2016-7-6.
 */
public class PointUtil {

    private static double PI = Math.PI;
    private static double AXIS = 6378245.0;  //
    private static double OFFSET = 0.00669342162296594323;  //(a^2 - b^2) / a^2
    private static double X_PI = PI * 3000.0 / 180.0;


    /**
     * WGS84=>GCJ02   地球坐标系=>火星坐标系
     * @param wgs84Point
     * @return
     */
    public static GCJ02Point wgs2GCJ(WGS84Point wgs84Point) {
        double wgs84Latitude = wgs84Point.getLatitude();
        double wgs84Longitude = wgs84Point.getLongitude();

        if (outOfChina(wgs84Latitude, wgs84Longitude)) {
            return new GCJ02Point(wgs84Latitude, wgs84Longitude);
        }
        double[] deltaD = delta(wgs84Latitude, wgs84Longitude);
        return new GCJ02Point(wgs84Latitude + deltaD[0], wgs84Longitude + deltaD[1]);
    }


    /**
     * GCJ02=>WGS84   火星坐标系=>地球坐标系(粗略)
     * @param gcj02Point
     * @return
     */
    public static WGS84Point gcj2WGS(GCJ02Point gcj02Point) {
        double gcj02Latitude = gcj02Point.getLatitude();
        double gcj02Longitude = gcj02Point.getLongitude();
        if (outOfChina(gcj02Latitude, gcj02Longitude)) {
            return new WGS84Point(gcj02Latitude, gcj02Longitude);
        }
        double[] deltaD = delta(gcj02Latitude, gcj02Longitude);
        return new WGS84Point(gcj02Latitude - deltaD[0], gcj02Longitude - deltaD[1]);
    }


    /**
     * GCJ02=>WGS84   火星坐标系=>地球坐标系（精确）
     * @param gcj02Point
     * @return
     */
    public static WGS84Point gcj2WGSExactly(GCJ02Point gcj02Point) {
        double gcjLat = gcj02Point.getLatitude();
        double gcjLon = gcj02Point.getLongitude();

        double initDelta = 0.01;
        double threshold = 0.000000001;
        double dLat = initDelta, dLon = initDelta;
        double mLat = gcjLat - dLat, mLon = gcjLon - dLon;
        double pLat = gcjLat + dLat, pLon = gcjLon + dLon;
        double wgsLat, wgsLon, i = 0;
        while (true) {
            wgsLat = (mLat + pLat) / 2;
            wgsLon = (mLon + pLon) / 2;
            GCJ02Point tmp = wgs2GCJ(new WGS84Point(wgsLat, wgsLat));
            dLat = tmp.getLatitude() - gcjLat;
            dLon = tmp.getLongitude() - gcjLon;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
                break;

            if (dLat > 0) pLat = wgsLat;
            else mLat = wgsLat;
            if (dLon > 0) pLon = wgsLon;
            else mLon = wgsLon;

            if (++i > 10000) break;
        }

        return new WGS84Point(wgsLat, wgsLon);

    }

    /**
     * GCJ-02=>BD09 火星坐标系=>百度坐标系
     * @param gcj02Point
     * @return
     */
    public static BD09Point gcj2BD09(GCJ02Point gcj02Point) {
        double x = gcj02Point.getLongitude();
        double y = gcj02Point.getLatitude();
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
        double bd09Latitude = z * Math.sin(theta) + 0.006;
        double bd09Longitude = z * Math.cos(theta) + 0.0065;
        return new BD09Point(bd09Latitude, bd09Longitude);
    }


    /**
     * BD09=>GCJ-02 百度坐标系=>火星坐标系
     * @param bd09Point
     * @return
     */
    public static GCJ02Point bd092GCJ(BD09Point bd09Point) {
        double x = bd09Point.getLongitude() - 0.0065;
        double y = bd09Point.getLatitude() - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double gcj02Latitude = z * Math.sin(theta);
        double gcj02Longitude = z * Math.cos(theta);
        return new GCJ02Point(gcj02Latitude, gcj02Longitude);
    }


    /**
     * BD09=>WGS84 百度坐标系=>地球坐标系
     * @param bd09Point
     * @return
     */
    public static WGS84Point bd092WGS(BD09Point bd09Point) {
        GCJ02Point gcj02Point = bd092GCJ(bd09Point);
        return gcj2WGS(gcj02Point);
    }


    /**
     * WGS84=>BD09   地球坐标系=>百度坐标系
     * @param wgs84Point
     * @return
     */
    public static BD09Point wgs2BD09(WGS84Point wgs84Point) {
        GCJ02Point gcj02Point = wgs2GCJ(wgs84Point);
        return gcj2BD09(gcj02Point);
    }

    /**
     * 计算2点之间距离
     * @param latA
     * @param logA
     * @param latB
     * @param logB
     * @return
     */
    public static double distance(double latA, double logA, double latB, double logB) {
        int earthR = 6371000;
        double x = Math.cos(latA * Math.PI / 180) * Math.cos(latB * Math.PI / 180) * Math.cos((logA - logB) * Math.PI / 180);
        double y = Math.sin(latA * Math.PI / 180) * Math.sin(latB * Math.PI / 180);
        double s = x + y;
        if (s > 1)
            s = 1;
        if (s < -1)
            s = -1;
        double alpha = Math.acos(s);
        double distance = alpha * earthR;
        return distance;
    }

    private static double[] delta(double latitude, double longitude) {
        double[] latlng = new double[2];
        double dLat = transformLat(longitude - 105.0, latitude - 35.0);
        double dLon = transformLon(longitude - 105.0, latitude - 35.0);
        double radLat = latitude / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - OFFSET * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((AXIS * (1 - OFFSET)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (AXIS / sqrtMagic * Math.cos(radLat) * PI);
        latlng[0] = dLat;
        latlng[1] = dLon;
        return latlng;
    }

    /**
     * 判断是否国外
     * @param lat
     * @param lon
     * @return
     */
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    public static void main(String []args) {

        WGS84Point wgs84Point = new WGS84Point(32.10023, 117.20234);
        GCJ02Point gcj02Point = wgs2GCJ(wgs84Point);
        System.out.println(gcj02Point.toString());
        wgs84Point = gcj2WGS(gcj02Point);
        System.out.println(wgs84Point.toString());

        WGS84Point wgs84Point2 = new WGS84Point(33.20021, 120.92021);
        GCJ02Point gcj02Point2 = wgs2GCJ(wgs84Point2);
        System.out.println(distance(wgs84Point.getLatitude(), wgs84Point.getLongitude(), wgs84Point2.getLatitude(), wgs84Point2.getLongitude()));
        System.out.println(distance(gcj02Point.getLatitude(), gcj02Point.getLongitude(), gcj02Point2.getLatitude(), gcj02Point2.getLongitude()));

    }
}
