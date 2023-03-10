import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static List<Node> nodeList = new ArrayList<>();

    static List<Link> linkList = new ArrayList<>();
    static int maxNodes;
    static boolean DEBUG = true;
    public static void main(String[] args) {
        String pathToFile = "C:\\Users\\st\\Documents\\DHBWLokal\\Semester\\SJ3\\LaborNT\\SpanningTreeOO\\src\\spanningTreeInputs\\spanningTreeInput.txt";

        maxNodes = readInFile(pathToFile);

        //Ausgabe der Nodes und Links
        //todo: manchmal zielnode und startnode vertauscht?
        if(DEBUG){
            System.out.println("Maxnodes: " + maxNodes);

            for (Node node: nodeList) {
                System.out.println(node.getName() + " = " + node.getNodeID() + ", " + node.getNextHop());

                List <Link> linkList = node.getLinkList();
                for (Link link: linkList) {

                        System.out.println(link.startknoten + " -> " + link.zielknoten);

                }
            }
        }

        //jeder node soll eine message schreiben an alle Verbundenen nodes -> minimum maxnodesanzahl über alle nodes iterieren
        for (int i = 0; i <= maxNodes; i++) {
            if (DEBUG){
                System.out.println("Durchlauf: " +i);
            }
            for (Node node:nodeList) {
                List <Node.Message> messageList = node.outputMessanges();

                for (Node.Message message: messageList) {
                    if(DEBUG){
                        System.out.println("Startknoten: " + message.startNode() + ", Id: " + message.startId() + ", Zielknoten: " + message.zielNode() + ", calculatedRootId: " + message.calculatedRootId() + ", wegekosten zum Root: " + message.calculatedSummeRootKosten());
                    }

                    sendMessageToNode(message);
                }
            }
            System.out.println("Durchlauf: " +i);
            outputOfSpannigTree();

        }




        if(DEBUG){
            System.out.println("Maxnodes: " + maxNodes);

            for (Node node: nodeList) {
                System.out.println(node.getName() + " = " + node.getNodeID() + " Root: " + node.getRoot()  + ", next hop: " + node.getNextHop() + ", kosten zum root: " + node.getSummeKosten());

                List <Link> linkList = node.getLinkList();
                for (Link link: linkList) {
                    System.out.println(link.startknoten + " -> " + link.zielknoten + ": "+ link.getSummeKosten());
                }
            }
        }

        outputOfSpannigTree();

    }

    private static void outputOfSpannigTree() {
        System.out.println("Output of Spannig tree:");
        for (Node node: nodeList) {
            if(node.getNodeID() == node.getRoot()){
                System.out.println("Root: " + node.getName());
            }
        }

        for (Node node: nodeList) {
            if(node.getNodeID() != node.getRoot()) {
                System.out.println(node.getName() + "-" + node.getNextHopName());
            }
        }
    }

    private static void sendMessageToNode(Node.Message message) {
        for (Node node: nodeList) {
            //wenn message zu node gehört und der node nicht der nexteHop der node ID ist (!hochschaukeln der pfadkosten)
            if(node.getName().equals(message.zielNode())){
                int pfadkosten = node.inputMessage(message);

                //pfadkosten an gegenüberliegende instanz zum pfad hinzufügen
//                if(pfadkosten > 0){
//                    for (Node startNode: nodeList) {
//                        if(startNode.getNodeID() == message.startId()){
//                            for(Link link: startNode.getLinkList()){
//                                if(link.startknoten.equals(message.startNode()) && link.zielknoten.equals(message.zielNode())){
//                                    link.setSummePfadKosten(pfadkosten);
//                                }
//                            }
//                        }
//                    }
//
//                }

            }
        }
    }

    private static int readInFile(String pathToFile) {
        int maxNodes = 0;

        //öffne file und read in
        File spanningTreeInput = new File(pathToFile);
        try {
            //Read from file
            Scanner reader = new Scanner(spanningTreeInput);
            reader.nextLine();


            //Read untile end of file
            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                //todo: regex parser?

                    //check if line is valid
                if(lineIsInvalid(line)){


                    //nodes einlesen
                } else if(line.contains("=")){
                    nodeList.add(new Node(line));
                    maxNodes++;

                //links einlesen
                } else if (line.contains("-")) {
                    //linkList.add(new Link(line));
                    addLinkToNodes(new Link(line));

                } else {
                    //fehler
                }

            }
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }

        return maxNodes;
    }

    private static void addLinkToNodes(Link link) {
        for (Node node: nodeList) {
            //link dem startkoten übergeben
            //System.out.println("Node: link" + node.getName() + ": " + link.startknoten);
            if(node.getName().equals(link.startknoten)){
                //System.out.println("Add link to Node: link" + node.getName() + ": " + link.startknoten);
                node.addLink(link);
            }
        }

        //tausche start und zielknoten
        link.changeNodes();

        for (Node node: nodeList) {
            //link dem startkoten übergeben
            if(node.getName().equals(link.startknoten)){
                node.addLink(link);
                //System.out.println("Add Link " + link.startknoten + "->" + link.zielknoten + " to " + node.getName());
//                return;
            }
        }
    }

    private static boolean lineIsInvalid(String line) {
        return (line.contains("/") || line.contains("{") || line.contains("}"));
    }
}