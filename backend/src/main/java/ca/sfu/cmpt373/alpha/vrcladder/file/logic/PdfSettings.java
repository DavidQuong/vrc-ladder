package ca.sfu.cmpt373.alpha.vrcladder.file.logic;

/**
 * This class holds all general constants needed to generate PDF files.
 */
public class PdfSettings {

    // General PDF settings.
    //----------------------
    public static final String OUTPUT_PATH              = "PDF";
    public static final int NUMBER_OF_COLUMNS           = 4;
    public static final int NUMBER_OF_TEAMS_PER_COLUMN  = 8;

    // Templates and paths.
    //---------------------
    public static final String LAYOUT_PATH       = "src\\main\\resources\\pdf\\templates\\layout.html";
    public static final String CONTENT_PATH      = "src\\main\\resources\\pdf\\templates\\content.html";
    public static final String FONT_PATH         = "src\\main\\resources\\pdf\\fonts\\Lato-Medium.ttf";
    static final String COLUMNS_TEAM_HOLDER      = "src\\main\\resources\\pdf\\templates\\column_team_holder.html";
    static final String COLUMNS_CONTENT_PATH     = "src\\main\\resources\\pdf\\templates\\column_content.html";
    static final String COLUMNS_CONTAINER_PATH   = "src\\main\\resources\\pdf\\templates\\column_indicator.html";
    static final String COLUMNS_SEPARATOR_PATH   = "src\\main\\resources\\pdf\\templates\\column_separator.html";
    static final String IMAGE_PATH               = "src\\main\\resources\\pdf\\images\\";
    static final String IMAGE_SOURCE             = "src";

}
