package de.ixsen.accsaber.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Configuration extends BaseEntity {

    @Column(name = "player_curve_y1")
    private double playerCurveY1;

    @Column(name = "player_curve_x1")
    private double playerCurveX1;

    @Column(name = "player_curve_k")
    private double playerCurveK;

    @Column(name = "ap_curve_a")
    private double apCurveA;

    @Column(name = "ap_curve_b")
    private double apCurveB;

    @Column(name = "ap_curve_c")
    private double apCurveC;

    @Column(name = "ap_curve_d")
    private double apCurveD;

    @Column(name = "ap_curve_e")
    private double apCurveE;

    public double getPlayerCurveY1() {
        return this.playerCurveY1;
    }

    public void setPlayerCurveY1(double playerCurveY1) {
        this.playerCurveY1 = playerCurveY1;
    }

    public double getPlayerCurveX1() {
        return this.playerCurveX1;
    }

    public void setPlayerCurveX1(double playerCurveX1) {
        this.playerCurveX1 = playerCurveX1;
    }

    public double getPlayerCurveK() {
        return this.playerCurveK;
    }

    public void setPlayerCurveK(double playerCurveK) {
        this.playerCurveK = playerCurveK;
    }

    public double getApCurveA() {
        return this.apCurveA;
    }

    public void setApCurveA(double apCurveA) {
        this.apCurveA = apCurveA;
    }

    public double getApCurveB() {
        return this.apCurveB;
    }

    public void setApCurveB(double apCurveB) {
        this.apCurveB = apCurveB;
    }

    public double getApCurveC() {
        return this.apCurveC;
    }

    public void setApCurveC(double apCurveC) {
        this.apCurveC = apCurveC;
    }

    public double getApCurveD() {
        return this.apCurveD;
    }

    public void setApCurveD(double apCurveD) {
        this.apCurveD = apCurveD;
    }

    public double getApCurveE() {
        return this.apCurveE;
    }

    public void setApCurveE(double apCurveE) {
        this.apCurveE = apCurveE;
    }
}
