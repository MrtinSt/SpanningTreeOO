public class Link {
    String startknoten;
    String zielknoten;

    // Linkkosten von Node_i -> Node_k
    // kosten=0: kein Link vorhanden
    // Entspricht ursprüngliche Initialisierung des eingelesenen Graphen
    private int kosten;

    // Über diesen Link erhaltene Nachricht der Nachbarknoten
    // mit Vorschlag der Root incl. Gesamtkosten zur Root
    private int rootID;

    //zum rootknoten
    private int summeKosten;


    // E - F : 2;
    public Link(String line) {
        String names = line.strip().split(":")[0];

        this.startknoten = names.split("-")[0].strip();
        this.zielknoten = names.split("-")[1].strip();

        if(Main.SHOW_READING_IN){
            System.out.println(startknoten);
            System.out.println(zielknoten);
        }


        this.kosten = Integer.parseInt(line.strip().split(":")[1].split(";")[0].strip());
        this.summeKosten = this.kosten;
    }

    public int getKosten(){
        return kosten;
    }

    public int getSummeKosten(){
        return summeKosten;
    }

    public void changeNodes() {
        String behilfsKnoten = this.startknoten;
        this.startknoten = this.zielknoten;
        this.zielknoten = behilfsKnoten;
    }

    public void setSummePfadKosten(int pfadkosten) {
        summeKosten = pfadkosten;
    }
}
