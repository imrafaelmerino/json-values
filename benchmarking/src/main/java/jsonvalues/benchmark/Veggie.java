package jsonvalues.benchmark;



public class Veggie {

    private String veggieName;

    private boolean veggieLike;

    public String getVeggieName() {
        return veggieName;
    }

    public void setVeggieName(final String veggieName) {
        this.veggieName = veggieName;
    }

    public boolean isVeggieLike() {
        return veggieLike;
    }

    public void setVeggieLike(final boolean veggieLike) {
        this.veggieLike = veggieLike;
    }

    @Override
    public String toString() {
        return "Veggie{" +
                "veggieName='" + veggieName + '\'' +
                ", veggieLike=" + veggieLike +
                '}';
    }
}
