package com.pet.adoption;

public class animal_profil {

    String image;
    String nom;
    String desc;
    String loc;
    String race;
    String weight;
    String description;
    String owner;
    String uniqueID;

    public animal_profil(String image, String nom, String desc, String loc, String race, String weight, String description, String owner, String uniqueID) {
        this.image = image;
        this.nom = nom;
        this.desc = desc;
        this.loc = loc;
        this.race = race;
        this.weight = weight;
        this.description = description;
        this.owner = owner;
        this.uniqueID = uniqueID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

}
