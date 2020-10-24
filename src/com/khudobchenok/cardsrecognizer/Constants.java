package com.khudobchenok.cardsrecognizer;

public interface Constants {

    public static final String [] RANKS_OF_CARD = {"2.png","3.png","4.png","5.png","6.png","7.png",
                                                  "8.png","9.png","10.png","A_7.png","J.png","K.png","Q.png"};
    public static final String [] SUITS_OF_CARD = {"c.png","d.png","h.png","s.png"};

    public static final String DEFAULT_INPUT_DIR = System.getProperty("user.dir") + "\\image";

    public static final int [] OFFSET_LEFT= {148, 220, 290, 363, 435};

    public static final int [] OFFSET_TOP = {591, 615};

    public static final int WIDTH = 30;

    public static final int HEIGHT = 25;

    public static final int IMAGE_WIDTH = 636;

    public static final int IMAGE_HEIGHT = 1166;
}