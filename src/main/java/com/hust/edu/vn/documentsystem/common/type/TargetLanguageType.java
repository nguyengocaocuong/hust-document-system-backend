package com.hust.edu.vn.documentsystem.common.type;

public enum TargetLanguageType {

    AFRIKAANS("af"), ALBANIAN("sq"), AMHARIC("am"), ARABIC("ar"), ARMENIAN("hy"),
    AZERBAIJANI("az"), BASQUE("eu"), BELARUSIAN("be"), BENGALI("bn"), BOSNIAN("bs"),
    BULGARIAN("bg"), CATALAN("ca"), CEBUANO("ceb"), CHICHEWA("ny"), CHINESE_SIMPLIFIED("zh-CN"),
    CHINESE_TRADITIONAL("zh-TW"), CORSICAN("co"), CROATIAN("hr"), CZECH("cs"), DANISH("da"),
    DUTCH("nl"), ENGLISH("en"), ESPERANTO("eo"), ESTONIAN("et"), FILIPINO("tl"), FINNISH("fi"),
    FRENCH("fr"), FRISIAN("fy"), GALICIAN("gl"), GEORGIAN("ka"), GERMAN("de"), GREEK("el"),
    GUJARATI("gu"), HAITIAN_CREOLE("ht"), HAUSA("ha"), HAWAIIAN("haw"), HEBREW("he"),
    HINDI("hi"), HMONG("hmn"), HUNGARIAN("hu"), ICELANDIC("is"), IGBO("ig"), INDONESIAN("id"),
    IRISH("ga"), ITALIAN("it"), JAPANESE("ja"), JAVANESE("jw"), KANNADA("kn"), KAZAKH("kk"),
    KHMER("km"), KINYARWANDA("rw"), KOREAN("ko"), KURDISH("ku"), KYRGYZ("ky"), LAO("lo"),
    LATIN("la"), LATVIAN("lv"), LITHUANIAN("lt"), LUXEMBOURGISH("lb"), MACEDONIAN("mk"),
    MALAGASY("mg"), MALAY("ms"), MALAYALAM("ml"), MALTESE("mt"), MAORI("mi"), MARATHI("mr"),
    MONGOLIAN("mn"), MYANMAR("my"), NEPALI("ne"), NORWEGIAN("no"), OCCITAN("oc"), ODIA("or"),
    PASHTO("ps"), PERSIAN("fa"), POLISH("pl"), PORTUGUESE("pt"), PUNJABI("pa"), ROMANIAN("ro"),
    RUSSIAN("ru"), SAMOAN("sm"), SCOTS_GAELIC("gd"), SERBIAN("sr"), SESOTHO("st"), SHONA("sn"),
    SINDHI("sd"), SINHALA("si"), SLOVAK("sk"), SLOVENIAN("sl"), SOMALI("so"), SPANISH("es"),
    SUNDANESE("su"), SWAHILI("sw"), SWEDISH("sv"), TAJIK("tg"), TAMIL("ta"), TATAR("tt"),
    TELUGU("te"), THAI("th"), TURKISH("tr"), TURKMEN("tk"), UKRAINIAN("uk"), URDU("ur"),
    UYGHUR("ug"), UZBEK("uz"), VIETNAMESE("vi"), WELSH("cy"), XHOSA("xh"), YIDDISH("yi"),;

    private final String code;

    TargetLanguageType(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
