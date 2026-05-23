package model;

public enum AreaConhecimento {
    EXATAS("Ciências Exatas"),
    HUMANAS("Ciências Humanas"),
    BIOLOGICAS("Ciências Biológicas"),
    TECNOLOGIA("Tecnologia");

    private final String descricao;

    AreaConhecimento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
