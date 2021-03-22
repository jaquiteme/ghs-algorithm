/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rt0803;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class Graphe {

    LinkedList<Node> nodes;
    //LinkedList<Arc> successeurs;

    private static HashMap<Integer, Node> hmap;
    File file;
    //Ajouts
    public static BlockingQueue<Message> messageQueue;
    public static Queue<Message> queue;
    private static ThreadGroup threadgroup;
    private static HashMap<Integer, Thread> nodesThreads;

    public Graphe() {

    }

   /* public Graphe(int k) {
        this.nodes = new LinkedList<>();
        this.hmap = new HashMap<>();
        for (int i = 0; i < k; i++) {
            this.addNode(i + 1);
        }
    }*/
    
    //Le constructeur qui permet d'initialiser le graphe
    public Graphe(String file) {
        this.nodes = new LinkedList<>();
        this.hmap = new HashMap<>();
        this.messageQueue = new LinkedBlockingQueue<>();
        this.file = new File(file);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String strLine = " ";

            while ((strLine = br.readLine()) != null) {
                String cols[] = strLine.split(",");
                //System.out.println("Column x "+cols[0].toString()+" Column y "+cols[1]);
                Node n1 = new Node(Integer.parseInt(cols[0]));
                Node n2 = new Node(Integer.parseInt(cols[1]));
                if (!this.hmap.containsKey(Integer.parseInt(cols[0]))) {
                    this.hmap.put(Integer.parseInt(cols[0]), n1);
                    this.nodes.add(n1);
                }

                if (!this.hmap.containsKey(Integer.parseInt(cols[1]))) {
                    this.hmap.put(Integer.parseInt(cols[1]), n2);
                    this.nodes.add(n2);
                }

                this.addArc(Integer.parseInt(cols[0]), Integer.parseInt(cols[1]), Integer.parseInt(cols[2]));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Graphe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Graphe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Cette procédure permet d'initialiser le graphe 
    public void initialize() {
        this.hmap.forEach((Integer key, Node value) -> {
            Message init = new Message(-1, key, Blocs.INITIALISATION);
            try {
                Graphe.messageQueue.put(init);
            } catch (InterruptedException ex) {
                Logger.getLogger(Graphe.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void getMessageQueue() {
        System.out.println("Size:" + Graphe.messageQueue.size());
    }

    public void addNode(int n) {

        if (this.hmap.get(n) == null) {
            Node _n = new Node(n);
            this.hmap.put(n, _n);
            this.nodes.add(_n);
        }

    }

    public Node getNode(int n) {

        return this.hmap.get(n);

    }
    
    //Cette fonction permet d'ajouter un noeud dans le graphe 
    public void addArc(int x, int y, double poids) {

        /*this.addNode(x);
        this.addNode(y);*/
        Node _x = this.getNode(x);
        Node _y = this.getNode(y);

        if (!_x.hasSuccesseur(y)) {
            Arc a = new Arc(_x, _y, poids);
            _x.addSuccesseur(a);
            //Ajout
            _x.addCanal(y, "basic");
            //
            //Modif TP3
            Arc b = new Arc(_y, _x, poids);
            _y.addSuccesseur(b);
            //Ajout
            _y.addCanal(x, "basic");
            //
        }
    }

    //cette procédure initialize les Threads pour chaque noeud
    public void startGHS() {
        this.nodesThreads = new HashMap<>();
        threadgroup = new ThreadGroup("GHS Nodes");
        //Création des Threads sur chaque Noeud
        this.hmap.forEach((Integer key, Node value) -> {
            Thread thread = new Thread(threadgroup, value, String.valueOf(key));
            this.nodesThreads.put(key, thread);
            thread.start();
        });
    }

    public Thread getThread(int id) {
        return this.nodesThreads.get(id);
    }

    //Cette fonction permet aux noeud de déposer un message dans la file de messages
    public static void sendMessage(Message message) {
        if (message.getMessage() != 0) {
            Graphe.messageQueue.offer(message);
        } else {
            Graphe.terminate();
        }
    }

    //Cette fonction termine la simulation 
    public static void terminate() {
        Graphe.hmap.forEach((Integer key, Node value) -> {
            Message terminate = new Message(-1, key, Blocs.TERMINATE);
            try {
                Graphe.messageQueue.put(terminate);
            } catch (InterruptedException ex) {
                Logger.getLogger(Graphe.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    //Cette fonction permet aux noeud de récuperer les messages dans la file de messages
    public static Message receiveMessage(int id) {
        if (Graphe.messageQueue.size() > 1) {
            Iterator<Message> listQueue = Graphe.messageQueue.iterator();
            //Message m = Graphe.messageQueue.peek();
            while (listQueue.hasNext()) {
                Message m = listQueue.next();
                if (m.getReceiverID() == id) {
                    listQueue.remove();
                    return m;
                }
            }

        }
        return new Message(0, 0, -1);
    }

    /*public void export() {
        String buff = "Source,Target\n";
        String sep = ",";
        for (Node n : this.nodes) {
            for (Arc a : n.getSuccesseurs()) {
                buff += a.getY().getId() + sep
                        + a.getX().getId() + "\n";
            }
        }
        File outputFile = new File(this.getClass() + ".csv");
        FileWriter out;
        try {
            out = new FileWriter(outputFile);
            out.write(buff);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
    @Override
    public String toString() {
        return this.nodes.toString();
    }

}
