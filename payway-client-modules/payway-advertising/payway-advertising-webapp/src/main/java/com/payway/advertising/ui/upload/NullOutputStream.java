/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.upload;

import java.io.IOException;
import java.io.OutputStream;

/**
 * NullOutputStream
 *
 * @author Sergey Kichenko
 * @created 12.05.15 00:00
 */
public class NullOutputStream extends OutputStream {

    @Override
    public void write(byte[] b, int off, int len) {
        //
    }

    @Override
    public void write(int b) {
        //
    }

    @Override
    public void write(byte[] b) throws IOException {
        //
    }

}
