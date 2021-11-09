package elec5620.sydney.edu.au.smarthealth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Evidence
{
    public Evidence(HashMap<String, String> evidence) {
        this.evidence = evidence;
    }

    public HashMap<String, String> getEvidence() {
        return evidence;
    }

    public void setEvidence(HashMap<String, String> evidence) {
        this.evidence = evidence;
    }

    HashMap<String,String> evidence;

}
