package com.cgu.ist303.project.ui.controls;

import javafx.scene.control.TextField;

/**
 * This code was lifted from StackOverflow
 *
 * http://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
 */
public class NumberTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text) {
        return text.matches("[0-9]*");
    }
}
