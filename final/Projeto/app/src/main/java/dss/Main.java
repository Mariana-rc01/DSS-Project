package dss;

import dss.business.LNFacade;
import dss.ui.TextUI;

public class Main {

    static LNFacade facade = new LNFacade();
    public static void main(String[] args) {
        try {
            TextUI ui = new TextUI(facade);
            ui.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
