package me.vistark.coppavietnam.Model.Entity;

public class fsCatched {
    int ID;
    int ElementID;
    String CreatedDate;
    String Length;
    String Weight;
    String CatchedTime;
    String Latitude;
    String Longitude;
    String ImagePath;
    String Trip_id;
    String Finished_time;

    public fsCatched(int ID, int elementID, String createdDate, String length, String weight, String catchedTime, String latitude, String longitude, String imagePath, String Trip_id, String Finished_time) {
        this.ID = ID;
        ElementID = elementID;
        CreatedDate = createdDate;
        Length = length;
        Weight = weight;
        CatchedTime = catchedTime;
        Latitude = latitude;
        Longitude = longitude;
        ImagePath = imagePath;
        this.Trip_id = Trip_id;
        this.Finished_time = Finished_time;
    }
//    public String getJSON() {
//        JSONObject catched = new JSONObject();
//        catched.put()
//    }


    public String getFinished_time() {
        return Finished_time;
    }

    public String getTrip_id() {
        return Trip_id;
    }

    public void setFinished_time(String finished_time) {
        Finished_time = finished_time;
    }

    public void setTrip_id(String trip_id) {
        Trip_id = trip_id;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getElementID() {
        return ElementID;
    }

    public void setElementID(int elementID) {
        ElementID = elementID;
    }

    public void setCatchedTime(String catchedTime) {
        CatchedTime = catchedTime;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCatchedTime() {
        return CatchedTime;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }


    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
