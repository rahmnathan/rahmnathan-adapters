package com.github.rahmnathan.commute.provider;

public interface CommuteProvider {

    String getCommuteTime(String startLocation, String endLocation, String key);

}
