package com.pet.adoption;

public class notifications {

    String titleNoti;
    String dateNoti;
    String descNoti;

    public notifications(String titleNoti, String dateNoti, String descNoti) {
        this.titleNoti = titleNoti;
        this.dateNoti = dateNoti;
        this.descNoti = descNoti;
    }

    public String getTitleNoti() {
        return titleNoti;
    }

    public void setTitleNoti(String titleNoti) {
        this.titleNoti = titleNoti;
    }

    public String getDateNoti() {
        return dateNoti;
    }

    public void setDateNoti(String dateNoti) {
        this.dateNoti = dateNoti;
    }

    public String getDescNoti() {
        return descNoti;
    }

    public void setDescNoti(String descNoti) {
        this.descNoti = descNoti;
    }
}
