/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rt0803;

/**
 *
 * @author DELL
 */
public class Arc {
    Node x;
    Node y;
    double poids;

    public Arc() {
    }

    public Arc(Node x, Node y) {
        this.x = x;
        this.y = y;
    }

    public Arc(Node x, Node y, double poids) {
        this.x = x;
        this.y = y;
        this.poids = poids;
    }

    public Node getX() {
        return x;
    }

    public void setX(Node x) {
        this.x = x;
    }

    public Node getY() {
        return y;
    }

    public void setY(Node y) {
        this.y = y;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    /*@Override
    public String toString() {
        return "Arc{" + "x=" + x + ", y=" + y + ", poids=" + poids + '}';
    }*/
    
   
}
