package parser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class Main {
    private static final Color white = new Color(-1);
    private static final Color white_dark = new Color(-8882856);
    private static final Color red = new Color(-3323575);
    private static final Color red_dark = new Color(-10477022);
    private static final Color black_dark = new Color(-15724526);
    private static final Color black = new Color(-14474458);

    public static void main(String[] args) throws IOException {
        if (args.length>0) {
            File files = new File(args[0]);
            for (File file : Objects.requireNonNull(files.listFiles())) {
                long startTime = new Date().getTime();
                parseFile(file);
                long endTime = new Date().getTime();
                System.out.println("  Ушло на итерацию: " + (endTime - startTime) + "ms");
            }
        } else {
            System.out.println("Отсутствуют аргументы");
        }

    }

    private static void parseFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("На этой картинке: \"").append(file.getPath()).append("\"\n");
        sb.append("На столе лежат карты: \"");

        BufferedImage image = ImageIO.read(file);
        BufferedImage[] cards = parseCards(image);
        for (BufferedImage card : cards) {
            sb.append(getCardData(card));
        }
        sb.append("\"");
        System.out.print(sb);
    }

    private static String getCardData(BufferedImage card) {
        String nominale = getCardNominale(card);
        String suite = getCardSuite(card);
        return nominale + suite;
    }

    private static String getCardNominale(BufferedImage card) {
        int startX_num = 5, startY_num = 6, width_num = 33, height_num = 26;
        BufferedImage subImage = card.getSubimage(startX_num, startY_num, width_num, height_num);
        return parseNominale(subImage);
    }

    private static String parseNominale(BufferedImage num) {
        Card[] cards = cardsInit();
        ArrayList<Card> cardList = new ArrayList<>();
        Collections.addAll(cardList, cards);
        Collections.reverse(cardList);
        for (Card card : cardList) {
            int matchCounter = 0;
            int pixelsLength = card.pixelsToMatch.length;
            for (MatchPixel pixel : card.pixelsToMatch) {
                int x = pixel.x;
                int y = pixel.y;
                int pxl = num.getRGB(x, y);
                if (pxl == white.getRGB() || pxl == white_dark.getRGB()) {
                    break;
                }
                matchCounter++;
            }
            if (matchCounter == pixelsLength) {
                return card.name;
            }
        }
        return "NaN";
    }

    private static BufferedImage[] parseCards(BufferedImage image) {
        int startX = 141, startY = 584, width = 66, height = 91;
        int[] map_startx = {startX, startX + width + 5, startX + 144, startX + 215, startX + 287};
        ArrayList<BufferedImage> cards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BufferedImage card = image.getSubimage(map_startx[i], startY, width, height);
            if (isExist(card)) {
                cards.add(card);
            }
        }
        BufferedImage[] outputCards = new BufferedImage[cards.size()];
        return cards.toArray(outputCards);
    }

    public static String getCardSuite(BufferedImage im) {
        int posX = im.getWidth() - 25;
        int posY = im.getHeight() - 25;
        int intRed = red.getRGB();
        int intRedDark = red_dark.getRGB();
        int intBlack = black.getRGB();
        int intBlackDark = black_dark.getRGB();
        int color = im.getRGB(posX, posY);
        if (color == intRed || color == intRedDark) {
            return checkRed(im);
        } else if (color == intBlack || color == intBlackDark) {
            return checkBlack(im);
        }
        return "NaN";
    }

    public static boolean isExist(BufferedImage im) {
        int posX = im.getWidth() - 20;
        int posY = im.getHeight() - 60;
        return im.getRGB(posX, posY) == white.getRGB() || im.getRGB(posX, posY) == white_dark.getRGB();
    }

    public static String checkRed(BufferedImage im) {
        int posX = im.getWidth() - 24;
        int posY = im.getHeight() - 36;
        if (im.getRGB(posX, posY) == red.getRGB() || im.getRGB(posX, posY) == red_dark.getRGB()) {
            return "d";
        } else {
            return "h";
        }
    }

    public static String checkBlack(BufferedImage im) {
        int posX = im.getWidth() - 17;
        int posY = im.getHeight() - 30;
        if (im.getRGB(posX, posY) == black.getRGB() || im.getRGB(posX, posY) == black_dark.getRGB()) {
            return "s";
        } else {
            return "c";
        }
    }

    public static Card[] cardsInit() {
        return new Card[]{
                new Card("2", new MatchPixel[]{
                        new MatchPixel(8, 6),
                        new MatchPixel(10, 4),
                        new MatchPixel(13, 3),
                        new MatchPixel(16, 4),
                        new MatchPixel(19, 7),
                        new MatchPixel(19, 10),
                        new MatchPixel(16, 13),
                        new MatchPixel(13, 16),
                        new MatchPixel(10, 19),
                        new MatchPixel(7, 22),
                        new MatchPixel(10, 22),
                        new MatchPixel(13, 22),
                        new MatchPixel(16, 22),
                        new MatchPixel(19, 22)
                }),
                new Card("3", new MatchPixel[]{
                        new MatchPixel(9, 3),
                        new MatchPixel(12, 3),
                        new MatchPixel(15, 3),
                        new MatchPixel(18, 3),
                        new MatchPixel(17, 6),
                        new MatchPixel(14, 10),
                        new MatchPixel(12, 12),
                        new MatchPixel(17, 13),
                        new MatchPixel(20, 16),
                        new MatchPixel(19, 20),
                        new MatchPixel(16, 22),
                        new MatchPixel(12, 22),
                        new MatchPixel(8, 20)
                }),
                new Card("4", new MatchPixel[]{
                        new MatchPixel(19, 3),
                        new MatchPixel(16, 6),
                        new MatchPixel(19, 6),
                        new MatchPixel(13, 9),
                        new MatchPixel(19, 10),
                        new MatchPixel(11, 12),
                        new MatchPixel(19, 13),
                        new MatchPixel(8, 15),
                        new MatchPixel(8, 17),
                        new MatchPixel(12, 17),
                        new MatchPixel(16, 17),
                        new MatchPixel(19, 17),
                        new MatchPixel(22, 17),
                        new MatchPixel(19, 22)
                }),
                new Card("5", new MatchPixel[]{
                        new MatchPixel(19, 3),
                        new MatchPixel(16, 3),
                        new MatchPixel(12, 3),
                        new MatchPixel(9, 3),
                        new MatchPixel(9, 6),
                        new MatchPixel(9, 9),
                        new MatchPixel(9, 12),
                        new MatchPixel(12, 11),
                        new MatchPixel(15, 11),
                        new MatchPixel(19, 13),
                        new MatchPixel(20, 17),
                        new MatchPixel(19, 20),
                        new MatchPixel(14, 22),
                        new MatchPixel(11, 22),
                        new MatchPixel(8, 20)
                }),
                new Card("6", new MatchPixel[]{
                        new MatchPixel(19, 4),
                        new MatchPixel(16, 3),
                        new MatchPixel(13, 3),
                        new MatchPixel(10, 5),
                        new MatchPixel(8, 7),
                        new MatchPixel(7, 11),
                        new MatchPixel(8, 13),
                        new MatchPixel(8, 16),
                        new MatchPixel(10, 21),
                        new MatchPixel(14, 22),
                        new MatchPixel(19, 20),
                        new MatchPixel(20, 16),
                        new MatchPixel(18, 12),
                        new MatchPixel(14, 11)
                }),
                new Card("7", new MatchPixel[]{
                        new MatchPixel(8, 3),
                        new MatchPixel(11, 3),
                        new MatchPixel(14, 3),
                        new MatchPixel(17, 3),
                        new MatchPixel(19, 4),
                        new MatchPixel(18, 7),
                        new MatchPixel(17, 9),
                        new MatchPixel(15, 13),
                        new MatchPixel(13, 16),
                        new MatchPixel(12, 18),
                        new MatchPixel(10, 21),
                        new MatchPixel(15, 10),
                        new MatchPixel(20, 2)
                }),
                new Card("8", new MatchPixel[]{
                        new MatchPixel(8, 6),
                        new MatchPixel(8, 9),
                        new MatchPixel(8, 15),
                        new MatchPixel(8, 18),
                        new MatchPixel(11, 3),
                        new MatchPixel(10, 11),
                        new MatchPixel(10, 13),
                        new MatchPixel(10, 21),
                        new MatchPixel(12, 12),
                        new MatchPixel(14, 3),
                        new MatchPixel(14, 12),
                        new MatchPixel(14, 22),
                        new MatchPixel(19, 21),
                        new MatchPixel(19, 8)
                }),
                new Card("9", new MatchPixel[]{
                        new MatchPixel(18, 13),
                        new MatchPixel(14, 14),
                        new MatchPixel(11, 13),
                        new MatchPixel(8, 11),
                        new MatchPixel(8, 8),
                        new MatchPixel(9, 5),
                        new MatchPixel(12, 3),
                        new MatchPixel(17, 3),
                        new MatchPixel(19, 5),
                        new MatchPixel(21, 8),
                        new MatchPixel(21, 11),
                        new MatchPixel(21, 14),
                        new MatchPixel(20, 17),
                        new MatchPixel(18, 20),
                        new MatchPixel(15, 22),
                        new MatchPixel(12, 22),
                        new MatchPixel(10, 21)
                }),
                new Card("10", new MatchPixel[]{
                        new MatchPixel(2, 4),
                        new MatchPixel(27, 19)
//                        new MatchPixel(3, 4),
//                        new MatchPixel(7, 3),
//                        new MatchPixel(7, 6),
//                        new MatchPixel(7, 9),
//                        new MatchPixel(7, 12),
//                        new MatchPixel(7, 16),
//                        new MatchPixel(7, 19),
//                        new MatchPixel(7, 22)
                }),
                new Card("J", new MatchPixel[]{
                        new MatchPixel(16, 3),
                        new MatchPixel(16, 6),
                        new MatchPixel(16, 9),
                        new MatchPixel(16, 12),
                        new MatchPixel(16, 15),
                        new MatchPixel(16, 18),
                        new MatchPixel(14, 20),
                        new MatchPixel(11, 21),
                        new MatchPixel(8, 19),
                        new MatchPixel(16, 19),
                }),
                new Card("Q", new MatchPixel[]{
                        new MatchPixel(17, 3),
                        new MatchPixel(12, 4),
                        new MatchPixel(9, 6),
                        new MatchPixel(7, 9),
                        new MatchPixel(7, 12),
                        new MatchPixel(7, 15),
                        new MatchPixel(8, 18),
                        new MatchPixel(11, 20),
                        new MatchPixel(14, 22),
                        new MatchPixel(17, 22),
                        new MatchPixel(20, 21),
                        new MatchPixel(19, 16),
                        new MatchPixel(21, 18),
                        new MatchPixel(25, 21)

                }),
                new Card("K", new MatchPixel[]{
                        new MatchPixel(9, 3),
                        new MatchPixel(9, 8),
                        new MatchPixel(9, 11),
                        new MatchPixel(9, 14),
                        new MatchPixel(9, 17),
                        new MatchPixel(9, 22),
                        new MatchPixel(11, 14),
                        new MatchPixel(13, 12),
                        new MatchPixel(16, 9),
                        new MatchPixel(19, 6),
                        new MatchPixel(21, 4),
                        new MatchPixel(23, 3),
                        new MatchPixel(22, 21)
                }),
                new Card("A", new MatchPixel[]{
                        new MatchPixel(3, 23),
                        new MatchPixel(4, 20),
                        new MatchPixel(5, 18),
                        new MatchPixel(6, 16),
                        new MatchPixel(7, 13),
                        new MatchPixel(9, 10),
                        new MatchPixel(10, 6),
                        new MatchPixel(12, 4),
                        new MatchPixel(14, 7),
                        new MatchPixel(16, 12),
                        new MatchPixel(18, 16),
                        new MatchPixel(21, 23),
                        new MatchPixel(12, 17)
                })
        };
    }
}
