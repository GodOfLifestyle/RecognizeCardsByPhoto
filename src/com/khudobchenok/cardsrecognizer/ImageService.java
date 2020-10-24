package com.khudobchenok.cardsrecognizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

public class ImageService implements Constants {

    public void recognizer(Map<String, StringBuilder> ranksMap, Map<String, StringBuilder> suitsMap, int width, int height, int[] offsetTop, int[] offsetLeft) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите путь до папки с изображениями \n(Тестировочные изображения лежат в папке image)");
        String pathToFolder = reader.readLine();
        File[] files = new File(pathToFolder).listFiles((d, n) -> n.endsWith(".png"));
        if (files.length == 0) {
            System.out.println("В папке " + pathToFolder + " файлов с картинками не найдено");
            System.exit(0);
        }
        for (File inFile : Objects.requireNonNull(files)) {
            BufferedImage image = ImageIO.read(inFile);
            if (image.getWidth()!= IMAGE_WIDTH || image.getHeight() != IMAGE_HEIGHT){
                System.out.println("Неверный размер исходного изображения в файле " + inFile.getName());
            }
            String findRank = "";
            String findSuit = "";
            System.out.print(inFile.getName() + " - ");
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 2; j++) {
                    BufferedImage img1 = image.getSubimage(offsetLeft[i], offsetTop[j], width, height);
                    StringBuilder binaryString = pixelConversion(height, width, img1);
                    if (j == 0) {
                        int minx = 1000_000;
                        for (Map.Entry<String, StringBuilder> entryRanks : ranksMap.entrySet()) {
                            int levenshtein = levenshtein(binaryString.toString(), entryRanks.getValue().toString());
                            if (levenshtein < minx) {
                                minx = levenshtein;
                                if (levenshtein < 100) {
                                    findRank = entryRanks.getKey();
                                } else findRank = "";
                            }
                        }
                        System.out.print(findRank);
                    }
                    if (j == 1) {
                        int min = 1000_000;
                        for (Map.Entry<String, StringBuilder> entrySuits : suitsMap.entrySet()) {
                            int levenshteins = levenshtein(binaryString.toString(), entrySuits.getValue().toString());

                            if (levenshteins < min) {
                                min = levenshteins;
                                if (levenshteins < 100) {
                                    findSuit = entrySuits.getKey();
                                } else findSuit = "";
                            }
                        }
                        System.out.print(findSuit);
                    }
                }
            }
            System.out.println("");
        }
    }

    public static int levenshtein(String targetStr, String sourceStr) {
        int m = targetStr.length(), n = sourceStr.length();
        int[][] delta = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++)
            delta[i][0] = i;
        for (int j = 1; j <= n; j++)
            delta[0][j] = j;
        for (int j = 1; j <= n; j++)
            for (int i = 1; i <= m; i++) {
                if (targetStr.charAt(i - 1) == sourceStr.charAt(j - 1))
                    delta[i][j] = delta[i - 1][j - 1];
                else
                    delta[i][j] = Math.min(delta[i - 1][j] + 1,
                            Math.min(delta[i][j - 1] + 1, delta[i - 1][j - 1] + 1));
            }
        return delta[m][n];
    }

    public static StringBuilder pixelConversion(int height, int width, BufferedImage image) {
        short whiteBg = -1;
        StringBuilder binaryString = new StringBuilder();
        for (short y = 1; y < height; y++) {
            for (short x = 1; x < width; x++) {
                int rgb = image.getRGB(x, y);
                binaryString.append(rgb == whiteBg ? " " : "*");
            }
        }
        return binaryString;
    }
}