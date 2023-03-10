import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final boolean SHOW_READING_IN = false;
    private static final boolean SHOW_SPANNINGTREE_BUILD_UP = false;
    static final boolean DEBUG = false;

    static List<Node> nodeList = new ArrayList<>();
    static int maxNodes;

    public static void main(String[] args) {
        String pathToFile = "src\\spanningTreeInputs\\spanningTreeBig.txt";

        maxNodes = readInFile(pathToFile);

        if(DEBUG){
            showAllInformations();
        }

        //sending all messanges from the nodes to the others
        for (int i = 0; i <= maxNodes; i++) {
            //get the messanges from every node
            for (Node node:nodeList) {
                List <Node.Message> messageList = node.sendOutMessages();
                // send all messages from a node to the linked nodes
                for (Node.Message message: messageList) {
                    sendMessageToNode(message);
                }
            }

            if(SHOW_SPANNINGTREE_BUILD_UP){
                System.out.println("Durchlauf: " +i);
                outputOfSpannigTree();
            }
        }

        if(DEBUG){
            showAllInformations();
        }

        outputOfSpannigTree();
    }

    //shows all information about the nodes and links
    private static void showAllInformations() {
        System.out.println("Maxnodes: " + maxNodes);

        for (Node node: nodeList) {
            System.out.println(node.getName() + " = " + node.getNodeID() + " Root: " + node.getRoot()  + ", next hop: " + node.getNextHop() + ", kosten zum root: " + node.getSummeKosten());

            List <Link> linkList = node.getLinkList();
            for (Link link: linkList) {
                System.out.println(link.startknoten + " -> " + link.zielknoten + ": "+ link.getSummeKosten());
            }
        }
    }

    //prints out the connected links and the root
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

    //forward the incoming messages to the nodes
    private static void sendMessageToNode(Node.Message message) {
        for (Node node: nodeList) {
            //send messae to the endnode
            if(node.getName().equals(message.zielNode())){
                node.inputMessage(message);
            }
        }
    }

    //reads in the data from the file into the objects
    private static int readInFile(String pathToFile) {
        int maxNodes = 0;

        //open file to read in
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


                    //read in nodes
                } else if(line.contains("=")){
                    nodeList.add(new Node(line));
                    maxNodes++;

                //read in links
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

    //give the link to the two nodes to connect
    private static void addLinkToNodes(Link link) {
        for (Node node: nodeList) {
            //send the link to the startnode
            if(node.getName().equals(link.startknoten)){
                node.addLink(link);
            }
        }

        //change start and endnode
        link.changeNodes();

        for (Node node: nodeList) {
            //send the link to the startnode
            if(node.getName().equals(link.startknoten)){
                node.addLink(link);
            }
        }
    }

    //check if line is a comment or a start or endframe
    private static boolean lineIsInvalid(String line) {
        return (line.contains("/") || line.contains("{") || line.contains("}"));
    }
}