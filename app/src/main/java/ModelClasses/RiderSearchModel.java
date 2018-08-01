package ModelClasses;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by hamza on 20-Jul-18.
 */

public class RiderSearchModel implements Searchable {
    String title;

    public RiderSearchModel(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
