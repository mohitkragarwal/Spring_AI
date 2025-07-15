package com.mohit.SpringAICode;

public class Movie {

    private String moviename;
    private String leadactor;
    private String director;
    private int year;
    private  double imdb;

    public double getImdb() {
        return imdb;
    }

    public void setImdb(double imdb) {
        this.imdb = imdb;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getLeadactor() {
        return leadactor;
    }

    public void setLeadactor(String leadactor) {
        this.leadactor = leadactor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
