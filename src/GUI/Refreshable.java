package GUI;
/*
Interface voor het automatisch updaten van alle GUI elementen, in de refresh worden alle updates gedaan,
de update update alle waarden in de klassen en met de refresh wordt dit in alle andere ook gedaan
 */

public interface Refreshable {
    void refresh(GUI gui);
    void update();
}
