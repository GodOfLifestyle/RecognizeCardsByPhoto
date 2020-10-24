package com.khudobchenok.cardsrecognizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CardsRecognizer implements Constants{

    static ImageService imageService = new ImageService();

    private static Map<String, BufferedImage> loadTemplates(String[] templs)  {
        return Arrays.stream(templs).collect(Collectors
                .toMap(s->s.replaceFirst("[.][^.]+$", ""),
                        s->{
                            try
                            {
                                return ImageIO.read(Objects.requireNonNull(CardsRecognizer.class.getClassLoader().getResourceAsStream(s)));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ));
    }

    public static void main(String[] args) throws IOException {

        Map<String, BufferedImage> mapRankTempls = loadTemplates(Constants.RANKS_OF_CARD);
        if (mapRankTempls == null || mapRankTempls.isEmpty()) {
            System.out.println("Отсутствуют шаблоны рангов карт");
            System.exit(0);
        }
        Map<String, BufferedImage> mapSuitsTempls = loadTemplates(Constants.SUITS_OF_CARD);
        if (mapSuitsTempls == null || mapSuitsTempls.isEmpty()) {
            System.out.println("Отсутствуют шаблоны мастей карт");
            System.exit(0);
        }

        Map<String, StringBuilder> ranksMap = new HashMap<>();
        Map<String, StringBuilder> suitsMap = new HashMap<>();

        for (Map.Entry<String, BufferedImage> entry : mapRankTempls.entrySet()) {
            if (entry.getValue().getHeight() != HEIGHT || entry.getValue().getWidth() != WIDTH) {
                System.out.println("Неверный размер заготовленного шаблона в файле " + entry.getKey());
            }
            BufferedImage image = entry.getValue().getSubimage(0,0,WIDTH, HEIGHT);
            StringBuilder binaryString = ImageService.pixelConversion(HEIGHT, WIDTH, image);
            ranksMap.put(entry.getKey(), binaryString);
        }

        for (Map.Entry<String, BufferedImage> entry : mapSuitsTempls.entrySet()) {
            if (entry.getValue().getHeight() != HEIGHT || entry.getValue().getWidth() != WIDTH) {
                System.out.println("Неверный размер заготовленного шаблона в файле " + entry.getKey());
            }
            BufferedImage image = entry.getValue().getSubimage(0,0,WIDTH, HEIGHT);
            StringBuilder binaryString = ImageService.pixelConversion(HEIGHT, WIDTH, image);
            suitsMap.put(entry.getKey(), binaryString);
        }
        try {
            imageService.recognizer(ranksMap, suitsMap, 30, 25, OFFSET_TOP, OFFSET_LEFT);
        } catch (NullPointerException e) {
            System.out.println("Папки с таким названием не существует");
        }
        System.out.println("Конец работы");
    }
}