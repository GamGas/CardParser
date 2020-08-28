package parser;

public class Card {
    public String name;
    public MatchPixel[] pixelsToMatch;
    public Card(String name, MatchPixel[] pixelsToMatch){
        this.name = name;
        this.pixelsToMatch = pixelsToMatch;
    }
}
