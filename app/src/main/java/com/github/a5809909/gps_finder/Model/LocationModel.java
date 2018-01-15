package com.github.a5809909.gps_finder.Model;

public class LocationModel {

    private int _id;
    private String dateAndTime;
    private String cellId;
    private String lac;
    private String mcc;
    private String mnc;
    private String json_first;
    private String lat;
    private String lng;
    private String acc;
    private String address;
    private String urlPhotos;

    private String errors;

    public int get_id() {
        return _id;
    }

    public void set_id(int p_id) {
        _id = p_id;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String pDateAndTime) {
        dateAndTime = pDateAndTime;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String pCelId) {
        cellId = pCelId;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String pLac) {
        lac = pLac;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String pMcc) {
        mcc = pMcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String pMnc) {
        mnc = pMnc;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String pLat) {
        lat = pLat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String pLng) {
        lng = pLng;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String pAcc) {
        acc = pAcc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String pAddress) {
        address = pAddress;
    }

    public String getJson_first() {
        return json_first;
    }

    public void setJson_first(String json_first) {
        this.json_first = json_first;
    }

    public String getUrlPhotos() {
        return urlPhotos;
    }

    public void setUrlPhotos(String urlPhotos) {
        this.urlPhotos = urlPhotos;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

}
