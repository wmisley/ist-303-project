package com.cgu.ist303.project.ui.controls;

import javafx.scene.control.TextField;

/**
 * This code was lifted from StackOverflow
 *
 * http://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength
 */
public class LimitedTextField extends TextField {
    private int maxlength;

    public LimitedTextField() {
        this.maxlength = 10;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.equals("")) {
            super.replaceText(start, end, text);
        } else if (getText().length() < maxlength) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (text.equals("")) {
            super.replaceSelection(text);
        } else if (getText().length() < maxlength) {
            if (text.length() > maxlength - getText().length()) {
                text = text.substring(0, maxlength- getText().length());
            }
            super.replaceSelection(text);
        }
    }
}