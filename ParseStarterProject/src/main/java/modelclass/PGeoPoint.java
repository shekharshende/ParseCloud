package modelclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 14/1/16.
 */
public class PGeoPoint implements Parcelable {

    public double getLongg() {
        return longg;
    }

    public void setLongg(double longg) {
        this.longg = longg;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    private double lat;
    private double longg;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
