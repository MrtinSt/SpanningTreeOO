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
    }



    //generate and send message to others
    public List<Message> sendOutMessages(){
        List<Message> messageList = new ArrayList<Message>();
        msgCnt++;
        for (Link link: linkList) {

            if(link.startknoten.equals(name)){
                messageList.add(new Message(link.startknoten, nodeID, link.zielknoten,calculatedRootID, link.getKosten()+this.shortestWayToRoot, nextHop));
            }else{
                messageList.add(new Message(link.zielknoten, nodeID, link.startknoten,calculatedRootID, link.getKosten()+this.shortestWayToRoot, nextHop));
            }
        }

        return messageList;
    }

    //get message from other node
    public void inputMessage(Message message) {
        msgCnt++;

        if(Main.DEBUG){
            System.out.println("Aktueller Node: " + name + ", Startknoten: " + message.startNode() + ", Id: " + message.startId() + ", Zielknoten: " + message.zielNode() + ", calculatedRootId: " + message.calculatedRootId() + ", wegekosten zum Root: " + message.calculatedSummeRootKosten());
        }

        //root aktualisieren
        if(message.calculatedRootId < calculatedRootID){
            this.calculatedRootID = message.calculatedRootId;
            nextHop = message.startId;
            nextHopName = message.startNode;
            shortestWayToRoot = message.calculatedSummeRootKosten;
        //kürzesten weg finden
        } else if ((message.calculatedRootId == calculatedRootID) && this.shortestWayToRoot > message.calculatedSummeRootKosten) {
            shortestWayToRoot = message.calculatedSummeRootKosten;
            nextHop = message.startId;
            nextHopName = message.startNode;
        }else {
            // bester pfad schon gefunden, tue nichts
        }
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

    public int getNextHop() {
        return nextHop;
    }

    public String getNextHopName() {
        return nextHopName;
    }

    public int getSummeKosten() {
        return shortestWayToRoot;
    }

    protected record Message(String startNode, int startId, String zielNode, int calculatedRootId, int calculatedSummeRootKosten, int nextHopOfStart){};
}
