package com.example.myapplication.entity;

public class Museum {
    private String name;
    private String type;
    private String address;
    private String TicketPrice;
    private String OpeningHours;
    private String Suggestedtraveltime;
    private String Museumlevel;
    private String units;
    private String attractionlevel;
    private String number;
    private String introduction;
    private String Scenery;
    private String Howtogo;
    private String Scenicspotsaround;
    private String cover;
    private String note;

    public Museum(String name, String type, String address, String ticketPrice, String openingHours, String suggestedtraveltime, String museumlevel, String units, String attractionlevel, String number, String introduction, String scenery, String howtogo, String scenicspotsaround, String cover, String note) {
        this.name = name;
        this.type = type;
        this.address = address;
        TicketPrice = ticketPrice;
        OpeningHours = openingHours;
        Suggestedtraveltime = suggestedtraveltime;
        Museumlevel = museumlevel;
        this.units = units;
        this.attractionlevel = attractionlevel;
        this.number = number;
        this.introduction = introduction;
        Scenery = scenery;
        Howtogo = howtogo;
        Scenicspotsaround = scenicspotsaround;
        this.cover = cover;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTicketPrice() {
        return TicketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        TicketPrice = ticketPrice;
    }

    public String getOpeningHours() {
        return OpeningHours;
    }

    public void setOpeningHours(String openingHours) {
        OpeningHours = openingHours;
    }

    public String getSuggestedtraveltime() {
        return Suggestedtraveltime;
    }

    public void setSuggestedtraveltime(String suggestedtraveltime) {
        Suggestedtraveltime = suggestedtraveltime;
    }

    public String getMuseumlevel() {
        return Museumlevel;
    }

    public void setMuseumlevel(String museumlevel) {
        Museumlevel = museumlevel;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getAttractionlevel() {
        return attractionlevel;
    }

    public void setAttractionlevel(String attractionlevel) {
        this.attractionlevel = attractionlevel;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getScenery() {
        return Scenery;
    }

    public void setScenery(String scenery) {
        Scenery = scenery;
    }

    public String getHowtogo() {
        return Howtogo;
    }

    public void setHowtogo(String howtogo) {
        Howtogo = howtogo;
    }

    public String getScenicspotsaround() {
        return Scenicspotsaround;
    }

    public void setScenicspotsaround(String scenicspotsaround) {
        Scenicspotsaround = scenicspotsaround;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
