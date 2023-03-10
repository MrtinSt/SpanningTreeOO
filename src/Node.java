import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name; //bezeichner des Knoten
    private int nodeID; //knotenID > 0

    private List<Link> linkList = new ArrayList<>(); // Liste aller verbindungen

    private int nextHop; //berechneter Link zum nächsten Knoten in richtung root
    private String nextHopName;
    private int calculatedRootID;

    private int shortestWayToRoot;
    private boolean connectedToRoot;
    private int msgCnt; //zählt mit wie oft der Knoten bei der bearbeitung aufgerufen wird

    //F = 4;
    public Node(String line) {
        this.name = line.split("=")[0].strip();
        this.nodeID = Integer.parseInt(line.split("=")[1].split(";")[0].strip());
        this.calculatedRootID = nodeID;
        this.msgCnt = 0;
        this.nextHop = nodeID;
        this.nextHopName = name;
        shortestWayToRoot = 0;
        connectedToRoot = false;
    }



    //send message to others
    public List<Message> outputMessanges(){
        List<Message> messageList = new ArrayList<Message>();
        msgCnt++;
        for (Link link: linkList) {
            //einzurückschicken und aufschaukeln verhindern
            //if(!link.zielknoten.equals(nextHopName)){
            //if(calculatedRootID != nodeID && this.shortestWayToRoot != 0){
                if(link.startknoten.equals(name)){
                    messageList.add(new Message(link.startknoten, nodeID, link.zielknoten,calculatedRootID, link.getKosten()+this.shortestWayToRoot, nextHop, true));
                }else{
                    messageList.add(new Message(link.zielknoten, nodeID, link.startknoten,calculatedRootID, link.getKosten()+this.shortestWayToRoot, nextHop, true));
                }
//            }else{
//                if(link.startknoten.equals(name)){
//                    messageList.add(new Message(link.startknoten, nodeID, link.zielknoten,calculatedRootID, 0, nextHop, connectedToRoot));
//                }else{
//                    messageList.add(new Message(link.zielknoten, nodeID, link.startknoten,calculatedRootID, 0, nextHop, connectedToRoot));
//                }
//            }




            //nur wenn die rootkosten größer 0 sind, wenn er kein root ist, darf er senden
//            if(calculatedRootID != nodeID && this.shortestWayToRoot != 0){
//                if(link.startknoten.equals(name)){
//                    messageList.add(new Message(link.startknoten, nodeID, link.zielknoten,calculatedRootID, link.getSummeKosten()+this.shortestWayToRoot));
//                }else{
//                    messageList.add(new Message(link.zielknoten, nodeID, link.startknoten,calculatedRootID, link.getSummeKosten()+this.shortestWayToRoot));
//                }
//            }else{

            //wenn ziel nodeId == nexthop ->

           // }


        }

        return messageList;
    }

    //get message from other node and return gesamtpfadkosten
    public int inputMessage(Message message) {


        if(Main.DEBUG){
            System.out.println("Aktueller Node: " + name + ", Startknoten: " + message.startNode() + ", Id: " + message.startId() + ", Zielknoten: " + message.zielNode() + ", calculatedRootId: " + message.calculatedRootId() + ", wegekosten zum Root: " + message.calculatedSummeRootKosten() + ", connectedToRoot: " + message.connectedToRoot);
        }

        if(message.calculatedRootId < calculatedRootID){
            this.calculatedRootID = message.calculatedRootId;
            connectedToRoot = false;

            nextHop = message.startId;
            nextHopName = message.startNode;
            shortestWayToRoot = message.calculatedSummeRootKosten;
            //return -1;
        } else if ((message.calculatedRootId == calculatedRootID) && this.shortestWayToRoot > message.calculatedSummeRootKosten) {
            shortestWayToRoot = message.calculatedSummeRootKosten;
            nextHop = message.startId;
            nextHopName = message.startNode;
            connectedToRoot = true;
        }else {
            // bester pfad schon gefunden, tue nichts
        }


        //root id rausfinden
//        if(message.calculatedRootId < calculatedRootID){
//            this.calculatedRootID = message.calculatedRootId;
//            connectedToRoot = false;
//
//            nextHop = message.startId;
//            nextHopName = message.startNode;
//            //return -1;
//        }
//
//        //wenn eigener root ist, dann pfadkosten = 0
//        if (this.nodeID == this.calculatedRootID){
//            shortestWayToRoot = 0;
//            connectedToRoot = true;
//
//            //return -1;
//        }
//
//        if ((this.nodeID != this.calculatedRootID) && (shortestWayToRoot == 0) && message.connectedToRoot){ // && (message.startId != nextHop) && (message.startId != nodeID)  // //wenn message.startsId == next hop aufpassen, dass es sich nicht hochschaukelt!!!!
//            shortestWayToRoot = message.calculatedSummeRootKosten;
//            nextHop = message.startId;
//            nextHopName = message.startNode;
//            connectedToRoot = true;
//        }
//
//        //im zugehörigen link gesamtpfadkosten ändern
//        //und im gegenteiligen link des anderen knotens ebenfalls gesamtpfadkosten ändern
//        //next hop mit geringsten pfadkosten rausfinden
//        if((message.calculatedSummeRootKosten < this.shortestWayToRoot) && connectedToRoot){  // && (message.startId != nextHop)  && (message.startId != nodeID)
//            nextHop = message.startId;
//            nextHopName = message.startNode;
//            shortestWayToRoot = message.calculatedSummeRootKosten;
//        }
//
//        int pathCosts = getPathCosts(message.startNode, message.zielNode);
//        System.out.println("Pfadkosten " + pathCosts + ", calculated: " + message.calculatedSummeRootKosten);
//        if(message.calculatedSummeRootKosten < pathCosts && pathCosts > 0){
//            //nexthop ändern
//
//            nextHop = message.startId;
//            System.out.println("Node: " + name + ", nextHop: " + nextHop);
//
//            //pfadkosten zurück, um im gegenteiligen pfad zu ändern
//            return pathCosts;
//        }
        return -1;
    }

    private int getPathCosts(String startNode, String zielNode) {
        for (Link link: linkList) {
            System.out.println("Link Startknoten: -" + link.startknoten + "- Link zielknoten: -" + link.zielknoten + "- Startknoten: -" + startNode + "- Zielknoten: -" + zielNode + "- Eigener name: -" + name + "-");

            if(link.startknoten.equals(name)){
                if (link.zielknoten.equals(zielNode)){
                    return link.getSummeKosten();
                }
            }else if(link.zielknoten.equals(name)){
                if (link.startknoten.equals(startNode)){
                    return link.getSummeKosten();
                }
            }
        }
        return -1;
    }


    public String getName(){
        return this.name;
    }

    public int getNodeID() {
        return nodeID;
    }

    public void addLink(Link link) {
        if(this.name.equals(link.zielknoten)){
            link.changeNodes();
        }
        if(this.name.equals(link.startknoten)) {
            this.linkList.add(link);
            return;
        }
        System.out.println("ERROR: Links to Nodes!!");
    }

    public List<Link> getLinkList() {
        return this.linkList;
    }

    public int getRoot() {
        return calculatedRootID;
    }

    public boolean isConnectedToRoot() {
        return connectedToRoot;
    }

    public int getNextHop() {
        return nextHop;
    }

    public String getNextHopName() {
        return nextHopName;
    }

    public int getSummeKosten() {
        return shortestWayToRoot;
    }

    protected record Message(String startNode, int startId, String zielNode, int calculatedRootId, int calculatedSummeRootKosten, int nextHopOfStart, boolean connectedToRoot){};
}
