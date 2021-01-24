package com.example.BarcelonaTeam;

public class Player {
   private String firstName;
   private String lastName;
   private String nationalTeam;
   private String birth;
   private String goals;
   private String assists;
   private String saves;
   private boolean lineup;
   private int imgPlayer;


    public boolean getLineup() {
        return lineup;
    }

    public void setLineup(boolean lineup) {
        this.lineup = lineup;
    }

    public int getImgPlayer() {
        return imgPlayer;
    }

    public void setImgPlayer(int imgPlayer) {
        this.imgPlayer = imgPlayer;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalTeam() {
        return nationalTeam;
    }

    public void setNationalTeam(String nationalTeam) {
        this.nationalTeam = nationalTeam;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getAssists() {
        return assists;
    }

    public void setAssists(String assists) {
        this.assists = assists;
    }

    public String getSaves() {
        return saves;
    }

    public void setSaves(String saves) {
        this.saves = saves;
    }

}
