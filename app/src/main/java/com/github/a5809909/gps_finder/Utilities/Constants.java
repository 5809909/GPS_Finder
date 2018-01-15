package com.github.a5809909.gps_finder.Utilities;

public class Constants {

    public interface ACTION {

        public static String MAIN_ACTION = "com.github.a5809909.gps_finder.action.main";
        public static String STARTFOREGROUND_ACTION = "com.github.a5809909.gps_finder.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.github.a5809909.gps_finder.action.stopforeground";
    }

    public interface NOTIFICATION_ID {

        public static int FOREGROUND_SERVICE = 101;
        public static int LOCATION_PERMISSION_CODE = 855;
        public static final String GOOGLE_GEOLOCATE_URI = "https://www.googleapis.com/geolocation/v1/geolocate?key=";
        public static final String GOOGLE_GEOLOCATE_API_KEY = "AIzaSyDNsRNkiJddjICdCY9fiFw3U6_nziORLC4";
        public static final String GOOGLE_GEOCODING_URI = "https://maps.googleapis.com/maps/api/geocode/json?";
        public static final String GOOGLE_GEOCODING_API_KEY = "AIzaSyA_W1ByU1TkqSfL9_soCJtq1M_OrgrhFKk";
        public static final String FETCHR_API_KEY = "23b3976fd2e717eaf8337db6c7656db3";
        public static final String FETCHR_URI = "https://api.flickr.com/services/rest/";
        public static final String GOOGLE_PLACES_API_KEY = "AIzaSyCtr8GiqK1JUNfJ7Pgp7U-p6Nl__i3GuNo";
        public static final String GOOGLE_PLACES_URI = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        public static final int PHOTO_COUNT = 50;

    }
}
