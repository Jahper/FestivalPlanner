package GUI.Simulator;

public class Target {
    String name;
    int id;
    double height;
    double width;
    double x;
    double y;

    public Target(String name, int id, double height, double width, double x, double y){
        this.name=name;
        this.id=id;
        this.height=height;
        this.width=width;
        this.x=x;
        this.y=y;
    }


    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "name: "+this.getName()+" id: "+this.id+" height: " + this.height + " whidth: "+ this.width + " x: "+ this.x + " y: "+this.y;
    }
}
