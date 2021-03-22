/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rt0803;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class Node implements Runnable {

    int id;
    LinkedList<Arc> successeurs;
    //Ajout
    int niv = 0, recu = 0, pere, mcan, testcan, messageEnvoye = 0, messageRecu = 0;
    HashMap<Integer, String> canal;
    String state;
    double nom, mPoids;

    public Node() {
    }

    public Node(int id) {
        this.id = id;
        this.successeurs = new LinkedList<>();
        this.canal = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LinkedList<Arc> getSuccesseurs() {
        return successeurs;
    }

    public void setSuccesseurs(LinkedList<Arc> successeurs) {
        this.successeurs = successeurs;
    }

    public int getNiv() {
        return niv;
    }

    public void setNiv(int niv) {
        this.niv = niv;
    }

    public int getRecu() {
        return recu;
    }

    public void setRecu(int recu) {
        this.recu = recu;
    }

    public HashMap<Integer, String> getCanal() {
        return canal;
    }

    public String getOneCanal(int j) {
        return this.canal.get(j);
    }

    public void setCanal(HashMap<Integer, String> canal) {
        this.canal = canal;
    }

    //Focntion qui permet de mettre à jour l'état d'un canal
    public void updateCanal(int j, String state) {
        this.canal.replace(j, state);
    }

    public void addCanal(int id, String state) {
        this.canal.put(id, state);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getNom() {
        return nom;
    }

    public void setNom(double nom) {
        this.nom = nom;
    }

    public int getPere() {
        return pere;
    }

    public void setPere(int pere) {
        this.pere = pere;
    }

    public int getMcan() {
        return mcan;
    }

    public void setMcan(int mcan) {
        this.mcan = mcan;
    }

    public int getTestcan() {
        return testcan;
    }

    public void setTestcan(int testcan) {
        this.testcan = testcan;
    }

    public double getmPoids() {
        return mPoids;
    }

    public void setmPoids(double mPoids) {
        this.mPoids = mPoids;
    }

    //Fonction qui permet de récupérer un successeur de poids minimum
    public int getMinSuccPoid() {
        double mPoids = this.successeurs.get(0).getPoids();
        int idPoidsMin = this.successeurs.get(0).getY().getId();
        for (Arc a : successeurs) {
            if (a.getPoids() < mPoids) {
                mPoids = a.getPoids();
                idPoidsMin = a.getY().getId();
            }
        }
        return idPoidsMin;
    }

    public Double getOneSucceurPoids(int j) {
        for (Arc a : this.successeurs) {
            if (a.getY().getId() == j) {
                return a.getPoids();
            }
        }
        return null;
    }

    public boolean hasSuccesseur(int j) {
        for (Arc a : this.successeurs) {
            if (a.getY().getId() == j) {
                return true;
            }
        }

        return false;
    }

    public void addSuccesseur(Arc a) {
        this.successeurs.add(a);
    }

    public Integer getSuccLength() {
        return this.successeurs.size();
    }

    public int getMessageEnvoye() {
        return messageEnvoye;
    }

    public void setMessageEnvoye(int messageEnvoye) {
        this.messageEnvoye = messageEnvoye;
    }

    public int getMessageRecu() {
        return messageRecu;
    }

    public void setMessageRecu(int messageRecu) {
        this.messageRecu = messageRecu;
    }

    @Override
    public String toString() {
        String result = "[";
        for (Arc a : successeurs) {
            result += "{id=" + a.getY().getId() + ": poids=" + a.getPoids() + "}\n";
        }
        result += "]";

        return "\n==============NOEUD " + id + "==============\nCANAUX \n"
                + this.canal + "\nSUCCESSEURS\n" + result + "\n"
                + "NIVEAU \n"
                + this.getNiv() + "\n"
                + "MCAN \n"
                + this.getMcan() + "\n"
                + "PERE \n"
                + this.getPere() + "\n"
                + "MESSAGES ENVOYES\n"
                + this.getMessageEnvoye() + "\n"
                + "MESSAGES RECUS\n"
                + this.getMessageRecu() + "\n"
                + "=================FIN==============";
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj);
    }

    //Bloc 1
    public void initialisation() throws InterruptedException {
        System.out.println(Colors.ANSI_YELLOW+this.getId() + " start initialisation"+Colors.ANSI_RESET);
        int j = this.getMinSuccPoid();
        this.updateCanal(j, "branch");
        this.setRecu(0);
        this.setNiv(0);
        this.setState("found");
        //Envoie connect à j
        HashMap<String, String> params = new HashMap<>();
        params.put("L", String.valueOf(0));
        Message connect = new Message(this.getId(), j, Blocs.CONNECT_RECEPTION, params);
        Graphe.sendMessage(connect);
        this.setMessageEnvoye(this.messageEnvoye + 1);
        System.err.println(this.getId() + " send connect(" + String.valueOf(0) + ") to " + j);
    }

    //Bloc 2 Réception de connect
    public void connect(int from, int L) {
        System.out.println(Colors.ANSI_CYAN+this.getId() + " recieve connect from " + from + " Niv = " + L+Colors.ANSI_RESET);
        this.setMessageRecu(this.messageRecu + 1);
        //System.err.println(this.getId()+" NIV IS "+this.getNiv());
        if (L < this.getNiv()) {
            this.updateCanal(from, "branch");
            //Envoie initiate
            HashMap<String, String> params = new HashMap<>();
            params.put("niv", String.valueOf(this.getNiv()));
            params.put("nom", String.valueOf(this.getNom()));
            params.put("state", String.valueOf(this.getState()));
            Message initiate = new Message(this.getId(), from, Blocs.INITIATE_RECEPTION, params);
            Graphe.sendMessage(initiate);
            System.out.println(Colors.ANSI_PURPLE+this.getId() + " send initiate(" + this.getNiv()
                        + ", " + this.getNom() + ", " + this.getState() + ") to " + from+Colors.ANSI_RESET);
        } else {
            if ("basic".equals(this.getOneCanal(from))) {
                //Traiter le message plus tard
                //System.out.println(this.getId() + " Treat message later");
                HashMap<String, String> params = new HashMap<>();
                //System.out.println("L was: " + L);
                params.put("L", String.valueOf(L));
                Message traiter = new Message(from, this.getId(), Blocs.CONNECT_RECEPTION, params);
                Graphe.sendMessage(traiter);
            } else {
                //Envoie de initiate a j
                String nom = String.valueOf(getOneSucceurPoids(from));
                HashMap<String, String> params = new HashMap<>();
                params.put("niv", String.valueOf(this.getNiv() + 1));
                params.put("nom", nom);
                params.put("state", "find");
                Message initiate = new Message(this.getId(), from, Blocs.INITIATE_RECEPTION, params);
                Graphe.sendMessage(initiate);
                this.setMessageEnvoye(this.messageEnvoye + 1);
                System.out.println(Colors.ANSI_PURPLE+this.getId() + " send initiate(" + (this.getNiv() + 1)
                        + ", " + nom + ", find) to " + from+Colors.ANSI_RESET);
            }
        }
    }

    //Bloc 3 Réception de initiate
    public void initiate(int from, int niv, double nom, String state) {
        System.out.println(Colors.ANSI_GREEN+this.getId() + " recieve initiate from " + from+Colors.ANSI_RESET);
        this.setMessageRecu(this.messageRecu + 1);
        this.setNiv(niv);
        this.setNom(nom);
        this.setState(state);
        this.setPere(from);
        this.setMcan(-1);
        this.setmPoids(Double.POSITIVE_INFINITY);
        for (Arc k : this.successeurs) {
            int current = k.getY().getId();
            if ("branch".equals(this.canal.get(current)) && current != from) {
                //Envoie de initiate
                HashMap<String, String> params = new HashMap<>();
                params.put("niv", String.valueOf(this.getNiv()));
                params.put("nom", String.valueOf(this.getNom()));
                params.put("state", String.valueOf(this.getState()));
                Message initiate = new Message(this.getId(), current,
                        Blocs.INITIATE_RECEPTION, params);
                Graphe.sendMessage(initiate);
                this.setMessageEnvoye(this.messageEnvoye + 1);
                System.out.println(Colors.ANSI_PURPLE+this.getId() + " send initiate(" + this.getNiv()
                        + ", " + this.getNom() + ", " + this.getState() + ") to " + from+Colors.ANSI_RESET);
            }
        }
        if ("find".equals(this.getState())) {
            this.setRecu(0);
            TEST();
        }

    }

    //Bloc4
    public int TEST() {
        //System.out.println(this.getId() + " called proc TEST");
        for (Arc j : this.successeurs) {
            int current = j.getY().getId();
            if ("basic".equals(this.canal.get(current)) && current == this.getMinSuccPoid()) {
                this.setTestcan(current);
                //Envoie de test
                //test(x, i.getTestcan(), i.getNiv(), i.getNom());
                HashMap<String, String> params = new HashMap<>();
                params.put("niv", String.valueOf(this.getNiv()));
                params.put("nom", String.valueOf(this.getNom()));
                Message test = new Message(this.getId(), this.getTestcan(), Blocs.TEST_RECEPTION, params);
                Graphe.sendMessage(test);
                this.setMessageEnvoye(this.messageEnvoye + 1);
                System.out.println(this.getId() + " send test(" + this.getNiv()
                        + ", " + this.getNom() + ") to " + this.getTestcan());
                return 1;
            } else {
                this.setTestcan(-1);
                //APPEL PROCEDURE REPORT
                REPORT();
                return 1;
            }
        }
        return 0;
    }

    //Bloc 5
    public void test(int from, int niv, double F) {
        System.out.println(this.getId() + " recieve test message from " + from);
        this.setMessageRecu(this.messageRecu + 1);
        if (niv > this.getNiv()) {
            //Traiter le message plus tard
            HashMap<String, String> params = new HashMap<>();
            params.put("niv", String.valueOf(niv));
            params.put("nom", String.valueOf(F));
            Message traiter = new Message(from, this.getId(), Blocs.TEST_RECEPTION, params);
            Graphe.sendMessage(traiter);
        } else {
            if (F == this.getNom()) {
                if ("basic".equals(this.getOneCanal(from))) {
                    this.updateCanal(from, "reject");
                }

                if (from != this.getTestcan()) {
                    //Envoie reject à j
                    Message reject = new Message(this.getId(), from, Blocs.REJECT_RECEPTION);
                    Graphe.sendMessage(reject);
                    this.setMessageEnvoye(this.messageEnvoye + 1);
                    //reject(to, from);
                } else {
                    //Appel procédure TEST
                    TEST();
                }
            } else {
                //Envoie accept 
                //accept(to, from);
                Message accept = new Message(this.getId(), from, Blocs.ACCEPT_RECEPTION);
                Graphe.sendMessage(accept);
                this.setMessageEnvoye(this.messageEnvoye + 1);
            }
        }
    }

    //Bloc 6 
    public void accept(int from) {
        System.out.println(this.getId() + " recieve accept message from " + from);
        this.setMessageRecu(this.messageRecu + 1);
        this.setTestcan(-1);
        if (this.getOneSucceurPoids(from) < this.mPoids) {
            this.setmPoids(this.getOneSucceurPoids(from));
            this.setMcan(from);
        }
        //Appel proccédure TEST
        TEST();
    }

    //Bloc 7 | Réception de reject
    public void reject(int from) {
        System.out.println("recieve reject message");
        this.setMessageRecu(this.messageRecu + 1);
        if ("basic".equals(this.getOneCanal(from))) {
            this.updateCanal(from, "reject");
        }
        //Appel proccédure TEST
        TEST();
    }

    //Bloc 8
    public void REPORT() {
        System.out.println(this.getId() + " called proc REPORT");
        int card = 0;
        for (Arc a : this.successeurs) {
            if ("branch".equals(this.getOneCanal(a.getY().id))
                    && a.getY().id != this.pere) {
                card++;
            }
        }

        if (this.recu == card && this.testcan == -1) {
            this.state = "found";
            //Envoie report à pere
            HashMap<String, String> params = new HashMap<>();
            params.put("poids", String.valueOf(this.getmPoids()));

            Message report = new Message(this.getId(), this.getPere(),
                    Blocs.REPORT_RECEPTION, params);
            Graphe.sendMessage(report);
            this.setMessageEnvoye(this.messageEnvoye + 1);
        }

    }

    //Bloc 9 | Réception de report
    public void report(int from, double poids) throws InterruptedException {
        System.out.println(this.getId()+" recieve report ("+poids+") from "+from);
        this.setMessageRecu(this.messageRecu + 1);
        if (from != this.pere) {
            if (poids < this.mPoids) {
                this.mPoids = poids;
                this.mcan = from;
            }
            this.recu = this.recu + 1;
            //Appel proccedure REPORT
            REPORT();
        } else {
            if ("find".equals(this.state)) {
                //Traiter le message plus tard
                HashMap<String, String> params = new HashMap<>();
                params.put("poids", String.valueOf(poids));
                Message traiter = new Message(from, this.getId(),
                        Blocs.REPORT_RECEPTION, params);
                Graphe.sendMessage(traiter);
            } else {
                if (poids > this.mPoids) {
                    //Appel proccedure CHANGEROOT
                    CHANGEROOT();
                } else {
                    if (poids == this.mPoids && this.mPoids
                            == Double.POSITIVE_INFINITY) {
                        //TERMINE
                        Message terminate = new Message(this.getId(), 0, Blocs.TERMINATE);
                        Thread.sleep(5000);
                        Graphe.sendMessage(terminate);
                        System.out.println(Colors.ANSI_GREEN+this.getId()+" TERMINATE"+Colors.ANSI_RESET);
                        //System.err.println(this.toString());

                    }
                }
            }
        }

    }

    //Bloc 10
    public void CHANGEROOT() {
        System.out.println("called CHANGEROOT message");
        if (this.canal.get(this.mcan).equals("branch")) {
            //Envoie changeroot à mcan
            Message changeRoot = new Message(this.getId(), this.getMcan(), Blocs.CHANGEROOT_RECEPTION);
            Graphe.sendMessage(changeRoot);
        } else {
            //Envoi connect niv à mcan
            HashMap<String, String> params = new HashMap<>();
            params.put("poids", String.valueOf(this.getNiv()));
            Message connect = new Message(this.getId(), this.getMcan(), Blocs.CONNECT_RECEPTION);
            Graphe.sendMessage(connect);
            this.updateCanal(this.mcan, "bracnh");
        }
    }

    //Bloc 11
    public void changeroot() {
        System.out.println("recieve changeroot message");
        this.setMessageRecu(this.messageRecu + 1);
        CHANGEROOT();
    }

    /*public static void sendMessage(Message message) {
        nodes[message.receiverID].getMessageQueue().offer(message);
    }*/
    /**
     * *****************
     * CONFIG DU THREAD
     */
    @Override
    public void run() {
        Message m = new Message();
        m = Graphe.receiveMessage(this.getId());
        //Tant que la simulation n'est pas terminé, exécuter les codes ci-dessous
        while (m.getMessage() != 0) {
            int rNiv;
            double rNom, rPoids;
            String rState;
            
            //"-1" représente de système dans ce cas 
            if (m.getMessage() != -1) {
                //Selon le message reçut, exécute le code approprié
                switch (m.getMessage()) {
                    case 1: {
                        try {
                            initialisation();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    m = Graphe.receiveMessage(this.getId());
                    break;
                    case 2:
                        HashMap<String, String> params = m.getParams();
                        int rL = Integer.parseInt((String) params.get("L"));
                        connect(m.getSenderID(), rL);
                        m = Graphe.receiveMessage(this.getId());
                        break;
                    case 3:
                        rNiv = Integer.parseInt((String) m.getParams().get("niv"));
                        rNom = Double.parseDouble((String) m.getParams().get("nom"));
                        rState = (String) m.getParams().get("state");
                        initiate(m.getSenderID(), rNiv, rNom, rState);
                        m = Graphe.receiveMessage(this.getId());
                        break;
                    case 4:
                        int r = TEST();
                        if (r == 1) {
                            m = Graphe.receiveMessage(this.getId());
                        }
                        break;
                    case 5:
                        rNiv = Integer.parseInt((String) m.getParams().get("niv"));
                        rNom = Double.parseDouble((String) m.getParams().get("nom"));
                        test(m.getSenderID(), rNiv, rNom);
                        m = Graphe.receiveMessage(this.getId());
                        break;
                    case 6:
                        accept(m.getSenderID());
                        m = Graphe.receiveMessage(this.getId());
                        break;
                    case 7:
                        reject(m.getSenderID());
                        m = Graphe.receiveMessage(this.getId());
                        break;
                    case 8:
                        break;
                    case 9:
                        rPoids = Double.parseDouble((String) m.getParams().get("poids"));
                         {
                            try {
                                report(m.getSenderID(), rPoids);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        m = Graphe.receiveMessage(this.getId());
                        break;
                    case 10:
                        break;
                    case 11:
                        changeroot();
                        break;
                    default:
                        m = Graphe.receiveMessage(this.getId());
                }
            } else {
                try {
                    Thread.sleep(500);
                    m = Graphe.receiveMessage(this.getId());
                } catch (InterruptedException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println(this.toString());
        /*
        System.out.println(Thread.currentThread().getName());*/
        //System.out.println(m.getMessage());
    }
}
