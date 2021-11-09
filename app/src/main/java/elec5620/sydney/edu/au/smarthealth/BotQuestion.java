package elec5620.sydney.edu.au.smarthealth;

public class BotQuestion {
    public BotQuestion(String id, String choices) {
        this.id = id;
        this.choices=choices;
    }

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    String choices;
}
