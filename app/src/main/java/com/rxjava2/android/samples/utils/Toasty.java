package com.rxjava2.android.samples.utils;

/**
 * Created by Edward on 2017/7/13 14:48.
 * Description
 * ${COMPANT}
 * Contact me andersonedwar@126.com
 */

class Toasty {
    private static final Toasty ourInstance = new Toasty ();

    static Toasty getInstance() {
        return ourInstance;
    }

    private Toasty() {
    }
}
