package jsonvalues.benchmark;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VeggieWithAnnotations {

    @NotNull
    @Size(min = 1, max = 255)
    private String veggieName;
    @NotNull
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
