package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverDifficultyDetails
{
    private double duration;

    private int length;

    private int njs;

    private int njsOffset;

    private int bombs;

    private int notes;

    private int obstacles;

    public void setDuration(double duration){
        this.duration = duration;
    }
    public double getDuration(){
        return this.duration;
    }
    public void setLength(int length){
        this.length = length;
    }
    public int getLength(){
        return this.length;
    }
    public void setNjs(int njs){
        this.njs = njs;
    }
    public int getNjs(){
        return this.njs;
    }
    public void setNjsOffset(int njsOffset){
        this.njsOffset = njsOffset;
    }
    public int getNjsOffset(){
        return this.njsOffset;
    }
    public void setBombs(int bombs){
        this.bombs = bombs;
    }
    public int getBombs(){
        return this.bombs;
    }
    public void setNotes(int notes){
        this.notes = notes;
    }
    public int getNotes(){
        return this.notes;
    }
    public void setObstacles(int obstacles){
        this.obstacles = obstacles;
    }
    public int getObstacles(){
        return this.obstacles;
    }
}