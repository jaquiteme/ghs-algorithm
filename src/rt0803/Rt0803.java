/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rt0803;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author DELL
 */
public class Rt0803 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        String chemin = "C:\\Users\\DELL\\Documents\\Java Projects\\rt0803\\src\\";
        String filename = "grapheEx1.txt";
        String fichier = chemin + filename;
        //Initialisation du graphe
        Graphe g = new Graphe(fichier);
        System.out.println(Colors.ANSI_GREEN + "==========GRAPHE DEPART===========" + Colors.ANSI_RESET);
        //Affichage du graphe de départ
        System.out.println(g.toString());
        System.out.println(Colors.ANSI_GREEN + "========== FIN GRAPHE DEPART===========" + Colors.ANSI_RESET);
        //Démarrage de GHS
        g.startGHS();
        //Initialisation de l'algorithme
        g.initialize();
        //g.getMessageQueue();

    }

}
