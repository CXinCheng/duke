public class Deadline extends Task {
    String by;
    public Deadline(String name, String by) {
        super(name);
        this.by = by;
    }

    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }
}