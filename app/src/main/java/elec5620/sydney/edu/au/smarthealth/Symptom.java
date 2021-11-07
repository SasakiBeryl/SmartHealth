package elec5620.sydney.edu.au.smarthealth;
public class Symptom {
    String id;
    String orth;
    String choid_id;
    String name;
    String common_name;
    String type;
    public Symptom (String id, String orth, String choid_id, String name, String common_name, String type)
    {
        this.id = id;
        this.orth = orth;
        this.choid_id = choid_id;
        this.name = name;
        this.common_name = common_name;
        this.type = type;
    }
    public Symptom ()
    {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrth() {
        return orth;
    }

    public void setOrth(String orth) {
        this.orth = orth;
    }

    public String getChoid_id() {
        return choid_id;
    }

    public void setChoid_id(String choid_id) {
        this.choid_id = choid_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
