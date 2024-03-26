package Data;

import GUI.Simulator.Target;

import java.io.Serializable;

public class Podium implements Serializable {
    private String name;
    private Target target;

    public Podium(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
