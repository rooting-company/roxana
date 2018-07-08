package br.com.rooting.roxana.translator;

public enum LocaleTagEnum {
    PT_BR("pt-BR"),
    EN_US("en-US"),
    DE_DE("de-DE"),
    JA_JP("ja-JP");

    private final String tag;

    LocaleTagEnum(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

}